package w094j.ctrl8.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import w094j.ctrl8.data.AliasData;
import w094j.ctrl8.database.config.AliasConfig;
import w094j.ctrl8.exception.DataException;

/**
 * Test for Alias Parser
 */
@RunWith(value = Parameterized.class)
public class AliasParserTest {

    private static AliasParser parser;

    private String expectedReplaced;
    private String inputToReplace;

    /**
     * Initializes a test with the input to perform the alias replace on and the
     * expected replaced string.
     *
     * @param inputToReplace
     *            string to perform the replacement operation.
     * @param expectedReplaced
     *            expected output after replacement.
     */
    public AliasParserTest(String inputToReplace, String expectedReplaced) {
        this.inputToReplace = inputToReplace;
        this.expectedReplaced = expectedReplaced;
    }

    /**
     * @return test data.
     */
    @Parameters(name = "{index}: Parse \"{0}\" to \"{1}\"")
    //@formatter:off
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /**
                 * Normal tests
                 */
                {"add ={eat lunch with |eric, |rod, |C and |CH}","add ={eat lunch with Han Liang Wee Eric, Chue Le Sheng Rodson, Tze Cheng and Chen Hsin}"},
                {"|a ={|e |profHW} -|2","add ={email Prof. Hon Wai} -2pm"},
                {"add ={eat\\|sleep}","add ={eat|sleep}"},
                /**
                 * Extreme tests
                 */
                {"add ={|C|CH}","add ={Tze ChengChen Hsin}"},
                {"add ={|CH|C}","add ={Chen HsinTze Cheng}"},
                {"add ={eat | sleep}","add ={eat | sleep}"},
                {"add ={eat || sleep}","add ={eat || sleep}"},
                {"add ={eat ||e| sleep}","add ={eat |email| sleep}"},
                /**
                 * Errornous tests
                 */
                {"add ={|sleep}",null},
                {"add ={s\\leep}","add ={s\\leep}"}
        });
    }

    /**
     * Initializes the lookup table with some values
     */
    @BeforeClass
    public static void initParser() {
        Map<String, String> aliasMap = new HashMap<String, String>();
        aliasMap.put("eric", "Han Liang Wee Eric");
        aliasMap.put("rod", "Chue Le Sheng Rodson");
        aliasMap.put("C", "Tze Cheng");
        aliasMap.put("CH", "Chen Hsin");
        aliasMap.put("a", "add");
        aliasMap.put("e", "email");
        aliasMap.put("profHW", "Prof. Hon Wai");
        aliasMap.put("2", "2pm");

        AliasConfig config = new AliasConfig();
        AliasData data = new AliasData();
        data.setAliasMap(aliasMap);
        config.setAliasData(data);
        config.setAliasCharacter('|');

        parser = new AliasParser(config);
    }

    // @formatter:on

    /**
     * Tests the parsing of the command text
     */
    @Test
    public void test() {
        try {
            String replacedAlias = parser.replaceAllAlias(this.inputToReplace);
            assertEquals(this.expectedReplaced, replacedAlias);
        } catch (DataException e) {
            if (this.expectedReplaced == null) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
        }
    }
}
