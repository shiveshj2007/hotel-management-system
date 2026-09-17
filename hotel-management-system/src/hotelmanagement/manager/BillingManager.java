package hotelmanagement.manager;

import hotelmanagement.model.*;
import hotelmanagement.storage.FileManager;
import hotelmanagement.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillingManager {

    private final List<Bill> bills;
    private final FileManager fileManager;

    public BillingManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.bills = fileManager.loadBills();
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    public Bill getBillById(String billId) {
        if (billId == null) return null;
        return bills.stream()
                .filter(b -> b.getBillId().equalsIgnoreCase(billId.trim()))
                .findFirst()
                .orElse(null);
    }

    public Bill getBillByReservationId(String reservationId) {
        if (reservationId == null) return null;
        return bills.stream()
                .filter(b -> b.getReservationId().equalsIgnoreCase(reservationId.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Bill> searchBills(String query, PaymentStatus status) {
        return bills.stream()
                .filter(b -> status == null || b.getPaymentStatus() == status)
                .filter(b -> ValidationUtil.isNullOrEmpty(query) ||
                             b.getBillId().toLowerCase().contains(query.trim().toLowerCase()) ||
                             b.getReservationId().toLowerCase().contains(query.trim().toLowerCase()) ||
                             b.getGuestName().toLowerCase().contains(query.trim().toLowerCase()) ||
                             b.getRoomNumber().toLowerCase().contains(query.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    public String generateBillId() {
        int max = 1000;
        for (Bill b : bills) {
            if (b.getBillId().startsWith("INV-")) {
                try {
                    int num = Integer.parseInt(b.getBillId().substring(4));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return "INV-" + (max + 1);
    }

    public Bill createBillForCheckout(Reservation reservation, Guest guest, Room room) {
        if (reservation == null || guest == null || room == null) {
            throw new IllegalArgumentException("Reservation, Guest, and Room details are required to generate a bill.");
        }

        Bill existing = getBillByReservationId(reservation.getReservationId());
        if (existing != null) {
            return existing;
        }

        String billId = generateBillId();
        double roomRate = room.getPricePerNight();
        int nights = reservation.getTotalNights();

        Bill bill = new Bill(billId, reservation.getReservationId(), guest.getGuestId(), guest.getFullName(),
                room.getRoomNumber(), nights, roomRate, Bill.DEFAULT_TAX_RATE);

        bills.add(bill);
        fileManager.saveBills(bills);
        return bill;
    }

    public void addServiceCharge(String billId, String description, double cost) throws IllegalArgumentException {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new IllegalArgumentException("Invoice not found: " + billId);
        }
        if (ValidationUtil.isNullOrEmpty(description)) {
            throw new IllegalArgumentException("Service description cannot be empty.");
        }
        if (cost <= 0) {
            throw new IllegalArgumentException("Service cost must be greater than zero.");
        }

        ServiceCharge sc = new ServiceCharge(description.trim(), cost, LocalDate.now());
        bill.addServiceCharge(sc);
        fileManager.saveBills(bills);
    }

    public void recordPayment(String billId, String paymentMethod) throws IllegalArgumentException {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new IllegalArgumentException("Invoice not found: " + billId);
        }
        if (bill.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException("Bill " + billId + " is already marked as PAID.");
        }
        if (ValidationUtil.isNullOrEmpty(paymentMethod)) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        bill.setPaymentStatus(PaymentStatus.PAID);
        bill.setPaymentMethod(paymentMethod.trim());
        fileManager.saveBills(bills);
    }
}
