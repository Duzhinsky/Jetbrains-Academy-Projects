package carsharing;

import java.util.HashMap;
import java.util.Map;


// TODO подключить как нибудь стримы в этот проект

public class Main {

    public static String dbName;

    private static Map<String, String> parseArgs(String[] args) {
        var result = new HashMap<String, String>();
        for(int i = 0; i < args.length; ++i) {
            if(args[i].startsWith("-") && i+1 < args.length)
                result.put(args[i].replaceFirst("-",""), args[i+1]);
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String,String> params = parseArgs(args);
        dbName = params.getOrDefault("databaseFileName", "carsharing");

        H2Utils.init();
        H2Utils.createTables();

        Menu menu = new Menu();
        menu.run();
    }
}
