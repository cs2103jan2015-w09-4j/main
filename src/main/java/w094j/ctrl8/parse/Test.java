package w094j.ctrl8.parse;

import java.util.Map;
import java.util.Scanner;

import org.apache.commons.collections4.map.HashedMap;

import w094j.ctrl8.database.config.ParserConfig;
import w094j.ctrl8.exception.DataException;
import w094j.ctrl8.exception.ParseException;
import w094j.ctrl8.statement.parameter.ParameterType;

import com.google.gson.Gson;

public class Test {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Map<ParameterType, String> test = new HashedMap<>();
        test.put(ParameterType.CATEGORY, "p");
        test.put(ParameterType.DEADLINE, "p");
        System.out.println(gson.toJson(test));
        String json = gson.toJson(test);
        Map<String, String> yoo = gson.fromJson(json, HashedMap.class);
        System.out.println(yoo);
        ParserConfig config = new ParserConfig();
// config.set(ParameterType.CATEGORY, '0');
// config.set(ParameterType.DESCRIPTION, ']');
// System.out.println(config.get(ParameterType.PRIORITY));
// System.out.println(config.isValid());
// String y = gson.toJson(config);
// System.out.println(y);
// ParameterConfig c = gson.fromJson(y, ParameterConfig.class);
// System.out.println(gson.toJson(c));
        Parser parser = new Parser(config);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                parser.parse(scanner.nextLine());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (DataException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
