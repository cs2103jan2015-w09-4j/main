package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.DBfile;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.pojo.Task.TaskType;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.gson.Gson;

/**
 * Google Calendar Storage
 */
public class GoogleStorage extends Storage {
    private static FileDataStoreFactory dataStoreFactory;
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = " file not found";
    private static HttpTransport httpTransport;
    private static JsonFactory jsonFactory = new JacksonFactory();
    private static Logger logger = LoggerFactory
            .getLogger(GoogleStorage.class);
    private Calendar calendar;
    private final String CLIENT_SECRETS_FILE = "/client_secrets.json";
    private com.google.api.services.calendar.Calendar clientCalendar;
    private GoogleClientSecrets clientSecrets;
    private com.google.api.services.tasks.Tasks clientTask;
    private Credential credential;
    private final java.io.File DATA_STORE_CALENDAR_INFO_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/CalendarInfo");
    private final java.io.File DATA_STORE_CREDENTIAL_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/StoredCredential");
    private final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME);
    private final java.io.File DATA_STORE_TASKLIST_INFO_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/TaskListInfo");
    private DBfile dbFile;
    private final String EVENT_REMINDER_METHOD_EMAIL = "email";
    private final String EVENT_REMINDER_METHOD_POPUP = "popup";
    private Gson gson;
    private TaskList taskList;
    private String userId = "user";

    /**
     * @param file
     * @param gson
     * @throws Exception
     */
    public GoogleStorage(DBfile file, Gson gson) throws Exception {
        super(file);
        this.dbFile = file;
        this.gson = gson;
        this.initialize();
    }

    /**
     * Deletes calendar and task list in Google and deletes user's local related
     * files.
     */
    public void deleteCalendarTaskList() {
        try {
            logger.info("Deleting Google " + NormalMessage.APP_NAME
                    + " Calendar");
            this.clientCalendar.calendars().delete(this.calendar.getId())
                    .execute();

            logger.info("Deleting Google " + NormalMessage.APP_NAME
                    + " Task List");
            this.clientTask.tasklists().delete(this.taskList.getId()).execute();

            this.deleteLocalGoogleInfo();

        } catch (Exception e) {
            logger.debug("Failed to delete");
        }
    }

    /**
     * Deletes user's local files that are related to Google Calendar & Google
     * Task List. This does not delete the calendar or task list in Google.
     */
    public void deleteLocalGoogleInfo() {
        this.deleteFile(this.DATA_STORE_CREDENTIAL_FILE);
        this.deleteFile(this.DATA_STORE_CALENDAR_INFO_FILE);
        this.deleteFile(this.DATA_STORE_TASKLIST_INFO_FILE);
        this.deleteFile(this.DATA_STORE_DIR);
    }

    @Override
    public void readData() {

    }

    @Override
    public void storeData() {

    }

    private void addAllEvents() throws IOException {
        logger.info("Adding all events...");
        List<Task> events = new ArrayList<Task>(this.dbFile.getData().getTask()
                .getTaskMap().values());
        for (Task i : events) {
            if (i.getTaskType() == TaskType.TIMED) {
                this.addEvent(i);
            }
        }
        logger.info("Added all events");
    }

    private void addAllTasks() throws IOException {
        logger.info("Adding all tasks...");
        List<Task> tasks = new ArrayList<Task>(this.dbFile.getData().getTask()
                .getTaskMap().values());
        ;
        for (Task i : tasks) {
            if (i.getTaskType() != TaskType.TIMED) {
                this.addTask(i);
            }
        }
        logger.info("Added all tasks");
    }

    private void addEvent(Task newEvent) throws IOException {
        logger.info("Adding event: " + newEvent.getTitle());
        Event event = new Event();
        DateTime dateTime;

        // add title
        event.setSummary(newEvent.getTitle());

        // add start date
        dateTime = new DateTime(newEvent.getStartDate(), TimeZone.getDefault());
        event.setStart(new EventDateTime().setDateTime(dateTime));

        // add end date
        dateTime = new DateTime(newEvent.getEndDate(), TimeZone.getDefault());
        event.setEnd(new EventDateTime().setDateTime(dateTime));

        // add category
        if (newEvent.getCategory() != null) {
            event.setKind(newEvent.getCategory());
        }

        // add description
        if (newEvent.getDescription() != null) {
            event.setDescription(newEvent.getDescription());
        }

        // add location
        if (newEvent.getLocation() != null) {
            event.setLocation(newEvent.getLocation());
        }

        // add reminder
        if (newEvent.getReminder() != null) {
            EventReminder eventReminderEmail = new EventReminder();
            eventReminderEmail.setMethod(this.EVENT_REMINDER_METHOD_EMAIL);
            eventReminderEmail.setMinutes((int) (newEvent.getStartDate()
                    .getTime() - newEvent.getReminder().getTime())
                    / (60 * 1000));

            EventReminder eventReminderPopUp = new EventReminder();
            eventReminderPopUp.setMethod(this.EVENT_REMINDER_METHOD_POPUP);
            eventReminderPopUp.setMinutes((int) (newEvent.getStartDate()
                    .getTime() - newEvent.getReminder().getTime())
                    / (60 * 1000));

            List<EventReminder> eventReminderList = new ArrayList<EventReminder>();
            eventReminderList.add(eventReminderEmail);
            eventReminderList.add(eventReminderPopUp);

            Reminders reminders = new Reminders();
            reminders.setUseDefault(false);
            reminders.setOverrides(eventReminderList);

            event.setReminders(reminders);
        }

        // insert event
        this.clientCalendar.events().insert(this.calendar.getId(), event)
                .execute();
    }

    private void addTask(Task newTask) throws IOException {
        logger.info("Adding task: " + newTask.getTitle());
        com.google.api.services.tasks.model.Task task = new com.google.api.services.tasks.model.Task();

        // add title
        task.setTitle(newTask.getTitle());

        // add description/note
        if (newTask.getDescription() != null) {
            task.setNotes(newTask.getDescription());
        }

        // add end date/due date
        if (newTask.getEndDate() != null) {
            DateTime dateTime = new DateTime(newTask.getEndDate(),
                    TimeZone.getDefault());
            task.setDue(dateTime);
        }

        this.clientTask.tasks().insert(this.taskList.getId(), task).execute();
    }

    private void createCalendar() throws IOException {
        logger.info("Adding a new calendar...");
        Calendar entry = new Calendar();
        entry.setSummary(NormalMessage.APP_NAME);
        this.calendar = this.clientCalendar.calendars().insert(entry).execute();

    }

    private void createOrGetCalendar() throws IOException {
        if (this.DATA_STORE_CALENDAR_INFO_FILE.exists()) {
            this.getCalendarInfo();
        } else {
            this.createCalendar();
            this.addAllEvents();
            this.saveCalendarInfo();
        }
    }

    private void createOrGetTaskList() throws IOException {
        if (this.DATA_STORE_TASKLIST_INFO_FILE.exists()) {
            this.getTaskListInfo();
        } else {
            this.createTaskList();
            this.addAllTasks();
            this.saveTaskListInfo();
        }
    }

    private void createTaskList() throws IOException {
        logger.info("Adding a new task list...");
        TaskList entry = new TaskList();
        entry.setTitle(NormalMessage.APP_NAME);
        this.taskList = this.clientTask.tasklists().insert(entry).execute();
    }

    private void deleteFile(File file) {
        try {
            if (file.delete()) {
                logger.info("Deleted " + file);
            } else {
                logger.debug("Failed to delete " + file);
            }
        } catch (Exception e) {
            logger.debug("Failed to delete " + file);
            e.printStackTrace();
        }

    }

    private void getCalendarInfo() throws IOException {
        logger.info("Getting user's calendar info...");
        String json = new String(
                Files.readAllBytes(this.DATA_STORE_CALENDAR_INFO_FILE.toPath()));
        this.calendar = new Calendar();
        this.calendar = this.gson.fromJson(json, Calendar.class);
    }

    private void getClientSecrets() {
        try {
            logger.info("Getting client secrets...");
            this.clientSecrets = GoogleClientSecrets.load(
                    jsonFactory,
                    new InputStreamReader(GoogleStorage.class
                            .getResourceAsStream(this.CLIENT_SECRETS_FILE)));
        } catch (Exception e) {
            logger.debug(this.CLIENT_SECRETS_FILE
                    + ERROR_MESSAGE_FILE_NOT_FOUND);
            System.err.println(this.CLIENT_SECRETS_FILE
                    + ERROR_MESSAGE_FILE_NOT_FOUND);
            System.exit(1);
        }
    }

    private void getTaskListInfo() throws IOException {
        logger.info("Getting user's tasklist info...");
        String json = new String(
                Files.readAllBytes(this.DATA_STORE_TASKLIST_INFO_FILE.toPath()));
        this.taskList = this.gson.fromJson(json, TaskList.class);
    }

    private void initialize() throws Exception {
        this.initializeTransport();
        this.initializeDataStoreFactory();
        this.getClientSecrets();
        this.setUpAuthorizationCodeFlow();
        this.setUpGlobalCaleandarInstance();
        this.setUpGlobalTaskInstance();
        this.createOrGetCalendar();
        this.createOrGetTaskList();
    }

    private void initializeDataStoreFactory() throws IOException {
        logger.info("Initializing DataStoreFactory...");
        dataStoreFactory = new FileDataStoreFactory(this.DATA_STORE_DIR);
    }

    private void initializeTransport() throws GeneralSecurityException,
            IOException {
        logger.info("Initializing Transport...");
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    private void saveCalendarInfo() {
        logger.info("Saving calendar info to "
                + this.DATA_STORE_CALENDAR_INFO_FILE);
        String json = this.gson.toJson(this.calendar);
        try {
            Files.write(this.DATA_STORE_CALENDAR_INFO_FILE.toPath(),
                    json.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void saveTaskListInfo() {
        logger.info("Saving tasklist info to "
                + this.DATA_STORE_TASKLIST_INFO_FILE);
        this.taskList.setUpdated(null);
        String json = this.gson.toJson(this.taskList);
        try {
            Files.write(this.DATA_STORE_TASKLIST_INFO_FILE.toPath(),
                    json.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private void setUpAuthorizationCodeFlow() throws IOException {
        logger.info("Setting up authorization code flow...");
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, this.clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR + " "
                        + TasksScopes.TASKS)).setDataStoreFactory(
                dataStoreFactory).build();
        this.credential = new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver()).authorize(this.userId);
    }

    private void setUpGlobalCaleandarInstance() {
        logger.info("Setting up global caleandar instance...");
        this.clientCalendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, jsonFactory, this.credential)
        .setApplicationName(NormalMessage.APP_NAME).build();

    }

    private void setUpGlobalTaskInstance() {
        logger.info("Setting up global task instance...");
        this.clientTask = new com.google.api.services.tasks.Tasks.Builder(
                httpTransport, jsonFactory, this.credential)
        .setApplicationName(NormalMessage.APP_NAME).build();
    }

}
