package hotelmanagement.model;

public enum RoomType {
    SINGLE("Single Room", 1, 1000.00),
    DOUBLE("Double Room", 2, 1800.00),
    DELUXE("Deluxe Room", 3, 3000.00),
    SUITE("Executive Suite", 4, 5000.00);

    private final String displayName;
    private final int defaultCapacity;
    private final double basePricePerNight;

    RoomType(String displayName, int defaultCapacity, double basePricePerNight) {
        this.displayName = displayName;
        this.defaultCapacity = defaultCapacity;
        this.basePricePerNight = basePricePerNight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultCapacity() {
        return defaultCapacity;
    }

    public double getBasePricePerNight() {
        return basePricePerNight;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
