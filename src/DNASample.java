/**
 * The {@code DNASample} class encapsulates functionality for DNA samples collected by the user
 * 
 * @author Alexander Chang
 * @version 1.4, 1/2/2025
 * @since 1.1
 */
public class DNASample extends EvidenceSample {

    DNASample(Room room, Suspect suspect) {
        super(room, suspect);
    }

    @Override
    public String toString() {
        return "DNA of " + suspect + " found in the " + room;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DNASample)) return false;
        return room == ((DNASample) obj).getRoom() &&
            suspect == ((DNASample) obj).getSuspect() &&
            hasResult == ((DNASample) obj).hasResult();
    }
}
