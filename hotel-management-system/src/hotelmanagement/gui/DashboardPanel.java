package hotelmanagement.gui;

import hotelmanagement.manager.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class DashboardPanel extends JPanel {

    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final ReservationManager reservationManager;
    private final BillingManager billingManager;

    private JLabel lblTotalRooms;
    private JLabel lblAvailableRooms;
    private JLabel lblOccupiedRooms;
    private JLabel lblTotalGuests;
    private JLabel lblTotalRevenue;

    public DashboardPanel(RoomManager roomManager, GuestManager guestManager,
                          ReservationManager reservationManager, BillingManager billingManager) {
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        this.reservationManager = reservationManager;
        this.billingManager = billingManager;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        initHeader();
        initMetricCards();
        initQuickInfoSection();

        refreshMetrics();
    }

    private void initHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Welcome to Hotel Management System");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.PRIMARY_DARK);

        JLabel subtitle = new JLabel("Overview of current hotel operations, occupancy, and revenue statistics.");
        subtitle.setFont(UITheme.FONT_REGULAR);
        subtitle.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initMetricCards() {
        JPanel cardsContainer = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsContainer.setOpaque(false);

        lblTotalRooms = new JLabel("0", SwingConstants.CENTER);
        lblAvailableRooms = new JLabel("0", SwingConstants.CENTER);
        lblOccupiedRooms = new JLabel("0", SwingConstants.CENTER);
        lblTotalGuests = new JLabel("0", SwingConstants.CENTER);
        lblTotalRevenue = new JLabel("Rs. 0.00", SwingConstants.CENTER);

        cardsContainer.add(createCard("Total Rooms", lblTotalRooms, UITheme.PRIMARY_ACCENT));
        cardsContainer.add(createCard("Available Rooms", lblAvailableRooms, UITheme.COLOR_SUCCESS));
        cardsContainer.add(createCard("Occupied Rooms", lblOccupiedRooms, UITheme.COLOR_DANGER));
        cardsContainer.add(createCard("Registered Guests", lblTotalGuests, UITheme.COLOR_INFO));
        cardsContainer.add(createCard("Total Revenue Collected", lblTotalRevenue, UITheme.COLOR_WARNING));
        cardsContainer.add(createQuickActionsCard());

        add(cardsContainer, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JPanel topBar = new JPanel();
        topBar.setBackground(accentColor);
        topBar.setPreferredSize(new Dimension(0, 4));
        card.add(topBar, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_SUBHEADER);
        lblTitle.setForeground(UITheme.TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(UITheme.TEXT_DARK);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQuickActionsCard() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Quick Controls", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_SUBHEADER);
        title.setForeground(UITheme.PRIMARY_DARK);

        JButton btnRefresh = UITheme.createStyledButton("Refresh Dashboard", UITheme.PRIMARY_ACCENT, Color.WHITE);
        btnRefresh.addActionListener(e -> refreshMetrics());

        card.add(title, BorderLayout.NORTH);
        card.add(btnRefresh, BorderLayout.CENTER);

        return card;
    }

    private void initQuickInfoSection() {
        JPanel infoPanel = UITheme.createCardPanel();
        infoPanel.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("VITyarthi Project Core Module Architecture");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.PRIMARY_DARK);

        JTextArea area = new JTextArea();
        area.setText("• Module 1: Guest Management — Manage guest profiles, input validation, document tracking & CRUD operations.\n" +
                     "• Module 2: Room & Reservation Management — Inventory control, date-overlap validation, room status machine & check-in/out.\n" +
                     "• Module 3: Billing & Reporting — Itemized invoicing, service charges, 12% GST tax calculation & real-time analytics.");
        area.setFont(UITheme.FONT_REGULAR);
        area.setEditable(false);
        area.setOpaque(false);

        infoPanel.add(title, BorderLayout.NORTH);
        infoPanel.add(area, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.SOUTH);
    }

    public void refreshMetrics() {
        Map<String, Object> metrics = ReportGenerator.generateMetrics(roomManager, guestManager, reservationManager, billingManager);

        lblTotalRooms.setText(String.valueOf(metrics.get("totalRooms")));
        lblAvailableRooms.setText(String.valueOf(metrics.get("availableRooms")));
        lblOccupiedRooms.setText(String.valueOf(metrics.get("occupiedRooms")));
        lblTotalGuests.setText(String.valueOf(metrics.get("totalGuests")));
        lblTotalRevenue.setText("Rs. " + String.format("%.2f", (Double) metrics.get("totalRevenue")));
    }
}
