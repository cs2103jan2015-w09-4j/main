package w094j.ctrl8.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import w094j.ctrl8.message.NormalMessage;
import w094j.ctrl8.pojo.DBfile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Google Calendar Storage
 */
public class GoogleCalStorage extends Storage {
    private static com.google.api.services.calendar.Calendar client;
    private static final String CLIENT_SECRETS_FILE = "/client_secrets.json";
    private static final java.io.File DATA_STORE_CREDENTIAL_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/StoredCredential");
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME);
    private static FileDataStoreFactory dataStoreFactory;
    private static final String ERROR_MESSAGE_CLIENT_SECRETS_NOT_FOUND = "client secrets not found";
    private static HttpTransport httpTransport;
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static Logger logger = LoggerFactory
            .getLogger(GoogleCalStorage.class);
    private static final String userId = "user";
    private Calendar calendar;
    private GoogleClientSecrets clientSecrets;
    private Credential credential;
    private final java.io.File DATA_STORE_CALENDAR_INFO_FILE = new java.io.File(
            System.getProperty("user.home"), ".store/" + NormalMessage.APP_NAME
                    + "/CalendarInfo");

    /**
     * @param file
     * @throws Exception
     */
    public GoogleCalStorage(DBfile file) throws Exception {
        super(file);
        this.initialize();
    }

    /**
     * Deletes the calendar in Google
     */
    public void deleteCalendar() {
        try {
            client.calendars().delete(this.calendar.getId()).execute();
            this.disconnect();
            logger.info("Deleted the calendar in Google");
        } catch (Exception e) {
            logger.info("Failed to delete the calendar in Google");
        }
    }

    /**
     * Deletes user's local files that are related to Google Calendar. This does
     * not delete the calendar in Google.
     */
    public void disconnect() {
        this.deleteFile(DATA_STORE_CREDENTIAL_FILE);
        this.deleteFile(this.DATA_STORE_CALENDAR_INFO_FILE);
        this.deleteFile(DATA_STORE_DIR);
    }

    @Override
    public void readData() {

    }

    @Override
    public void storeData() {

    }

    private void createCalendar() throws IOException {
        logger.info("Adding a new calendar...");
        Calendar entry = new Calendar();
        entry.setSummary(NormalMessage.APP_NAME);
        this.calendar = client.calendars().insert(entry).execute();

        logger.info("Saving calendar info locally...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.calendar);
        try {
            Files.write(this.DATA_STORE_CALENDAR_INFO_FILE.toPath(),
                    json.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void createOrGetCalendar() throws IOException {
        if (this.DATA_STORE_CALENDAR_INFO_FILE.exists()) {
            this.getCalendar();
        } else {
            this.createCalendar();
        }
    }

    private void deleteFile(File file) {
        try {
            if (file.delete()) {
                logger.info("Deleted " + file);
            } else {
                logger.info("Failed to delete " + file);
            }
        } catch (Exception e) {
            logger.info("Failed to delete " + file);
            e.printStackTrace();
        }

    }

    private void getCalendar() throws IOException {
        logger.info("Getting user's calendar info...");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = new String(
                Files.readAllBytes(this.DATA_STORE_CALENDAR_INFO_FILE.toPath()));
        this.calendar = gson.fromJson(json, Calendar.class);
    }

    private void getClientSecrets() {
        try {
            logger.info("Getting client secrets...");
            this.clientSecrets = GoogleClientSecrets.load(
                    jsonFactory,
                    new InputStreamReader(CalendarSample.class
                            .getResourceAsStream(CLIENT_SECRETS_FILE)));
        } catch (Exception e) {
            logger.debug(ERROR_MESSAGE_CLIENT_SECRETS_NOT_FOUND);
            System.err.println(ERROR_MESSAGE_CLIENT_SECRETS_NOT_FOUND);
            System.exit(1);
        }
    }

    private void initialize() throws Exception {
        this.initializeTransport();
        this.initializeDataStoreFactory();
        this.getClientSecrets();
        this.setUpAuthorizationCodeFlow();
        this.setUpGlobalCaleandarInstance();
        this.createOrGetCalendar();
    }

    private void initializeDataStoreFactory() throws IOException {
        logger.info("Initializing DataStoreFactory...");
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
    }

    private void initializeTransport() throws GeneralSecurityException,
            IOException {
        logger.info("Initializing Transport...");
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    private void setUpAuthorizationCodeFlow() throws IOException {
        logger.info("Setting up authorization code flow...");
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, this.clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR))
                .setDataStoreFactory(dataStoreFactory).build();
        this.credential = new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver()).authorize(userId);
    }

    private void setUpGlobalCaleandarInstance() {
        logger.info("Setting up global caleandar instance...");
        client = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, jsonFactory, this.credential)
        .setApplicationName(NormalMessage.APP_NAME).build();
    }

}
