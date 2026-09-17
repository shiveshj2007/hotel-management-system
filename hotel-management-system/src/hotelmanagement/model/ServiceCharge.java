package hotelmanagement.model;

import java.io.Serializable;
import java.time.LocalDate;

public class ServiceCharge implements Serializable {
    private static final long serialVersionUID = 1L;

    private String description;
    private double amount;
    private LocalDate dateAdded;

    public ServiceCharge() {
    }

    public ServiceCharge(String description, double amount, LocalDate dateAdded) {
        this.description = description;
        this.amount = amount;
        this.dateAdded = dateAdded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return description + " - Rs. " + String.format("%.2f", amount);
    }
}
