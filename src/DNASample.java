/**
 * The DNASample class encapsulates functionality for DNA samples collected by the user
 * 
 * @author Alexander Chang
 * @version 1.02, 12/30/2024
 */
public class DNASample {
    private Room room;
    private Suspect suspect;
    private boolean hasResult;
    private final int turnsForAnalysis = (int)(Math.random() * 6) + 5;

    DNASample(Room room, Suspect suspect) {
        this.room = room;
        this.suspect = suspect;
        hasResult = suspect != null;
    }

    Room getRoom() {
        return room;
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
        return "DNA of " + suspect + " found in the " + room;
    }

    @Override
    public boolean equals(Object obj) {
        return room == ((DNASample) obj).getRoom() &&
            suspect == ((DNASample) obj).getSuspect() &&
            hasResult == ((DNASample) obj).hasResult();
    }
}
