package w094j.run;

import w094j.ctrl8.database.Database;
import w094j.ctrl8.database.GoogleStorage;
import w094j.ctrl8.pojo.DBfile;

public class Main {
    public static void main(String[] args) throws Exception {

        testGoogle();

    }

    static void bson() {
        org.bson.types.ObjectId id = new org.bson.types.ObjectId();
        System.out.println(id.toString());
    }

    static void testGoogle() throws Exception {
        new Database(null);
        DBfile db = new DBfile();

        //db = database.getFile();

        GoogleStorage g = new GoogleStorage(db, null);
        g.readData();
        //g.sync();
        //g.deleteCalendarTaskList();

    }

}
