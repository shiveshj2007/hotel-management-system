package hotelmanagement.gui;

import hotelmanagement.manager.GuestManager;
import hotelmanagement.model.Guest;
import hotelmanagement.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuestPanel extends JPanel {

    private final GuestManager guestManager;

    private JTextField txtGuestId;
    private JTextField txtFullName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtIdDocument;

    private JTextField txtSearch;
    private JTable tableGuests;
    private DefaultTableModel tableModel;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public GuestPanel(GuestManager guestManager) {
        this.guestManager = guestManager;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initLeftFormPanel();
        initRightTablePanel();

        refreshGuestTable();
    }

    private void initLeftFormPanel() {
        JPanel formContainer = UITheme.createCardPanel();
        formContainer.setLayout(new BorderLayout(10, 15));
        formContainer.setPreferredSize(new Dimension(360, 0));

        JLabel title = new JLabel("Guest Details Form");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.PRIMARY_DARK);
        formContainer.add(title, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtGuestId = UITheme.createStyledTextField(15);
        txtGuestId.setEditable(false);
        txtGuestId.setBackground(new Color(238, 238, 238));
        txtGuestId.setText(guestManager.generateGuestId());

        txtFullName = UITheme.createStyledTextField(15);
        txtPhone = UITheme.createStyledTextField(15);
        txtEmail = UITheme.createStyledTextField(15);
        txtAddress = UITheme.createStyledTextField(15);
        txtIdDocument = UITheme.createStyledTextField(15);

        addFormField(fieldsPanel, gbc, 0, "Guest ID (Auto):", txtGuestId);
        addFormField(fieldsPanel, gbc, 1, "Full Name *:", txtFullName);
        addFormField(fieldsPanel, gbc, 2, "Phone Number *:", txtPhone);
        addFormField(fieldsPanel, gbc, 3, "Email Address *:", txtEmail);
        addFormField(fieldsPanel, gbc, 4, "Address:", txtAddress);
        addFormField(fieldsPanel, gbc, 5, "ID Document *:", txtIdDocument);

        formContainer.add(fieldsPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);

        btnAdd = UITheme.createStyledButton("Add Guest", UITheme.COLOR_SUCCESS, Color.WHITE);
        btnUpdate = UITheme.createStyledButton("Update Guest", UITheme.PRIMARY_ACCENT, Color.WHITE);
        btnDelete = UITheme.createStyledButton("Delete Guest", UITheme.COLOR_DANGER, Color.WHITE);
        btnClear = UITheme.createStyledButton("Clear Form", UITheme.TEXT_MUTED, Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        formContainer.add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> handleAddGuest());
        btnUpdate.addActionListener(e -> handleUpdateGuest());
        btnDelete.addActionListener(e -> handleDeleteGuest());
        btnClear.addActionListener(e -> clearForm());

        add(formContainer, BorderLayout.WEST);
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

    private void initRightTablePanel() {
        JPanel tableContainer = UITheme.createCardPanel();
        tableContainer.setLayout(new BorderLayout(10, 15));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Guest: ");
        lblSearch.setFont(UITheme.FONT_BOLD);
        lblSearch.setForeground(UITheme.PRIMARY_DARK);

        txtSearch = UITheme.createStyledTextField(20);
        JButton btnSearch = UITheme.createStyledButton("Search", UITheme.PRIMARY_ACCENT, Color.WHITE);
        JButton btnResetSearch = UITheme.createStyledButton("Reset", UITheme.TEXT_MUTED, Color.WHITE);

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchControls.setOpaque(false);
        searchControls.add(btnSearch);
        searchControls.add(btnResetSearch);

        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(searchControls, BorderLayout.EAST);

        tableContainer.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"Guest ID", "Full Name", "Phone", "Email", "Address", "ID Document"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableGuests = new JTable(tableModel);
        UITheme.styleTable(tableGuests);

        JScrollPane scrollPane = new JScrollPane(tableGuests);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        tableGuests.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableGuests.getSelectedRow() != -1) {
                int row = tableGuests.getSelectedRow();
                txtGuestId.setText((String) tableModel.getValueAt(row, 0));
                txtFullName.setText((String) tableModel.getValueAt(row, 1));
                txtPhone.setText((String) tableModel.getValueAt(row, 2));
                txtEmail.setText((String) tableModel.getValueAt(row, 3));
                txtAddress.setText((String) tableModel.getValueAt(row, 4));
                txtIdDocument.setText((String) tableModel.getValueAt(row, 5));
            }
        });

        btnSearch.addActionListener(e -> handleSearch());
        btnResetSearch.addActionListener(e -> {
            txtSearch.setText("");
            refreshGuestTable();
        });

        add(tableContainer, BorderLayout.CENTER);
    }

    private void handleAddGuest() {
        try {
            Guest guest = new Guest(
                    txtGuestId.getText().trim(),
                    txtFullName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim(),
                    txtIdDocument.getText().trim()
            );

            String generatedId = guestManager.addGuest(guest);
            JOptionPane.showMessageDialog(this, "Guest registered successfully! Assigned ID: " + generatedId,
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            refreshGuestTable();
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateGuest() {
        try {
            Guest guest = new Guest(
                    txtGuestId.getText().trim(),
                    txtFullName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim(),
                    txtIdDocument.getText().trim()
            );

            guestManager.updateGuest(guest);
            JOptionPane.showMessageDialog(this, "Guest updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            refreshGuestTable();
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteGuest() {
        String guestId = txtGuestId.getText().trim();
        if (ValidationUtil.isNullOrEmpty(guestId)) {
            JOptionPane.showMessageDialog(this, "Select a guest from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Guest " + guestId + " (" + txtFullName.getText() + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                guestManager.deleteGuest(guestId);
                JOptionPane.showMessageDialog(this, "Guest deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshGuestTable();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSearch() {
        String query = txtSearch.getText().trim();
        List<Guest> results = guestManager.searchGuests(query);
        populateTable(results);
    }

    private void refreshGuestTable() {
        populateTable(guestManager.getAllGuests());
    }

    private void populateTable(List<Guest> list) {
        tableModel.setRowCount(0);
        for (Guest g : list) {
            tableModel.addRow(new Object[]{
                    g.getGuestId(),
                    g.getFullName(),
                    g.getPhone(),
                    g.getEmail(),
                    g.getAddress(),
                    g.getIdDocument()
            });
        }
    }

    private void clearForm() {
        txtGuestId.setText(guestManager.generateGuestId());
        txtFullName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtIdDocument.setText("");
        tableGuests.clearSelection();
    }
}
