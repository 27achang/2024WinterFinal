/**
 * The {@code EvidenceSample} class encapsulates basic functionality for DNA and fingerprint samples collected by the user
 * 
 * @author Alexander Chang
 * @version 1.4, 1/2/2025
 * @since 1.4
 */
public class EvidenceSample {
    protected Room room;
    protected Suspect suspect;
    protected boolean hasResult;
    protected final int turnsForAnalysis = (int)(Math.random() * 6) + 5;

    EvidenceSample(Room room, Suspect suspect) {
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
        return "Evidence sample of " + suspect + " found in the " + room;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EvidenceSample)) return false;
        return room == ((EvidenceSample) obj).getRoom() &&
            suspect == ((EvidenceSample) obj).getSuspect() &&
            hasResult == ((EvidenceSample) obj).hasResult();
    }
}
