public class DNASample {
    private Room room;
    private Suspect suspect;
    private boolean hasResult;

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
