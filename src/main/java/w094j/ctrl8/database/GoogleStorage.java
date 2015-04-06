package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

//@author A0112521B
/**
 * Google Storage
 */
public class GoogleStorage extends Storage {
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = " file not found";
    private static FileDataStoreFactory fileDataStoreFactory;
    private static HttpTransport httpTransport;
    private static JsonFactory jsonFactory = new JacksonFactory();
    private static Logger logger = LoggerFactory.getLogger(GoogleStorage.class);
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
    private final String GOOGLE_EVENT_REMINDER_METHOD_EMAIL = "email";
    private final String GOOGLE_EVENT_REMINDER_METHOD_POPUP = "popup";
    private final String GOOGLE_TASK_COMPLETED = "completed";
    private Calendar googleCalendar;
    private TaskList googleTaskList;
    private Gson gson;
    private List<String> tempTaskIdList;
    private HashMap<String, Date> toBeDeleted; //googleid, last modified date
    private String userId = "user";

    /**
     * @param file
     * @param gson
     * @throws Exception
     */
    public GoogleStorage(DBfile file, Gson gson) throws Exception {
        super(file);
        this.dbFile = file;
        this.tempTaskIdList = this.getTaskIdList();
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
            this.clientCalendar.calendars().delete(this.googleCalendar.getId())
                    .execute();

            logger.info("Deleting Google " + NormalMessage.APP_NAME
                    + " Task List");
            this.clientTask.tasklists().delete(this.googleTaskList.getId())
            .execute();

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
    public void storeData() throws IOException {
        // TODO: Change after the key is task ID instead of task title
        List<Task> localTasklist = new ArrayList<Task>(this.dbFile.getData()
                .getTask().getTaskMap().values());

        if (this.tempTaskIdList.size() > localTasklist.size()) {
            for (String taskId : this.tempTaskIdList) {
                if (!localTasklist.contains(taskId)) {
                    Task toBeDeletedTask = this.dbFile.getData().getTask()
                            .getTaskMap().get(taskId);
                    this.toBeDeleted.put(toBeDeletedTask.getGoogleId(),
                            new Date());
                    this.tempTaskIdList.remove(taskId);
                }
            }

        }

        for (Task i : localTasklist) {
            if ((i.getIsSynced() == null) || !i.getIsSynced()) {
                if (i.getTaskType() == TaskType.TIMED) {
                    this.addEvent(i);
                } else {
                    this.addTask(i);
                }
            }
        }

    }

    private void addAllEventsAndTasks() throws IOException {
        logger.info("Adding all events and tasks...");
        List<Task> localTasklist = new ArrayList<Task>(this.dbFile.getData()
                .getTask().getTaskMap().values());
        for (Task i : localTasklist) {
            if (i.getTaskType() == TaskType.TIMED) {
                this.addEvent(i);
            } else {
                this.addTask(i);
            }
        }
        logger.info("Added all events and tasks");
    }

    private void addEvent(Task localEvent) throws IOException {
        logger.info("Adding event: " + localEvent.getTitle());
        DateTime dateTime;

        Event googleEvent = new Event();

        // set google id
        if (localEvent.getGoogleId() != null) {
            googleEvent.setId(localEvent.getGoogleId());
        }

        // set title
        googleEvent.setSummary(localEvent.getTitle());

        // set start date
        dateTime = new DateTime(localEvent.getStartDate(),
                TimeZone.getDefault());
        googleEvent.setStart(new EventDateTime().setDateTime(dateTime));

        // set end date
        dateTime = new DateTime(localEvent.getEndDate(), TimeZone.getDefault());
        googleEvent.setEnd(new EventDateTime().setDateTime(dateTime));

        // set description
        if (localEvent.getDescription() != null) {
            googleEvent.setDescription(localEvent.getDescription());
        }

        // set location
        if (localEvent.getLocation() != null) {
            googleEvent.setLocation(localEvent.getLocation());
        }

        // set reminder
        if (localEvent.getReminder() != null) {
            EventReminder eventReminderEmail = new EventReminder();
            eventReminderEmail
                    .setMethod(this.GOOGLE_EVENT_REMINDER_METHOD_EMAIL);
            eventReminderEmail.setMinutes((int) (localEvent.getStartDate()
                    .getTime() - localEvent.getReminder().getTime())
                    / (60 * 1000));

            EventReminder eventReminderPopUp = new EventReminder();
            eventReminderPopUp
                    .setMethod(this.GOOGLE_EVENT_REMINDER_METHOD_POPUP);
            eventReminderPopUp.setMinutes((int) (localEvent.getStartDate()
                    .getTime() - localEvent.getReminder().getTime())
                    / (60 * 1000));

            List<EventReminder> eventReminderList = new ArrayList<EventReminder>();
            eventReminderList.add(eventReminderEmail);
            eventReminderList.add(eventReminderPopUp);

            Reminders reminders = new Reminders();
            reminders.setUseDefault(false);
            reminders.setOverrides(eventReminderList);

            googleEvent.setReminders(reminders);
        }

        // insert event
        Event updatedGoogleEvent = this.clientCalendar.events()
                .insert(this.googleCalendar.getId(), googleEvent).execute();

        // update local event
        if (localEvent.getGoogleId() == null) {
            localEvent.setGoogleId(updatedGoogleEvent.getId());
        }
        localEvent.setEtag(updatedGoogleEvent.getEtag());
    }

    private void addTask(Task localTask) throws IOException {
        logger.info("Adding task: " + localTask.getTitle());
        com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();

        // set google id
        if (localTask.getGoogleId() != null) {
            googleTask.setId(localTask.getGoogleId());
        }

        // set title
        googleTask.setTitle(localTask.getTitle());

        // set status
        if (localTask.getStatus()) {
            googleTask.setStatus(this.GOOGLE_TASK_COMPLETED);
        }
        // set description/note
        if (localTask.getDescription() != null) {
            googleTask.setNotes(localTask.getDescription());
        }

        // set end date/due date
        if (localTask.getEndDate() != null) {
            DateTime dateTime = new DateTime(localTask.getEndDate(),
                    TimeZone.getDefault());
            googleTask.setDue(dateTime);
        }

        // upload local task
        googleTask = this.clientTask.tasks()
                .insert(this.googleTaskList.getId(), googleTask).execute();
        localTask.setEtag(googleTask.getEtag());
    }

    private void createCalendar() throws IOException {
        logger.info("Adding a new calendar...");
        Calendar entry = new Calendar();
        entry.setSummary(NormalMessage.APP_NAME);
        this.googleCalendar = this.clientCalendar.calendars().insert(entry)
                .execute();

    }

    private void createOrGetCalendarAndTaskList() throws IOException {
        if (this.DATA_STORE_CALENDAR_INFO_FILE.exists()
                && this.DATA_STORE_TASKLIST_INFO_FILE.exists()) {
            this.getCalendarInfo();
            this.getTaskListInfo();
        } else {
            this.createCalendar();
            this.createTaskList();
            this.addAllEventsAndTasks();
            this.saveCalendarInfo();
            this.saveTaskListInfo();
        }
    }

    private void createTaskList() throws IOException {
        logger.info("Adding a new task list...");
        TaskList entry = new TaskList();
        entry.setTitle(NormalMessage.APP_NAME);
        this.googleTaskList = this.clientTask.tasklists().insert(entry)
                .execute();
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
        this.googleCalendar = new Calendar();
        this.googleCalendar = this.gson.fromJson(json, Calendar.class);
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

    private List<String> getTaskIdList() {
        List<String> tempList = new ArrayList<String>();
        List<Task> localTasklist = new ArrayList<Task>(this.dbFile.getData()
                .getTask().getTaskMap().values());
        for (Task i : localTasklist) {
            tempList.add(i.getId());
        }
        return tempList;
    }

    private void getTaskListInfo() throws IOException {
        logger.info("Getting user's tasklist info...");
        String json = new String(
                Files.readAllBytes(this.DATA_STORE_TASKLIST_INFO_FILE.toPath()));
        this.googleTaskList = this.gson.fromJson(json, TaskList.class);
    }

    private void initialize() throws Exception {
        this.initializeTransport();
        this.initializeDataStoreFactory();
        this.getClientSecrets();
        this.setUpAuthorizationCodeFlow();
        this.setUpGlobalInstance();
        this.createOrGetCalendarAndTaskList();
    }

    private void initializeDataStoreFactory() throws IOException {
        logger.info("Initializing DataStoreFactory...");
        fileDataStoreFactory = new FileDataStoreFactory(this.DATA_STORE_DIR);
    }

    private void initializeTransport() throws GeneralSecurityException,
            IOException {
        logger.info("Initializing Transport...");
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    private void saveCalendarInfo() {
        logger.info("Saving calendar info to "
                + this.DATA_STORE_CALENDAR_INFO_FILE);
        String json = this.gson.toJson(this.googleCalendar);
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
        this.googleTaskList.setUpdated(null);
        String json = this.gson.toJson(this.googleTaskList);
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
                fileDataStoreFactory).build();
        this.credential = new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver()).authorize(this.userId);
    }

    private void setUpGlobalInstance() {
        logger.info("Setting up global caleandar instance...");
        this.clientCalendar = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, jsonFactory, this.credential)
        .setApplicationName(NormalMessage.APP_NAME).build();

        logger.info("Setting up global task instance...");
        this.clientTask = new com.google.api.services.tasks.Tasks.Builder(
                httpTransport, jsonFactory, this.credential)
        .setApplicationName(NormalMessage.APP_NAME).build();

    }

}
