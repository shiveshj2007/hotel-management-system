package hotelmanagement.gui;

import hotelmanagement.manager.*;
import hotelmanagement.model.*;
import hotelmanagement.util.DateUtil;
import hotelmanagement.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RoomReservationPanel extends JPanel {

    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final ReservationManager reservationManager;
    private final BillingManager billingManager;

    private JTabbedPane tabbedPane;

    private JTextField txtRoomNumber;
    private JComboBox<RoomType> cbRoomType;
    private JTextField txtPricePerNight;
    private JTextField txtCapacity;
    private JComboBox<RoomStatus> cbRoomStatus;

    private JComboBox<RoomType> cbFilterType;
    private JComboBox<RoomStatus> cbFilterStatus;
    private JTable tableRooms;
    private DefaultTableModel modelRooms;

    private JComboBox<String> cbResGuest;
    private JComboBox<String> cbResRoom;
    private JTextField txtCheckInDate;
    private JTextField txtCheckOutDate;
    private JLabel lblCalculatedNights;
    private JLabel lblCalculatedEstCost;

    private JTable tableReservations;
    private DefaultTableModel modelReservations;
    private JComboBox<ReservationStatus> cbFilterResStatus;

    public RoomReservationPanel(RoomManager roomManager, GuestManager guestManager,
                                ReservationManager reservationManager, BillingManager billingManager) {
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        this.reservationManager = reservationManager;
        this.billingManager = billingManager;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BOLD);

        tabbedPane.addTab("Room Management", initRoomManagementTab());
        tabbedPane.addTab("Reservation Management", initReservationTab());

        add(tabbedPane, BorderLayout.CENTER);

        refreshRoomTable();
        refreshReservationTable();
    }

    private JPanel initRoomManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 15));
        formCard.setPreferredSize(new Dimension(340, 0));

        JLabel title = new JLabel("Room Details");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.PRIMARY_DARK);
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRoomNumber = UITheme.createStyledTextField(15);
        cbRoomType = UITheme.createStyledComboBox(RoomType.values());
        txtPricePerNight = UITheme.createStyledTextField(15);
        txtCapacity = UITheme.createStyledTextField(15);
        cbRoomStatus = UITheme.createStyledComboBox(RoomStatus.values());

        cbRoomType.addActionListener(e -> {
            RoomType sel = (RoomType) cbRoomType.getSelectedItem();
            if (sel != null && txtPricePerNight.getText().isEmpty()) {
                txtPricePerNight.setText(String.valueOf(sel.getBasePricePerNight()));
                txtCapacity.setText(String.valueOf(sel.getDefaultCapacity()));
            }
        });

        addFormField(fields, gbc, 0, "Room Number *:", txtRoomNumber);
        addFormField(fields, gbc, 1, "Room Type *:", cbRoomType);
        addFormField(fields, gbc, 2, "Price/Night (Rs) *:", txtPricePerNight);
        addFormField(fields, gbc, 3, "Capacity (Guests) *:", txtCapacity);
        addFormField(fields, gbc, 4, "Status *:", cbRoomStatus);

        formCard.add(fields, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);

        JButton btnAddRoom = UITheme.createStyledButton("Add Room", UITheme.COLOR_SUCCESS, Color.WHITE);
        JButton btnUpdateRoom = UITheme.createStyledButton("Update Room", UITheme.PRIMARY_ACCENT, Color.WHITE);
        JButton btnDeleteRoom = UITheme.createStyledButton("Delete Room", UITheme.COLOR_DANGER, Color.WHITE);
        JButton btnClearRoom = UITheme.createStyledButton("Clear", UITheme.TEXT_MUTED, Color.WHITE);

        btnPanel.add(btnAddRoom);
        btnPanel.add(btnUpdateRoom);
        btnPanel.add(btnDeleteRoom);
        btnPanel.add(btnClearRoom);

        formCard.add(btnPanel, BorderLayout.SOUTH);

        btnAddRoom.addActionListener(e -> handleAddRoom());
        btnUpdateRoom.addActionListener(e -> handleUpdateRoom());
        btnDeleteRoom.addActionListener(e -> handleDeleteRoom());
        btnClearRoom.addActionListener(e -> clearRoomForm());

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 15));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterBar.setOpaque(false);

        cbFilterType = new JComboBox<>();
        cbFilterType.addItem(null);
        for (RoomType rt : RoomType.values()) cbFilterType.addItem(rt);

        cbFilterStatus = new JComboBox<>();
        cbFilterStatus.addItem(null);
        for (RoomStatus rs : RoomStatus.values()) cbFilterStatus.addItem(rs);

        JButton btnFilter = UITheme.createStyledButton("Filter", UITheme.PRIMARY_ACCENT, Color.WHITE);

        filterBar.add(new JLabel("Type:"));
        filterBar.add(cbFilterType);
        filterBar.add(new JLabel("Status:"));
        filterBar.add(cbFilterStatus);
        filterBar.add(btnFilter);

        btnFilter.addActionListener(e -> {
            RoomType ft = (RoomType) cbFilterType.getSelectedItem();
            RoomStatus fs = (RoomStatus) cbFilterStatus.getSelectedItem();
            populateRoomTable(roomManager.filterRooms(ft, fs, null));
        });

        tableCard.add(filterBar, BorderLayout.NORTH);

        String[] cols = {"Room No", "Room Type", "Price/Night", "Capacity", "Status"};
        modelRooms = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableRooms = new JTable(modelRooms);
        UITheme.styleTable(tableRooms);

        tableRooms.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableRooms.getSelectedRow() != -1) {
                int r = tableRooms.getSelectedRow();
                txtRoomNumber.setText((String) modelRooms.getValueAt(r, 0));
                String typeStr = (String) modelRooms.getValueAt(r, 1);
                for (RoomType rt : RoomType.values()) {
                    if (rt.getDisplayName().equalsIgnoreCase(typeStr)) {
                        cbRoomType.setSelectedItem(rt);
                        break;
                    }
                }
                txtPricePerNight.setText(String.valueOf(modelRooms.getValueAt(r, 2)).replace("Rs. ", ""));
                txtCapacity.setText(String.valueOf(modelRooms.getValueAt(r, 3)));
                String statusStr = (String) modelRooms.getValueAt(r, 4);
                for (RoomStatus rs : RoomStatus.values()) {
                    if (rs.getDisplayName().equalsIgnoreCase(statusStr)) {
                        cbRoomStatus.setSelectedItem(rs);
                        break;
                    }
                }
            }
        });

        tableCard.add(new JScrollPane(tableRooms), BorderLayout.CENTER);

        panel.add(formCard, BorderLayout.WEST);
        panel.add(tableCard, BorderLayout.CENTER);

        return panel;
    }

    private void handleAddRoom() {
        try {
            String num = txtRoomNumber.getText().trim();
            RoomType type = (RoomType) cbRoomType.getSelectedItem();
            double price = Double.parseDouble(txtPricePerNight.getText().trim());
            int cap = Integer.parseInt(txtCapacity.getText().trim());
            RoomStatus status = (RoomStatus) cbRoomStatus.getSelectedItem();

            Room room = new Room(num, type, price, cap, status);
            roomManager.addRoom(room);
            JOptionPane.showMessageDialog(this, "Room " + num + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            refreshRoomTable();
            clearRoomForm();
            refreshReservationDropdowns();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Room Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateRoom() {
        try {
            String num = txtRoomNumber.getText().trim();
            RoomType type = (RoomType) cbRoomType.getSelectedItem();
            double price = Double.parseDouble(txtPricePerNight.getText().trim());
            int cap = Integer.parseInt(txtCapacity.getText().trim());
            RoomStatus status = (RoomStatus) cbRoomStatus.getSelectedItem();

            Room room = new Room(num, type, price, cap, status);
            roomManager.updateRoom(room);
            JOptionPane.showMessageDialog(this, "Room " + num + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            refreshRoomTable();
            clearRoomForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteRoom() {
        String num = txtRoomNumber.getText().trim();
        if (ValidationUtil.isNullOrEmpty(num)) {
            JOptionPane.showMessageDialog(this, "Select a room to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete Room " + num + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                roomManager.deleteRoom(num);
                JOptionPane.showMessageDialog(this, "Room deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshRoomTable();
                clearRoomForm();
                refreshReservationDropdowns();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshRoomTable() {
        populateRoomTable(roomManager.getAllRooms());
    }

    private void populateRoomTable(List<Room> list) {
        modelRooms.setRowCount(0);
        for (Room r : list) {
            modelRooms.addRow(new Object[]{
                    r.getRoomNumber(),
                    r.getRoomType().getDisplayName(),
                    "Rs. " + String.format("%.2f", r.getPricePerNight()),
                    r.getCapacity(),
                    r.getStatus().getDisplayName()
            });
        }
    }

    private void clearRoomForm() {
        txtRoomNumber.setText("");
        cbRoomType.setSelectedIndex(0);
        txtPricePerNight.setText("");
        txtCapacity.setText("");
        cbRoomStatus.setSelectedIndex(0);
        tableRooms.clearSelection();
    }

    private JPanel initReservationTab() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 15));
        formCard.setPreferredSize(new Dimension(360, 0));

        JLabel title = new JLabel("Create Reservation");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.PRIMARY_DARK);
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbResGuest = new JComboBox<>();
        cbResRoom = new JComboBox<>();
        txtCheckInDate = UITheme.createStyledTextField(12);
        txtCheckOutDate = UITheme.createStyledTextField(12);

        LocalDate today = LocalDate.now();
        txtCheckInDate.setText(DateUtil.formatDate(today));
        txtCheckOutDate.setText(DateUtil.formatDate(today.plusDays(2)));

        lblCalculatedNights = new JLabel("2 Nights");
        lblCalculatedNights.setFont(UITheme.FONT_BOLD);

        lblCalculatedEstCost = new JLabel("Rs. 0.00");
        lblCalculatedEstCost.setFont(UITheme.FONT_BOLD);
        lblCalculatedEstCost.setForeground(UITheme.COLOR_SUCCESS);

        refreshReservationDropdowns();

        addFormField(fields, gbc, 0, "Select Guest *:", cbResGuest);
        addFormField(fields, gbc, 1, "Select Room *:", cbResRoom);
        addFormField(fields, gbc, 2, "Check-In (YYYY-MM-DD) *:", txtCheckInDate);
        addFormField(fields, gbc, 3, "Check-Out (YYYY-MM-DD) *:", txtCheckOutDate);
        addFormField(fields, gbc, 4, "Stay Duration:", lblCalculatedNights);
        addFormField(fields, gbc, 5, "Est. Amount:", lblCalculatedEstCost);

        formCard.add(fields, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        btnPanel.setOpaque(false);

        JButton btnCheckAvailability = UITheme.createStyledButton("Check Availability & Calculate", UITheme.PRIMARY_ACCENT, Color.WHITE);
        JButton btnBook = UITheme.createStyledButton("Book Reservation", UITheme.COLOR_SUCCESS, Color.WHITE);

        btnPanel.add(btnCheckAvailability);
        btnPanel.add(btnBook);

        formCard.add(btnPanel, BorderLayout.SOUTH);

        btnCheckAvailability.addActionListener(e -> calculateAndCheckAvailability());
        btnBook.addActionListener(e -> handleBookReservation());

        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 15));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel filterPnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPnl.setOpaque(false);
        filterPnl.add(new JLabel("Status Filter:"));
        cbFilterResStatus = new JComboBox<>();
        cbFilterResStatus.addItem(null);
        for (ReservationStatus rs : ReservationStatus.values()) cbFilterResStatus.addItem(rs);
        filterPnl.add(cbFilterResStatus);
        JButton btnFilterRes = UITheme.createStyledButton("Filter", UITheme.PRIMARY_ACCENT, Color.WHITE);
        filterPnl.add(btnFilterRes);
        topBar.add(filterPnl, BorderLayout.WEST);

        btnFilterRes.addActionListener(e -> {
            ReservationStatus st = (ReservationStatus) cbFilterResStatus.getSelectedItem();
            populateReservationTable(reservationManager.searchReservations(null, st));
        });

        tableCard.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Res ID", "Guest ID", "Room No", "Check-In", "Check-Out", "Nights", "Est. Amount", "Status"};
        modelReservations = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableReservations = new JTable(modelReservations);
        UITheme.styleTable(tableReservations);

        tableCard.add(new JScrollPane(tableReservations), BorderLayout.CENTER);

        JPanel actionToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionToolbar.setOpaque(false);

        JButton btnCheckIn = UITheme.createStyledButton("Check-In Guest", UITheme.COLOR_SUCCESS, Color.WHITE);
        JButton btnCheckOut = UITheme.createStyledButton("Check-Out & Bill", UITheme.COLOR_WARNING, Color.WHITE);
        JButton btnCancel = UITheme.createStyledButton("Cancel Reservation", UITheme.COLOR_DANGER, Color.WHITE);

        actionToolbar.add(btnCheckIn);
        actionToolbar.add(btnCheckOut);
        actionToolbar.add(btnCancel);

        tableCard.add(actionToolbar, BorderLayout.SOUTH);

        btnCheckIn.addActionListener(e -> handleCheckIn());
        btnCheckOut.addActionListener(e -> handleCheckOut());
        btnCancel.addActionListener(e -> handleCancelReservation());

        panel.add(formCard, BorderLayout.WEST);
        panel.add(tableCard, BorderLayout.CENTER);

        return panel;
    }

    public void refreshReservationDropdowns() {
        cbResGuest.removeAllItems();
        for (Guest g : guestManager.getAllGuests()) {
            cbResGuest.addItem(g.getGuestId() + " - " + g.getFullName());
        }

        cbResRoom.removeAllItems();
        for (Room r : roomManager.getAllRooms()) {
            cbResRoom.addItem(r.getRoomNumber() + " - " + r.getRoomType().getDisplayName() + " (Rs. " + r.getPricePerNight() + ")");
        }
    }

    private void calculateAndCheckAvailability() {
        try {
            String roomStr = (String) cbResRoom.getSelectedItem();
            if (roomStr == null) throw new IllegalArgumentException("No room selected.");
            String roomNum = roomStr.split(" - ")[0].trim();

            LocalDate inDate = DateUtil.parseDate(txtCheckInDate.getText());
            LocalDate outDate = DateUtil.parseDate(txtCheckOutDate.getText());

            if (!outDate.isAfter(inDate)) {
                throw new IllegalArgumentException("Check-Out date must be after Check-In date.");
            }

            boolean avail = reservationManager.isRoomAvailable(roomNum, inDate, outDate, null);
            int nights = DateUtil.calculateNights(inDate, outDate);
            Room room = roomManager.getRoomByNumber(roomNum);
            double cost = nights * room.getPricePerNight();

            lblCalculatedNights.setText(nights + " Nights");
            lblCalculatedEstCost.setText("Rs. " + String.format("%.2f", cost));

            if (avail) {
                JOptionPane.showMessageDialog(this, "Room " + roomNum + " is AVAILABLE for selected dates!", "Availability Check", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Room " + roomNum + " is NOT AVAILABLE for selected dates (Double Booking Warning).", "Availability Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBookReservation() {
        try {
            String guestStr = (String) cbResGuest.getSelectedItem();
            String roomStr = (String) cbResRoom.getSelectedItem();

            if (guestStr == null || roomStr == null) {
                throw new IllegalArgumentException("Guest and Room selection required.");
            }

            String guestId = guestStr.split(" - ")[0].trim();
            String roomNum = roomStr.split(" - ")[0].trim();

            LocalDate inDate = DateUtil.parseDate(txtCheckInDate.getText());
            LocalDate outDate = DateUtil.parseDate(txtCheckOutDate.getText());

            String resId = reservationManager.createReservation(guestId, roomNum, inDate, outDate);
            JOptionPane.showMessageDialog(this, "Reservation booked successfully! Reservation ID: " + resId, "Success", JOptionPane.INFORMATION_MESSAGE);

            refreshReservationTable();
            refreshRoomTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCheckIn() {
        int r = tableReservations.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation to Check-In.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String resId = (String) modelReservations.getValueAt(r, 0);

        try {
            reservationManager.checkIn(resId);
            JOptionPane.showMessageDialog(this, "Guest checked in! Room status updated to OCCUPIED.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshReservationTable();
            refreshRoomTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Check-In Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCheckOut() {
        int r = tableReservations.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a checked-in reservation to Check-Out.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String resId = (String) modelReservations.getValueAt(r, 0);

        try {
            Bill bill = reservationManager.checkOut(resId, billingManager);
            JOptionPane.showMessageDialog(this, "Check-Out completed! Invoice " + bill.getBillId() +
                            " generated for Rs. " + String.format("%.2f", bill.getGrandTotal()) +
                            ". Navigating to Billing tab for payment.",
                    "Check-Out Success", JOptionPane.INFORMATION_MESSAGE);

            refreshReservationTable();
            refreshRoomTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Check-Out Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancelReservation() {
        int r = tableReservations.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation to cancel.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String resId = (String) modelReservations.getValueAt(r, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel Reservation " + resId + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                reservationManager.cancelReservation(resId);
                JOptionPane.showMessageDialog(this, "Reservation cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshReservationTable();
                refreshRoomTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cancellation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshReservationTable() {
        populateReservationTable(reservationManager.getAllReservations());
    }

    private void populateReservationTable(List<Reservation> list) {
        modelReservations.setRowCount(0);
        for (Reservation r : list) {
            modelReservations.addRow(new Object[]{
                    r.getReservationId(),
                    r.getGuestId(),
                    r.getRoomNumber(),
                    DateUtil.formatDate(r.getCheckInDate()),
                    DateUtil.formatDate(r.getCheckOutDate()),
                    r.getTotalNights(),
                    "Rs. " + String.format("%.2f", r.getEstimatedAmount()),
                    r.getStatus().getDisplayName()
            });
        }
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.FONT_BOLD);
        label.setForeground(UITheme.TEXT_DARK);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }
}
