//@author A0112521B
package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.parse.statement.CommandType;
import w094j.ctrl8.pojo.Actions;
import w094j.ctrl8.pojo.DBfile;
import w094j.ctrl8.pojo.Task;
import w094j.ctrl8.pojo.Task.TaskType;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.Tasks;
import com.google.gson.Gson;

/**
 * Google Calendar Storage
 */
public class GoogleStorage extends Storage {
    private static DataStore<String> dataStoreEvent;
    private static FileDataStoreFactory dataStoreFactory;
    private static DataStore<String> dataStoreSyncSettings;
    private static DataStore<String> dataStoreTask;
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = " file not found";
    private static final int FULL_SYNC_YEAR_FROM_NOW = -1;
    private static final String GOOGLE_EVENT_STATUS_CANCELLED = "cancelled";
    private static final String GOOGLE_EVENT_STATUS_CONFIRMED = "confirmed";
    private static final String GOOGLE_TASK_STATUS_COMPLETED = "completed";
    private static final String GOOGLE_TASK_STATUS_NEEDS_ACTION = "needsAction";
    private static HttpTransport httpTransport;
    private static JsonFactory jsonFactory = new JacksonFactory();
    private static Logger logger = LoggerFactory.getLogger(GoogleStorage.class);
    private static final String NO_TITLE = "(No title)";
    private static final String SYNC_TOKEN_KEY = "syncToken";
    private com.google.api.services.calendar.Calendar.Events.List calendarRequest;
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
    private final java.io.File DATA_STORE_INETERNET_STATUS_LAST_SESSION = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/InternetStatusLastSession");
    private final java.io.File DATA_STORE_TASKLIST_INFO_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/TaskListInfo");
    private DBfile dbFile;
    private final String EVENT_REMINDER_METHOD_EMAIL = "email";
    private final String EVENT_REMINDER_METHOD_POPUP = "popup";
    private final String EVENT_STORE = "EventStore";
    private com.google.api.services.calendar.model.Events events;
    private final String GOOGLE_WEBSITE = "www.google.com";
    private Calendar googleCalendar;
    private TaskList googleTaskList;
    private Gson gson;
    private boolean isInternetReachableLastSession;
    private final String SYNC_SETTINGS = "SyncSettings";
    private final String TASK_STORE = "TaskStore";
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
    public void deleteCalendarAndTaskList() {
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
        this.deleteFile(this.DATA_STORE_INETERNET_STATUS_LAST_SESSION);
        this.deleteFile(this.DATA_STORE_DIR);
    }

    @Override
    public void readData() {

    }

    @Override
    public void storeData() throws Exception, IOException {
        boolean isInternetReachableThisSession = this.isInternetReachable();

        if (isInternetReachableThisSession) {
            ArrayList<Actions> actionList = this.dbFile.getData().getTask()
                    .getActionsList();
            Actions action = actionList.get(actionList.size() - 1);
            CommandType command = action.getStatement().getCommand();
            String taskId = action.getTaskID();
            Task localTask = this.dbFile.getData().getTask().getTaskStateMap()
                    .get(taskId).getFinalTask();

            switch (command) {
                case ADD :
                    this.addToGoogle(localTask);
                    break;
                case DELETE :
                    this.deleteFromGoogle();
                    break;
                case DONE :
                    this.markAsDoneInGoogle(localTask);
                    break;
                case MODIFY :
                    this.modifyInGoogle(localTask);
                    break;
                default :
                    break;
            }
            this.sync();
            if (!this.isInternetReachableLastSession) {
                //this.uploadUnsyncedEventsAndTasksToGoogle();
            }
        }

        if (isInternetReachableThisSession != this.isInternetReachableLastSession) {
            this.saveIntenetStatusInfo();
        }
    }

    /**
     * Sync with Google Calendar and Google Task
     *
     * @throws Exception
     */
    public void sync() throws Exception {
        logger.info("Syncing...");
        this.syncGoogleCalendar();
        this.syncGoogleTasks();
    }

