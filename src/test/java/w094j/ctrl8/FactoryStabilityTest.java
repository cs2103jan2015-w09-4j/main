//@author A0110787A
package w094j.ctrl8;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import w094j.ctrl8.database.Factory;
import w094j.ctrl8.terminal.Terminal;

/**
 * These JUnit test(s) aim to test Factory component. Factory constructor throws
 * IOException when Database Object throws it up to the Factory which calls it.
 * This JUnit test seeks to ensure that Factory constructor handles it
 * correctly. Since the factory also involves many other components, this serves
 * as an Integration test to test minimal functionality of the related
 * components.
 */
public class FactoryStabilityTest {
    private static String fileLoc = "src/test/resources/Ctrl-8.txt";
    private static String inputFileLoc = "src/test/resources/input.in";
    private Factory factory;

    @Before
    // Delete the file in the testing directory if it exists to ensure
    // correctness of test initial conditions
    public void deleteFile() {
        File fl = new File(fileLoc);
        if (fl.exists()) {
            fl.delete();
        }
    }

    @Ignore
    public void testNullString() {
        try {
            produceInputFile(inputFileLoc, "exit\n");
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }

        try {
            FileInputStream newIn = new FileInputStream(inputFileLoc);
            System.setIn(newIn);
        } catch (FileNotFoundException e) {
            assertTrue("Missing Input TextFile for test", false);
        }

        try {
            factory = new Factory(null);

        } catch (IOException e) {
            assertTrue("Factory throws Exception", false);
        }
        try {
            Terminal.getInstance().start();
        } catch (Exception e) {
            assertTrue("Terminal Exception", false);
        }

    }

    @Test
    public void testValidFilePathWithoutFile() {
        try {
            produceInputFile(inputFileLoc, "exit\n");
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }

        try {
            FileInputStream newIn = new FileInputStream(inputFileLoc);
            System.setIn(newIn);
        } catch (FileNotFoundException e) {
            assertTrue("Missing Input TextFile for test", false);
        }

        try {
            factory = new Factory(fileLoc);

        } catch (IOException e) {
            assertTrue("Factory throws Exception", false);
        }
        try {
            Terminal.getInstance().start();
        } catch (Exception e) {
            assertTrue("Terminal start Exception", false);
        }

        assertTrue(true);
    }

    private void produceInputFile(String inputFileLoc, String string)
            throws IOException {
        File fl = new File(inputFileLoc);
        PrintWriter pw = new PrintWriter(fl);
        pw.print(string);
        pw.close();

    }
}
