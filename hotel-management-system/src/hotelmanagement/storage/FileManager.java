package hotelmanagement.storage;

import hotelmanagement.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String GUESTS_FILE = DATA_DIR + File.separator + "guests.dat";
    private static final String ROOMS_FILE = DATA_DIR + File.separator + "rooms.dat";
    private static final String RESERVATIONS_FILE = DATA_DIR + File.separator + "reservations.dat";
    private static final String BILLS_FILE = DATA_DIR + File.separator + "bills.dat";

    public FileManager() {
        ensureDataDirectoryExists();
    }

    private void ensureDataDirectoryExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Guest> loadGuests() {
        File file = new File(GUESTS_FILE);
        if (!file.exists()) {
            List<Guest> sampleGuests = generateSampleGuests();
            saveGuests(sampleGuests);
            return sampleGuests;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Guest>) ois.readObject();
        } catch (Exception e) {
            List<Guest> sampleGuests = generateSampleGuests();
            saveGuests(sampleGuests);
            return sampleGuests;
        }
    }

    public boolean saveGuests(List<Guest> guests) {
        ensureDataDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUESTS_FILE))) {
            oos.writeObject(guests);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Room> loadRooms() {
        File file = new File(ROOMS_FILE);
        if (!file.exists()) {
            List<Room> sampleRooms = generateSampleRooms();
            saveRooms(sampleRooms);
            return sampleRooms;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Room>) ois.readObject();
        } catch (Exception e) {
            List<Room> sampleRooms = generateSampleRooms();
            saveRooms(sampleRooms);
            return sampleRooms;
        }
    }

    public boolean saveRooms(List<Room> rooms) {
        ensureDataDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE))) {
            oos.writeObject(rooms);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Reservation> loadReservations() {
        File file = new File(RESERVATIONS_FILE);
        if (!file.exists()) {
            List<Reservation> sampleReservations = generateSampleReservations();
            saveReservations(sampleReservations);
            return sampleReservations;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Reservation>) ois.readObject();
        } catch (Exception e) {
            List<Reservation> sampleReservations = generateSampleReservations();
            saveReservations(sampleReservations);
            return sampleReservations;
        }
    }

    public boolean saveReservations(List<Reservation> reservations) {
        ensureDataDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_FILE))) {
            oos.writeObject(reservations);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Bill> loadBills() {
        File file = new File(BILLS_FILE);
        if (!file.exists()) {
            List<Bill> sampleBills = generateSampleBills();
            saveBills(sampleBills);
            return sampleBills;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Bill>) ois.readObject();
        } catch (Exception e) {
            List<Bill> sampleBills = generateSampleBills();
            saveBills(sampleBills);
            return sampleBills;
        }
    }

    public boolean saveBills(List<Bill> bills) {
        ensureDataDirectoryExists();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BILLS_FILE))) {
            oos.writeObject(bills);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<Guest> generateSampleGuests() {
        List<Guest> list = new ArrayList<>();
        list.add(new Guest("GST-1001", "Aarav Sharma", "9876543210", "aarav.sharma@example.com", "12 Park Street, Delhi", "AADHAAR-8901"));
        list.add(new Guest("GST-1002", "Priya Verma", "9812345678", "priya.v@example.com", "45 MG Road, Bengaluru", "PASSPORT-A98123"));
        list.add(new Guest("GST-1003", "Rohan Gupta", "9765432109", "rohan.gupta@example.com", "78 Anna Salai, Chennai", "DL-TN0120198"));
        list.add(new Guest("GST-1004", "Ananya Rao", "9654321098", "ananya.rao@example.com", "102 Banjara Hills, Hyderabad", "AADHAAR-4521"));
        return list;
    }

    public List<Room> generateSampleRooms() {
        List<Room> list = new ArrayList<>();
        list.add(new Room("101", RoomType.SINGLE, 1200.00, 1, RoomStatus.OCCUPIED));
        list.add(new Room("102", RoomType.SINGLE, 1200.00, 1, RoomStatus.AVAILABLE));
        list.add(new Room("201", RoomType.DOUBLE, 2200.00, 2, RoomStatus.RESERVED));
        list.add(new Room("202", RoomType.DOUBLE, 2200.00, 2, RoomStatus.AVAILABLE));
        list.add(new Room("301", RoomType.DELUXE, 3500.00, 3, RoomStatus.OCCUPIED));
        list.add(new Room("302", RoomType.DELUXE, 3500.00, 3, RoomStatus.AVAILABLE));
        list.add(new Room("401", RoomType.SUITE, 6000.00, 4, RoomStatus.AVAILABLE));
        list.add(new Room("402", RoomType.SUITE, 6000.00, 4, RoomStatus.MAINTENANCE));
        return list;
    }

    public List<Reservation> generateSampleReservations() {
        List<Reservation> list = new ArrayList<>();
        LocalDate today = LocalDate.now();

        Reservation r1 = new Reservation("RES-1001", "GST-1001", "101",
                today.minusDays(2), today.plusDays(1),
                ReservationStatus.CHECKED_IN, 3, 3600.00);
        list.add(r1);

        Reservation r2 = new Reservation("RES-1002", "GST-1002", "201",
                today.plusDays(1), today.plusDays(3),
                ReservationStatus.CONFIRMED, 2, 4400.00);
        list.add(r2);

        Reservation r3 = new Reservation("RES-1003", "GST-1003", "301",
                today.minusDays(1), today.plusDays(2),
                ReservationStatus.CHECKED_IN, 3, 10500.00);
        list.add(r3);

        Reservation r4 = new Reservation("RES-1004", "GST-1004", "102",
                today.minusDays(5), today.minusDays(2),
                ReservationStatus.CHECKED_OUT, 3, 3600.00);
        list.add(r4);

        return list;
    }

    public List<Bill> generateSampleBills() {
        List<Bill> list = new ArrayList<>();
        LocalDate today = LocalDate.now();

        Bill b1 = new Bill("INV-1001", "RES-1004", "GST-1004", "Ananya Rao", "102", 3, 1200.00, 0.12);
        b1.addServiceCharge(new ServiceCharge("Breakfast Buffet", 450.00, today.minusDays(4)));
        b1.addServiceCharge(new ServiceCharge("Laundry Service", 300.00, today.minusDays(3)));
        b1.setPaymentStatus(PaymentStatus.PAID);
        b1.setPaymentMethod("Credit Card");
        list.add(b1);

        return list;
    }
}
