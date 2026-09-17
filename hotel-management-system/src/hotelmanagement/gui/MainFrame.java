package hotelmanagement.gui;

import hotelmanagement.manager.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final ReservationManager reservationManager;
    private final BillingManager billingManager;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private DashboardPanel dashboardPanel;
    private GuestPanel guestPanel;
    private RoomReservationPanel roomReservationPanel;
    private BillingPanel billingPanel;
    private ReportPanel reportPanel;

    private JButton btnNavDashboard;
    private JButton btnNavGuest;
    private JButton btnNavRoomRes;
    private JButton btnNavBilling;
    private JButton btnNavExit;

    public MainFrame(RoomManager roomManager, GuestManager guestManager,
                     ReservationManager reservationManager, BillingManager billingManager) {
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        this.reservationManager = reservationManager;
        this.billingManager = billingManager;

        setTitle("HOTEL MANAGEMENT SYSTEM - VITyarthi Project");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1280, 768);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExit();
            }
        });

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        initHeader();
        initSidebar();
        initContentCards();
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_DARK);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("GRAND HORIZON HOTEL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Management & Reservation Operations System");
        sub.setFont(UITheme.FONT_REGULAR);
        sub.setForeground(new Color(189, 195, 199));

        header.add(title, BorderLayout.WEST);
        header.add(sub, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new MatteBorder(1, 0, 0, 1, new Color(44, 62, 80)));

        JLabel lblMenu = new JLabel("NAVIGATION MENU");
        lblMenu.setFont(UITheme.FONT_SMALL);
        lblMenu.setForeground(new Color(149, 165, 166));
        lblMenu.setBorder(new EmptyBorder(15, 20, 10, 0));
        sidebar.add(lblMenu);

        btnNavDashboard = createNavButton("Dashboard Panel");
        btnNavGuest = createNavButton("Guest Management");
        btnNavRoomRes = createNavButton("Room & Reservations");
        btnNavBilling = createNavButton("Billing & Reporting");
        btnNavExit = createNavButton("Exit Application");
        btnNavExit.setForeground(UITheme.COLOR_DANGER);

        sidebar.add(btnNavDashboard);
        sidebar.add(btnNavGuest);
        sidebar.add(btnNavRoomRes);
        sidebar.add(btnNavBilling);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnNavExit);
        sidebar.add(Box.createVerticalStrut(20));

        btnNavDashboard.addActionListener(e -> {
            dashboardPanel.refreshMetrics();
            cardLayout.show(cardPanel, "DASHBOARD");
            setActiveNavButton(btnNavDashboard);
        });

        btnNavGuest.addActionListener(e -> {
            cardLayout.show(cardPanel, "GUEST");
            setActiveNavButton(btnNavGuest);
        });

        btnNavRoomRes.addActionListener(e -> {
            roomReservationPanel.refreshReservationDropdowns();
            cardLayout.show(cardPanel, "ROOM_RES");
            setActiveNavButton(btnNavRoomRes);
        });

        btnNavBilling.addActionListener(e -> {
            billingPanel.refreshBillTable();
            reportPanel.refreshReport();
            cardLayout.show(cardPanel, "BILLING");
            setActiveNavButton(btnNavBilling);
        });

        btnNavExit.addActionListener(e -> confirmExit());

        add(sidebar, BorderLayout.WEST);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(UITheme.PRIMARY_DARK);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(44, 62, 80)),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setActiveNavButton(JButton active) {
        JButton[] buttons = {btnNavDashboard, btnNavGuest, btnNavRoomRes, btnNavBilling};
        for (JButton b : buttons) {
            if (b == active) {
                b.setBackground(UITheme.PRIMARY_ACCENT);
            } else {
                b.setBackground(UITheme.PRIMARY_DARK);
            }
        }
    }

    private void initContentCards() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel(roomManager, guestManager, reservationManager, billingManager);
        guestPanel = new GuestPanel(guestManager);
        roomReservationPanel = new RoomReservationPanel(roomManager, guestManager, reservationManager, billingManager);
        
        JTabbedPane module3TabbedPane = new JTabbedPane();
        module3TabbedPane.setFont(UITheme.FONT_BOLD);
        billingPanel = new BillingPanel(billingManager);
        reportPanel = new ReportPanel(roomManager, guestManager, reservationManager, billingManager);
        module3TabbedPane.addTab("Billing & Invoicing (Module 3A)", billingPanel);
        module3TabbedPane.addTab("Hotel Reporting & Analytics (Module 3B)", reportPanel);

        cardPanel.add(dashboardPanel, "DASHBOARD");
        cardPanel.add(guestPanel, "GUEST");
        cardPanel.add(roomReservationPanel, "ROOM_RES");
        cardPanel.add(module3TabbedPane, "BILLING");

        add(cardPanel, BorderLayout.CENTER);
        setActiveNavButton(btnNavDashboard);
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the Hotel Management System?",
                "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
