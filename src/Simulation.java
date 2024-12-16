import java.util.*;

/**
 * The Simulation class contains the core mechanics of the game including turn actions and prompting for input
 * 
 * @author Alexander Chang
 * @version 0.14, 12/13/2024
 */
public class Simulation {
    // ANSI COLOR CODES
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
    private boolean gameActive;

    Simulation() {
        input = new Scanner(System.in);

        clearConsole();

        // Welcome the user
        System.out.println("""
        -----------------------
          Welcome to Clue 2.0
        -----------------------""");

        Command command;
        do {
            // Prompt for input
            command = promptInput("Please select from the options below:", true,
                Command.ABOUT,
                Command.BEGIN
            );
            // Process input and complete the requested task
            if (command == Command.BEGIN) run();
            else if (command == Command.ABOUT) about();
            else throw new IllegalStateException("Invalid command: " + command);
        } while (command != Command.BEGIN);
    }

    /**
     * Opens the about screen, providing information about Clue 2.0
     */
    private void about() {
        clearConsole();

        // Tell the user about Clue 2.0
        rollingPrintln("This project attempts to recreate the classic mystery game Clue, adding more modern features and items. This project was completed in its entirety by Alexander Chang. Unathorized reproduction " +
            ANSI_WHITE_BACKGROUND + ANSI_BLACK + " or distribution " + ANSI_RESET + """
             of this project is prohibited by law.
            (c) 2024 by Alexander Chang. All rights reserved."""
        );

        // Provide instructions to return to the main menu
        rollingPrintln("""

            To return to the main menu, press enter.""");
        String answer;
        do{
            answer = input.nextLine();
        } while (!answer.equals(""));

        clearConsole();
    }

    /**
     * Runs the text adventure, prompting the user for input on each turn
     */
    private void run() {
        // Not yet implemented
        
        clearConsole();

        // Introduction
        rollingPrint("Welcome, detective. ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println();
        }
        
        String[] messages = {
            "I've been awaiting your arrival. ",
            "Thank you for coming on such short notice. ",
            "I'm Detective Joseph Kenny with the local police department. ",
            "We recently received a report of a murder of famous millionare Brian Thompson."
        };
        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            rollingPrint(message);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println();
            }
        }

        System.out.println();
        System.out.println();
        rollingPrint("Thomson was killed in his mansion last Thursday and the mystery killer's been on the run ever since. ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println();
        }
        rollingPrintln("We need your help in solving this mystery.");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println();
        }
        boolean willingToHelp = promptYesNo("Are you willing to help?",true) == Command.YES;

        clearConsole();
        
        if (!willingToHelp) {
            rollingPrintln("Well detective, it was nice meeting you. Farewell for now.");
            return;
        }

        // Collect user details

        rollingPrint("Glad to hear that, detective. ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println();
        }
        rollingPrint("Also, ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println();
        }
        rollingPrintln("what's your name?");
        System.out.print("> ");
        String answer;
        answer = input.nextLine();
        
        // The length of the name must be between 1 and 26, not inclusive.
        while (answer.length() > 25 || answer.length() == 1) {
            clearConsole();

            // Provide an error message
            rollingPrint("Are you sure that's a real name? ");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println();
            }
            rollingPrintln("It seems too \" + (answer.length() > 25 ? \"long.\" : \"short.\")");

            // Prompt the user for a new name
            System.out.print("> ");
            answer = input.nextLine();
        }

        gameActive = true;
        while(gameActive) {

        }
    }

    // Game Utility Methods

    /**
     * Prompts the user for input with a choice from the given commands and with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to System output should be rolling
     * @param commands the commands to list and accept as valid input
     * @return the command selected by the user
     */
    public Command promptInput(String message, boolean rolling, Command... commands) {
        String answer;
        ArrayList<String> acceptedCommands = new ArrayList<>();

        // Print the user prompt
        if(rolling) rollingPrintln(message);
        else System.out.println(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for(Command command : commands) {
            if(rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
            else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
            acceptedCommands.add(command.getName());
            if(command.containsAliases()) Arrays.stream(command.getAliases()).forEach((alias) -> acceptedCommands.add(alias));
        }
        
        // Allow the user to provide input
        System.out.print("> ");
        answer = input.nextLine();

        // If the input is not a recognized command or alias, clear the console and prompt the user again.
        while (!acceptedCommands.contains(answer)) {
            clearConsole();

            // Print the user prompt
            if(rolling){
                rollingPrintln("You can't do that right now.");
                rollingPrintln(message);
            } else {
                System.out.println("You can't do that right now.");
                System.out.println(message);
            }

            // Print the command options
            for(Command command : commands) {
                if(rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
                else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
            }
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine();
        }
        final String finalAnswer = answer;
        return ((Command)(Arrays.stream(commands).filter((a) -> a.matches(finalAnswer)).toArray()[0]));
    }

    /**
     * Prompts the user for a yes/no input with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to System output should be rolling
     * @return the command selected by the user
     */
    public Command promptYesNo(String message, boolean rolling) {
        String answer;
        Command[] commands = {Command.YES, Command.NO};
        ArrayList<String> acceptedCommands = new ArrayList<>();

        // Print the user prompt
        if(rolling) rollingPrintln(message);
        else System.out.println(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for(Command command : commands) {
            acceptedCommands.add(command.getName());
            if(command.containsAliases()) Arrays.stream(command.getAliases()).forEach((alias) -> acceptedCommands.add(alias));
        }
        
        // Allow the user to provide input
        System.out.print("> ");
        answer = input.nextLine();

        // If the input is not a recognized command or alias, clear the console and prompt the user again.
        while (!acceptedCommands.contains(answer)) {
            clearConsole();

            // Print the user prompt
            if(rolling){
                rollingPrintln(message + " (yes/no)");
            } else {
                System.out.println(message + " (yes/no)");
            }
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine();
        }
        final String finalAnswer = answer;
        return ((Command)(Arrays.stream(commands).filter((a) -> a.matches(finalAnswer)).toArray()[0]));
    }

    /**
     * Clears the output console
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    public static void rollingPrint(Object obj) {
        try {
            String output = obj.toString();
            for(int i = 0; i < output.length() - 1; i++) {
                System.out.print(output.charAt(i));
                Thread.sleep(15);
            }
            System.out.print(output.charAt(output.length() - 1));
        } catch (InterruptedException e) {
            System.out.print(obj);
        }
    }

    public static void rollingPrintln(Object obj) {
        try {
            String output = obj.toString();
            for (int i = 0; i < output.length() - 1; i++) {
                System.out.print(output.charAt(i));
                Thread.sleep(15);
            }
            System.out.println(output.charAt(output.length() - 1));
        } catch (InterruptedException e) {
            System.out.println(obj);
        }
    }

    public static void main(String[] args) throws Exception {
        new Simulation();
    }
}
