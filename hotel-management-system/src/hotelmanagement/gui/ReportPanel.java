package hotelmanagement.gui;

import hotelmanagement.manager.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ReportPanel extends JPanel {

    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final ReservationManager reservationManager;
    private final BillingManager billingManager;

    private JLabel lblTotalRev;
    private JLabel lblPendingRev;
    private JLabel lblOccupancy;
    private JLabel lblTotalBookings;

    private JTable tableMetrics;
    private DefaultTableModel modelMetrics;

    public ReportPanel(RoomManager roomManager, GuestManager guestManager,
                       ReservationManager reservationManager, BillingManager billingManager) {
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        this.reservationManager = reservationManager;
        this.billingManager = billingManager;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        initHeader();
        initTopSummaryCards();
        initDetailedReportTable();

        refreshReport();
    }

    private void initHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Hotel Analytics & Business Reports");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY_DARK);

        JButton btnRefresh = UITheme.createStyledButton("Refresh Real-Time Report", UITheme.PRIMARY_ACCENT, Color.WHITE);
        btnRefresh.addActionListener(e -> refreshReport());

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initTopSummaryCards() {
        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 15, 15));
        pnlCards.setOpaque(false);

        lblTotalRev = new JLabel("Rs. 0.00", SwingConstants.CENTER);
        lblPendingRev = new JLabel("Rs. 0.00", SwingConstants.CENTER);
        lblOccupancy = new JLabel("0.0%", SwingConstants.CENTER);
        lblTotalBookings = new JLabel("0", SwingConstants.CENTER);

        pnlCards.add(createCard("Total Revenue Collected", lblTotalRev, UITheme.COLOR_SUCCESS));
        pnlCards.add(createCard("Pending Receivables", lblPendingRev, UITheme.COLOR_DANGER));
        pnlCards.add(createCard("Hotel Occupancy Rate", lblOccupancy, UITheme.PRIMARY_ACCENT));
        pnlCards.add(createCard("Total Reservations", lblTotalBookings, UITheme.COLOR_INFO));

        add(pnlCards, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JLabel valLabel, Color barColor) {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JPanel bar = new JPanel();
        bar.setBackground(barColor);
        bar.setPreferredSize(new Dimension(0, 4));
        card.add(bar, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_SUBHEADER);
        lblTitle.setForeground(UITheme.TEXT_MUTED);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLabel.setForeground(UITheme.TEXT_DARK);

        card.add(lblTitle, BorderLayout.CENTER);
        card.add(valLabel, BorderLayout.SOUTH);

        return card;
    }

    private void initDetailedReportTable() {
        JPanel container = UITheme.createCardPanel();
        container.setLayout(new BorderLayout(10, 15));
        container.setPreferredSize(new Dimension(0, 320));

        JLabel title = new JLabel("Comprehensive Operational Metric Breakdown");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.PRIMARY_DARK);
        container.add(title, BorderLayout.NORTH);

        String[] cols = {"Metric Category", "Description / Parameter", "Value / Count"};
        modelMetrics = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableMetrics = new JTable(modelMetrics);
        UITheme.styleTable(tableMetrics);

        container.add(new JScrollPane(tableMetrics), BorderLayout.CENTER);

        add(container, BorderLayout.SOUTH);
    }

    public void refreshReport() {
        Map<String, Object> metrics = ReportGenerator.generateMetrics(roomManager, guestManager, reservationManager, billingManager);

        double totalRev = (Double) metrics.get("totalRevenue");
        double pendingRev = (Double) metrics.get("pendingRevenue");
        double occ = (Double) metrics.get("occupancyRate");

        lblTotalRev.setText("Rs. " + String.format("%.2f", totalRev));
        lblPendingRev.setText("Rs. " + String.format("%.2f", pendingRev));
        lblOccupancy.setText(String.format("%.1f", occ) + "%");
        lblTotalBookings.setText(String.valueOf(metrics.get("totalReservations")));

        modelMetrics.setRowCount(0);
        modelMetrics.addRow(new Object[]{"Room Inventory", "Total Rooms Configured", metrics.get("totalRooms")});
        modelMetrics.addRow(new Object[]{"Room Inventory", "Available Rooms", metrics.get("availableRooms")});
        modelMetrics.addRow(new Object[]{"Room Inventory", "Reserved Rooms", metrics.get("reservedRooms")});
        modelMetrics.addRow(new Object[]{"Room Inventory", "Occupied Rooms", metrics.get("occupiedRooms")});
        modelMetrics.addRow(new Object[]{"Room Inventory", "Maintenance Rooms", metrics.get("maintenanceRooms")});
        modelMetrics.addRow(new Object[]{"Guests", "Total Registered Guests", metrics.get("totalGuests")});
        modelMetrics.addRow(new Object[]{"Bookings", "Total Reservations", metrics.get("totalReservations")});
        modelMetrics.addRow(new Object[]{"Bookings", "Active Reservations (Confirmed/Checked-In)", metrics.get("activeReservations")});
        modelMetrics.addRow(new Object[]{"Bookings", "Completed Stay Reservations", metrics.get("completedReservations")});
        modelMetrics.addRow(new Object[]{"Bookings", "Cancelled Reservations", metrics.get("cancelledReservations")});
        modelMetrics.addRow(new Object[]{"Finance", "Total Revenue Collected (Paid Bills)", "Rs. " + String.format("%.2f", totalRev)});
        modelMetrics.addRow(new Object[]{"Finance", "Pending Invoices Balance", "Rs. " + String.format("%.2f", pendingRev)});
    }
}
