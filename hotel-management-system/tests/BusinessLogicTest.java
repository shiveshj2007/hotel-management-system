package tests;

import hotelmanagement.manager.*;
import hotelmanagement.model.*;
import hotelmanagement.storage.FileManager;
import hotelmanagement.util.DateUtil;
import hotelmanagement.util.ValidationUtil;

import java.time.LocalDate;
import java.util.Map;

public class BusinessLogicTest {

    private FileManager fileManager;
    private GuestManager guestManager;
    private RoomManager roomManager;
    private ReservationManager reservationManager;
    private BillingManager billingManager;

    public void setUp() {
        fileManager = new FileManager();
        guestManager = new GuestManager(fileManager);
        roomManager = new RoomManager(fileManager);
        reservationManager = new ReservationManager(fileManager, roomManager, guestManager);
        billingManager = new BillingManager(fileManager);
    }

    public void testValidationUtil() {
        assertTrue(ValidationUtil.isValidEmail("test.user@vityarthi.ac.in"), "Valid email check failed.");
        assertFalse(ValidationUtil.isValidEmail("invalid-email"), "Invalid email accepted error.");

        assertTrue(ValidationUtil.isValidPhone("9876543210"), "Valid phone check failed.");
        assertFalse(ValidationUtil.isValidPhone("123"), "Invalid short phone accepted error.");

        assertTrue(ValidationUtil.isValidPositiveDouble("1500.50"), "Valid positive double failed.");
        assertFalse(ValidationUtil.isValidPositiveDouble("-50.0"), "Negative double accepted error.");
    }

    public void testGuestCreationAndDuplicateCheck() {
        String phone = "9998887776";
        String email = "unit.test@vityarthi.com";

        Guest g = new Guest(null, "Test Student", phone, email, "VIT Campus", "ID-9999");
        String id = guestManager.addGuest(g);
        assertNotNull(id, "Generated Guest ID should not be null.");

        try {
            Guest duplicate = new Guest(null, "Another Name", phone, "other@test.com", "Address", "ID-111");
            guestManager.addGuest(duplicate);
            fail("Adding duplicate phone should throw IllegalArgumentException.");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testRoomManagement() {
        String roomNum = "TEST-101";
        Room room = new Room(roomNum, RoomType.DELUXE, 3500.00, 3, RoomStatus.AVAILABLE);
        roomManager.addRoom(room);

        Room fetched = roomManager.getRoomByNumber(roomNum);
        assertNotNull(fetched, "Room fetched should not be null.");
        assertEquals(RoomType.DELUXE, fetched.getRoomType(), "Room type mismatch.");
        assertEquals(3500.00, fetched.getPricePerNight(), "Room price mismatch.");
    }

    public void testReservationDateOverlapAndDoubleBooking() {
        String roomNum = "102";
        Guest guest = guestManager.getAllGuests().get(0);

        LocalDate checkIn1 = LocalDate.now().plusDays(10);
        LocalDate checkOut1 = LocalDate.now().plusDays(15);

        String res1 = reservationManager.createReservation(guest.getGuestId(), roomNum, checkIn1, checkOut1);
        assertNotNull(res1, "First reservation should succeed.");

        LocalDate checkInOverlapping = LocalDate.now().plusDays(12);
        LocalDate checkOutOverlapping = LocalDate.now().plusDays(18);

        try {
            reservationManager.createReservation(guest.getGuestId(), roomNum, checkInOverlapping, checkOutOverlapping);
            fail("Overlapping reservation should have been blocked!");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testCheckInCheckOutStateTransitions() {
        Guest guest = guestManager.getAllGuests().get(0);

        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);

        String resId = reservationManager.createReservation(guest.getGuestId(), "202", checkIn, checkOut);
        Reservation res = reservationManager.getReservationById(resId);
        assertEquals(ReservationStatus.CONFIRMED, res.getStatus(), "Initial status should be CONFIRMED.");

        reservationManager.checkIn(resId);
        assertEquals(ReservationStatus.CHECKED_IN, res.getStatus(), "Status after check-in should be CHECKED_IN.");
        assertEquals(RoomStatus.OCCUPIED, roomManager.getRoomByNumber("202").getStatus(), "Room status should be OCCUPIED.");

        Bill bill = reservationManager.checkOut(resId, billingManager);
        assertNotNull(bill, "Bill generated during check-out should not be null.");
        assertEquals(ReservationStatus.CHECKED_OUT, res.getStatus(), "Status after check-out should be CHECKED_OUT.");
        assertEquals(RoomStatus.AVAILABLE, roomManager.getRoomByNumber("202").getStatus(), "Room status should revert to AVAILABLE.");
    }

    public void testBillAndTaxCalculations() {
        Bill bill = new Bill("INV-TEST", "RES-TEST", "GST-TEST", "Test Guest", "101", 3, 1000.00, 0.12);

        assertEquals(3000.00, bill.getTotalRoomCharge(), "Room charge should be 3 * 1000 = 3000.");
        assertEquals(3000.00, bill.getSubtotal(), "Subtotal without services should be 3000.");

        bill.addServiceCharge(new ServiceCharge("Breakfast", 500.00, LocalDate.now()));
        assertEquals(3500.00, bill.getSubtotal(), "Subtotal with service should be 3500.");

        assertEquals(420.00, bill.getTaxAmount(), "Tax 12% of 3500 should be 420.00.");

        assertEquals(3920.00, bill.getGrandTotal(), "Grand total should be 3920.00.");
    }

    public void testReportMetrics() {
        Map<String, Object> stats = ReportGenerator.generateMetrics(roomManager, guestManager, reservationManager, billingManager);
        assertTrue((Long) stats.get("totalRooms") > 0, "Total rooms count should be positive.");
        assertTrue((Long) stats.get("totalGuests") > 0, "Total guests count should be positive.");
        assertNotNull(stats.get("occupancyRate"), "Occupancy rate should not be null.");
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError("FAILED: " + message);
    }

    private void assertFalse(boolean condition, String message) {
        if (condition) throw new AssertionError("FAILED: " + message);
    }

    private void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("FAILED: " + message + " Expected: " + expected + ", Actual: " + actual);
    }

    private void assertEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.001) {
            throw new AssertionError("FAILED: " + message + " Expected: " + expected + ", Actual: " + actual);
        }
    }

    private void assertNotNull(Object obj, String message) {
        if (obj == null) throw new AssertionError("FAILED: " + message);
    }

    private void fail(String message) {
        throw new AssertionError("FAILED: " + message);
    }
}