    private void addGoogleEvent(Task localEvent) throws IOException {
        logger.info("Adding event: " + localEvent.getTitle());
        Event googleEvent = new Event();

        this.setGoogleEventFields(googleEvent, localEvent);
        googleEvent.setStatus(GOOGLE_EVENT_STATUS_CONFIRMED);

        // insert event
        googleEvent = this.clientCalendar.events()
                .insert(this.googleCalendar.getId(), googleEvent).execute();

        // set google id and etag
        localEvent.setGoogleId(googleEvent.getId());
        localEvent.setEtag(googleEvent.getEtag());

        logger.info("Saving dataStoreEvent: " + localEvent.getTitle());
        dataStoreEvent.set(googleEvent.getId(), localEvent.getId());
    }

    private void addGoogleTask(Task localTask) throws IOException {
        logger.info("Adding task: " + localTask.getTitle());
        com.google.api.services.tasks.model.Task googleTask = new com.google.api.services.tasks.model.Task();

        this.setGoogleTaskFields(googleTask, localTask);
        googleTask.setDeleted(false);

        // insert task
        googleTask = this.clientTask.tasks()
                .insert(this.googleTaskList.getId(), googleTask).execute();

        // set google id and etag
        localTask.setGoogleId(googleTask.getId());
        localTask.setEtag(googleTask.getEtag());

        logger.info("Saving dataStoreTask: " + localTask.getTitle());
        dataStoreTask.set(googleTask.getId(), localTask.getId());
    }

    private void addLocalEvent(Event googleEvent) throws Exception {
        logger.info("Adding new event from Google: " + googleEvent.getSummary());
        Task localEvent = new Task();
        this.setLocalEventFields(googleEvent, localEvent);
        logger.info("Saving dataStoreEvent: " + localEvent.getTitle());
        dataStoreEvent.set(googleEvent.getId(), localEvent.getId());

        this.dbFile.getData().getTask().updateTaskMap(localEvent, null, false);
    }

    private void addLocalTask(
            com.google.api.services.tasks.model.Task googleTask)
                    throws Exception {
        logger.info("Adding new task from Google: " + googleTask.getTitle()
                + " " + googleTask.getNotes());
        Task localTask = new Task();
        this.setLocalTaskFields(googleTask, localTask);
        logger.info("Saving dataStoreTask: " + localTask.getTitle());
        dataStoreTask.set(googleTask.getId(), localTask.getId());

        this.dbFile.getData().getTask().updateTaskMap(localTask, null, false);
    }

    private void addToGoogle(Task localTask) throws IOException {
        if (localTask.getTaskType() == TaskType.TIMED) {
            this.addGoogleEvent(localTask);
        } else {
            this.addGoogleTask(localTask);
        }
    }

    private void createCalendar() throws IOException {
        logger.info("Adding a new calendar...");
        Calendar entry = new Calendar();
        entry.setSummary(NormalMessage.APP_NAME);
        this.googleCalendar = this.clientCalendar.calendars().insert(entry)
                .execute();
    }

    private void createOrGetCalendar() throws IOException {
        if (this.DATA_STORE_CALENDAR_INFO_FILE.exists()) {
            this.getCalendarInfo();
        } else {
            this.createCalendar();
            this.saveCalendarInfo();
        }
    }

    private void createOrGetIntenetStatusLastSession() throws IOException {
        if (this.DATA_STORE_INETERNET_STATUS_LAST_SESSION.exists()) {
            this.getInternetStatusLastSession();
        } else {
            this.saveIntenetStatusInfo();
        }
    }

