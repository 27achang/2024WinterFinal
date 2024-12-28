import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The Simulation class contains the core mechanics of the game including turn actions and prompting for input
 * 
 * @author Alexander Chang
 * @version 1.00, 12/19/2024
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
        X1    2|  |1  2|  |1    2X
        X4    2|  |1  2|  |1    2X
        X------|  |1  2|  |1    2X
        XX     ^ >|1  2|  |1    3X
        X         |1  2|  |------X
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
        X1   2|  |--12--|  |1   2X
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
    private ArrayList<Item> inventory = new ArrayList<>();
    private Room currentRoom;
    private ArrayList<Room> visitedRooms = new ArrayList<>();
    private int numDonuts;
    private int totalDonutsFound;
    private int xPos; // Starting at 1
    private int yPos; // Starting at 1
    private boolean solvedMystery;
    private boolean tookTooLong;

    private int actionableTurns;
    private int turns;
    private int turnsSinceLastDonutFound; // Only donuts found in the kitchen
    
    private int totalRoomSearches;
    private int totalFingerprintsCollected;
    private int totalFingerprintsAnalyzed;
    private int totalDNACollected;
    private int totalDNAAnalyzed;
    private int totalUVScans;
    private int totalCamerasRequested;
    private int totalDonutsEatenByJoseph;

    private Room roomOfCollectedFingerprints;
    private Weapon weaponOfCollectedFingerprints;
    private Suspect suspectOfCollectedFingerprints;
    private boolean labScanningFingerprints;
    private Room roomOfLabFingerprints;
    private Weapon weaponOfLabFingerprints;
    private Suspect suspectOfLabFingerprints;
    private int turnsSinceFingerprintsSubmitted;

    private Room roomOfCollectedDNA;
    private Suspect suspectOfCollectedDNA;
    private boolean labScanningDNA;
    private Room roomOfLabDNA;
    private Suspect suspectOfLabDNA;
    private int turnsSinceDNASubmitted;
    
    private Room roomOfRequestedCameras;
    private Suspect suspectOfRequestedCameras;
    private boolean findingCameras;
    private int turnsSinceCamerasRequested;

    private Suspect answerSuspect;
    private Weapon answerWeapon;
    private Room answerRoom;

    private String guessedSuspect;
    private String guessedWeapon;
    private String guessedRoom;

    private boolean suspectCorrect;
    private boolean weaponCorrect;
    private boolean roomCorrect;

    Simulation() {
        input = new Scanner(System.in);

        clearConsole();

        if (!pollANSISupport()) return;
        ensureTerminalHeight();

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
     * Ensure the user's terminal is tall enough to accomodate the map and command list
     */
    private void ensureTerminalHeight() {
        String answer;
        do {
            clearConsole();
            for (int i = 1; i <= 39 - 4; i++) {
                System.out.println(i);
            }
            rollingPrintln("Please ensure all the above numbers and this and following lines are visible on your terminal window.");
            rollingPrintln("If they are not, resize and press enter to see these numbers again.");
            rollingPrintln("To exit this test, type done and press enter.");
            System.out.print("> ");
            answer = input.nextLine();
        } while (!answer.equals("done"));
        clearConsole();
    }

    /**
     * Opens the about screen, providing information about Clue 2.0
     */
    private void about() {
        clearConsole();

        // Tell the user about Clue 2.0
        rollingPrintln("This project attempts to recreate the classic mystery game Clue, adding more modern features and items. This project was completed in its entirety by Alexander Chang. Unauthorized reproduction " +
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
        rollingPrint("Thompson was killed in his mansion just last night and the mystery killer's been on the run since. ");
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
        rollingPrint("If at any point you need to reach me or can't manage the investigation any longer for whatever reason, I'll be right at the base of the central staircase with the media. ");
        textDelay();
        rollingPrint("It'd be to your advantage to take notes on the clues you find and remember, ");
        textDelay(250);
        rollingPrint(ANSI_WHITE_BACKGROUND + ANSI_BLACK + " time is of the essence. " + ANSI_RESET);
        textDelay();
        System.out.println();
        System.out.println();
        rollingPrint("Best of luck, ");
        textDelay(250);
        rollingPrint("detective.");
        System.out.println();
        System.out.println();
        textDelay(5000);
        
        // Provide the user key game tips
        System.out.println("========================================== KEY GAME TIPS ==========================================");
        rollingPrintln(" Press enter to exit results and dialogue screens.");
        rollingPrintln(" Don't rush through. Keep an eye out for your lab results.");
        rollingPrintln(" Analyze samples you collect with Detective Joseph. Not everything is free. What would a cop want?");
        rollingPrintln(" Keep track of the clues you have collected. There is no way to go back.");
        System.out.println("===================================================================================================");

        textDelay();        
        
        rollingPrint("To begin, press enter.");
        input.nextLine();

        selectAnswers();
        placeItems();
        placeClues();

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

            // Deliver lab and camera results
            if (labScanningDNA && turnsSinceDNASubmitted == 5) {
                System.out.println("===== Lab Results =====");
                rollingPrintln("The DNA you collected from the " + roomOfLabDNA.getName().toLowerCase() + (suspectOfLabDNA != null ? " was identified as that of " + suspectOfLabDNA.getName() + "." : " could not be identified."));
                System.out.println("=======================");
                String answer;
                do {
                    System.out.println();
                    rollingPrintln("To acknowledge these results, please type ok below and then press enter.");
                    System.out.print("> ");
                    answer = input.nextLine();
                } while (!answer.toLowerCase().equals("ok"));
                roomOfLabDNA = null;
                suspectOfLabDNA = null;
                labScanningDNA = false;
            } else if (labScanningFingerprints && turnsSinceFingerprintsSubmitted == 5) {
                System.out.println("===== Lab Results =====");
                rollingPrintln("The fingerprints you collected from the " + weaponOfLabFingerprints.getName().toLowerCase() + " in the " + roomOfLabFingerprints.getName().toLowerCase() + " were identified as those of " + suspectOfLabFingerprints.getName() + ".");
                System.out.println("=======================");
                String answer;
                do {
                    System.out.println();
                    rollingPrintln("To acknowledge these results, please type ok below and then press enter.");
                    System.out.print("> ");
                    answer = input.nextLine();
                } while (!answer.toLowerCase().equals("ok"));
                roomOfLabFingerprints = null;
                weaponOfLabFingerprints = null;
                suspectOfLabFingerprints = null;
                labScanningFingerprints = false;
            } else if (findingCameras && turnsSinceCamerasRequested == 5) {
                System.out.println("===== Camera Results =====");
                rollingPrintln("The footage that you requested of the " + roomOfRequestedCameras.getName().toLowerCase() + " on the night of the murder showed " + suspectOfRequestedCameras.getName());
                double random = Math.random();
                if (random < 0.2) rollingPrint(" sulking around.");
                else if (random < 0.4) rollingPrint(" acting suspicious.");
                else if (random < 0.6) rollingPrint(" barely at the edge of the frame.");
                else if (random < 0.8) rollingPrint(" sneaking across the opposite wall.");
                else if (random < 1) rollingPrint(" hunched over something.");
                System.out.println("==========================");
                String answer;
                do {
                    System.out.println();
                    rollingPrintln("To acknowledge these results, please type ok below and then press enter.");
                    System.out.print("> ");
                    answer = input.nextLine();
                } while (!answer.toLowerCase().equals("ok"));
                roomOfRequestedCameras = null;
                suspectOfRequestedCameras = null;
                findingCameras = false;
            }

            // Determine the actions available to the user
            ArrayList<Command> options = new ArrayList<>();
            char currentCell = mapArray[yPos - 1][xPos - 1];

            if (currentRoom == Room.STAIRCASE) {
                options.add(Command.UP);
                if (numDonuts >= 2) {
                    if (!labScanningDNA && !labScanningFingerprints) {
                        if (roomOfCollectedDNA != null) options.add(Command.SUBMIT_DNA);
                        if (roomOfCollectedFingerprints != null) options.add(Command.SUBMIT_FINGERPRINTS);
                    }
                }
                if (!findingCameras && numDonuts >= 1) options.add(Command.REQUEST_CAMERA);
                options.add(Command.ACCUSE);

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
                // Determine available room options
                if (currentRoom != null && currentRoom != Room.STAIRCASE) {
                    options.add(Command.SEARCH);
                    if (inventory.contains(Item.DNA_COLLECTOR) && roomOfCollectedDNA == null) options.add(Command.COLLECT_DNA);
                    if (inventory.contains(Item.FINGERPRINT_COLLECTOR) && roomOfCollectedFingerprints == null) options.add(Command.COLLECT_FINGERPRINTS);
                    if (inventory.contains(Item.UV_SCANNER)) options.add(Command.UV_SCAN);
                    if(currentCell == '/' || currentCell == '\\') options.add(Command.PASS);
                }
            }

            // Determine if the inventory can be opened
            if (inventory.size() > 0 || numDonuts > 0) options.add(Command.INVENTORY);

            // Prompt the user to choose from the available options
            Command action = promptInputWithMap("So, detective, what do you want to do?", false, options);
            // If the user is trying to use a secret passageway, process the action.
            if (action == Command.PASS) {
                // Top left to bottom right
                if (xPos == 1 && yPos == 3) {
                    xPos = 19;
                    yPos = 24;
                    currentRoom = Room.KITCHEN;
                }
                // Bottom right to top left
                else if (xPos == 19 && yPos == 24) {
                    xPos = 1;
                    yPos = 3;
                    currentRoom = Room.STUDY;
                }
                // Top right to bottom left
                else if (xPos == 24 && yPos == 5) {
                    xPos = 2;
                    yPos = 21;
                    currentRoom = Room.CONSERVATORY;
                }
                // Bottom left to top right
                else if (xPos == 2 && yPos == 21) {
                    xPos = 24;
                    yPos = 5;
                    currentRoom = Room.LOUNGE;
                } else throw new IllegalStateException("Unexpected attempted secret pathway travel");
            }
            // If the user is trying to enter or leave a room with a 'diagonal' door (study, lounge, and conservatory), process the action.
            else if (xPos == 6 && yPos == 3 && action == Command.DOWN) {
                xPos++;
                yPos += 2;
                currentRoom = null;
            } else if (xPos == 7 && yPos == 5 && action == Command.UP) {
                xPos--;
                yPos -= 2;
            } else if (xPos == 19 && yPos == 5 && action == Command.DOWN) {
                xPos--;
                yPos += 2;
                currentRoom = null;
            } else if (xPos == 18 && yPos == 7 && action == Command.UP) {
                xPos++;
                yPos -= 2;
            } else if (xPos == 5 && yPos == 21 && action == Command.RIGHT) {
                xPos++;
                yPos--;
                currentRoom = null;
            } else if (xPos == 6 && yPos == 20 && action == Command.LEFT) {
                xPos--;
                yPos++;
            }
            // Process any other entry or exit from rooms
            else if (currentCell == '^' && action == Command.UP) {
                yPos -= 2;
                if(currentRoom != null) {
                    currentRoom = null;
                }
            } else if (currentCell == 'v' && action == Command.DOWN) {
                yPos += 2;
                if(currentRoom != null) {
                    currentRoom = null;
                }
            } else if (currentCell == '<' && action == Command.LEFT) {
                xPos -= 2;
                if(currentRoom != null) {
                    currentRoom = null;
                }
            } else if (currentCell == '>' && action == Command.RIGHT) {
                xPos += 2;
                if(currentRoom != null) {
                    currentRoom = null;
                }
            } else if (currentRoom == Room.STAIRCASE && action == Command.UP) {
                yPos--;
                currentRoom = null;
            }
            // Process standard motion and other actions
            else switch (action) {
                case Command.UP -> yPos--;
                case Command.DOWN -> yPos++;
                case Command.LEFT -> xPos--;
                case Command.RIGHT -> xPos++;
                case Command.INVENTORY -> openInventory();
                case Command.SEARCH -> searchRoom();
                case Command.COLLECT_DNA -> collectDNA();
                case Command.COLLECT_FINGERPRINTS -> collectFingerprints();
                case Command.UV_SCAN -> scanUV();
                case Command.SUBMIT_DNA -> submitDNA();
                case Command.SUBMIT_FINGERPRINTS -> submitFingerprints();
                case Command.REQUEST_CAMERA -> requestCamera();
                case Command.ACCUSE -> {
                    Command answer = promptYesNo("You think you know whodunit? Are you sure you're ready to report your final findings?",true);
                    if (answer == Command.NO) {
                        rollingPrint("Alright then, detective. Come back when you're fully sure you got it.");
                    } else {
                        solvedMystery = accuse();
                        gameActive = false;
                    }
                }
                default -> throw new IllegalStateException("Unexpected game command execution");
            }

            // If the user entered a room, update the currentRoom variable
            try {
                currentRoom = Arrays.stream(Room.values()).filter((a) -> Arrays.stream(a.getEntrances()).anyMatch((b) -> b[0] == xPos && b[1] == yPos)).findFirst().get();
                if(!visitedRooms.contains(currentRoom)) visitedRooms.add(currentRoom);
            } catch (NoSuchElementException e) {}

            // Increment the turns counters
            if(action != Command.INVENTORY) actionableTurns++;
            turns++;
            if (totalDonutsFound > 0) turnsSinceLastDonutFound++;
            if (labScanningDNA && action != Command.SUBMIT_DNA) turnsSinceDNASubmitted++;
            if (labScanningFingerprints && action != Command.SUBMIT_FINGERPRINTS) turnsSinceFingerprintsSubmitted++;
            if (findingCameras && action != Command.REQUEST_CAMERA) turnsSinceCamerasRequested++;

            if (actionableTurns > 250) {
                gameActive = false;
                tookTooLong = true;
            }
        }
        clearConsole();
        if (tookTooLong) {
            rollingPrint("Unfortunately, detective " + name + ", you took too long in your investigation. ");
            textDelay();
            rollingPrint("The suspects most likely out of the country and out of our control by now. ");
            textDelay();
            rollingPrint("Better luck next time.");
        } else {
            rollingPrint("Ready to find out together whether you were right, detective? ");
            textDelay();
            rollingPrint("When you're ready, press enter and we'll find out.");
            input.nextLine();
            System.out.println();
            rollingPrintln("         Your Guess      | Correct Answer");
            rollingPrint("Suspect: " + guessedSuspect + (new String(new char[15 - guessedSuspect.length()]).replace("\0", " ")) + " | ");
            textDelay();
            rollingPrintln((guessedSuspect.equals(answerSuspect.getName()) ? ANSI_GREEN_BACKGROUND : ANSI_RED_BACKGROUND) + answerSuspect + (new String(new char[15 - answerSuspect.getName().length()]).replace("\0", " ")) + ANSI_RESET);
            textDelay();
            rollingPrint(" Weapon: " + guessedWeapon + (new String(new char[15 - guessedWeapon.length()]).replace("\0", " ")) + " | ");
            textDelay();
            rollingPrintln((guessedWeapon.equals(answerWeapon.getName()) ? ANSI_GREEN_BACKGROUND : ANSI_RED_BACKGROUND) + answerWeapon + (new String(new char[15 - answerWeapon.getName().length()]).replace("\0", " ")) + ANSI_RESET);
            textDelay();
            rollingPrint("   Room: " + guessedRoom + (new String(new char[15 - guessedRoom.length()]).replace("\0", " ")) + " | ");
            textDelay();
            rollingPrintln((guessedRoom.equals(answerRoom.getName()) ? ANSI_GREEN_BACKGROUND : ANSI_RED_BACKGROUND) + answerRoom + (new String(new char[15 - answerRoom.getName().length()]).replace("\0", " ")) + ANSI_RESET);
            System.out.println();
            if (solvedMystery) {
                rollingPrint("Very nicely done, detective " + name + "! I'm glad I could trust you.");
            } else if (
                (suspectCorrect && weaponCorrect) ||
                (weaponCorrect && roomCorrect) ||
                (suspectCorrect && roomCorrect)
            ) {
                rollingPrint("Nearly there, detective " + name + "! I'm sure you'll get it next time.");
            } else if (suspectCorrect || weaponCorrect || roomCorrect) {
                rollingPrint("Well done on the " + (suspectCorrect ? "suspect" : (weaponCorrect ? "weapon" : "room")) + ". Better luck with the others next time.");
            } else {
                rollingPrint("Better luck next time, detective.");
            }
        }
        textDelay(2000);
        System.out.println();
        System.out.println();
        rollingPrintln(" Final inventory: " + inventory.stream().map(Item::getName).collect(Collectors.joining(", ")));
        rollingPrintln("   Rooms visited: " + visitedRooms.stream().map(Room::getName).filter((a) -> !a.equals("Staircase")).collect(Collectors.joining(", ")));
        System.out.println("-------------------------------------------" + (turns >= 10 ? "-" : "") + (turns >= 100 ? "-" : ""));
        rollingPrintln("                            Turns taken: " + turns);
        rollingPrintln("                     Total donuts found: " + totalDonutsFound);
        rollingPrintln("                    Total room searches: " + totalRoomSearches);
        rollingPrintln("           Total fingerprints collected: " + totalFingerprintsCollected);
        rollingPrintln("            Total fingerprints analyzed: " + totalFingerprintsAnalyzed);
        rollingPrintln("            Total DNA samples collected: " + totalDNACollected);
        rollingPrintln("             Total DNA samples analyzed: " + totalDNAAnalyzed);
        rollingPrintln("               Total UV scans completed: " + totalUVScans);
        rollingPrintln("      Total pieces of footage requested: " + totalCamerasRequested);
        rollingPrintln(" Total donuts eaten by detective Joseph: " + totalDonutsEatenByJoseph);

    }

    private void searchRoom() {
        loadingAnimation(1);
        totalRoomSearches++;
        double random = Math.random();
        if (currentRoom.hasItem() && !(inventory.contains(currentRoom.getItem()))) {
            getItem(currentRoom.getItem());
        } else if (currentRoom == Room.KITCHEN) {
            if ((totalDonutsFound > 0 && turnsSinceLastDonutFound < 10 + 1) || random < 0.2) rollingPrint("You found nothing!");
            else if (random < 0.3) getDonuts(1);
            else if (random <= 0.8) getDonuts(2);
            else if (random <= 0.95) getDonuts(3);
            else if (random <= 0.99) getDonuts(4);
            else getDonuts(5);
        } else if (random < 0.05) getDonuts(1);
        else rollingPrint("You found nothing!");
        input.nextLine();
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

    private void collectDNA() {
        loadingAnimation(1);
        // 30% chance you can still find DNA if it is not relevant to the case
        if (currentRoom.getDNA() != null || Math.random() < 0.3) {
            rollingPrint("You've collected DNA from the " + currentRoom.getName().toLowerCase() + ". ");
            textDelay();
            rollingPrint("Submit it to Detective Joseph at the central staircase for analysis.");
            totalDNACollected++;
            roomOfCollectedDNA = currentRoom;
            suspectOfCollectedDNA = currentRoom.getDNA();
        } else rollingPrint("You struggled to find anything to collect DNA from in the " + currentRoom.getName().toLowerCase() + ".");
        input.nextLine();
    }

    private void collectFingerprints() {
        loadingAnimation(1);
        if(currentRoom.hasWeapon()) {
            rollingPrint("You found a bloody " + currentRoom.getWeapon().getName().toLowerCase() + " with some fingerprints on it in the " + currentRoom.getName().toLowerCase() + "! ");
            textDelay();
            rollingPrint("Submit it to Detective Joseph at the central staircase for analysis.");
            totalFingerprintsCollected++;
            roomOfCollectedFingerprints = currentRoom;
            weaponOfCollectedFingerprints = currentRoom.getWeapon();
            suspectOfCollectedFingerprints = currentRoom.getWeaponFingerprints();
        }
        else{
            double random = Math.random();
            if(random < 0.5) rollingPrint("You struggled to find any fingerprints in the " + currentRoom.getName().toLowerCase() + ".");
            else rollingPrint("You found some weak fingerprints in the corner of the " + currentRoom.getName().toLowerCase() + " but struggled to collect them.");
        }
        input.nextLine();
    }

    private void scanUV() {
        loadingAnimation(1);
        if(currentRoom.isUVCluePresent()) {
            rollingPrint("You found a some bloodspots on the wall of the " + currentRoom.getName().toLowerCase() + "!");
            totalUVScans++;
        }
        else{
            double random = Math.random();
            if(random < 0.5) rollingPrint("You thought you saw something on the wall of the " + currentRoom.getName().toLowerCase() + ", but it turned out to be a fluke.");
            else rollingPrint("You didn't see a thing when you turned on your UV light in the " + currentRoom.getName().toLowerCase() + ".");
        }
        input.nextLine();
    }

    private void submitDNA() {
        clearConsole();
        rollingPrint("You have some DNA for the lab to scan? ");
        textDelay();
        rollingPrint("I'll need a couple donuts to do that, please. ");
        textDelay();
        Command answer = promptYesNo("Are you willing to give me two?", true);
        if (answer == Command.YES){
            rollingPrint("Thanks for the donuts! ");
            textDelay();
            rollingPrint("It'll be about 5 turns until the analysis will be ready. ");
            textDelay();
            rollingPrint("Nice work, detective.");
            labScanningDNA = true;
            suspectOfLabDNA = suspectOfCollectedDNA;
            roomOfLabDNA = roomOfCollectedDNA;
            suspectOfCollectedDNA = null;
            roomOfCollectedDNA = null;
            totalDNAAnalyzed++;
            numDonuts -= 2;
            totalDonutsEatenByJoseph += 2;
        } else {
            rollingPrint("Alright, detective. ");
            textDelay();
            rollingPrint("If you bring me two donuts next time, I'll be sure to send it to scan right away.");
        }
        input.nextLine();
    }

    private void submitFingerprints() {
        clearConsole();
        rollingPrint("You have some fingerprints for the lab to scan? ");
        textDelay();
        Command answer = promptYesNo("Could you maybe give me two donuts for it?", true);
        if (answer == Command.YES){
            rollingPrint("Thanks for obliging. ");
            textDelay();
            rollingPrint("It'll be about 5 turns until the analysis will be ready. ");
            textDelay();
            rollingPrint("Nice work, detective.");
            labScanningFingerprints = true;
            roomOfLabFingerprints = roomOfCollectedFingerprints;
            weaponOfLabFingerprints = weaponOfCollectedFingerprints;
            suspectOfLabFingerprints = suspectOfCollectedFingerprints;
            roomOfCollectedFingerprints = null;
            weaponOfCollectedFingerprints = null;
            suspectOfCollectedFingerprints = null;
            totalFingerprintsAnalyzed++;
            numDonuts -= 2;
            totalDonutsEatenByJoseph += 2;
        } else {
            rollingPrint("Whatever you say, detective. ");
            textDelay();
            rollingPrint("If you do happen to have two donuts for me next time, I'll be sure to send it to the lab on the double.");
        }
        input.nextLine();
    }

    private void requestCamera() {
        clearConsole();
        rollingPrint("You want to request some camera footage from the night of the murder? ");
        textDelay();
        rollingPrint("I'd be happy to oblige if you have a couple donuts, maybe two, for me. ");
        textDelay();
        Command answer = promptYesNo("Do you have some I could take?", true);
        if (answer == Command.YES){
            rollingPrint("Thanks for the donuts! ");
            textDelay();
            String room = promptInput("Which room do you want the footage from? (Hall, Lounge, Dining Room, Kitchen, Ballroom, Conservatory, Billiard Room, Library, Study)", true, "Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study");
            rollingPrint("Alright, the clips should be back in about 5 turns. ");
            textDelay();
            rollingPrint("Nice work, detective.");
            findingCameras = true;
            roomOfRequestedCameras = Arrays.stream(Room.values()).filter((a) -> room.equals(a.getName())).findFirst().get();
            suspectOfRequestedCameras = roomOfRequestedCameras.getCameraSubject();
            totalCamerasRequested++;
            numDonuts--;
            totalDonutsEatenByJoseph++;
        } else {
            rollingPrint("You don't? ");
            textDelay();
            rollingPrint("Well maybe next time. ");
            textDelay();
            rollingPrint("If you do happen upon some extra, I'd be happy to take them off your hands and find you the footage you need.");
        }
        input.nextLine();
    }

    private boolean accuse() {
        clearConsole();
        rollingPrint("I sure hope you're right. ");
        textDelay();
        rollingPrint("Who murdered Thompson? ");
        textDelay();
        rollingPrintln("(Miss Scarlet, Colonel Mustard, Mrs. White, Reverend Green, Mrs. Peacock, Professor Plum)");
        guessedSuspect = promptInput(true, "Miss Scarlet", "Colonel Mustard", "Mrs. White", "Reverend Green", "Mrs. Peacock", "Professor Plum");
        System.out.println();
        rollingPrint("What was he murdered with? ");
        textDelay();
        rollingPrintln("(Candlestick, Dagger, Lead pipe, Revolver, Rope, Wrench)");
        guessedWeapon = promptInput(true, "Candlestick", "Dagger", "Lead pipe", "Revolver", "Rope", "Wrench");
        System.out.println();
        rollingPrint("Where was he murdered? ");
        textDelay();
        rollingPrintln("(Hall, Lounge, Dining Room, Kitchen, Ballroom, Conservatory, Billiard Room, Library, Study)");
        guessedRoom = promptInput(true, "Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study");
        suspectCorrect = guessedSuspect.equals(answerSuspect.getName());
        weaponCorrect = guessedWeapon.equals(answerWeapon.getName());
        roomCorrect = guessedRoom.equals(answerRoom.getName());
        return suspectCorrect && weaponCorrect && roomCorrect;
    }

    // Game Utility Methods

    /**
     * Place the 3 forensic items in random rooms
     */
    private void placeItems() {
        for(Item item : Item.values()) {
            int random;
            Room room;
            do{
                random = (int)(Math.random() * 9);
                room = Room.values()[random];
            } while (room.hasItem());
            room.setItem(item);
        }
    }

    /**
     * Select the correct suspect, room, and weapon
     */
    private void selectAnswers() {
        int random;
        random = (int)(Math.random() * 6);
        answerSuspect = Suspect.values()[random];
        random = (int)(Math.random() * 9);
        answerRoom = Room.values()[random];
        random = (int)(Math.random() * 6);
        answerWeapon = Weapon.values()[random];
    }

    /**
     * Place clues in various rooms
     */
    private void placeClues() {
        // Place the weapon
        int random = (int)(Math.random() * 9);
        Room room = Room.values()[random];
        room.setWeapon(answerWeapon);
        room.setWeaponFingerprints(answerSuspect);

        // Place the DNA
        answerRoom.setDNA(answerSuspect);

        // Place the camera sighting
        answerRoom.setCameraSubject(answerSuspect);

        // Place the UV clue (75% chance one is placed)
        answerRoom.setUVCluePresent(Math.random() < 0.75);
    }
    
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
        if (numDonuts > 0) rollingPrintln(numDonuts + " donut" + (numDonuts > 1 ? "s" : ""));
        inventory.forEach((a) -> rollingPrintln(a.getName() + " - " + a.getDescription()));
        System.out.println();
        rollingPrint("To return to the map, press enter.");
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
                if (rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
                );
                else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
                );
            }
        
            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine().toLowerCase();
        }
        final String finalAnswer = answer;
        return ((Command)(Arrays.stream(commands.toArray()).filter((a) -> ((Command) a).matches(finalAnswer)).toArray()[0]));
    }

    /**
     * Prints the map and prompts the user for input with a choice from the given commands and with rolling printing if {@code rolling} is true
     * 
     * @param message the message with which to prompt the user
     * @param rolling whether the print to system output should be rolling
     * @param commands the commands to list and accept as valid input
     * @return the command selected by the user
     */
    public Command promptInputWithMap(String message, boolean rolling, ArrayList<Command> commands) {
        printMap();
        ArrayList<String> acceptedCommands = new ArrayList<>();

        // Print the user prompt
        rollingPrintln(message);

        // Print the command options and update the list of accepted commands (including aliases)
        // This list will be used to validate the input
        for (int i = 0; i < commands.size(); i++) {
            Command command = (Command) commands.get(i);
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
            System.out.println();
            printMap();
            rollingPrintln(message);

            // Print the command options
            for(Command command : commands) {
                if (rolling) rollingPrintln(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
                );
                else System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " " + command.getName() + " " + ANSI_RESET + " - " + command.getDescription() +
                            (command.getAliases().length > 0 ? " (Alias" + (command.getAliases().length > 1 ? "es" : "") + ": " + Arrays.stream(command.getAliases()).limit(command.getAliases().length - 1).collect(Collectors.joining(", ")) + (command.getAliases().length == 1 ? "" : (command.getAliases().length == 2 ? "" : ",") + " and ") + command.getAliases()[command.getAliases().length - 1] + ")" : "")
                );
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
        String answer = input.nextLine();

        // If the input is not a recognized option, prompt the user again.
        while (!acceptedOptions.contains(answer)) {
            clearConsole();

            // Prompt the user again
            if (rolling) rollingPrintln(message);
            else System.out.println(message);

            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine();
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
        String answer = input.nextLine();

        // If the input is not a recognized option, prompt the user again.
        while (!acceptedOptions.contains(answer)) {
            // Prompt the user again
            if (rolling) rollingPrintln("That's not an option right now.");
            else System.out.println("That's not an option right now.");

            // Allow the user to provide input
            System.out.print("> ");
            answer = input.nextLine();
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
