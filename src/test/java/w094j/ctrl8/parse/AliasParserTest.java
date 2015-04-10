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
 * Test for Alias Parser's parsing of the Alias.
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
     *            expected output after replacement, null if a DataException is
     *            expected.
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
                // Normal use case of Alias, where each alias is replaced with each. Only one alias is tested
                {"add ={eat lunch with |eric}","add ={eat lunch with Han Liang Wee Eric}"},
                // Normal use but 4 aliases are tested
                {"add ={eat lunch with |eric, |rod, |C and |CH}","add ={eat lunch with Han Liang Wee Eric, Chue Le Sheng Rodson, Tze Cheng and Chen Hsin}"},
                // Normal use case but with a short explicit symbol
                {"|a ={|e |profHW} -|2","add ={email Prof. Hon Wai} -2pm"},
                // Normal use of alias, with an escaped alias symbol
                {"add ={eat\\|sleeps |sleeps}","add ={eat|sleeps Sleep at Home}"},
                // Statement without alias, but has an escaped character
                {"add ={s\\leep}","add ={s\\leep}"},
                // Statement does not have any alias
                {"add ={sleeps}","add ={sleeps}"},
                /**
                 * Extreme tests
                 */
                // Mix 2 alias side by side, ensure it can be properly parsed
                {"add ={|C|CH}","add ={Tze ChengChen Hsin}"},
                // Same as previous but reversed
                {"add ={|CH|C}","add ={Chen HsinTze Cheng}"},
                // The Alias Character here should not be picked up by the parser as it is not a valid alias
                {"add ={eat | sleep}","add ={eat | sleep}"},
                // Same as previous but with more characters
                {"add ={eat || sleep}","add ={eat || sleep}"},
                // Same as previous but having a valid alias in the middle
                {"add ={eat ||e| sleep}","add ={eat |email| sleep}"},
                // Alias that has a parameter character
                {"add |#A", "add #abc"},
                /**
                 * Errornous tests
                 */
                // Alias that does not exist should throw an error
                {"add ={|sleep}",null}
        });
    }
    //@formatter:on

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
        aliasMap.put("#A", "#abc");
        aliasMap.put("sleeps", "Sleep at Home");

        AliasConfig config = new AliasConfig();
        AliasData data = new AliasData();
        data.setAliasMap(aliasMap);
        config.setAliasCharacter('|');

        parser = new AliasParser(config, data);
    }

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
