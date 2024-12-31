/**
 * The FingerprintSample class encapsulates functionality for fingerprint samples collected by the user
 * 
 * @author Alexander Chang
 * @version 1.02, 12/30/2024
 */
public class FingerprintSample {
    private Room room;
    private Weapon weapon;
    private Suspect suspect;
    private boolean hasResult;
    private final int turnsForAnalysis = (int)(Math.random() * 6) + 5;

    FingerprintSample(Room room, Weapon weapon, Suspect suspect) {
        this.room = room;
        this.weapon = weapon;
        this.suspect = suspect;
        hasResult = suspect != null;
    }

    Room getRoom() {
        return room;
    }

    Weapon getWeapon() {
        return weapon;
    }

    Suspect getSuspect() {
        return suspect;
    }

    boolean hasResult() {
        return hasResult;
    }

    int getTurnsForAnalysis() {
        return turnsForAnalysis;
    }

    @Override
    public String toString() {
        return "Fingerprints of " + suspect + " found on a " + weapon + " in the " + room;
    }

    @Override
    public boolean equals(Object obj) {
        return room == ((FingerprintSample) obj).getRoom() &&
            suspect == ((FingerprintSample) obj).getSuspect() &&
            weapon == ((FingerprintSample) obj).getWeapon() &&
            hasResult == ((FingerprintSample) obj).hasResult();
    }
}
