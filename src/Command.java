import java.util.Arrays;

public class Command {
    private String name;
    private String[] aliases;
    private String description;

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

    String getName() {
        return name;
    }

    String getDescription() {
        return name;
    }

    String getAliases() {
        return name;
    }
}
