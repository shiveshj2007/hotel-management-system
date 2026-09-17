package hotelmanagement.manager;

import hotelmanagement.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public static Map<String, Object> generateMetrics(RoomManager roomManager,
                                                      GuestManager guestManager,
                                                      ReservationManager reservationManager,
                                                      BillingManager billingManager) {
        Map<String, Object> stats = new HashMap<>();

        List<Room> rooms = roomManager.getAllRooms();
        List<Guest> guests = guestManager.getAllGuests();
        List<Reservation> reservations = reservationManager.getAllReservations();
        List<Bill> bills = billingManager.getAllBills();

        long totalRooms = rooms.size();
        long availableRooms = rooms.stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count();
        long reservedRooms = rooms.stream().filter(r -> r.getStatus() == RoomStatus.RESERVED).count();
        long occupiedRooms = rooms.stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count();
        long maintenanceRooms = rooms.stream().filter(r -> r.getStatus() == RoomStatus.MAINTENANCE).count();

        long totalGuests = guests.size();
        long totalReservations = reservations.size();
        long activeReservations = reservations.stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED || r.getStatus() == ReservationStatus.CHECKED_IN).count();
        long cancelledReservations = reservations.stream().filter(r -> r.getStatus() == ReservationStatus.CANCELLED).count();
        long completedReservations = reservations.stream().filter(r -> r.getStatus() == ReservationStatus.CHECKED_OUT).count();

        double totalRevenue = bills.stream()
                .filter(b -> b.getPaymentStatus() == PaymentStatus.PAID)
                .mapToDouble(Bill::getGrandTotal)
                .sum();

        double pendingRevenue = bills.stream()
                .filter(b -> b.getPaymentStatus() == PaymentStatus.PENDING)
                .mapToDouble(Bill::getGrandTotal)
                .sum();

        stats.put("totalRooms", totalRooms);
        stats.put("availableRooms", availableRooms);
        stats.put("reservedRooms", reservedRooms);
        stats.put("occupiedRooms", occupiedRooms);
        stats.put("maintenanceRooms", maintenanceRooms);

        stats.put("totalGuests", totalGuests);
        stats.put("totalReservations", totalReservations);
        stats.put("activeReservations", activeReservations);
        stats.put("cancelledReservations", cancelledReservations);
        stats.put("completedReservations", completedReservations);

        stats.put("totalRevenue", totalRevenue);
        stats.put("pendingRevenue", pendingRevenue);

        double occupancyRate = totalRooms > 0 ? ((double) (occupiedRooms + reservedRooms) / totalRooms) * 100.0 : 0.0;
        stats.put("occupancyRate", occupancyRate);

        return stats;
    }
}
