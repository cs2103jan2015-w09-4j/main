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

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.database.config.GoogleStorageConfig;
import w094j.ctrl8.exception.DataException;
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
    private static DataStore<ObjectId> dataStoreEvent;
    private static FileDataStoreFactory dataStoreFactory;
    private static DataStore<String> dataStoreSyncSettings;
    private static DataStore<ObjectId> dataStoreTask;
    private static final String EMPTY_STRING = "";
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = " file not found";
    private static final int FULL_SYNC_YEAR_FROM_NOW = -1;
    private static final String GOOGLE_EVENT_STATUS_CANCELLED = "cancelled";
    private static final String GOOGLE_EVENT_STATUS_CONFIRMED = "confirmed";
    private static final String GOOGLE_TASK_STATUS_COMPLETED = "completed";
    private static final String GOOGLE_TASK_STATUS_NEEDS_ACTION = "needsAction";
    private static HttpTransport httpTransport;
    private static JsonFactory jsonFactory = new JacksonFactory();
    private static Logger logger = LoggerFactory.getLogger(GoogleStorage.class);
    private static final int MILLISECONDS_IN_A_MINUTE = 60000;
    private static final String NO_TITLE = "(No title)";
    private static final String SYNC_TOKEN_KEY = "syncToken";
    private com.google.api.services.calendar.Calendar.Events.List calendarRequest;
    private final String CLIENT_SECRETS_FILE = "/client_secrets.json";
    private com.google.api.services.calendar.Calendar clientCalendar;
    private GoogleClientSecrets clientSecrets;
    private com.google.api.services.tasks.Tasks clientTask;
    private GoogleStorageConfig config;
    private Credential credential;
    private final java.io.File DATA_STORE_CALENDAR_INFO_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/CalendarInfo");
    private final java.io.File DATA_STORE_CREDENTIAL_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/StoredCredential");
    private final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME);
    private final java.io.File DATA_STORE_EVENT_STORE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/EventStore");
    private final java.io.File DATA_STORE_INETERNET_STATUS_LAST_SESSION = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/InternetStatusLastSession");
    private final java.io.File DATA_STORE_SYNC_SETTINGS = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/SyncSettings");
    private final java.io.File DATA_STORE_TASK_STORE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/TaskStore");
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
    private boolean isAutoDownloadAfterSave;
    private boolean isInternetReachableLastSession;
    private boolean isSyncWithGoogle;
    private final String SYNC_SETTINGS = "SyncSettings";
    private final String TASK_STORE = "TaskStore";
    private ArrayList<String> toBeDeletedGoogleEventIdList;
    private ArrayList<String> toBeDeletedGoogleTaskIdList;
    private final String userId = "user";

    /**
     * @param file
     * @param gson
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public GoogleStorage(DBfile file, Gson gson)
            throws GeneralSecurityException, IOException {
        super(file);
        this.dbFile = file;
        this.gson = gson;
        this.setConfig();
        this.initialize();
    }

    @Override
    public void readData() throws IOException, DataException {
        if (this.isSyncWithGoogle && this.isInternetReachable()) {
            this.getUnsyncedEventsAndTasksFromGoogle();
        }
    }

    @Override
    public void storeData() throws IOException, DataException {
        if (this.isSyncWithGoogle) {
            boolean isInternetReachableThisSession = this.isInternetReachable();

            if (isInternetReachableThisSession) {
                if (this.isInternetReachableLastSession) {
                    this.updateOneUnsyncedEventOrTaskToGoogle();
                } else {
                    this.updateAllUnsyncedEventsAndTasksToGoogle();
                }
                if (this.isAutoDownloadAfterSave) {
                    this.getUnsyncedEventsAndTasksFromGoogle();
                }
            }

            this.saveIntenetStatusInfo();

        }
    }

    @Override
    public void sync() throws IOException, DataException {
        if (this.isSyncWithGoogle && this.isInternetReachable()) {
            this.updateAllUnsyncedEventsAndTasksToGoogle();
            this.getUnsyncedEventsAndTasksFromGoogle();
        }
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

        dataStoreTask.set(googleTask.getId(), localTask.getId());
    }

    private void addLocalEvent(Event googleEvent) throws DataException,
            IOException {
        logger.info("Adding new event from Google: " + googleEvent.getSummary());
        Task localEvent = new Task();
        localEvent.setGoogleId(googleEvent.getId());
        this.setLocalEventFields(googleEvent, localEvent);
        dataStoreEvent.set(googleEvent.getId(), localEvent.getId());

        this.dbFile.getData().getTask().updateTaskMap(localEvent, null, false);
    }

    private void addLocalTask(
            com.google.api.services.tasks.model.Task googleTask)
            throws IOException, DataException {
        logger.info("Adding new task from Google: " + googleTask.getTitle());
        Task localTask = new Task();
        localTask.setGoogleId(googleTask.getId());
        this.setLocalTaskFields(googleTask, localTask);
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
        ArrayList<String> tempGoogleTaskIdList = (ArrayList<String>) this.toBeDeletedGoogleTaskIdList
                .clone();
        ArrayList<String> tempGoogleEventIdList = (ArrayList<String>) this.toBeDeletedGoogleEventIdList
                .clone();

        for (Task i : localTaskList) {
            tempGoogleTaskIdList.remove(i.getGoogleId());
            tempGoogleEventIdList.remove(i.getGoogleId());
        }

        for (String googleId : tempGoogleTaskIdList) {
            this.deleteGoogleTask(googleId);
        }

        for (String googleId : tempGoogleEventIdList) {
            this.deleteGoogleEvent(googleId);
        }
    }

    private void deleteGoogleEvent(String googleId) throws IOException {
        logger.info("Deleting Google Event: " + googleId);
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
        dataStoreTask.delete(googleId);
        try {
            this.clientTask.tasks()
                    .delete(this.googleTaskList.getId(), googleId).execute();
        } catch (IOException e) {
            logger.info("Fail to delete Google Task: " + googleId);
        }
    }

    private void deleteLocalGoogleInfo() {
        this.deleteFile(this.DATA_STORE_CALENDAR_INFO_FILE);
        this.deleteFile(this.DATA_STORE_CREDENTIAL_FILE);
        this.deleteFile(this.DATA_STORE_EVENT_STORE);
        this.deleteFile(this.DATA_STORE_INETERNET_STATUS_LAST_SESSION);
        this.deleteFile(this.DATA_STORE_SYNC_SETTINGS);
        this.deleteFile(this.DATA_STORE_TASK_STORE);
        this.deleteFile(this.DATA_STORE_TASKLIST_INFO_FILE);
        this.deleteFile(this.DATA_STORE_DIR);
    }

    private void deleteLocalTask(Task localTask) throws IOException {
        if (localTask != null) {
            logger.info("Deleting local task/event: " + localTask.getTitle());
            if (localTask.getTaskType() == TaskType.TIMED) {
                dataStoreEvent.delete(localTask.getGoogleId());
            } else {
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

    private java.util.Date getGoogleCalendarStartSyncDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(java.util.Calendar.YEAR, FULL_SYNC_YEAR_FROM_NOW);
        return cal.getTime();
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

        ObjectId taskId = null;
        try {
            if (dataStoreEvent.get(googleId) != null) {
                taskId = dataStoreEvent.get(googleId);
            }
        } catch (IOException e) {
            return null;
        }
        if (taskId != null) {
            return this.dbFile.getData().getTask().getTask(taskId);
        }
        return null;

    }

    private Task getLocalTaskByGoogleId(String googleId) {

        ObjectId taskId = null;
        try {
            if (dataStoreEvent.get(googleId) != null) {
                taskId = dataStoreEvent.get(googleId);
            }

        } catch (IOException e) {
            return null;
        }
        if (taskId != null) {
            return this.dbFile.getData().getTask().getTask(taskId);
        }
        return null;

    }

    private void getTaskListInfo() throws IOException {
        logger.info("Getting user's tasklist info...");
        String json = new String(
                Files.readAllBytes(this.DATA_STORE_TASKLIST_INFO_FILE.toPath()));
        this.googleTaskList = this.gson.fromJson(json, TaskList.class);
    }

    private void getTempGoogleIdList() throws IOException {
        this.toBeDeletedGoogleEventIdList = new ArrayList<String>(
                dataStoreEvent.keySet());
        this.toBeDeletedGoogleTaskIdList = new ArrayList<String>(
                dataStoreTask.keySet());
    }

    private void getUnsyncedEventsAndTasksFromGoogle() throws IOException,
    DataException {
        this.syncGoogleCalendar();
        this.syncGoogleTaskList();
    }

    private void initialize() throws GeneralSecurityException, IOException {
        if (this.isSyncWithGoogle) {
            if (this.isInternetReachable()) {
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
                this.getTempGoogleIdList();
            }
        } else if (this.DATA_STORE_DIR.exists()) {
            this.deleteLocalGoogleInfo();
            this.removeUnnecessaryInfoForLocalTasks();
        }
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
            InetAddress google = InetAddress.getByName(this.GOOGLE_WEBSITE);
            try {
                google.isReachable(0);
                logger.info("Has internet connection");
            } catch (IOException e) {
                logger.info("No internet connection");
                return false;
            }
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
                    .getGoogleCalendarStartSyncDate(), TimeZone.getDefault()));
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

    private void modifyInGoogle(Task localTask) throws IOException,
            DataException {
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
                this.addGoogleTask(localTask);
            }
        }

    }

    private void removeUnnecessaryInfoForLocalTasks() {
        List<Task> localTaskList = Arrays.asList(this.dbFile.getData()
                .getTask().getTaskList());

        for (Task i : localTaskList) {
            i.setGoogleId(null);
            i.setEtag(null);
            i.setSyncStatus(null);
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

    private void setConfig() {
        this.config = this.dbFile.getConfig().getDatabase()
                .getGoogleStorageConfig();
        this.isAutoDownloadAfterSave = this.config.getAutoDownloadAfterSave();
        this.isSyncWithGoogle = this.config.getSyncWithGoogle();
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
                    / MILLISECONDS_IN_A_MINUTE);

            EventReminder eventReminderPopUp = new EventReminder();
            eventReminderPopUp.setMethod(this.EVENT_REMINDER_METHOD_POPUP);
            eventReminderPopUp.setMinutes((int) (localEvent.getStartDate()
                    .getTime() - localEvent.getReminder().getTime())
                    / MILLISECONDS_IN_A_MINUTE);

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
            throws DataException {

        java.util.Calendar cal = java.util.Calendar.getInstance();

        if (googleEvent.getSummary() != null) {
            localEvent.setTitle(googleEvent.getSummary());
        } else {
            localEvent.setTitle(NO_TITLE);
        }

        if (googleEvent.getDescription() != null) {
            localEvent.setDescription(googleEvent.getDescription());
        }

        if (googleEvent.getStart().getDate() == null) {
            localEvent.setStartDate(new Date(googleEvent.getStart()
                    .getDateTime().getValue()));
        } else {
            localEvent.setStartDate(new Date(googleEvent.getStart().getDate()
                    .getValue()));
        }

        if (googleEvent.getEnd().getDate() == null) {
            localEvent.setEndDate(new Date(googleEvent.getEnd().getDateTime()
                    .getValue()));
        } else {
            localEvent.setEndDate(new Date(googleEvent.getEnd().getDate()
                    .getValue()));
        }

        if (googleEvent.getLocation() != null) {
            localEvent.setLocation(googleEvent.getLocation());
        }

        if (googleEvent.getReminders().getUseDefault()
                || (googleEvent.getReminders().getOverrides() == null)) {
            localEvent.setReminder(null);
        } else {
            int minutesBeforeStartDate = googleEvent.getReminders()
                    .getOverrides().get(0).getMinutes();
            cal.setTime(localEvent.getStartDate());
            cal.add(java.util.Calendar.MINUTE, (-1) * minutesBeforeStartDate);
            localEvent.setReminder(cal.getTime());
        }
        localEvent.toCompleteTask();
        localEvent.setEtag(googleEvent.getEtag());
    }

    private void setLocalTaskFields(
            com.google.api.services.tasks.model.Task googleTask, Task localTask)
            throws DataException {
        if ((googleTask.getTitle() == null)
                || googleTask.getTitle().equals(EMPTY_STRING)) {
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
        GoogleAuthorizationCodeFlow flow;

        flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
                jsonFactory, this.clientSecrets,
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

    private void syncAllEvents() throws IOException, DataException {
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
                    this.syncGoogleCalendar();
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

    private void syncEvent(Task localEvent, Event googleEvent)
            throws IOException, DataException {
        logger.info("Updating Google event: " + localEvent.getTitle());

        Date googleEventLastModifiedTime = new Date(googleEvent.getUpdated()
                .getValue());

        if (localEvent.getLastModifiedTime()
                .before(googleEventLastModifiedTime)) {
            this.updateLocalEvent(googleEvent, localEvent);
        } else {
            this.updateGoogleEvent(googleEvent, localEvent);
        }
    }

    private void syncEventFromGoogle(Event googleEvent) throws IOException,
            DataException {
        Task localEvent = this.getLocalEventByGoogleId(googleEvent.getId());

        if (GOOGLE_EVENT_STATUS_CANCELLED.equals(googleEvent.getStatus())) {
            if (dataStoreEvent.containsKey(googleEvent.getId())) {
                if (localEvent != null) {
                    this.deleteLocalTask(localEvent);
                }
            }
        } else {
            if (localEvent != null) {
                this.syncEvent(localEvent, googleEvent);
            } else {
                this.addLocalEvent(googleEvent);
            }
        }
    }

    private void syncGoogleCalendar() throws IOException, DataException {
        logger.info("Syncing Google Calendar...");
        this.calendarRequest = this.clientCalendar.events().list(
                this.googleCalendar.getId());
        this.loadSyncToken();
        this.syncAllEvents();
        this.setNextSyncToken();
    }

    private void syncGoogleTaskList() throws IOException, DataException {
        logger.info("Syncing Google Task List...");
        Tasks tasks = this.clientTask.tasks().list(this.googleTaskList.getId())
                .execute();

        if (tasks.getItems() != null) {
            List<com.google.api.services.tasks.model.Task> items = tasks
                    .getItems();
            logger.info("Google Task List size: " + items.size());

            for (com.google.api.services.tasks.model.Task googleTask : items) {
                Task localTask = this
                        .getLocalTaskByGoogleId(googleTask.getId());

                if ((localTask == null)) {
                    this.addLocalTask(googleTask);
                    this.toBeDeletedGoogleTaskIdList.remove(googleTask.getId());
                } else {
                    this.syncTask(localTask, googleTask);
                    this.toBeDeletedGoogleTaskIdList.remove(googleTask.getId());
                }
            }
        }

        for (String googleId : this.toBeDeletedGoogleTaskIdList) {
            this.deleteLocalTask(this.getLocalTaskByGoogleId(googleId));
        }
    }

    private void syncTask(Task localTask,
            com.google.api.services.tasks.model.Task googleTask)
            throws IOException, DataException {

        Date googleTaskLastModifiedTime = new Date(googleTask.getUpdated()
                .getValue());

        if (!localTask.getIsSynced()) {
            if (!localTask.getEtag().equals(googleTask.getEtag())) {
                // both local and google tasks changed, overwrite the latest one
                if (localTask.getLastModifiedTime().after(
                        googleTaskLastModifiedTime)) {
                    this.updateGoogleTask(googleTask, localTask);
                } else {
                    if (googleTask.getDeleted() == null) {
                        this.updateLocalTask(googleTask, localTask);
                    } else {
                        // google task flagged as deleted
                        this.deleteLocalTask(localTask);
                    }
                }
            } else {
                // only local task changed
                this.updateGoogleTask(googleTask, localTask);
            }
        } else {
            if (!localTask.getEtag().equals(googleTask.getEtag())) {
                // only google task changed
                this.updateGoogleTask(googleTask, localTask);
            }
        }
    }

    private void updateAllUnsyncedEventsAndTasksToGoogle() throws IOException,
    DataException {
        logger.info("Uploading unsynced Events and Tasks");
        List<Task> localTaskList = Arrays.asList(this.dbFile.getData()
                .getTask().getTaskList());

        for (Task i : localTaskList) {
            if ((i.getIsSynced() == null)) {
                this.addToGoogle(i);
            } else if (!i.getIsSynced()) {
                this.modifyInGoogle(i);
                this.toBeDeletedGoogleTaskIdList.remove(i.getGoogleId());
                this.toBeDeletedGoogleEventIdList.remove(i.getGoogleId());
            } else if (i.getIsSynced()) {
                this.toBeDeletedGoogleTaskIdList.remove(i.getGoogleId());
                this.toBeDeletedGoogleEventIdList.remove(i.getGoogleId());
            }
        }

        for (String googleId : this.toBeDeletedGoogleTaskIdList) {
            this.deleteGoogleTask(googleId);
        }

        for (String googleId : this.toBeDeletedGoogleEventIdList) {
            this.deleteGoogleEvent(googleId);
        }

    }

    private void updateGoogleEvent(Event googleEvent, Task localEvent)
            throws IOException {
        this.setGoogleEventFields(googleEvent, localEvent);
        googleEvent.setStatus(null);
        logger.info("Updating Google event: " + localEvent.getTitle());

        googleEvent = this.clientCalendar
                .events()
                .update(this.googleCalendar.getId(), localEvent.getGoogleId(),
                        googleEvent).execute();

        localEvent.setEtag(googleEvent.getEtag());
        this.toBeDeletedGoogleEventIdList.remove(googleEvent.getId());
    }

    private void updateGoogleTask(
            com.google.api.services.tasks.model.Task googleTask, Task localTask)
            throws IOException {
        logger.info("Updating Google task: " + localTask.getTitle());
        this.setGoogleTaskFields(googleTask, localTask);
        googleTask.setDeleted(null);
        googleTask = this.clientTask
                .tasks()
                .update(this.googleTaskList.getId().toString(),
                        localTask.getId().toString(), googleTask).execute();

        localTask.setEtag(googleTask.getEtag());
        this.toBeDeletedGoogleTaskIdList.remove(googleTask.getId());
    }

    private void updateLocalEvent(Event googleEvent, Task localEvent)
            throws DataException {
        logger.info("Updating local event: " + googleEvent.getSummary());
        this.setLocalEventFields(googleEvent, localEvent);
        this.toBeDeletedGoogleEventIdList.remove(googleEvent.getId());
    }

    private void updateLocalTask(
            com.google.api.services.tasks.model.Task googleTask, Task localTask)
            throws DataException {
        logger.info("Updating local task: " + googleTask.getTitle());
        this.setLocalTaskFields(googleTask, localTask);
        this.toBeDeletedGoogleTaskIdList.remove(googleTask.getId());
    }

    private void updateOneUnsyncedEventOrTaskToGoogle() throws IOException,
            DataException {
        ArrayList<Actions> actionList = this.dbFile.getData().getTask()
                .getActionsList();
        Actions lastAction = actionList.get(actionList.size() - 1);
        CommandType command = lastAction.getStatement().getCommand();
        ObjectId taskId = lastAction.getTaskID();
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
    }

}