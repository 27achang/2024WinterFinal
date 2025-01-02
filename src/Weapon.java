/**
 * The Weapon enum type contains the possible weapons used in the murder
 * 
 * @author Alexander Chang
 * @version 1.1, 12/29/2024
 * @since 1.0
 */
public enum Weapon {
    CANDLESTICK("Candlestick"),
    DAGGER("Dagger"),
    LEAD_PIPE("Lead pipe"),
    REVOLVER("Revolver"),
    ROPE("Rope"),
    WRENCH("Wrench");

    private String name;

    Weapon(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
