/**
 * The Suspect enum type contains the possible suspects
 * 
 * @author Alexander Chang
 * @version 1.00, 12/19/2024
 */
public enum Suspect {
    SCARLET("Miss Scarlet"),
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
