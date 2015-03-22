//@author A0112521B
package w094j.ctrl8.statement.parameter;

import java.security.InvalidParameterException;

import org.junit.Assert;
import org.junit.Test;

import w094j.ctrl8.database.config.ParameterConfig;
import w094j.ctrl8.parse.ParameterParser;

public class PriorityParameterExceptionTest {
    private ParameterConfig config = new ParameterConfig();
    private ParameterParser parser = new ParameterParser(this.config);

    @Test
    public void testDouble() throws Exception {
        try {
            this.parser.parse("%{0.0}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be an integer.")) {
                Assert.fail("should have printed: Priority must be an integer.");
            }
        }
    }

    @Test
    public void testExtremelyLargeIntValue() throws Exception {
        try {
            this.parser.parse("%{" + (Integer.MAX_VALUE + Integer.MAX_VALUE)
                    + "}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be an integer.")) {
                Assert.fail("should have printed: Priority must be an integer.");
            }
        }
    }

    @Test
    public void testExtremelySmallIntValue() throws Exception {
        try {
            this.parser.parse("%{" + (Integer.MIN_VALUE + Integer.MIN_VALUE)
                    + "}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be an integer.")) {
                Assert.fail("should have printed: Priority must be an integer.");
            }
        }
    }

    @Test
    public void testIntMaxValue() throws Exception {
        try {
            this.parser.parse("%{" + (Integer.MAX_VALUE) + "}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10")) {
                Assert.fail("should have printed: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10");
            }
        }
    }

    @Test
    public void testIntMinValue() throws Exception {
        try {
            this.parser.parse("%{" + (Integer.MIN_VALUE) + "}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10")) {
                Assert.fail("should have printed: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10");
            }
        }
    }

    @Test
    public void testOutOfRangeNegative() throws Exception {
        try {
            this.parser.parse("%{-1}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10")) {
                Assert.fail("should have printed: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10");
            }
        }

    }

    @Test
    public void testOutOfRangePositive() throws Exception {
        try {
            this.parser.parse("%{11}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10")) {
                Assert.fail("should have printed: Priority must be between and inclusive of 0 and 10. 0 <= priority <= 10");
            }

        }
    }

    @Test
    public void testString() throws Exception {
        try {
            this.parser.parse("%{string string}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be an integer.")) {
                Assert.fail("should have printed: Priority must be an integer.");
            }
        }
    }

    @Test
    public void testWord() throws Exception {
        try {
            this.parser.parse("%{word}");
            Assert.fail("should have thrown InvalidParameterException");
        } catch (InvalidParameterException e) {
            if (!e.toString()
                    .equals("java.security.InvalidParameterException: Priority must be an integer.")) {
                Assert.fail("should have printed: Priority must be an integer.");
            }
        }
    }

}
