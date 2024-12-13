import java.util.Arrays;

public enum Command {
    ABOUT("about", "Learn more about Clue 2.0"),
    BEGIN("begin","Start the game");

    private final String name;
    private String[] aliases = new String[0];
    private final String description;

    Command(String name, String description) {
        this.name = name;
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
}
