package carsharing;

import java.util.Scanner;

public abstract class MenuBase {
    protected boolean runnable = true;
    protected String optionsString = "";
    protected Scanner scanner = new Scanner(System.in);

    abstract boolean handleOption(int option);

    public void run() {
        while(runnable) {
            System.out.println(optionsString);
            int option = getRangedInput(0, Integer.MAX_VALUE);
            runnable = handleOption(option);
        }
    }

    protected int getRangedInput(int fromInc, int toInc) {
        while (true) {
            try {
                int in = Integer.parseInt(scanner.nextLine());
                if (in < fromInc || in > toInc)
                    throw new RuntimeException();
                return in;
            } catch (Exception e) {
                System.out.println("Incorrect input! Try again!");
            }
        }
    }
}
