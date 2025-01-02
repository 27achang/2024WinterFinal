/**
 * The Suspect enum type contains the possible suspects
 * 
 * @author Alexander Chang
 * @version 1.3, 12/31/2024
 * @since 1.0
 */
public enum Suspect {
    SCARLETT("Miss Scarlett"),
    MUSTARD("Colonel Mustard"),
    WHITE("Mrs. White"),
    GREEN("Reverend Green"),
    PEACOCK("Mrs. Peacock"),
    PLUM("Professor Plum");

    private String name;

    Suspect(String name) {
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
