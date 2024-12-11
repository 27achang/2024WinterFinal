import java.util.Scanner;

public class Simulation {
    public static final String ANSI_RESET = "\u001B[0m";
    
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private Scanner input;

    Simulation() {
        input = new Scanner(System.in);

        // Collect user details
        System.out.println("""
            -----------------------
              Welcome to Clue 2.0!
            -----------------------
            """);
    }

    private void run() {
        // Implementation not shown
    }

    public static void rollingPrint(Object obj) throws InterruptedException {
        String output = obj.toString();
        for(int i = 0; i < output.length() - 1; i++) {
            System.out.print(output.charAt(i));
            Thread.sleep(15);
        }
        System.out.print(output.charAt(output.length() - 1));
    }

    public static void rollingPrintln(Object obj) throws InterruptedException {
        String output = obj.toString();
        for(int i = 0; i < output.length() - 1; i++) {
            System.out.print(output.charAt(i));
        }
        System.out.println(output.charAt(output.length() - 1));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}
