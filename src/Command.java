import java.util.Arrays;

/**
 * The Command enum type contains all accepted user commands and their aliases.
 * The enum type also contains simple processing functions for game commands
 * 
 * @author Alexander Chang
 * @version 1.00, 12/19/2024
 */
public enum Command {
    ABOUT("about", "Learn more about Clue 2.0"),
    BEGIN("begin", "Start the game", "start"),
    YES("yes", "","y"),
    NO("no", "","n"),
    UP("up", "Move upwards", "w"),
    DOWN("down", "Move downwards", "s"),
    LEFT("left", "Move leftwards", "a"),
    RIGHT("right", "Move rightwards", "d"),
    PASS("pass", "Pass through the secret pathway to the room at the opposite corner of the mansion."),
    INVENTORY("inventory", "Open your inventory", "inv"),
    SEARCH("search", "Search around the room you are currently in"),
    COLLECT_DNA("collectdna", "Collect DNA from the room you are currently in"),
    COLLECT_FINGERPRINTS("collectfingerprints", "Collect fingerprints from the room you are currently in", "fingerprint"),
    UV_SCAN("uvscan","Use a UV light to scan the room you are currently in for hidden clues"),
    SUBMIT_DNA("submitdna", "Submit DNA to Detective Joseph to send to the lab for analysis"),
    SUBMIT_FINGERPRINTS("submitfingerprints", "Submit fingerprints to Detective Joseph to send to the lab for analysis"),
    REQUEST_CAMERA("requestcamera", "Request security camera footage from the night of the murder"),
    ACCUSE("accuse","Report to Detective Joseph who you think committed the murder, with what, and where. One chance only.");

    private final String name;
    private final String[] aliases;
    private final String description;

    Command(String name, String description) {
        this.name = name;
        this.aliases = new String[0];
        this.description = description;
    }

    Command(String name, String description, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }

    boolean matches(String givenCommand) {
        return givenCommand.equals(name) || containsAlias(givenCommand);
    }

    boolean containsAlias(String alias) {
        return Arrays.stream(aliases).anyMatch(alias::equals);
    }

    boolean containsAliases() {
        return aliases == null || aliases.length > 0;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    String[] getAliases() {
        return aliases;
    }

    @Override
    public String toString() {
        return name;
    }
}
