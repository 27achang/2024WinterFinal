/**
 * The Item enum type contains functionality for game items
 * 
 * @author Alexander Chang
 * @version 0.14, 12/13/2024
 */
public enum Item {
    FINGERPRINT_COLLECTOR("Fingerprint Collector", "Collect fingerprints from various surfaces. Analyze these with Detective Joseph."),
    DNA_COLLECTOR("DNA Collector", "Collect DNA samples from various surfaces. Analyze these with Detective Joseph."),
    UV_SCANNER("UV Scanner", "Scan rooms with UV light for hidden clues.");

    private String name;
    private String description;
    
    Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Use an item
     */
    void use() {
        return;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
