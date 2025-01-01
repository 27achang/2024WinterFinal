import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author Alexander Chang
 * @version 1.2, 11/14/2024
 */
public class CameraResultTest {
    private final String message = "The footage that you requested from the night of the murder showed ";

    @Test
    public void roomWeaponSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green killing Thompson with a candlestick in the ballroom.", footage.generateMessage());
    }
    
    @Test
    public void roomWeaponRHSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(true);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green passing through the ballroom with a bloody candlestick and a dead body in the background.", footage.generateMessage());
    }
    
    @Test
    public void roomWeapon() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(null);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "a figure killing Thompson with a candlestick in the ballroom.", footage.generateMessage());
    }

    @Test
    public void roomRHWeaponSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(true);
        assertEquals(message + "Reverend Green holding a candlestick with some red liquid on it in the ballroom.", footage.generateMessage());
    }

    @Test
    public void roomRHWeaponRHSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(true);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(true);
        assertEquals(message + "Reverend Green passing through the ballroom with a candlestick with some red liquid on it and a dead body in the background.", footage.generateMessage());
    }

    @Test
    public void roomRHWeapon() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(null);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(true);
        assertEquals(message + "a figure holding a candlestick with some red liquid on it in the ballroom.", footage.generateMessage());
    }

    @Test
    public void roomSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(null);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green killing Thompson in the ballroom.", footage.generateMessage());
    }

    @Test
    public void roomRHSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(true);
        footage.setWeapon(null);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green passing through the ballroom with a dead body in the background.", footage.generateMessage());
    }

    @Test
    public void room() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(false);
        footage.setSuspect(null);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(null);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "a figure killing Thompson in the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoomWeaponSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green holding a bloody candlestick sneaking across the back wall of the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoomWeaponRHSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(true);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green passing through the ballroom with a bloody candlestick in the background.", footage.generateMessage());
    }

    @Test
    public void rHRoomWeapon() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(null);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "a bloody candlestick in the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoomRHWeaponSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(true);
        assertEquals(message + "Reverend Green holding a candlestick with some red liquid on in sneaking across the back wall of the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoomRHWeaponRHSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(true);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(true);
        assertEquals(message + "Reverend Green passing through the ballroom with a candlestick with some red liquid on it in the background.", footage.generateMessage());
    }

    @Test
    public void rHRoomRHWeapon() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(null);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(Weapon.CANDLESTICK);
        footage.setWeaponIsRedHerring(true);
        assertEquals(message + "a candlestick with some red liquid on it in the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoomSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(null);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green sneaking across the back wall of the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoomRHSuspect() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(Suspect.GREEN);
        footage.setSuspectIsRedHerring(true);
        footage.setWeapon(null);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "Reverend Green passing through the ballroom.", footage.generateMessage());
    }

    @Test
    public void rHRoom() {
        CameraResult footage = new CameraResult(Room.BALLROOM, Weapon.CANDLESTICK, Suspect.GREEN);
        footage.setRoom(Room.BALLROOM);
        footage.setRoomIsRedHerring(true);
        footage.setSuspect(null);
        footage.setSuspectIsRedHerring(false);
        footage.setWeapon(null);
        footage.setWeaponIsRedHerring(false);
        assertEquals(message + "some activity in the ballroom.", footage.generateMessage());
    }
}
