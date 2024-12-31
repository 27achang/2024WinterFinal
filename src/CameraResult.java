/**
 * The CameraResult class encapsulates functionality for surveillance camera clips requested by the user
 * 
 * @author Alexander Chang
 * @version 1.02, 12/30/2024
 */
public class CameraResult {
    private Room room;
    private boolean roomIsRedHerring;
    private Weapon weapon;
    private boolean weaponIsRedHerring;
    private Suspect suspect;
    private boolean suspectIsRedHerring;
    private final int turnsForAnalysis = (int)(Math.random() * 3) + 8;

    CameraResult(Room answerRoom, Weapon answerWeapon, Suspect answerSuspect) {
        double random = Math.random();
        // 80% chance you get the room
        // <10% chance red herring for room
        if (random <= 0.9) {
            if (random > 0.8) {
                roomIsRedHerring = true;
                // Select a random room that is not the answerRoom
                do{
                    room = Room.values()[(int)(Math.random() * 10)];
                } while (room == answerRoom);
            } else room = answerRoom;

            // 35% chance you get the weapon
            random = Math.random();
            if (random <= 0.35) {
                weapon = answerWeapon;
            // <10% chance red herring for weapon
            } else if (random > 0.9) {
                weaponIsRedHerring = true;
                // Select a random weapon that is not the answerWeapon
                do{
                    weapon = Weapon.values()[(int)(Math.random() * 7)];
                } while (weapon == answerWeapon);
            }

            // 10% chance you get the suspect
            random = Math.random();
            if (random <= 0.1) {
                suspect = answerSuspect;
            // <10% chance red herring for suspect
            } else if (random > 0.9) {
                suspectIsRedHerring = true;
                // Select a random suspect that is not the answerSuspect
                do{
                    suspect = Suspect.values()[(int)(Math.random() * 7)];
                } while (suspect == answerSuspect);
            }
        }
    }

    String generateMessage() {
        // Implementation not complete
        String message = "The footage that you requested of the ";
        /*
            rollingPrint("The footage that you requested of the " + roomOfRequestedCameras.getName().toLowerCase() + " on the night of the murder showed ");
            if (suspectOfRequestedCameras != null) {
                rollingPrintln(suspectOfRequestedCameras.getName());
                double random = Math.random();
                if (random < 0.2) rollingPrint(" sulking around.");
                else if (random < 0.4) rollingPrint(" acting suspicious.");
                else if (random < 0.6) rollingPrint(" barely at the edge of the frame.");
                else if (random < 0.8) rollingPrint(" sneaking across the opposite wall.");
                else if (random < 1) rollingPrint(" hunched over something.");
            } else {
                rollingPrintln("no activity.");
            }
         */
        return message;
    }

    Room getRoom() {
        return room;
    }

    boolean isRoomIsRedHerring() {
        return roomIsRedHerring;
    }

    Weapon getWeapon() {
        return weapon;
    }

    boolean isWeaponIsRedHerring() {
        return weaponIsRedHerring;
    }

    Suspect getSuspect() {
        return suspect;
    }

    boolean isSuspectIsRedHerring() {
        return suspectIsRedHerring;
    }

    int getTurnsForAnalysis() {
        return turnsForAnalysis;
    }

    @Override
    public boolean equals(Object obj) {
        return room == ((FingerprintSample) obj).getRoom() &&
            suspect == ((FingerprintSample) obj).getSuspect() &&
            weapon == ((FingerprintSample) obj).getWeapon();
    }
}
