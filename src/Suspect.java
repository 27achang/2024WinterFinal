/**
 * The Suspect enum type contains the possible suspects
 * 
 * @author Alexander Chang
 * @version 1.01, 12/29/2024
 */
public enum Suspect {
    SCARLETT("Miss Scarlett", "her"),
    MUSTARD("Colonel Mustard", "his"),
    WHITE("Mrs. White", "her"),
    GREEN("Reverend Green", "his"),
    PEACOCK("Mrs. Peacock", "her"),
    PLUM("Professor Plum", "his");

    private String name;
    private String possessivePronoun;

    Suspect(String name, String possessivePronoun) {
        this.name = name;
        this.possessivePronoun = possessivePronoun;
    }

    String getName() {
        return name;
    }

    String getPossessivePronoun() {
        return possessivePronoun;
    }

    @Override
    public String toString() {
        return name;
    }
}
