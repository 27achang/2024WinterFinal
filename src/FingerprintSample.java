/**
 * The {@code FingerprintSample} class encapsulates functionality for fingerprint samples collected by the user
 * 
 * @author Alexander Chang
 * @version 1.4, 1/2/2025
 * @since 1.1
 */
public class FingerprintSample extends EvidenceSample {
    private Weapon weapon;

    FingerprintSample(Room room, Weapon weapon, Suspect suspect) {
        super(room, suspect);
        this.weapon = weapon;
    }

    Weapon getWeapon() {
        return weapon;
    }

    @Override
    public String toString() {
        return "Fingerprints of " + suspect + " found on a " + weapon + " in the " + room;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FingerprintSample)) return false;
        return room == ((FingerprintSample) obj).getRoom() &&
            suspect == ((FingerprintSample) obj).getSuspect() &&
            weapon == ((FingerprintSample) obj).getWeapon() &&
            hasResult == ((FingerprintSample) obj).hasResult();
    }
}
