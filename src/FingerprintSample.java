public class FingerprintSample {
    private Room room;
    private Weapon weapon;
    private Suspect suspect;
    private boolean hasResult;

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
