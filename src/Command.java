import java.util.Arrays;

/**
 * The Command enum type contains all accepted user commands and their aliases.
 * The enum type also contains simple processing functions for game commands
 * 
 * @author Alexander Chang
 * @version 0.15, 12/16/2024
 */
public enum Command {
    ABOUT("about", "Learn more about Clue 2.0"),
    BEGIN("begin", "Start the game"),
    YES("yes", "","y"),
    NO("no", "","n"),
    UP("up", "Move upwards", "u"),
    DOWN("down", "Move downwards", "d"),
    LEFT("left", "Move leftwards", "l"),
    RIGHT("right", "Move rightwards", "r"),
    INVENTORY("inventory", "Open your inventory", "inv");

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
