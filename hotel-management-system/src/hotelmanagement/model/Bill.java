package hotelmanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_TAX_RATE = 0.12;

    private String billId;
    private String reservationId;
    private String guestId;
    private String guestName;
    private String roomNumber;
    private int nights;
    private double roomRate;
    private double totalRoomCharge;
    private List<ServiceCharge> serviceCharges;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double grandTotal;
    private PaymentStatus paymentStatus;
    private LocalDate billDate;
    private String paymentMethod;

    public Bill() {
        this.serviceCharges = new ArrayList<>();
        this.taxRate = DEFAULT_TAX_RATE;
        this.paymentStatus = PaymentStatus.PENDING;
        this.billDate = LocalDate.now();
    }

    public Bill(String billId, String reservationId, String guestId, String guestName,
                String roomNumber, int nights, double roomRate, double taxRate) {
        this();
        this.billId = billId;
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.roomRate = roomRate;
        this.taxRate = taxRate;
        this.totalRoomCharge = nights * roomRate;
        recalculateTotals();
    }

    public void addServiceCharge(ServiceCharge serviceCharge) {
        if (serviceCharge != null) {
            this.serviceCharges.add(serviceCharge);
            recalculateTotals();
        }
    }

    public void recalculateTotals() {
        this.totalRoomCharge = nights * roomRate;
        double servicesTotal = 0.0;
        if (serviceCharges != null) {
            for (ServiceCharge sc : serviceCharges) {
                servicesTotal += sc.getAmount();
            }
        }
        this.subtotal = totalRoomCharge + servicesTotal;
        this.taxAmount = subtotal * taxRate;
        this.grandTotal = subtotal + taxAmount;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
        recalculateTotals();
    }

    public double getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
        recalculateTotals();
    }

    public double getTotalRoomCharge() {
        return totalRoomCharge;
    }

    public List<ServiceCharge> getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(List<ServiceCharge> serviceCharges) {
        this.serviceCharges = serviceCharges;
        recalculateTotals();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
        recalculateTotals();
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(billId, bill.billId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId);
    }

    @Override
    public String toString() {
        return "Invoice " + billId + " [Reservation " + reservationId + ", Total: Rs. " + String.format("%.2f", grandTotal) + "]";
    }
}
