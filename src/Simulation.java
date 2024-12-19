import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private static final String map = """
        XXXXXXXXXXXXXXXXXXXXXXXXXX
        X1    2X XXXXXXXX X1    2X
        X1    2|  |    |  |1    2X
        X4    2|  |    |  |1    2X
        X------|  |    |  |1    2X
        XX     ^ >|    |  |1    3X
        X         |    |  |------X
        XX-----   ------  ^     XX
        X1    2|    ^^           X
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
        X        |1    2|  |----XX
        XX---|< >|1    2|< |1   2X
        X1/  2|  |1    2|  |1   2X
        X1   2|  |1    2|  |1   2X
        X1   2|  |--  --|  |1   2X
        X1    2X   |12|   X4    2X
        XXXXXXXXXX XXXX XXXXXXXXXX
        XXXXXXXXXXXXXXXXXXXXXXXXXX""";

    private static final char[][] mapArray = {
        {' ', ' ', ' ', ' ', ' ', ' ', 'X', ' ', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'S', 'X', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
        {'\\', ' ', ' ', ' ', ' ', 'v', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
        {'-', '-', '-', '-', '-', '-', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
        {'X', ' ', ' ', ' ', ' ', ' ', '^', ' ', '>', '|', '<', ' ', ' ', ' ', '|', ' ', ' ', '|', 'v', ' ', ' ', ' ', ' ', '/'},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', 'v', 'v', ' ', '|', ' ', ' ', '|', '-', '-', '-', '-', '-', '-'},
        {'X', '-', '-', '-', '-', '-', ' ', ' ', ' ', '-', '-', '-', '-', '-', '|', ' ', ' ', '^', ' ', ' ', ' ', ' ', ' ', 'X'},
        {' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', '^', '^', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', '>', '|', '<', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', 'v', ' ', ' ', ' ', ' ', ' ', 'X'},
        {' ', ' ', ' ', 'v', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', '|', '-', '-', '-', '-', '-', '-', '-'},
        {'X', '-', '-', '-', '-', '-', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', '|', '^', ' ', ' ', ' ', ' ', ' ', ' '},
        {'X', 'v', ' ', '^', ' ', ' ', ' ', ' ', ' ', '-', '-', '-', '-', '-', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'-', '-', '-', '-', '-', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>', '|', '<', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', '^', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', '-', '-', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', '>', '|', '<', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '-', '-', '-', '-', '-'},
        {'-', '-', '-', '-', '-', '|', ' ', ' ', ' ', 'v', ' ', ' ', ' ', ' ', 'v', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X'},
        {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', '-', '-', '-', '-', '-', '-', '|', ' ', ' ', ' ', 'v', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', '^', ' ', ' ', ' ', ' ', '^', '|', ' ', ' ', '|', '-', '-', '-', '-', 'X'},
        {'X', '-', '-', '-', '|', '<', ' ', '>', '|', '<', ' ', ' ', ' ', ' ', '>', '|', '<', ' ', '|', '^', ' ', ' ', ' ', ' '},
        {' ', '/', ' ', ' ', '>', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', '|', '-', '-', ' ', ' ', '-', '-', '|', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', 'X', ' ', ' ', ' ', '|', ' ', ' ', '|', ' ', ' ', ' ', 'X', '\\', ' ', ' ', ' ', ' ', ' '},
        {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', ' ', 'X', 'X', 'X', 'X', ' ', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
    };

    private Scanner input;
    private boolean gameActive;
    private String name;
    private String color;
    private int turns;
    private ArrayList<Room> visitedRooms = new ArrayList<>();
    private Room currentRoom;
    private boolean roomSearched;
    private ArrayList<Item> inventory = new ArrayList<>();
    private int numDonuts;
    private int totalDonutsFound;
    private int turnsSinceLastDonutFound; // Only donuts found in the kitchen
    private int xPos; // Starting at 1
    private int yPos; // Starting at 1

    Simulation() {
        input = new Scanner(System.in);

        clearConsole();

        name = "Danny";
        color = "green";
        xPos = 6;
        yPos = 20;
        run();

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
        rollingPrint("The arrows represent entrances to each room. ");
        textDelay();
        rollingPrint("If at any point you need to reach me or can't manage the investigation any longer for whatever reason, I'll be right at the base of the central staircase with the media.");
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

        // Spawn the user
        switch (color) {
            case "scarlet":
                xPos = 17;
                yPos = 1;
                break;
            
            case "mustard":
                xPos = 24;
                yPos = 8;
                break;

            case "white":
                xPos = 15;
                yPos = 25;
                break;
            
            case "green":
                xPos = 10;
                yPos = 25;
                break;
            
            case "blue":
                xPos = 1;
                yPos = 19;
                break;
            
            case "plum":
                xPos = 1;
                yPos = 6;
                break;

            default:
                xPos = 8;
                yPos = 1;
                break;
        }

        // Begins the actual text adventure
        run();
    }

    /**
     * Runs the text adventure, prompting the user for input on each turn
     */
    private void run() {
        gameActive = true;
        while (gameActive) {
            clearConsole();
            printMap();
            // Determine the actions available to the user
            ArrayList<Command> options = new ArrayList<>();
            char currentCell = mapArray[yPos - 1][xPos - 1];

            if (currentRoom == Room.STAIRCASE) {
                options.add(Command.UP);
                options.add(Command.SUBMIT_DNA);
                options.add(Command.SUBMIT_FINGERPRINTS);
                if(numDonuts >= 1) options.add(Command.REQUEST_CAMERA);
            } else {
                // Determine available movement options
                char aboveCell = (yPos == 1 ? ' ' : mapArray[yPos - 2][xPos - 1]);
                char belowCell = (yPos == 25 ? ' ' : mapArray[yPos][xPos - 1]);
                char leftCell = (xPos == 1 ? ' ' : mapArray[yPos - 1][xPos - 2]);
                char rightCell = (xPos == 24 ? ' ' : mapArray[yPos - 1][xPos]);
                if (yPos != 1 && (currentCell == '^' || (aboveCell != '|' && aboveCell != '-' && aboveCell != 'X'))) options.add(Command.UP);
                if (yPos != 25 && (currentCell == 'v' || (belowCell != '|' && belowCell != '-' && belowCell != 'X'))) options.add(Command.DOWN);
                if (xPos != 1 && (currentCell == '<' || (leftCell != '|' && leftCell != '-' && leftCell != 'X'))) options.add(Command.LEFT);
                if (xPos != 24 && (currentCell == '>' || (rightCell != '|' && rightCell != '-' && rightCell != 'X'))) options.add(Command.RIGHT);
                if (currentRoom != null && currentRoom != Room.STAIRCASE && !roomSearched) options.add(Command.SEARCH);
            }

            // Determine if the inventory can be opened
            if (inventory.size() > 0) options.add(Command.INVENTORY);

            // Prompt the user to choose from the available options
            Command action = promptInput("So, detective, what do you want to do?", false, options);
            // If the user is trying to enter or leave a room with a 'diagonal' door (study, lounge, and conservatory), process the action.
            if (xPos == 6 && yPos == 3 && action == Command.DOWN) {
                xPos++;
                yPos += 2;
                currentRoom = null;
                roomSearched = false;
            } else if (xPos == 7 && yPos == 5 && action == Command.UP) {
                xPos--;
                yPos -= 2;
            } else if (xPos == 19 && yPos == 5 && action == Command.DOWN) {
                xPos--;
                yPos += 2;
                currentRoom = null;
                roomSearched = false;
            } else if (xPos == 18 && yPos == 7 && action == Command.UP) {
                xPos++;
                yPos -= 2;
            } else if (xPos == 5 && yPos == 21 && action == Command.RIGHT) {
                xPos++;
                yPos--;
                currentRoom = null;
                roomSearched = false;
            } else if (xPos == 6 && yPos == 20 && action == Command.LEFT) {
                xPos--;
                yPos++;
            }
            // Process any other entry or exit from rooms
            else if (currentCell == '^' && action == Command.UP) {
                yPos -= 2;
                if(currentRoom != null) {
                    currentRoom = null;
                    roomSearched = false;
                }
            } else if (currentCell == 'v' && action == Command.DOWN) {
                yPos += 2;
                if(currentRoom != null) {
                    currentRoom = null;
                    roomSearched = false;
                }
            } else if (currentCell == '<' && action == Command.LEFT) {
                xPos -= 2;
                if(currentRoom != null) {
                    currentRoom = null;
                    roomSearched = false;
                }
            } else if (currentCell == '>' && action == Command.RIGHT) {
                xPos += 2;
                if(currentRoom != null) {
                    currentRoom = null;
                    roomSearched = false;
                }
            }
            // Process standard motion and other actions
            else switch (action) {
                case Command.UP -> yPos--;
                case Command.DOWN -> yPos++;
                case Command.LEFT -> xPos--;
                case Command.RIGHT -> xPos++;
                case Command.INVENTORY -> openInventory();
                case Command.SEARCH -> searchRoom();
                default -> throw new IllegalStateException("Unexpected game command execution");
            }

            // If the user entered a room, update the currentRoom variable
            try {
                currentRoom = Arrays.stream(Room.values()).filter((a) -> Arrays.stream(a.getEntrances()).anyMatch((b) -> b[0] == xPos && b[1] == yPos)).findFirst().get();
                if(!visitedRooms.contains(currentRoom)) visitedRooms.add(currentRoom);
            } catch (NoSuchElementException e) {}

            // Increment the turns counters
            turns++;
            if (totalDonutsFound > 0) turnsSinceLastDonutFound++;
        }
    }

    private void searchRoom() {
        roomSearched = true;
        /*
         * Kitchen:
         *  On 5th turn after last donut finding:
         *   20% Nothing found
         *   10% 1 donut
         *   50% 2 donuts
         *   15% 3 donuts
         *   4% 4 donuts
         *   1% 5 donuts
         * 
         * Other Rooms:
         *  No items left to find:
         *   99% Nothing found
         *   1% 1 donut
         *  Otherwise:
         *   70% Nothing found
         *   10% Fingerprint collector
         *   10% DNA collector
         *   10% UV scanner
         */
        double random = Math.random();
        if (currentRoom == Room.KITCHEN) {
            if (turnsSinceLastDonutFound < 5 || random < 0.2) rollingPrint("You found nothing!");
            else if (random < 0.3) getDonuts(1);
            else if (random <= 0.8) getDonuts(2);
            else if (random <= 0.95) getDonuts(3);
            else if (random <= 0.99) getDonuts(4);
            else getDonuts(5);
        } else {
            if (inventory.contains(Item.FINGERPRINT_COLLECTOR) && inventory.contains(Item.DNA_COLLECTOR) && inventory.contains(Item.UV_SCANNER)) {
                if (random < 0.99) rollingPrint("You found nothing!");
                else getDonuts(1);
            } else {
                if (random <= 0.7) rollingPrint("You found nothing!");
                else if (random < 0.8) getItem(Item.FINGERPRINT_COLLECTOR);
                else if (random < 0.9) getItem(Item.DNA_COLLECTOR);
                else getItem(Item.UV_SCANNER);
            }
        }
        textDelay(2000);
    }

    private void getDonuts(int numFound) {
        rollingPrint("You found " + numFound + " donut" + (numFound > 1 ? "s" : "") +"!");
        numDonuts += numFound;
        totalDonutsFound += numFound;
        if (currentRoom == Room.KITCHEN) turnsSinceLastDonutFound = 0;
    }

    private void getItem(Item itemFound) {
        rollingPrint("You found a " + itemFound.getName() + "!");
        inventory.add(itemFound);
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
        boolean inRoom = false;
         for (int i = 0; i < map.length(); i++) {
            if (map.charAt(i) == '1' || map.charAt(i) == '4') inRoom = true;
            else if (map.charAt(i) == '2' || map.charAt(i) == '3') inRoom = false;
            switch (map.charAt(i)) {
                case 'X':
                    System.out.print(ANSI_RED_BACKGROUND + '\u00A0');
                    if(i == map.length() - 1 || map.charAt(i + 1) != 'X') System.out.print(ANSI_RESET);
                    break;
                
                case '1':
                    if (i == 27 * yPos + xPos) System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_GRAY_BACKGROUND);
                    else System.out.print(ANSI_GRAY_BACKGROUND + " ");
                    break;
                
                case '2':
                    if (i == 27 * yPos + xPos) System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_RESET);
                    else System.out.print(" " + ANSI_RESET);
                    break;

                case '3':
                    if (i == 27 * yPos + xPos) System.out.print(ANSI_WHITE_BACKGROUND + "/" + ANSI_RESET);
                    else System.out.print("/" + ANSI_RESET);
                    break;
                
                case '4':
                    if (i == 27 * yPos + xPos) System.out.print(ANSI_WHITE_BACKGROUND + "\\" + ANSI_GRAY_BACKGROUND);
                    else System.out.print(ANSI_GRAY_BACKGROUND + "\\");
                    break;

                default:
                    if (i == 27 * yPos + xPos) System.out.print(ANSI_WHITE_BACKGROUND + map.charAt(i) + (inRoom ? ANSI_GRAY_BACKGROUND : ANSI_RESET));
                    else System.out.print(map.charAt(i));
                    break;
            }
        }
        System.out.println();
    }

    /**
     * Opens the player's inventory
     */
    private void openInventory() {
        clearConsole();

        rollingPrintln("===== Your Inventory =====");
        if (numDonuts > 0) rollingPrintln(numDonuts + " donuts");
        inventory.forEach((a) -> rollingPrintln(a.getName() + " - " + a.getDescription()));
        System.out.println();
        rollingPrint("To return to the map, press enter");
        input.nextLine();
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
        rollingPrintln(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for (Command command : commands) {
            if (rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
            );
            else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
            );
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
            if (rolling) rollingPrintln("You can't do that right now.");
            else System.out.println("You can't do that right now.");
            rollingPrintln(message);

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
     * Prompts the user for input with a choice from the given commands and with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to system output should be rolling
     * @param commands the commands to list and accept as valid input
     * @return the command selected by the user
     */
    public Command promptInput(String message, boolean rolling, ArrayList<Command> commands) {
        ArrayList<String> acceptedCommands = new ArrayList<>();

        // Print the user prompt
        rollingPrintln(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for (Command command : commands) {
            if (rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
            );
            else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
            );
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
            if (rolling) rollingPrintln("You can't do that right now.");
            else System.out.println("You can't do that right now.");
            rollingPrintln(message);

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
        return ((Command)(Arrays.stream(commands.toArray()).filter((a) -> ((Command) a).matches(finalAnswer)).toArray()[0]));
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
