package advisor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Menu implements Runnable {
    private Map<String, Consumer<String[]>> actions = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    private boolean runnable = true;
    private Consumer<String[]> defaultAction = s -> System.out.println("Command not found");

    public void run() {
        while (runnable) {
            System.out.print("> ");
            var in = scanner.nextLine().split(" ");
            if(in.length == 0) continue;
            actions.getOrDefault(in[0], defaultAction)
                    .accept(in);
        }
    }

    public void exit() {
        System.out.println("---GOODBYE!---");
        runnable = false;
    }

    public void addOption(String option, Consumer<String[]> action) {
        actions.put(option, action);
    }

}
