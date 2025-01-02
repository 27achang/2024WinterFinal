/**
 * The {@code CameraResult} class encapsulates functionality for surveillance camera clips requested by the user
 * 
 * @author Alexander Chang
 * @version 1.3, 1/1/2025
 * @since 1.2
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
                do {
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
                do {
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
                do {
                    suspect = Suspect.values()[(int)(Math.random() * 7)];
                } while (suspect == answerSuspect);
            }
        }
    }

    String generateMessage() {
        return generateMessage("The footage that you requested from the night of the murder showed ");
    }

    String generateMessage(String message) {
        // Edge cases
        if (room == null) return message + "no activity.";
        if (roomIsRedHerring && weapon == null && suspect == null) return message + "some activity in the " + room.getName().toLowerCase() + ".";
        if (!roomIsRedHerring && !weaponIsRedHerring && !suspectIsRedHerring) return message + (suspect != null ? suspect.getName() : "a figure") + " killing Thompson " + (weapon != null ? "with a " + weapon.getName().toLowerCase() + " " : "") + "in the " + room.getName().toLowerCase() + ".";
        
        // General processing
        if (suspectIsRedHerring) {
            message += suspect.getName() + " passing through the " + room.getName().toLowerCase();
            if (weapon != null || !roomIsRedHerring) {
                message += " with a ";
                if (weapon != null) {
                    if (!weaponIsRedHerring) message += "bloody " + weapon.getName().toLowerCase();
                    else message += weapon.getName().toLowerCase() + " with some red liquid on it";
                    if (!roomIsRedHerring) message += " and a ";
                    else message += " in the background";
                }
                if (!roomIsRedHerring) {
                    message += "dead body in the background";
                }
            }
            message += ".";
        } else if (suspect == null) {
            if (!roomIsRedHerring) message += "a figure holding ";
            if (!weaponIsRedHerring) message += "a bloody " + weapon.getName().toLowerCase();
            else message += "a " + weapon.getName().toLowerCase() + " with some red liquid on it";
            message += " in the " + room.getName().toLowerCase() + ".";
        } else {
            message += suspect.getName();
            if (weapon != null) {
                message += " holding ";
                if (!weaponIsRedHerring) message += "a bloody " + weapon.getName().toLowerCase();
                else message += "a " + weapon.getName().toLowerCase() + " with some red liquid on it";
            }
            if (roomIsRedHerring) message += " sneaking across the back wall of the " + room.getName().toLowerCase();
            else message += " in the " + room.getName().toLowerCase();
            message += ".";
        }
        return message;
        /*
         * Alternate messages:
         *  sulking around
         *  acting suspicious
         *  barely at the edge of the frame
         *  sneaking across the opposite wall
         *  hunched over something
         */
    }

    String generateLogMessage() {
        return generateMessage("Cameras showed ") + "\n";
    }

    Room getRoom() {
        return room;
    }

    boolean isRoomRedHerring() {
        return roomIsRedHerring;
    }

    Weapon getWeapon() {
        return weapon;
    }

    boolean isWeaponRedHerring() {
        return weaponIsRedHerring;
    }

    Suspect getSuspect() {
        return suspect;
    }

    boolean isSuspectRedHerring() {
        return suspectIsRedHerring;
    }

    int getTurnsForAnalysis() {
        return turnsForAnalysis;
    }

    void setRoom(Room room) {
        this.room = room;
    }

    void setRoomIsRedHerring(boolean roomIsRedHerring) {
        this.roomIsRedHerring = roomIsRedHerring;
    }

    void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    void setWeaponIsRedHerring(boolean weaponIsRedHerring) {
        this.weaponIsRedHerring = weaponIsRedHerring;
    }

    void setSuspect(Suspect suspect) {
        this.suspect = suspect;
    }

    void setSuspectIsRedHerring(boolean suspectIsRedHerring) {
        this.suspectIsRedHerring = suspectIsRedHerring;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CameraResult)) return false;
        return room == ((CameraResult) obj).getRoom() &&
            roomIsRedHerring == ((CameraResult) obj).isRoomRedHerring() &&
            weapon == ((CameraResult) obj).getWeapon() &&
            weaponIsRedHerring == ((CameraResult) obj).isWeaponRedHerring() &&
            suspect == ((CameraResult) obj).getSuspect() &&
            suspectIsRedHerring == ((CameraResult) obj).isSuspectRedHerring() &&
            turnsForAnalysis == ((CameraResult) obj).getTurnsForAnalysis();
    }
}
