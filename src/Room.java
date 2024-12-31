/**
 * The Room enum type contains functionality for rooms within the game
 * 
 * @author Alexander Chang
 * @version 1.01, 12/29/2024
 */
public enum Room {
    HALL("Hall", new int[][] {{11, 5}, {12, 6}, {13, 6}}),
    LOUNGE("Lounge", new int[][] {{19, 5}}),
    DINING_ROOM("Dining Room", new int[][] {{18, 11}, {18, 13}}),
    KITCHEN("Kitchen", new int[][] {{20, 20}}),
    BALLROOM("Ballroom", new int[][] {{10, 19}, {15, 19}, {10, 20}, {15, 20}}),
    CONSERVATORY("Conservatory", new int[][] {{5, 21}}),
    BILLIARD_ROOM("Billiard Room", new int[][] {{2, 14}, {5, 16}}),
    LIBRARY("Library", new int[][] {{6, 9}, {4, 10}}),
    STUDY("Study", new int[][] {{6, 3}}),
    STAIRCASE("Staircase", new int[][] {{11, 9}, {12, 9}, {13, 9}});

    private String name;
    private int[][] entrances;
    private Item item;
    private Weapon weapon;
    private Suspect weaponFingerprints;
    private Suspect DNA;
    private boolean UVCluePresent;

    Room(String name, int[][] entrances) {
        this.name = name;
        this.entrances = entrances;
    }

    void setItem(Item item) {
        this.item = item;
    }

    boolean hasItem() {
        return item != null;
    }

    void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    boolean hasWeapon() {
        return weapon != null;
    }

    void setWeaponFingerprints(Suspect weaponFingerprints) {
        this.weaponFingerprints = weaponFingerprints;
    }

    void setDNA(Suspect DNA) {
        this.DNA = DNA;
    }

    void setUVCluePresent(boolean UVCluePresent) {
        this.UVCluePresent = UVCluePresent;
    }

    String getName() {
        return name;
    }

    int[][] getEntrances() {
        return entrances;
    }

    Item getItem() {
        return item;
    }

    Weapon getWeapon() {
        return weapon;
    }

    Suspect getWeaponFingerprints() {
        return weaponFingerprints;
    }

    Suspect getDNA() {
        return DNA;
    }

    boolean isUVCluePresent() {
        return UVCluePresent;
    }

    @Override
    public String toString() {
        return name;
    }
}