    private void createOrGetTaskList() throws IOException {
        if (this.DATA_STORE_TASKLIST_INFO_FILE.exists()) {
            this.getTaskListInfo();
        } else {
            this.createTaskList();
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

    private void deleteFromGoogle() throws IOException {
        logger.info("Deleting Google Event/Task...");
        List<Task> localTaskList = Arrays.asList(this.dbFile.getData()
                .getTask().getTaskList());
        ArrayList<String> googleTaskIdList = new ArrayList<String>(
                dataStoreTask.keySet());
        ArrayList<String> googleEventIdList = new ArrayList<String>(
                dataStoreEvent.keySet());

        for (Task i : localTaskList) {
            googleTaskIdList.remove(i.getGoogleId());
            googleEventIdList.remove(i.getGoogleId());
        }

        for (String googleId : googleTaskIdList) {
            this.deleteGoogleTask(googleId);
        }

        for (String googleId : googleEventIdList) {
            this.deleteGoogleEvent(googleId);
        }
    }

    private void deleteGoogleEvent(String googleId) throws IOException {
        logger.info("Deleting Google Event: " + googleId);
        logger.info("Deleting dataStoreEvent: " + googleId);
        dataStoreEvent.delete(googleId);
        try {
            this.clientCalendar.events()
                    .delete(this.googleCalendar.getId(), googleId).execute();
        } catch (IOException e) {
            logger.info("Fail to delete Google Event: " + googleId);
        }
    }

    private void deleteGoogleTask(String googleId) throws IOException {
        logger.info("Deleting Google Task: " + googleId);
        logger.info("Deleting dataStoreTask: " + googleId);
        dataStoreTask.delete(googleId);
        try {
            this.clientTask.tasks()
                    .delete(this.googleTaskList.getId(), googleId).execute();
        } catch (IOException e) {
            logger.info("Fail to delete Google Task: " + googleId);
        }
    }

    private void deleteLocalTask(Task localTask) throws IOException {
        if (localTask != null) {
            logger.info("Deleting local task/event: " + localTask.getTitle());
            if (localTask.getTaskType() == TaskType.TIMED) {
                logger.info("Deleting dataStoreEvent: " + localTask.getTitle());
                dataStoreEvent.delete(localTask.getGoogleId());
            } else {
                logger.info("Deleting dataStoreTask: " + localTask.getTitle());
                dataStoreTask.delete(localTask.getGoogleId());
            }
            this.dbFile.getData().getTask().getTaskStateMap()
                    .get(localTask.getId()).setFinalTask(null);
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

    private void getDataStoreInfo() throws IOException {
        dataStoreEvent = dataStoreFactory.getDataStore(this.EVENT_STORE);
        dataStoreTask = dataStoreFactory.getDataStore(this.TASK_STORE);
        dataStoreSyncSettings = dataStoreFactory
                .getDataStore(this.SYNC_SETTINGS);
    }

    private void getInternetStatusLastSession() throws IOException {
        logger.info("Getting Internet status last session...");
        String json = new String(
                Files.readAllBytes(this.DATA_STORE_INETERNET_STATUS_LAST_SESSION
                        .toPath()));
        this.isInternetReachableLastSession = this.gson.fromJson(json,
                Boolean.class);
    }

    private Task getLocalEventByGoogleId(String googleId) {
        String taskId = null;
        try {
            taskId = dataStoreEvent.get(googleId);
        } catch (IOException e) {
            return null;
        }
        if (taskId != null) {
            return this.dbFile.getData().getTask().getTask(taskId);
        }
        return null;
    }

    private Task getLocalTaskByGoogleId(String googleId) {
        String taskId = null;
        try {
            taskId = dataStoreTask.get(googleId);
        } catch (IOException e) {
            return null;
        }
        if (taskId != null) {
            return this.dbFile.getData().getTask().getTask(taskId);
        }
        return null;
    }

    private java.util.Date getStartSyncDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(java.util.Calendar.YEAR, FULL_SYNC_YEAR_FROM_NOW);
        return cal.getTime();
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
        this.setUpGlobalCaleandarInstance();
        this.setUpGlobalTaskInstance();
        this.createOrGetCalendar();
        this.createOrGetTaskList();
        this.createOrGetIntenetStatusLastSession();
        this.getDataStoreInfo();
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

    private boolean isInternetReachable() {
        try {
            InetAddress.getByName(this.GOOGLE_WEBSITE);
            logger.info("Has internet connection");
        } catch (UnknownHostException e) {
            logger.info("No internet connection");
            return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    private boolean isValidGoogleEventId(String googleId) {
        try {
            this.clientCalendar.events()
                    .get(this.googleCalendar.getId(), googleId).execute();
            logger.info("Valid Google Event ID: " + googleId);
            return true;
        } catch (IOException e) {
            logger.info("Invalid Google Event ID: " + googleId);
            return false;
        }
    }

    @SuppressWarnings("unused")
    private boolean isValidGoogleTaskId(String googleId) {
        try {
            this.clientTask.tasks().get(this.googleTaskList.getId(), googleId)
                    .execute();
            logger.info("Valid Google Task ID: " + googleId);
            return true;
        } catch (IOException e) {
            logger.info("Invalid Google Task ID: " + googleId);
            return false;
        }
    }

    private void loadSyncToken() throws IOException {
        logger.info("Loading Sync Token...");
        String syncToken = dataStoreSyncSettings.get(SYNC_TOKEN_KEY);

        if (syncToken == null) {
            this.calendarRequest.setTimeMin(new DateTime(this
                    .getStartSyncDate(), TimeZone.getDefault()));
        } else {
            this.calendarRequest.setSyncToken(syncToken);
        }
    }

    private void markAsDoneInGoogle(Task localTask) throws IOException {
        if (localTask.getTaskType() == TaskType.TIMED) {
            Event googleEvent = this.clientCalendar.events()
                    .get(this.googleCalendar.getId(), localTask.getGoogleId())
                    .execute();
            try {
                this.syncEvent(localTask, googleEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            com.google.api.services.tasks.model.Task googleTask = this.clientTask
                    .tasks()
                    .get(this.googleTaskList.getId(), localTask.getGoogleId())
                    .execute();
            try {
                this.syncTask(localTask, googleTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void modifyInGoogle(Task localTask) throws Exception {

        if (localTask.getTaskType() == TaskType.TIMED) {
            if (dataStoreEvent.containsKey(localTask.getGoogleId())) {
                // modify event
                Event googleEvent = this.clientCalendar
                        .events()
                        .get(this.googleCalendar.getId(),
                                localTask.getGoogleId()).execute();
                try {
                    this.syncEvent(localTask, googleEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // change task -> event
                if (dataStoreTask.containsKey(localTask.getGoogleId())) {
                    this.deleteGoogleTask(localTask.getGoogleId());
                }
                this.addGoogleEvent(localTask);
            }
        } else {
            if (dataStoreTask.containsKey(localTask.getGoogleId())) {
                // modify task
                com.google.api.services.tasks.model.Task googleTask = this.clientTask
                        .tasks()
                        .get(this.googleTaskList.getId(),
                                localTask.getGoogleId()).execute();
                this.syncTask(localTask, googleTask);
            } else {
                // change event -> task
                if (dataStoreEvent.containsKey(localTask.getGoogleId())) {
                    this.deleteGoogleEvent(localTask.getGoogleId());
                }
                this.addGoogleEvent(localTask);
            }
        }

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

    private void saveIntenetStatusInfo() throws IOException {
        logger.info("Saving Internet Status info to "
                + this.DATA_STORE_INETERNET_STATUS_LAST_SESSION);
        String json = this.gson.toJson(this.isInternetReachable());
        try {
            Files.write(this.DATA_STORE_INETERNET_STATUS_LAST_SESSION.toPath(),
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

    private void setGoogleEventFields(Event googleEvent, Task localEvent) {
        DateTime dateTime;

        // set title/summary
        googleEvent.setSummary(localEvent.getTitle());

        // set start date
        dateTime = new DateTime(localEvent.getStartDate(),
                TimeZone.getDefault());
        googleEvent.setStart(new EventDateTime().setDateTime(dateTime));

        // set end date
        dateTime = new DateTime(localEvent.getEndDate(), TimeZone.getDefault());
        googleEvent.setEnd(new EventDateTime().setDateTime(dateTime));

        // set description
        googleEvent.setDescription(localEvent.getDescription());

        // set location
        googleEvent.setLocation(localEvent.getLocation());

        // set reminder
        if (localEvent.getReminder() != null) {
            EventReminder eventReminderEmail = new EventReminder();
            eventReminderEmail.setMethod(this.EVENT_REMINDER_METHOD_EMAIL);
            eventReminderEmail.setMinutes((int) (localEvent.getStartDate()
                    .getTime() - localEvent.getReminder().getTime())
                    / (60 * 1000));

            EventReminder eventReminderPopUp = new EventReminder();
            eventReminderPopUp.setMethod(this.EVENT_REMINDER_METHOD_POPUP);
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
        } else {
            googleEvent.setReminders(null);
        }

    }

    private void setGoogleTaskFields(
            com.google.api.services.tasks.model.Task googleTask, Task localTask) {
        // set title
        googleTask.setTitle(localTask.getTitle());

        // set status
        if (localTask.getStatus()) {
            googleTask.setStatus(GOOGLE_TASK_STATUS_COMPLETED);
        } else {
            googleTask.setStatus(GOOGLE_TASK_STATUS_NEEDS_ACTION);
        }

        // set description/note
        googleTask.setNotes(localTask.getDescription());

        // set end date/due date
        if (localTask.getEndDate() != null) {
            DateTime dateTime = new DateTime(localTask.getEndDate(),
                    TimeZone.getDefault());
            googleTask.setDue(dateTime);
        } else {
            googleTask.setDue(null);
        }
    }

    private void setLocalEventFields(Event googleEvent, Task localEvent)
            throws Exception {
        if (googleEvent.getSummary() != null) {
            localEvent.setTitle(googleEvent.getSummary());
        } else {
            localEvent.setTitle(NO_TITLE);
        }

        if (googleEvent.getDescription() != null) {
            localEvent.setDescription(googleEvent.getDescription());
        }
        if (googleEvent.getStart() != null) {
            localEvent.setStartDate(new Date(googleEvent.getStart()
                    .getDateTime().getValue()));
        }
        if (googleEvent.getEnd() != null) {
            localEvent.setEndDate(new Date(googleEvent.getEnd().getDateTime()
                    .getValue()));
        }
        if (googleEvent.getLocation() != null) {
            localEvent.setLocation(googleEvent.getLocation());
        }
        if (googleEvent.getReminders().getOverrides() == null) {
            localEvent.setReminder(null);
        } else {
            int minutesBeforeStartDate = googleEvent.getReminders()
                    .getOverrides().get(0).getMinutes();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(new Date(googleEvent.getStart().getDateTime()
                    .getValue()));
            cal.add(java.util.Calendar.MINUTE, (-1) * minutesBeforeStartDate);
            localEvent.setReminder(cal.getTime());
        }
        localEvent.toCompleteTask();
        localEvent.setEtag(googleEvent.getEtag());
    }

    private void setLocalTaskFields(
            com.google.api.services.tasks.model.Task googleTask, Task localTask)
            throws Exception {
        if ((googleTask.getTitle() == null) || googleTask.getTitle().equals("")) {
            localTask.setTitle(NO_TITLE);
        } else {
            localTask.setTitle(googleTask.getTitle());
        }
        localTask.setDescription(googleTask.getNotes());
        if (googleTask.getDue() != null) {
            localTask.setEndDate(new Date(googleTask.getDue().getValue()));
        }
        if ((googleTask.getStatus() != null)
                && googleTask.getStatus().equals(GOOGLE_TASK_STATUS_COMPLETED)) {
            localTask.setStatus(true);
        } else {
            localTask.setStatus(false);
        }
        localTask.toCompleteTask();
        localTask.setEtag(googleTask.getEtag());
    }

    private void setNextSyncToken() throws IOException {
        logger.info("Setting next sync token...");
        dataStoreSyncSettings.set(SYNC_TOKEN_KEY,
                this.events.getNextSyncToken());
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

    private void syncAllEvents() throws Exception {
        logger.info("Syncing all events...");
        String pageToken = null;
        this.events = null;
        do {
            this.calendarRequest.setPageToken(pageToken);

            try {
                this.events = this.calendarRequest.execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 410) {
                    logger.info("Invalid sync token, clearing event store and re-syncing...");
                    dataStoreSyncSettings.delete(SYNC_TOKEN_KEY);
                    dataStoreEvent.clear();
                    this.sync();
                } else {
                    throw e;
                }
            }

            List<Event> items = this.events.getItems();
            if (items.size() == 0) {
                logger.info("No new events to sync.");
            } else {
                for (Event event : items) {
                    this.syncEventFromGoogle(event);
                }
            }

            pageToken = this.events.getNextPageToken();
        } while (pageToken != null);
    }

    private void syncEvent(Task localEvent, Event googleEvent) throws Exception {
        logger.info("Updating event: " + localEvent.getTitle());

        Date googleEventLastModifiedTime = new Date(googleEvent.getUpdated()
                .getValue());

        if (localEvent.getLastModifiedTime().after(googleEventLastModifiedTime)) {
            this.updateGoogleEvent(googleEvent, localEvent);
        } else {
            this.updateLocalEvent(googleEvent, localEvent);
        }
    }

    private void syncEventFromGoogle(Event googleEvent) throws Exception {
        Task localEvent = this.getLocalEventByGoogleId(googleEvent.getId());

        if (GOOGLE_EVENT_STATUS_CANCELLED.equals(googleEvent.getStatus())
                && dataStoreEvent.containsKey(googleEvent.getId())) {
            if (localEvent != null) {
                this.deleteLocalTask(localEvent);
            }
        } else {
            if (localEvent != null) {
                this.syncEvent(localEvent, googleEvent);
            } else {
                this.addLocalEvent(googleEvent);
            }
        }
    }

    private void syncGoogleCalendar() throws Exception {
        logger.info("Syncing Google Calendar...");
        this.calendarRequest = this.clientCalendar.events().list(
                this.googleCalendar.getId());
        this.loadSyncToken();
        this.syncAllEvents();
        this.setNextSyncToken();
    }

    private void syncGoogleTasks() throws Exception {
        logger.info("Syncing Google Task...");
        Tasks tasks = this.clientTask.tasks().list(this.googleTaskList.getId())
                .execute();
        List<com.google.api.services.tasks.model.Task> items = tasks.getItems();
        ArrayList<String> googleTaskIdList = new ArrayList<String>(
                dataStoreTask.keySet());

        for (com.google.api.services.tasks.model.Task googleTask : items) {
            Task localTask = this.getLocalTaskByGoogleId(googleTask.getId());
            if (localTask == null) {
                this.addLocalTask(googleTask);
                googleTaskIdList.remove(googleTask.getId());
            } else {
                this.syncTask(localTask, googleTask);
                googleTaskIdList.remove(googleTask.getId());
            }
        }

        for (String googleId : googleTaskIdList) {
            this.deleteLocalTask(this.getLocalTaskByGoogleId(googleId));
        }
    }

    private void syncTask(Task localTask,
            com.google.api.services.tasks.model.Task googleTask)
                    throws Exception {
        logger.info("Updating task: " + localTask.getTitle());

        Date googleTaskLastModifiedTime = new Date(googleTask.getUpdated()
                .getValue());

        if (localTask.getLastModifiedTime().after(googleTaskLastModifiedTime)) {
            this.updateGoogleTask(googleTask, localTask);
        } else {
            this.updateLocalTask(googleTask, localTask);
        }
    }

    private void updateGoogleEvent(Event googleEvent, Task localEvent)
            throws IOException {
        this.setGoogleEventFields(googleEvent, localEvent);

        googleEvent = this.clientCalendar
                .events()
                .update(this.googleCalendar.getId(), localEvent.getGoogleId(),
                        googleEvent).execute();

        localEvent.setEtag(googleEvent.getEtag());
    }

    private void updateGoogleTask(
            com.google.api.services.tasks.model.Task googleTask, Task localTask)
            throws IOException {
        this.setGoogleTaskFields(googleTask, localTask);
        googleTask = this.clientTask
                .tasks()
                .update(this.googleTaskList.getId(), localTask.getId(),
                        googleTask).execute();
        localTask.setEtag(googleTask.getEtag());
    }

    private void updateLocalEvent(Event googleEvent, Task localEvent)
            throws Exception {
        this.setLocalEventFields(googleEvent, localEvent);
    }

    private void updateLocalTask(
            com.google.api.services.tasks.model.Task googleTask, Task localTask)
            throws Exception {
        this.setLocalTaskFields(googleTask, localTask);
    }

    private void uploadUnsyncedEventsAndTasksToGoogle() throws Exception {
        logger.info("Uploading unsynced Events and Tasks");
        List<Task> localTaskList = Arrays.asList(this.dbFile.getData()
                .getTask().getTaskList());
        ArrayList<String> googleTaskIdList = new ArrayList<String>(
                dataStoreTask.keySet());
        ArrayList<String> googleEventIdList = new ArrayList<String>(
                dataStoreEvent.keySet());

        for (Task i : localTaskList) {
            if ((i.getIsSynced() == null)) {
                this.addToGoogle(i);
            } else if (!i.getIsSynced()) {
                this.modifyInGoogle(i);
                googleTaskIdList.remove(i.getGoogleId());
                googleEventIdList.remove(i.getGoogleId());
            }
        }

    }

}
