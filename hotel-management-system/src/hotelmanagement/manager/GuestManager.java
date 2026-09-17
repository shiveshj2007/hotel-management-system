package hotelmanagement.manager;

import hotelmanagement.model.Guest;
import hotelmanagement.storage.FileManager;
import hotelmanagement.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuestManager {

    private final List<Guest> guests;
    private final FileManager fileManager;

    public GuestManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.guests = fileManager.loadGuests();
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }

    public Guest getGuestById(String guestId) {
        if (guestId == null) return null;
        return guests.stream()
                .filter(g -> g.getGuestId().equalsIgnoreCase(guestId.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Guest> searchGuests(String query) {
        if (ValidationUtil.isNullOrEmpty(query)) {
            return getAllGuests();
        }
        String q = query.trim().toLowerCase();
        return guests.stream()
                .filter(g -> g.getGuestId().toLowerCase().contains(q) ||
                             g.getFullName().toLowerCase().contains(q) ||
                             g.getPhone().contains(q) ||
                             g.getEmail().toLowerCase().contains(q) ||
                             g.getIdDocument().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public String generateGuestId() {
        int max = 1000;
        for (Guest g : guests) {
            if (g.getGuestId().startsWith("GST-")) {
                try {
                    int num = Integer.parseInt(g.getGuestId().substring(4));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return "GST-" + (max + 1);
    }

    public String addGuest(Guest guest) throws IllegalArgumentException {
        if (guest == null) throw new IllegalArgumentException("Guest object cannot be null.");

        if (ValidationUtil.isNullOrEmpty(guest.getFullName())) {
            throw new IllegalArgumentException("Full Name is required.");
        }
        if (!ValidationUtil.isValidPhone(guest.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number (must be 10-12 digits).");
        }
        if (!ValidationUtil.isValidEmail(guest.getEmail())) {
            throw new IllegalArgumentException("Invalid email address format.");
        }
        if (ValidationUtil.isNullOrEmpty(guest.getIdDocument())) {
            throw new IllegalArgumentException("Identification document is required.");
        }

        for (Guest g : guests) {
            if (g.getPhone().equalsIgnoreCase(guest.getPhone().trim())) {
                throw new IllegalArgumentException("A guest with this phone number already exists.");
            }
            if (g.getEmail().equalsIgnoreCase(guest.getEmail().trim())) {
                throw new IllegalArgumentException("A guest with this email address already exists.");
            }
        }

        if (ValidationUtil.isNullOrEmpty(guest.getGuestId())) {
            guest.setGuestId(generateGuestId());
        } else if (getGuestById(guest.getGuestId()) != null) {
            throw new IllegalArgumentException("Guest ID " + guest.getGuestId() + " already exists.");
        }

        guests.add(guest);
        fileManager.saveGuests(guests);
        return guest.getGuestId();
    }

    public void updateGuest(Guest updatedGuest) throws IllegalArgumentException {
        if (updatedGuest == null || ValidationUtil.isNullOrEmpty(updatedGuest.getGuestId())) {
            throw new IllegalArgumentException("Invalid guest ID for update.");
        }

        Guest existing = getGuestById(updatedGuest.getGuestId());
        if (existing == null) {
            throw new IllegalArgumentException("Guest not found: " + updatedGuest.getGuestId());
        }

        if (ValidationUtil.isNullOrEmpty(updatedGuest.getFullName())) {
            throw new IllegalArgumentException("Full Name is required.");
        }
        if (!ValidationUtil.isValidPhone(updatedGuest.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
        if (!ValidationUtil.isValidEmail(updatedGuest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        for (Guest g : guests) {
            if (!g.getGuestId().equalsIgnoreCase(updatedGuest.getGuestId())) {
                if (g.getPhone().equalsIgnoreCase(updatedGuest.getPhone().trim())) {
                    throw new IllegalArgumentException("Another guest with this phone number already exists.");
                }
                if (g.getEmail().equalsIgnoreCase(updatedGuest.getEmail().trim())) {
                    throw new IllegalArgumentException("Another guest with this email address already exists.");
                }
            }
        }

        existing.setFullName(updatedGuest.getFullName().trim());
        existing.setPhone(updatedGuest.getPhone().trim());
        existing.setEmail(updatedGuest.getEmail().trim());
        existing.setAddress(updatedGuest.getAddress() != null ? updatedGuest.getAddress().trim() : "");
        existing.setIdDocument(updatedGuest.getIdDocument() != null ? updatedGuest.getIdDocument().trim() : "");

        fileManager.saveGuests(guests);
    }

    public boolean deleteGuest(String guestId) throws IllegalArgumentException {
        Guest existing = getGuestById(guestId);
        if (existing == null) {
            throw new IllegalArgumentException("Guest not found: " + guestId);
        }
        boolean removed = guests.remove(existing);
        if (removed) {
            fileManager.saveGuests(guests);
        }
        return removed;
    }
}
