package hotelmanagement.model;

public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid"),
    PARTIAL("Partially Paid");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
