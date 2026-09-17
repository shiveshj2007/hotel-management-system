package hotelmanagement.gui;

import hotelmanagement.manager.BillingManager;
import hotelmanagement.model.Bill;
import hotelmanagement.model.PaymentStatus;
import hotelmanagement.model.ServiceCharge;
import hotelmanagement.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingPanel extends JPanel {

    private final BillingManager billingManager;

    private JTable tableBills;
    private DefaultTableModel modelBills;
    private JComboBox<PaymentStatus> cbFilterPayment;
    private JTextField txtSearchBill;

    private JLabel lblInvId;
    private JLabel lblGuestInfo;
    private JLabel lblRoomInfo;
    private JLabel lblNightsInfo;
    private JLabel lblRoomChargeTotal;
    private JLabel lblSubtotal;
    private JLabel lblTaxAmount;
    private JLabel lblGrandTotal;
    private JLabel lblPaymentStatus;

    private DefaultListModel<String> serviceListModel;
    private JList<String> listServices;

    private JButton btnAddService;
    private JButton btnRecordPayment;

    private Bill selectedBill;

    public BillingPanel(BillingManager billingManager) {
        this.billingManager = billingManager;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        initLeftTablePanel();
        initRightInvoicePanel();

        refreshBillTable();
    }

    private void initLeftTablePanel() {
        JPanel tableContainer = UITheme.createCardPanel();
        tableContainer.setLayout(new BorderLayout(10, 15));
        tableContainer.setPreferredSize(new Dimension(500, 0));

        JLabel title = new JLabel("Invoices & Billing History");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.PRIMARY_DARK);

        JPanel topBar = new JPanel(new BorderLayout(5, 5));
        topBar.setOpaque(false);
        topBar.add(title, BorderLayout.NORTH);

        JPanel searchPnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPnl.setOpaque(false);
        txtSearchBill = UITheme.createStyledTextField(10);
        cbFilterPayment = new JComboBox<>();
        cbFilterPayment.addItem(null);
        for (PaymentStatus ps : PaymentStatus.values()) cbFilterPayment.addItem(ps);

        JButton btnSearch = UITheme.createStyledButton("Search", UITheme.PRIMARY_ACCENT, Color.WHITE);
        searchPnl.add(new JLabel("Search:"));
        searchPnl.add(txtSearchBill);
        searchPnl.add(cbFilterPayment);
        searchPnl.add(btnSearch);

        topBar.add(searchPnl, BorderLayout.SOUTH);
        tableContainer.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Invoice ID", "Res ID", "Guest Name", "Total (Rs)", "Payment Status"};
        modelBills = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableBills = new JTable(modelBills);
        UITheme.styleTable(tableBills);

        tableBills.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableBills.getSelectedRow() != -1) {
                int r = tableBills.getSelectedRow();
                String invId = (String) modelBills.getValueAt(r, 0);
                selectedBill = billingManager.getBillById(invId);
                displayBillDetails(selectedBill);
            }
        });

        tableContainer.add(new JScrollPane(tableBills), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            String q = txtSearchBill.getText().trim();
            PaymentStatus ps = (PaymentStatus) cbFilterPayment.getSelectedItem();
            populateBillTable(billingManager.searchBills(q, ps));
        });

        add(tableContainer, BorderLayout.WEST);
    }

    private void initRightInvoicePanel() {
        JPanel invoiceCard = UITheme.createCardPanel();
        invoiceCard.setLayout(new BorderLayout(15, 15));

        JLabel header = new JLabel("Invoice Details & Payment Processing", SwingConstants.CENTER);
        header.setFont(UITheme.FONT_TITLE);
        header.setForeground(UITheme.PRIMARY_DARK);
        invoiceCard.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 15, 15));
        body.setOpaque(false);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblInvId = new JLabel("N/A");
        lblGuestInfo = new JLabel("N/A");
        lblRoomInfo = new JLabel("N/A");
        lblNightsInfo = new JLabel("N/A");
        lblRoomChargeTotal = new JLabel("Rs. 0.00");
        lblSubtotal = new JLabel("Rs. 0.00");
        lblTaxAmount = new JLabel("Rs. 0.00 (12% GST)");
        lblGrandTotal = new JLabel("Rs. 0.00");
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGrandTotal.setForeground(UITheme.PRIMARY_ACCENT);

        lblPaymentStatus = new JLabel("N/A");
        lblPaymentStatus.setFont(UITheme.FONT_BOLD);

        addDetailRow(detailsPanel, gbc, 0, "Invoice ID:", lblInvId);
        addDetailRow(detailsPanel, gbc, 1, "Guest:", lblGuestInfo);
        addDetailRow(detailsPanel, gbc, 2, "Room Number:", lblRoomInfo);
        addDetailRow(detailsPanel, gbc, 3, "Stay Duration:", lblNightsInfo);
        addDetailRow(detailsPanel, gbc, 4, "Room Charge Total:", lblRoomChargeTotal);
        addDetailRow(detailsPanel, gbc, 5, "Subtotal:", lblSubtotal);
        addDetailRow(detailsPanel, gbc, 6, "Tax Amount (12%):", lblTaxAmount);
        addDetailRow(detailsPanel, gbc, 7, "Grand Total:", lblGrandTotal);
        addDetailRow(detailsPanel, gbc, 8, "Payment Status:", lblPaymentStatus);

        body.add(detailsPanel);

        JPanel servicesPanel = new JPanel(new BorderLayout(10, 10));
        servicesPanel.setOpaque(false);

        JLabel lblSvcHeader = new JLabel("Itemized Additional Services", SwingConstants.LEFT);
        lblSvcHeader.setFont(UITheme.FONT_HEADER);
        servicesPanel.add(lblSvcHeader, BorderLayout.NORTH);

        serviceListModel = new DefaultListModel<>();
        listServices = new JList<>(serviceListModel);
        listServices.setFont(UITheme.FONT_REGULAR);
        servicesPanel.add(new JScrollPane(listServices), BorderLayout.CENTER);

        btnAddService = UITheme.createStyledButton("+ Add Additional Service Charge", UITheme.PRIMARY_ACCENT, Color.WHITE);
        servicesPanel.add(btnAddService, BorderLayout.SOUTH);

        body.add(servicesPanel);

        invoiceCard.add(body, BorderLayout.CENTER);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionBar.setOpaque(false);

        btnRecordPayment = UITheme.createStyledButton("Record Payment & Settle Invoice", UITheme.COLOR_SUCCESS, Color.WHITE);
        btnRecordPayment.setFont(new Font("Segoe UI", Font.BOLD, 14));

        actionBar.add(btnRecordPayment);
        invoiceCard.add(actionBar, BorderLayout.SOUTH);

        btnAddService.addActionListener(e -> handleAddServiceCharge());
        btnRecordPayment.addActionListener(e -> handleRecordPayment());

        add(invoiceCard, BorderLayout.CENTER);
    }

    private void addDetailRow(JPanel p, GridBagConstraints gbc, int row, String label, JLabel val) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.4;
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_BOLD);
        p.add(l, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        val.setFont(UITheme.FONT_REGULAR);
        p.add(val, gbc);
    }

    private void displayBillDetails(Bill b) {
        if (b == null) {
            lblInvId.setText("N/A");
            lblGuestInfo.setText("N/A");
            lblRoomInfo.setText("N/A");
            lblNightsInfo.setText("N/A");
            lblRoomChargeTotal.setText("Rs. 0.00");
            lblSubtotal.setText("Rs. 0.00");
            lblTaxAmount.setText("Rs. 0.00 (12% GST)");
            lblGrandTotal.setText("Rs. 0.00");
            lblPaymentStatus.setText("N/A");
            serviceListModel.clear();
            return;
        }

        lblInvId.setText(b.getBillId());
        lblGuestInfo.setText(b.getGuestName() + " (" + b.getGuestId() + ")");
        lblRoomInfo.setText("Room " + b.getRoomNumber() + " @ Rs. " + b.getRoomRate() + "/night");
        lblNightsInfo.setText(b.getNights() + " Nights");
        lblRoomChargeTotal.setText("Rs. " + String.format("%.2f", b.getTotalRoomCharge()));
        lblSubtotal.setText("Rs. " + String.format("%.2f", b.getSubtotal()));
        lblTaxAmount.setText("Rs. " + String.format("%.2f", b.getTaxAmount()) + " (12% GST)");
        lblGrandTotal.setText("Rs. " + String.format("%.2f", b.getGrandTotal()));

        lblPaymentStatus.setText(b.getPaymentStatus().getDisplayName() + (b.getPaymentMethod() != null ? " (" + b.getPaymentMethod() + ")" : ""));
        if (b.getPaymentStatus() == PaymentStatus.PAID) {
            lblPaymentStatus.setForeground(UITheme.COLOR_SUCCESS);
            btnRecordPayment.setEnabled(false);
            btnAddService.setEnabled(false);
        } else {
            lblPaymentStatus.setForeground(UITheme.COLOR_DANGER);
            btnRecordPayment.setEnabled(true);
            btnAddService.setEnabled(true);
        }

        serviceListModel.clear();
        for (ServiceCharge sc : b.getServiceCharges()) {
            serviceListModel.addElement(sc.toString());
        }
    }

    private void handleAddServiceCharge() {
        if (selectedBill == null) {
            JOptionPane.showMessageDialog(this, "Select an active invoice from the list first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String description = JOptionPane.showInputDialog(this, "Enter Service Name/Description (e.g., Room Service, Laundry, Spa):", "Add Service Charge", JOptionPane.QUESTION_MESSAGE);
        if (ValidationUtil.isNullOrEmpty(description)) return;

        String costStr = JOptionPane.showInputDialog(this, "Enter Cost for " + description + " (Rs):", "Service Cost", JOptionPane.QUESTION_MESSAGE);
        if (!ValidationUtil.isValidPositiveDouble(costStr)) {
            JOptionPane.showMessageDialog(this, "Invalid cost value. Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double cost = Double.parseDouble(costStr);
            billingManager.addServiceCharge(selectedBill.getBillId(), description, cost);
            displayBillDetails(selectedBill);
            refreshBillTable();
            JOptionPane.showMessageDialog(this, "Service charge added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRecordPayment() {
        if (selectedBill == null) {
            JOptionPane.showMessageDialog(this, "Select an invoice to record payment.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] methods = {"Credit Card", "UPI", "Cash", "NetBanking"};
        String method = (String) JOptionPane.showInputDialog(this,
                "Select Payment Settlement Method for Total Rs. " + String.format("%.2f", selectedBill.getGrandTotal()) + ":",
                "Record Payment", JOptionPane.QUESTION_MESSAGE, null, methods, methods[0]);

        if (method != null) {
            try {
                billingManager.recordPayment(selectedBill.getBillId(), method);
                JOptionPane.showMessageDialog(this, "Payment recorded successfully! Invoice status set to PAID.", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayBillDetails(selectedBill);
                refreshBillTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshBillTable() {
        populateBillTable(billingManager.getAllBills());
    }

    private void populateBillTable(List<Bill> list) {
        modelBills.setRowCount(0);
        for (Bill b : list) {
            modelBills.addRow(new Object[]{
                    b.getBillId(),
                    b.getReservationId(),
                    b.getGuestName(),
                    "Rs. " + String.format("%.2f", b.getGrandTotal()),
                    b.getPaymentStatus().getDisplayName()
            });
        }
    }
}
