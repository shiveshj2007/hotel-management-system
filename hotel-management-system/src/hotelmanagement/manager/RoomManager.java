package hotelmanagement.manager;

import hotelmanagement.model.Room;
import hotelmanagement.model.RoomStatus;
import hotelmanagement.model.RoomType;
import hotelmanagement.storage.FileManager;
import hotelmanagement.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomManager {

    private final List<Room> rooms;
    private final FileManager fileManager;

    public RoomManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.rooms = fileManager.loadRooms();
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomByNumber(String roomNumber) {
        if (roomNumber == null) return null;
        return rooms.stream()
                .filter(r -> r.getRoomNumber().equalsIgnoreCase(roomNumber.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Room> filterRooms(RoomType type, RoomStatus status, String searchQuery) {
        return rooms.stream()
                .filter(r -> type == null || r.getRoomType() == type)
                .filter(r -> status == null || r.getStatus() == status)
                .filter(r -> ValidationUtil.isNullOrEmpty(searchQuery) ||
                             r.getRoomNumber().toLowerCase().contains(searchQuery.trim().toLowerCase()) ||
                             r.getRoomType().getDisplayName().toLowerCase().contains(searchQuery.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void addRoom(Room room) throws IllegalArgumentException {
        if (room == null) throw new IllegalArgumentException("Room cannot be null.");
        if (ValidationUtil.isNullOrEmpty(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room Number is required.");
        }
        if (getRoomByNumber(room.getRoomNumber()) != null) {
            throw new IllegalArgumentException("Room " + room.getRoomNumber() + " already exists.");
        }
        if (room.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than zero.");
        }
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Room capacity must be greater than zero.");
        }

        rooms.add(room);
        fileManager.saveRooms(rooms);
    }

    public void updateRoom(Room updatedRoom) throws IllegalArgumentException {
        if (updatedRoom == null || ValidationUtil.isNullOrEmpty(updatedRoom.getRoomNumber())) {
            throw new IllegalArgumentException("Invalid room number for update.");
        }
        Room existing = getRoomByNumber(updatedRoom.getRoomNumber());
        if (existing == null) {
            throw new IllegalArgumentException("Room not found: " + updatedRoom.getRoomNumber());
        }

        if (updatedRoom.getPricePerNight() <= 0) {
            throw new IllegalArgumentException("Price per night must be positive.");
        }
        if (updatedRoom.getCapacity() <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive.");
        }

        existing.setRoomType(updatedRoom.getRoomType());
        existing.setPricePerNight(updatedRoom.getPricePerNight());
        existing.setCapacity(updatedRoom.getCapacity());
        existing.setStatus(updatedRoom.getStatus());

        fileManager.saveRooms(rooms);
    }

    public void updateRoomStatus(String roomNumber, RoomStatus newStatus) throws IllegalArgumentException {
        Room room = getRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }
        room.setStatus(newStatus);
        fileManager.saveRooms(rooms);
    }

    public boolean deleteRoom(String roomNumber) throws IllegalArgumentException {
        Room room = getRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomNumber);
        }
        if (room.getStatus() == RoomStatus.OCCUPIED || room.getStatus() == RoomStatus.RESERVED) {
            throw new IllegalArgumentException("Cannot delete room " + roomNumber + " because it is currently " + room.getStatus().getDisplayName());
        }
        boolean removed = rooms.remove(room);
        if (removed) {
            fileManager.saveRooms(rooms);
        }
        return removed;
    }
}
