package hotelmanagement.manager;

import hotelmanagement.model.*;
import hotelmanagement.storage.FileManager;
import hotelmanagement.util.DateUtil;
import hotelmanagement.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationManager {

    private final List<Reservation> reservations;
    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final FileManager fileManager;

    public ReservationManager(FileManager fileManager, RoomManager roomManager, GuestManager guestManager) {
        this.fileManager = fileManager;
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        this.reservations = fileManager.loadReservations();
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    public Reservation getReservationById(String reservationId) {
        if (reservationId == null) return null;
        return reservations.stream()
                .filter(r -> r.getReservationId().equalsIgnoreCase(reservationId.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Reservation> searchReservations(String query, ReservationStatus filterStatus) {
        return reservations.stream()
                .filter(r -> filterStatus == null || r.getStatus() == filterStatus)
                .filter(r -> ValidationUtil.isNullOrEmpty(query) ||
                             r.getReservationId().toLowerCase().contains(query.trim().toLowerCase()) ||
                             r.getGuestId().toLowerCase().contains(query.trim().toLowerCase()) ||
                             r.getRoomNumber().toLowerCase().contains(query.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    public String generateReservationId() {
        int max = 1000;
        for (Reservation r : reservations) {
            if (r.getReservationId().startsWith("RES-")) {
                try {
                    int num = Integer.parseInt(r.getReservationId().substring(4));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return "RES-" + (max + 1);
    }

    public boolean isRoomAvailable(String roomNumber, LocalDate checkIn, LocalDate checkOut, String excludeReservationId) {
        Room room = roomManager.getRoomByNumber(roomNumber);
        if (room == null) return false;
        if (room.getStatus() == RoomStatus.MAINTENANCE) return false;

        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.CANCELLED || res.getStatus() == ReservationStatus.CHECKED_OUT) {
                continue;
            }
            if (excludeReservationId != null && res.getReservationId().equalsIgnoreCase(excludeReservationId)) {
                continue;
            }
            if (res.getRoomNumber().equalsIgnoreCase(roomNumber.trim())) {
                if (DateUtil.isOverlapping(checkIn, checkOut, res.getCheckInDate(), res.getCheckOutDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    public String createReservation(String guestId, String roomNumber, LocalDate checkIn, LocalDate checkOut) throws IllegalArgumentException {
        Guest guest = guestManager.getGuestById(guestId);
        if (guest == null) {
            throw new IllegalArgumentException("Guest with ID " + guestId + " does not exist.");
        }

        Room room = roomManager.getRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and Check-out dates are required.");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date (" + DateUtil.formatDate(checkOut) +
                    ") must be strictly after Check-in date (" + DateUtil.formatDate(checkIn) + ").");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past.");
        }

        if (!isRoomAvailable(roomNumber, checkIn, checkOut, null)) {
            throw new IllegalArgumentException("Room " + roomNumber + " is unavailable or double-booked for dates " +
                    DateUtil.formatDate(checkIn) + " to " + DateUtil.formatDate(checkOut));
        }

        int nights = DateUtil.calculateNights(checkIn, checkOut);
        double estCost = nights * room.getPricePerNight();
        String resId = generateReservationId();

        Reservation reservation = new Reservation(resId, guestId, roomNumber, checkIn, checkOut, ReservationStatus.CONFIRMED, nights, estCost);
        reservations.add(reservation);

        if (checkIn.equals(LocalDate.now()) && room.getStatus() == RoomStatus.AVAILABLE) {
            roomManager.updateRoomStatus(roomNumber, RoomStatus.RESERVED);
        } else if (room.getStatus() == RoomStatus.AVAILABLE) {
            roomManager.updateRoomStatus(roomNumber, RoomStatus.RESERVED);
        }

        fileManager.saveReservations(reservations);
        return resId;
    }

    public void checkIn(String reservationId) throws IllegalArgumentException {
        Reservation res = getReservationById(reservationId);
        if (res == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        if (res.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot check in a cancelled reservation.");
        }
        if (res.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new IllegalArgumentException("Reservation is already checked out.");
        }
        if (res.getStatus() == ReservationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Reservation is already checked in.");
        }

        res.setStatus(ReservationStatus.CHECKED_IN);
        roomManager.updateRoomStatus(res.getRoomNumber(), RoomStatus.OCCUPIED);
        fileManager.saveReservations(reservations);
    }

    public Bill checkOut(String reservationId, BillingManager billingManager) throws IllegalArgumentException {
        Reservation res = getReservationById(reservationId);
        if (res == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        if (res.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Only CHECKED_IN reservations can be checked out. Current status: " + res.getStatus().getDisplayName());
        }

        Guest guest = guestManager.getGuestById(res.getGuestId());
        Room room = roomManager.getRoomByNumber(res.getRoomNumber());

        Bill bill = billingManager.createBillForCheckout(res, guest, room);

        res.setStatus(ReservationStatus.CHECKED_OUT);
        roomManager.updateRoomStatus(res.getRoomNumber(), RoomStatus.AVAILABLE);

        fileManager.saveReservations(reservations);
        return bill;
    }

    public void cancelReservation(String reservationId) throws IllegalArgumentException {
        Reservation res = getReservationById(reservationId);
        if (res == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        if (res.getStatus() == ReservationStatus.CHECKED_IN || res.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new IllegalArgumentException("Cannot cancel a reservation that is already " + res.getStatus().getDisplayName());
        }
        if (res.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("Reservation is already cancelled.");
        }

        res.setStatus(ReservationStatus.CANCELLED);
        roomManager.updateRoomStatus(res.getRoomNumber(), RoomStatus.AVAILABLE);
        fileManager.saveReservations(reservations);
    }
}
