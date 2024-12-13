import java.util.*;

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
        -----------------------""");

        System.out.println("Please select from the options below:");
        String command = promptInput(true,
            new Command("about","Learn more about Clue 2.0"),
            new Command("begin","Start the game")
        );
        System.out.println(command);
    }

    /**
     * Runs the text adventure, prompting the user for input on each turn
     */
    private void run() {
        // Not yet implemented
    }

    // Game Utility Methods

    /**
     * Prompts the user for input with a choice from the given commands and with rolling printing if {@code rolling} is true
     * 
     * @param rolling whether the print to System output should be rolling
     * @param commands the commands to list and accept as valid input
     * @return the command selected by the user
     * @throws InterruptedException
     */
    public String promptInput(boolean rolling, Command... commands) {
        String answer;
        ArrayList<String> acceptedCommands = new ArrayList<>();
        do {
            for(Command command : commands) {
                if(rolling){
                    try {
                        rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
                    } catch(InterruptedException e) {
                        System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
                    }
                } else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
                acceptedCommands.add(command.getName());
                if(command.containsAliases()) Arrays.stream(command.getAliases()).forEach((alias) -> acceptedCommands.add(alias));
            }
            System.out.print("> ");
            answer = input.nextLine();
            //!Arrays.stream(commands).anyMatch((a) -> a.matches(answer))
        } while(!acceptedCommands.contains(answer));
        final String finalAnswer = answer;
        return ((Command)(Arrays.stream(commands).filter((a) -> a.matches(finalAnswer)).toArray()[0])).getName();
    }

    public boolean isInvalidCommand(String givenCommand, Command[] commands) {
        for(Command command : commands){
            if(command.matches(givenCommand)) return true;
        }
        return false;
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
        new Simulation();
    }
}
