//package w094j.ctrl8.parse.statement;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
//
//import w094j.ctrl8.data.AliasData;
//import w094j.ctrl8.database.config.AliasConfig;
//import w094j.ctrl8.exception.DataException;
//
///**
// * Test for Alias Parser's parsing of the Alias.
// */
//@RunWith(value = Parameterized.class)
//public class AliasAddStatementTest {
//
//    private static AliasParser parser;
//
//    public AliasAddStatementTest() {
//    }
//
//    /**
//     * @return test data.
//     */
//    @Parameters(name = "{index}: Parse \"{0}\" to \"{1}\"")
//    //@formatter:off
//    public static Iterable<Object[]> data() {
//        return Arrays.asList(new Object[][] {
//                /**
//                 * Normal tests
//                 */
//                // Normal use case of Alias, where each alias is replaced with each. Only one alias is tested
//                {"add ={eat lunch with |eric}","add ={eat lunch with Han Liang Wee Eric}"}
//        });
//    }
//    //@formatter:on
//
//    /**
//     * Initializes the lookup table with some values
//     */
//    @BeforeClass
//    public static void initParser() {
//        Map<String, String> aliasMap = new HashMap<String, String>();
//        AliasConfig config = new AliasConfig();
//        AliasData data = new AliasData();
//        data.setAliasMap(aliasMap);
//        config.setAliasCharacter('|');
//
//        parser = new AliasParser(config, data);
//    }
//
//    /**
//     * Tests the parsing of the command text
//     */
//    @Test
//    public void test() {
//        try {
//            String replacedAlias = parser.replaceAllAlias(this.inputToReplace);
//            assertEquals(this.expectedReplaced, replacedAlias);
//        } catch (DataException e) {
//            if (this.expectedReplaced == null) {
//                assertTrue(true);
//            } else {
//                assertTrue(false);
//            }
//        }
//    }
// }
