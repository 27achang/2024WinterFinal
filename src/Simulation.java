import java.util.*;
import java.util.function.Predicate;

/**
 * The Simulation class contains the core mechanics of the game including turn actions and prompting for input
 * 
 * @author Alexander Chang
 * @version 0.15, 12/16/2024
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
    public static final String ANSI_GRAY = "\u001B[90m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_GRAY_BACKGROUND = "\u001B[100m";

    // 1 becomes a space and start of room color
    // 2 becomes a space and end of room color
    // 3 becomes a slash and end of room color
    // 4 becomes a backslash and start of room color
    private final String map = """
        XXXXXXXXXXXXXXXXXXXXXXXXXX
        X1    2X XXXXXXXXSX1    2X
        X1    2|  |    |  |1    2X
        X4    2|  |    |  |1    2X
        X------|  |    |  |1    2X
        XX     ^ >|    |  |1    3X
        XL        |    |  |------X
        XX-----   ------  ^     XX
        X1    2|    ^^          MX
        X1    2|< |1 2|   v     XX
        X1    2|  |1 2|  |-------X
        XX-----   |1 2|  |1     2X
        XXv ^     -----  |1     2X
        X-----|         >|1     2X
        X1   2|          |1     2X
        X1   2|          |--1   2X
        X1   2|<            -----X
        X-----|   v    v        XX
        XX       |------|   v    X
        XP       |1    2|  |----XX
        XX---|< >|1    2|< |1   2X
        X1/  2|  |1    2|  |1   2X
        X1   2|  |1    2|  |1   2X
        X1   2|  |--  --|  |1   2X
        X1    2X   |12|   X4    2X
        XXXXXXXXXX XXXX XXXXXXXXXX
        XXXXXXXXXXXXXXXXXXXXXXXXXX""";

    private Scanner input;
    private boolean gameActive;
    private String name;
    private String color;
    private int stamina = 100;
    private Room[] visitedRooms;
    private Room currentRoom;
    private int x_pos; // Starting at 1
    private int y_pos; // Starting at 1

    Simulation() {
        input = new Scanner(System.in);

        clearConsole();

        if (!pollANSISupport()) return;

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
            if (command == Command.BEGIN) start();
            else if (command == Command.ABOUT) about();
            else throw new IllegalStateException("Invalid command: " + command);
        } while (command != Command.BEGIN);
    }

    /**
     * Provide a user-guided test to determine whether the user is using a console that supports ANSI escape codes.
     */
    private boolean pollANSISupport() {
        printMap();
        System.out.println("""
            -----------------------------------------
              ANSI Escape Code Support Confirmation
            -----------------------------------------
            """);
        rollingPrint("As noted in the documentation for this project, proper execution of this execution requires the use of a console that supports ANSI escape codes. ");
        textDelay(1000);
        rollingPrint("These codes provide essential coloring and the ability to clear the console and directly support smooth gameplay. ");
        textDelay(1000);
        rollingPrint("Take a look at the next line:");
        System.out.println();
        if (promptYesNo(ANSI_WHITE_BACKGROUND + ANSI_BLACK + " Hello, world! " + ANSI_RESET + "\nDo you see any additional text on the above line apart from the text ' Hello, world! '?", true) == Command.YES) {
            System.out.println("Please find a console that supports ANSI support codes before running this project.");
            return false;
        }
        return true;
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
     * Starts the text adventure, providing the user the story introduction and collecting user details.
     */
    private void start() {
        clearConsole();

        // Introduction
        rollingPrint("Welcome, detective. ");
        textDelay(1000);
        
        String[] messages = {
            "I've been awaiting your arrival. ",
            "Thank you for coming on such short notice. ",
            "I'm Detective Joseph Kenny with the local police department. ",
            "We recently received a report of a murder of famous millionare Brian Thompson."
        };
        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            rollingPrint(message);
            textDelay();
        }

        System.out.println();
        System.out.println();
        rollingPrint("Thomson was killed in his mansion just last night and the mystery killer's been on the run since. ");
        textDelay();
        rollingPrint("We need your help in solving this mystery.");
        textDelay(1000);
        System.out.println();
        
        // Confirm user willingness to participate
        boolean willingToHelp = promptYesNo("Are you willing to help?",true) == Command.YES;

        clearConsole();

        // If the user is not willing to help, say farewell and end the execution.
        if (!willingToHelp) {
            rollingPrint("Well detective, it was nice meeting you. ");
            textDelay();
            rollingPrint("Farewell for now.");
            textDelay();
            return;
        }

        // Collect user details

        rollingPrint("Glad to hear that, detective. ");
        textDelay();
        rollingPrint("Also, ");
        textDelay();
        rollingPrintln("what's your name?");
        System.out.print("> ");
        String answer;
        answer = input.nextLine();
        
        // The length of the name must be between 1 and 26, not inclusive.
        while (answer.length() > 25 || answer.length() == 1) {
            clearConsole();

            // Provide an error message
            rollingPrint("Are you sure that's a real name? ");
            textDelay();
            rollingPrintln("It seems too \" + (answer.length() > 25 ? \"long.\" : \"short.\")");

            // Prompt the user for a new name
            System.out.print("> ");
            answer = input.nextLine();
        }

        name = answer;
        clearConsole();

        rollingPrint("Nice to meet you, detective " + name + ". ");
        textDelay();
        rollingPrint("Finally, what's your favorite color? ");
        textDelay();
        rollingPrintln("(scarlet, mustard, white, green, blue, or plum)");
        color = promptInput(true, "scarlet", "mustard", "white", "green", "blue", "plum");
        rollingPrint("Oh, ");
        textDelay(250);
        rollingPrint("nice choice. ");
        textDelay();
        rollingPrint("Let's head over to the crime scene now.");
        textDelay(1000);

        loadingAnimation(2);

        rollingPrint("Alright, detective, ");
        textDelay();
        rollingPrint("here we are. ");
        textDelay();
        rollingPrint("Thompson was killed on the second floor of his mansion, which has 9 rooms wrapped around the central staircase. ");
        textDelay();
        rollingPrint("Since the murder's so recent, my team has only gotten a chance to secure the scene and hasn't started any of the investigation yet. ");
        textDelay();
        rollingPrint("I've decided to leave it up to you to solve.");
        textDelay();
        System.out.println();
        System.out.println();
        rollingPrint("Here's a map of the top floor. ");
        textDelay();
        rollingPrint("The double-barred lines represent doors to each room. ");
        textDelay();
        rollingPrint("If at any point you need to reach me or can't manage the investigation any longer for whatever reason, I'll be right downstairs with the media.");
        textDelay();
        System.out.println();
        System.out.println();
        rollingPrint("Best of luck, ");
        textDelay(250);
        rollingPrint("detective.");
        System.out.println();
        System.out.println();
        textDelay(5000);
        
        rollingPrint("To begin, press enter.");
        input.nextLine();

        // Begins the actual text adventure
        run();
    }

    /**
     * Runs the text adventure, prompting the user for input on each turn
     */
    private void run() {
        gameActive = true;
        while (gameActive) {
            
        }
    }

    // Game Utility Methods

    /**
     * Prints the game map, showing room names on rooms which have been visited.
     */
    private void printMap() {
        // 24 across 25 down
        /*
         * https://www.unicode.org/charts/PDF/U2500.pdf
         * https://www.compart.com/en/unicode/block/U+2500
         * 
         * ┏━━━━━━━━━━━┓ ┏━┓               ┏━┓ ┏━━━━━━━━━━━┓
         * ┃           ┃ ┃ ┃               ┃S┃ ┃           ┃
         * ┃           ┗━╉─╄━┳━━━━━━━━━━━┳━╉─╄━┛           ┃
         * ┃             ┃ │ ┃           ┃ │ ┃             ┃
         * ┃    STUDY    ┠─┼─┨           ┠─┼─┨             ┃
         * ┃             ┃ │ ┃           ┃ │ ┃             ┃
         * ┃             ┠─┼─┨           ┠─┼─┨   LOUNGE    ┃
         * ┃             ┃ │ ┃           ┃ │ ┃             ┃
         * ┗━┳━┯━┯━┯━┯━┯═╃─┼─┨   HALL    ┠─┼─┨             ┃
         *   ┃ │ │ │ │ │ │ │ ║           ┃ │ ┃             ┃
         * ┏━╃─┼─┼─┼─┼─┼─┼─┼─┨           ┠─┼─┨             ┃
         * ┃L│ │ │ │ │ │ │ │ ┃           ┃ │ ┃             ┃
         * ┗━╈━┷━┷━┷━┷━╅─┼─┼─┨           ┠─┼─╄═┯━┯━┯━┯━┯━┳━┛
         *   ┃         ┃ │ │ ┃           ┃ │ │ │ │ │ │ │ ┃
         * ┏━┛         ┗━╅─┼─╄━┯━┯═╤═┯━┯━╃─┼─┼─┼─┼─┼─┼─┼─╄━┓
         * ┃             ┃ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │M┃
         * ┃             ┠─┼─╁─┴─┴─┴─┴─╁─┼─┼─┼─┼─┼─┼─┼─┼─╆━┛
         * ┃   LIBRARY   ║ │ ┃         ┃ │ │ │ │ │ │ │ │ ┃
         * ┃             ┠─┼─┨         ┠─┼─╆━┷━┷━┷━┷━┷━┷━┻━┓
         * ┃             ┃ │ ┃         ┃ │ ┃               ┃
         * ┗━┓         ┏━╃─┼─┨STAIRCASE┠─┼─┨               ┃
         *   ┃         ┃ │ │ ┃         ┃ │ ┃               ┃
         *   ┣━┯━┯═┯━┯━╃─┼─┼─┨         ┠─┼─┨               ┃
         *   ┃ │ │ │ │ │ │ │ ┃         ┃ │ ┃               ┃
         * ┏━┻═┷━┷━┷━┷━╅─┼─┼─╄━┯━┯━┯━┯━╃─┼─┨               ┃
         * ┃           ┃ │ │ │ │ │ │ │ │ │ ║  DINING ROOM  ┃
         * ┃           ┠─┼─┼─┼─┼─┼─┼─┼─┼─┼─┨               ┃
         * ┃           ┃ │ │ │ │ │ │ │ │ │ ┃               ┃
         * ┃ BILLIARD  ┠─┼─┼─┼─┼─┼─┼─┼─┼─┼─┨               ┃
         * ┃           ┃ │ │ │ │ │ │ │ │ │ ┃               ┃
         * ┃   ROOM    ┠─┼─┼─┼─┼─┼─┼─┼─┼─┼─╄━┯━┯━┓         ┃
         * ┃           ║ │ │ │ │ │ │ │ │ │ │ │ │ ┃         ┃
         * ┃           ┠─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─┼─╄━┯━┯━┯━┳━┛
         * ┃           ┃ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ │ ┃
         * ┗━┳━┯━┯━┯━┯━╃─┼─╆━═━┷━┷━┷━┷━┷═┷━╅─┼─┼─┼─┼─┼─┼─╄━┓
         *   ┃ │ │ │ │ │ │ ┃               ┃ │ │ │ │ │ │ │ ┃
         * ┏━╃─┼─┼─┼─┼─┼─┼─┨               ┠─┼─╆━┷═┷━┷━┷━╈━┛
         * ┃P│ │ │ │ │ │ │ ┃               ┃ │ ┃         ┗━┓
         * ┗━╈━┷━┷━┷━╅─┼─┼─┨               ┠─┼─┨           ┃
         *   ┃       ║ │ │ ║               ║ │ ┃           ┃
         * ┏━┛       ┗━╅─┼─┨   BALL ROOM   ┠─┼─┨           ┃
         * ┃           ┃ │ ┃               ┃ │ ┃           ┃
         * ┃           ┠─┼─┨               ┠─┼─┨  KITCHEN  ┃
         * ┃  CONSER-  ┃ │ ┃               ┃ │ ┃           ┃
         * ┃  VATORY   ┠─┼─┨               ┠─┼─┨           ┃
         * ┃           ┃ │ ┃               ┃ │ ┃           ┃
         * ┃           ┣━╅─╄━┯━┓       ┏━┯━╃─╆━┫           ┃
         * ┃           ┃ ┃ │ │ ┃       ┃ │ │ ┃ ┃           ┃
         * ┗━━━━━━━━━━━┛ ┗━┷━╅─╊━━━━━━━╉─╆━┷━┛ ┗━━━━━━━━━━━┛
         *                   ┃G┃       ┃W┃
         *                   ┗━┛       ┗━┛
         * 
         * \u250F\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2513 \u250F\u2501\u2513               \u250F\u2501\u2513 \u250F\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2513
         * \u2503           \u2503 \u2503 \u2503               \u2503S\u2503 \u2503           \u2503
         * \u2503           \u2517\u2501\u2549\u2500\u2544\u2501\u2533\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2533\u2501\u2549\u2500\u2544\u2501\u251B           \u2503
         * \u2503             \u2503 \u2502 \u2503           \u2503 \u2502 \u2503             \u2503
         * \u2503    STUDY    \u2520\u2500\u253C\u2500\u2528           \u2520\u2500\u253C\u2500\u2528             \u2503
         * \u2503             \u2503 \u2502 \u2503           \u2503 \u2502 \u2503             \u2503
         * \u2503             \u2520\u2500\u253C\u2500\u2528           \u2520\u2500\u253C\u2500\u2528   LOUNGE    \u2503
         * \u2503             \u2503 \u2502 \u2503           \u2503 \u2502 \u2503             \u2503
         * \u2517\u2501\u2533\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u252F\u2550\u2543\u2500\u253C\u2500\u2528   HALL    \u2520\u2500\u253C\u2500\u2528             \u2503
         *   \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2551           \u2503 \u2502 \u2503             \u2503
         * \u250F\u2501\u2543\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2528           \u2520\u2500\u253C\u2500\u2528             \u2503
         * \u2503L\u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503           \u2503 \u2502 \u2503             \u2503
         * \u2517\u2501\u2548\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2545\u2500\u253C\u2500\u253C\u2500\u2528           \u2520\u2500\u253C\u2500\u2544\u2550\u252F\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u2533\u2501\u251B
         *   \u2503         \u2503 \u2502 \u2502 \u2503           \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503
         * \u250F\u2501\u251B         \u2517\u2501\u2545\u2500\u253C\u2500\u2544\u2501\u252F\u2501\u252F\u2550\u2564\u2550\u252F\u2501\u252F\u2501\u2543\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2544\u2501\u2513
         * \u2503             \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502M\u2503
         * \u2503             \u2520\u2500\u253C\u2500\u2541\u2500\u2534\u2500\u2534\u2500\u2534\u2500\u2534\u2500\u2541\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2546\u2501\u251B
         * \u2503   LIBRARY   \u2551 \u2502 \u2503         \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503
         * \u2503             \u2520\u2500\u253C\u2500\u2528         \u2520\u2500\u253C\u2500\u2546\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u253B\u2501\u2513
         * \u2503             \u2503 \u2502 \u2503         \u2503 \u2502 \u2503               \u2503
         * \u2517\u2501\u2513         \u250F\u2501\u2543\u2500\u253C\u2500\u2528STAIRCASE\u2520\u2500\u253C\u2500\u2528               \u2503
         *   \u2503         \u2503 \u2502 \u2502 \u2503         \u2503 \u2502 \u2503               \u2503
         *   \u2523\u2501\u252F\u2501\u252F\u2550\u252F\u2501\u252F\u2501\u2543\u2500\u253C\u2500\u253C\u2500\u2528         \u2520\u2500\u253C\u2500\u2528               \u2503
         *   \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503         \u2503 \u2502 \u2503               \u2503
         * \u250F\u2501\u253B\u2550\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2545\u2500\u253C\u2500\u253C\u2500\u2544\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u2543\u2500\u253C\u2500\u2528               \u2503
         * \u2503           \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2551  DINING ROOM  \u2503
         * \u2503           \u2520\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2528               \u2503
         * \u2503           \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503               \u2503
         * \u2503 BILLIARD  \u2520\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2528               \u2503
         * \u2503           \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503               \u2503
         * \u2503   ROOM    \u2520\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2544\u2501\u252F\u2501\u252F\u2501\u2513         \u2503
         * \u2503           \u2551 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503         \u2503
         * \u2503           \u2520\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2544\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u2533\u2501\u251B
         * \u2503           \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503
         * \u2517\u2501\u2533\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u252F\u2501\u2543\u2500\u253C\u2500\u2546\u2501\u2550\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2537\u2550\u2537\u2501\u2545\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2544\u2501\u2513
         *   \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503               \u2503 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503
         * \u250F\u2501\u2543\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u253C\u2500\u2528               \u2520\u2500\u253C\u2500\u2546\u2501\u2537\u2550\u2537\u2501\u2537\u2501\u2537\u2501\u2548\u2501\u251B
         * \u2503P\u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2502 \u2503               \u2503 \u2502 \u2503         \u2517\u2501\u2513
         * \u2517\u2501\u2548\u2501\u2537\u2501\u2537\u2501\u2537\u2501\u2545\u2500\u253C\u2500\u253C\u2500\u2528               \u2520\u2500\u253C\u2500\u2528           \u2503
         *   \u2503       \u2551 \u2502 \u2502 \u2551               \u2551 \u2502 \u2503           \u2503
         * \u250F\u2501\u251B       \u2517\u2501\u2545\u2500\u253C\u2500\u2528   BALL ROOM   \u2520\u2500\u253C\u2500\u2528           \u2503
         * \u2503           \u2503 \u2502 \u2503               \u2503 \u2502 \u2503           \u2503
         * \u2503           \u2520\u2500\u253C\u2500\u2528               \u2520\u2500\u253C\u2500\u2528  KITCHEN  \u2503
         * \u2503  CONSER-  \u2503 \u2502 \u2503               \u2503 \u2502 \u2503           \u2503
         * \u2503  VATORY   \u2520\u2500\u253C\u2500\u2528               \u2520\u2500\u253C\u2500\u2528           \u2503
         * \u2503           \u2503 \u2502 \u2503               \u2503 \u2502 \u2503           \u2503
         * \u2503           \u2523\u2501\u2545\u2500\u2544\u2501\u252F\u2501\u2513       \u250F\u2501\u252F\u2501\u2543\u2500\u2546\u2501\u252B           \u2503
         * \u2503           \u2503 \u2503 \u2502 \u2502 \u2503       \u2503 \u2502 \u2502 \u2503 \u2503           \u2503
         * \u2517\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u251B \u2517\u2501\u2537\u2501\u2545\u2500\u254A\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2549\u2500\u2546\u2501\u2537\u2501\u251B \u2517\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u251B
         *                   \u2503G\u2503       \u2503W\u2503
         *                   \u2517\u2501\u251B       \u2517\u2501\u251B
         */
        /*
         * XXXXXXXXXXXXXXXXXXXXXXXXXX
         * X      X XXXXXXXXSX      X
         * X      |  |    |  |      X
         * X\     |  |    |  |      X
         * X------|  |    |  |      X
         * XX     ^ >|    |  |     /X
         * XL        |    |  |------X
         * XX-----   ------  ^     XX
         * X      |    ^^          MX
         * X      |< |   |   v     XX
         * X      |  |   |  |-------X
         * XX-----   |   |  |       X
         * XXv ^     -----  |       X
         * X-----|         >|       X
         * X     |          |       X
         * X     |          |--     X
         * X     |<            -----X
         * X-----|   v    v        XX
         * XX       |------|   v    X
         * XP       |      |  |----XX
         * XX---|< >|      |< |     X
         * X /   |  |      |  |     X
         * X     |  |      |  |     X
         * X     |  |--  --|  |     X
         * X      X   |  |   X\     X
         * XXXXXXXXXX XXXX XXXXXXXXXX
         * XXXXXXXXXXXXXXXXXXXXXXXXXX
         */
        for (int i = 0; i < map.length(); i++) {
            switch (map.charAt(i)) {
                case 'X':
                    System.out.print(ANSI_RED_BACKGROUND + " " + ANSI_RESET);
                    break;
                
                case '1':
                    System.out.print(ANSI_GRAY_BACKGROUND + " ");
                    break;
                
                case '2':
                    System.out.print(" " + ANSI_RESET);
                    break;

                case '3':
                    System.out.print("/" + ANSI_RESET);
                    break;
                
                case '4':
                    System.out.print("\\" + ANSI_GRAY_BACKGROUND);
                    break;

                default:
                    System.out.print(map.charAt(i));
                    break;
            }
        }
    }

    /**
     * Prompts the user for input with a choice from the given commands and with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to system output should be rolling
     * @param commands the commands to list and accept as valid input
     * @return the command selected by the user
     */
    public Command promptInput(String message, boolean rolling, Command... commands) {
        ArrayList<String> acceptedCommands = new ArrayList<>();

        // Print the user prompt
        if (rolling) rollingPrintln(message);
        else System.out.println(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for (Command command : commands) {
            if (rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
            else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
            acceptedCommands.add(command.getName());
            if(command.containsAliases()) Arrays.stream(command.getAliases()).forEach((alias) -> acceptedCommands.add(alias));
        }
        
        // Allow the user to provide input
        System.out.print("> ");
        String answer = input.nextLine().toLowerCase();

        // If the input is not a recognized command or alias, clear the console and prompt the user again.
        while (!acceptedCommands.contains(answer)) {
            clearConsole();

            // Print the user prompt
            if (rolling){
                rollingPrintln("You can't do that right now.");
                rollingPrintln(message);
            } else {
                System.out.println("You can't do that right now.");
                System.out.println(message);
            }

            // Print the command options
            for(Command command : commands) {
                if (rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
                else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription());
            }
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine().toLowerCase();
        }
        final String finalAnswer = answer;
        return ((Command)(Arrays.stream(commands).filter((a) -> a.matches(finalAnswer)).toArray()[0]));
    }

    /**
     * Prompts the user for input with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to system output should be rolling
     * @param options the options to accept as valid input
     * @return the option selected by the user
     */
    public String promptInput(String message, boolean rolling, String... options) {
        ArrayList<String> acceptedOptions = new ArrayList<>();

        // Print the user prompt
        if (rolling) rollingPrintln(message);
        else System.out.println(message);

        // Update the list of accepted inputs
        // This list will be used to validate the input
        for (String option : options) acceptedOptions.add(option);
        
        // Allow the user to provide input
        System.out.print("> ");
        String answer = input.nextLine().toLowerCase();

        // If the input is not a recognized option, prompt the user again.
        while (!acceptedOptions.contains(answer)) {
            clearConsole();

            // Prompt the user again
            if (rolling) rollingPrintln(message);
            else System.out.println(message);

            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine().toLowerCase();
        }
        return answer;
    }

    /**
     * Prompts the user for input with rolling printing if {@code rolling} is true
     * 
     * @param rolling whether the print to system output should be rolling
     * @param options the options to accept as valid input
     * @return the option selected by the user
     */
    public String promptInput(boolean rolling, String... options) {
        ArrayList<String> acceptedOptions = new ArrayList<>();

        // Update the list of accepted inputs
        // This list will be used to validate the input
        for (String option : options) acceptedOptions.add(option);
        
        // Allow the user to provide input
        System.out.print("> ");
        String answer = input.nextLine().toLowerCase();

        // If the input is not a recognized option, prompt the user again.
        while (!acceptedOptions.contains(answer)) {
            // Prompt the user again
            if (rolling) rollingPrintln("That's not an option right now.");
            else System.out.println("That's not an option right now.");

            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine().toLowerCase();
        }
        return answer;
    }

    /**
     * Prompts the user for input with rolling printing if {@code rolling} is true and validates the response with the provided predicate
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to system output should be rolling
     * @param tester the tester to use to validate input
     * @return the validated input provided by the user
     */
    public String promptConditionalInput(String message, boolean rolling, Predicate<String> tester) {
        // Print the user prompt
        if (rolling) rollingPrintln(message);
        else System.out.println(message);
        
        // Allow the user to provide input
        System.out.print("> ");
        String answer = input.nextLine();

        // If the input is not valid, clear the console and prompt the user again.
        while (!tester.test(answer)) {
            clearConsole();

            // Print the user prompt
            if (rolling){
                rollingPrintln("That's not a valid input.");
                rollingPrintln(message);
            } else {
                System.out.println("That's not a valid input.");
                System.out.println(message);
            }
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine();
        }
        return answer;
    }

    /**
     * Prompts the user for input with rolling printing if {@code rolling} is true and validates the response with the provided predicate
     * 
     * @param rolling whether the print to system output should be rolling
     * @param errorMessage the message to provide the user upon entry of invalid input
     * @param tester the tester to use to validate input
     * @return the validated input provided by the user
     */
    public String promptConditionalInput(boolean rolling, String errorMessage, Predicate<String> tester) {
        // Allow the user to provide input
        System.out.print("> ");
        String answer = input.nextLine();

        // If the input is not valid, clear the console and prompt the user again.
        while (!tester.test(answer)) {
            clearConsole();

            // Print the user prompt
            if (rolling) rollingPrintln(errorMessage);
            else System.out.println(errorMessage);
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine();
        }
        return answer;
    }

    /**
     * Prompts the user for a yes/no input with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to system output should be rolling
     * @return the command selected by the user
     */
    public Command promptYesNo(String message, boolean rolling) {
        Command[] commands = {Command.YES, Command.NO};
        ArrayList<String> acceptedCommands = new ArrayList<>();

        // Print the user prompt
        if (rolling) rollingPrintln(message);
        else System.out.println(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for (Command command : commands) {
            acceptedCommands.add(command.getName());
            if (command.containsAliases()) Arrays.stream(command.getAliases()).forEach((alias) -> acceptedCommands.add(alias));
        }
        
        // Allow the user to provide input
        System.out.print("> ");
        String answer = input.nextLine().toLowerCase();

        // If the input is not a recognized command or alias, clear the console and prompt the user again.
        while (!acceptedCommands.contains(answer)) {
            clearConsole();

            // Print the user prompt
            if (rolling) rollingPrintln(message + " (yes/no)");
            else System.out.println(message + " (yes/no)");
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine().toLowerCase();
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

    /**
     * Delays 500 milliseconds to provide a break in the printing to system output.
     */
    public static void textDelay() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println();
        }
    }

    /**
     * Delays the given number of milliseconds to provide a break in the printing to system output.
     */
    public static void textDelay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println();
        }
    }

    /**
     * Clears the console and prints a loading animations that cycles the given number of times.
     * 
     * @param cycles the number of times to cycle the loading animation
     * @throws IllegalArgumentException if {@code cycles} is less negative or zero.
     */
    public static void loadingAnimation(int cycles) throws IllegalArgumentException {
        if (cycles <= 0) throw new IllegalArgumentException("Illegal number of loading animation cycles");
        clearConsole();
        for (int i = 0; i < cycles; i++) {
            for(int j = 0; j < 5; j++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println();
                }
                System.out.print(".");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println();
            }
            clearConsole();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println();
        }
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
