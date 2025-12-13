package ui;

import dao.CitizenDAO;
import model.Citizen;
import util.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class AdminDashboard extends JPanel {
    private NIDAppGUI mainApp;
    private JTable citizensTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel statsLabel;
    private CitizenDAO citizenDAO;

    public AdminDashboard(NIDAppGUI mainApp) {
        this.mainApp = mainApp;
        this.citizenDAO = new CitizenDAO();
        initializeUI();
        loadCitizens();
        updateStats();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.getBackgroundColor());

        JPanel headerPanel = UIUtils.createHeaderPanel("Admin Dashboard - Citizen Management");

        JButton logoutButton = new JButton("Logout");
        UIUtils.styleDangerButton(logoutButton);
        logoutButton.addActionListener(e -> mainApp.showLoginScreen());

        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        UIUtils.stylePanel(contentPanel);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setBackground(UIUtils.getPanelColor());

        searchField = new JTextField(20);
        UIUtils.styleTextField(searchField);

        JButton searchButton = new JButton("Search");
        UIUtils.stylePrimaryButton(searchButton);
        searchButton.addActionListener(e -> searchCitizens());

        JButton approveButton = new JButton("Approve");
        UIUtils.styleSuccessButton(approveButton);
        approveButton.addActionListener(new ApproveButtonListener());

        JButton rejectButton = new JButton("Reject");
        UIUtils.styleDangerButton(rejectButton);
        rejectButton.addActionListener(new RejectButtonListener());

        JButton detailsButton = new JButton("Details");
        UIUtils.styleSecondaryButton(detailsButton);
        detailsButton.addActionListener(new DetailsButtonListener());

        JButton deleteButton = new JButton("Delete");
        UIUtils.styleDangerButton(deleteButton);
        deleteButton.addActionListener(new DeleteButtonListener());

        controlsPanel.add(new JLabel("Search:"));
        controlsPanel.add(searchField);
        controlsPanel.add(searchButton);
        controlsPanel.add(approveButton);
        controlsPanel.add(rejectButton);
        controlsPanel.add(detailsButton);
        controlsPanel.add(deleteButton);

        contentPanel.add(controlsPanel, BorderLayout.NORTH);

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsLabel.setForeground(UIUtils.getTextColor());
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(statsLabel, BorderLayout.SOUTH);

        String[] columnNames = {
                "ID", "Full Name", "Father Name", "Mother Name", "DOB",
                "Gender", "Phone", "Email", "NID Number", "Status"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        citizensTable = new JTable(tableModel);
        citizensTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        citizensTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        citizensTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        citizensTable.setRowHeight(25);

        citizensTable.setSelectionBackground(new Color(173, 216, 230));
        citizensTable.setSelectionForeground(Color.BLACK);
        citizensTable.setGridColor(new Color(200, 200, 200));

        citizensTable.setDefaultRenderer(Object.class, new StatusTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(citizensTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(1100, 500));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadCitizens() {
        tableModel.setRowCount(0);

        List<Citizen> citizens = citizenDAO.getAllCitizens();
        for (Citizen citizen : citizens) {
            Object[] rowData = {
                    citizen.getCitizenId(),
                    citizen.getFullName(),
                    citizen.getFatherName(),
                    citizen.getMotherName(),
                    citizen.getDateOfBirth() != null ? citizen.getDateOfBirth().toString() : "",
                    citizen.getGender(),
                    citizen.getPhoneNumber(),
                    citizen.getEmail(),
                    citizen.getNidNumber() != null ? citizen.getNidNumber() : "Not Assigned",
                    citizen.getStatus()
            };
            tableModel.addRow(rowData);
        }

        updateStats();
    }

    private void searchCitizens() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadCitizens();
            return;
        }

        tableModel.setRowCount(0);

        List<Citizen> citizens = citizenDAO.searchCitizens(searchTerm);
        for (Citizen citizen : citizens) {
            Object[] rowData = {
                    citizen.getCitizenId(),
                    citizen.getFullName(),
                    citizen.getFatherName(),
                    citizen.getMotherName(),
                    citizen.getDateOfBirth() != null ? citizen.getDateOfBirth().toString() : "",
                    citizen.getGender(),
                    citizen.getPhoneNumber(),
                    citizen.getEmail(),
                    citizen.getNidNumber() != null ? citizen.getNidNumber() : "Not Assigned",
                    citizen.getStatus()
            };
            tableModel.addRow(rowData);
        }

        updateStats();
    }

    private void updateStats() {
        int total = tableModel.getRowCount();
        int approved = 0;
        int pending = 0;
        int rejected = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = (String) tableModel.getValueAt(i, 9);
            if ("Approved".equals(status)) {
                approved++;
            } else if ("Pending".equals(status)) {
                pending++;
            } else if ("Rejected".equals(status)) {
                rejected++;
            }
        }

        statsLabel.setText(String.format(
                "Total: %d | Approved: %d | Pending: %d | Rejected: %d",
                total, approved, pending, rejected
        ));
    }

    private int getSelectedCitizenId() {
        int selectedRow = citizensTable.getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        return (int) tableModel.getValueAt(selectedRow, 0);
    }

    private String getSelectedCitizenStatus() {
        int selectedRow = citizensTable.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        return (String) tableModel.getValueAt(selectedRow, 9);
    }

    private String generateNidNumber() {
        Random random = new Random();
        String nid;

        do {
            StringBuilder nidBuilder = new StringBuilder();
            for (int i = 0; i < 20; i++) {
                nidBuilder.append(random.nextInt(10));
            }
            nid = nidBuilder.toString();
        } while (citizenDAO.getCitizenByNid(nid) != null);

        return nid;
    }

    private class ApproveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int citizenId = getSelectedCitizenId();
            if (citizenId == -1) {
                UIUtils.showErrorMessage(AdminDashboard.this, "Please select a citizen to approve.");
                return;
            }

            String currentStatus = getSelectedCitizenStatus();
            if (!"Pending".equals(currentStatus)) {
                UIUtils.showErrorMessage(AdminDashboard.this,
                        "Only pending citizens can be approved.\n" +
                                "Selected citizen status: " + currentStatus);
                return;
            }

            Citizen citizen = citizenDAO.getCitizenById(citizenId);
            if (citizen == null) {
                UIUtils.showErrorMessage(AdminDashboard.this, "Citizen not found.");
                return;
            }

            String nidNumber = citizen.getNidNumber();
            boolean hasExistingNid = nidNumber != null && !nidNumber.trim().isEmpty();

            String message;
            if (hasExistingNid) {
                message = "Are you sure you want to approve this citizen?\n\n" +
                        "Name: " + citizen.getFullName() + "\n" +
                        "Email: " + citizen.getEmail() + "\n" +
                        "Existing NID: " + nidNumber + "\n\n" +
                        "The existing NID number will be kept.";
            } else {
                message = "Are you sure you want to approve this citizen?\n\n" +
                        "Name: " + citizen.getFullName() + "\n" +
                        "Email: " + citizen.getEmail() + "\n\n" +
                        "A 20-digit NID number will be automatically generated.";
            }

            int confirm = JOptionPane.showConfirmDialog(
                    AdminDashboard.this,
                    message,
                    "Approve Citizen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (!hasExistingNid) {
                    nidNumber = generateNidNumber();
                }

                if (citizenDAO.approveCitizen(citizenId, nidNumber)) {
                    if (hasExistingNid) {
                        UIUtils.showSuccessMessage(AdminDashboard.this,
                                "Citizen approved successfully!\n" +
                                        "Existing NID Number: " + nidNumber);
                    } else {
                        UIUtils.showSuccessMessage(AdminDashboard.this,
                                "Citizen approved successfully!\n" +
                                        "New NID Number: " + nidNumber);
                    }
                    loadCitizens();
                } else {
                    UIUtils.showErrorMessage(AdminDashboard.this, "Failed to approve citizen.");
                }
            }
        }
    }

    private class RejectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int citizenId = getSelectedCitizenId();
            if (citizenId == -1) {
                UIUtils.showErrorMessage(AdminDashboard.this, "Please select a citizen to reject.");
                return;
            }

            String currentStatus = getSelectedCitizenStatus();
            if (!"Pending".equals(currentStatus)) {
                UIUtils.showErrorMessage(AdminDashboard.this,
                        "Only pending citizens can be rejected.\n" +
                                "Selected citizen status: " + currentStatus);
                return;
            }

            Citizen citizen = citizenDAO.getCitizenById(citizenId);
            if (citizen == null) {
                UIUtils.showErrorMessage(AdminDashboard.this, "Citizen not found.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    AdminDashboard.this,
                    "Are you sure you want to reject this citizen application?\n\n" +
                            "Name: " + citizen.getFullName() + "\n" +
                            "Email: " + citizen.getEmail() + "\n\n" +
                            "This action cannot be undone.",
                    "Reject Citizen Application",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (citizenDAO.rejectCitizen(citizenId)) {
                    UIUtils.showSuccessMessage(AdminDashboard.this, "Citizen application rejected successfully!");
                    loadCitizens();
                } else {
                    UIUtils.showErrorMessage(AdminDashboard.this, "Failed to reject citizen application.");
                }
            }
        }
    }

    private class DetailsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int citizenId = getSelectedCitizenId();
            if (citizenId == -1) {
                UIUtils.showErrorMessage(AdminDashboard.this, "Please select a citizen to view details.");
                return;
            }

            Citizen citizen = citizenDAO.getCitizenById(citizenId);
            if (citizen != null) {
                showDetailsDialog(citizen);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int citizenId = getSelectedCitizenId();
            if (citizenId == -1) {
                UIUtils.showErrorMessage(AdminDashboard.this, "Please select a citizen to delete.");
                return;
            }

            if (UIUtils.showConfirmationDialog(
                    AdminDashboard.this,
                    "Are you sure you want to delete this citizen record? This action cannot be undone."
            )) {
                if (citizenDAO.deleteCitizen(citizenId)) {
                    UIUtils.showSuccessMessage(AdminDashboard.this, "Citizen record deleted successfully!");
                    loadCitizens();
                } else {
                    UIUtils.showErrorMessage(AdminDashboard.this, "Failed to delete citizen record.");
                }
            }
        }
    }

    private void showDetailsDialog(Citizen citizen) {
        JDialog detailsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Citizen Details - " + citizen.getFullName(), true);
        detailsDialog.setLayout(new BorderLayout());
        detailsDialog.setSize(700, 800);
        detailsDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        detailsDialog.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 2;
        contentPanel.add(createSectionLabel("Personal Information"), gbc);
        gbc.gridwidth = 1;

        addDetailRow(contentPanel, gbc, row++, "Full Name:", citizen.getFullName());
        addDetailRow(contentPanel, gbc, row++, "Father's Name:", citizen.getFatherName());
        addDetailRow(contentPanel, gbc, row++, "Mother's Name:", citizen.getMotherName());
        addDetailRow(contentPanel, gbc, row++, "Date of Birth:", citizen.getDateOfBirth() != null ? citizen.getDateOfBirth().toString() : "N/A");
        addDetailRow(contentPanel, gbc, row++, "Gender:", citizen.getGender());
        addDetailRow(contentPanel, gbc, row++, "Blood Group:", citizen.getBloodGroup() != null ? citizen.getBloodGroup() : "Not Specified");
        addDetailRow(contentPanel, gbc, row++, "Religion:", citizen.getReligion() != null ? citizen.getReligion() : "Not Specified");

        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 2;
        contentPanel.add(createSectionLabel("Contact Information"), gbc);
        gbc.gridwidth = 1;

        addDetailRow(contentPanel, gbc, row++, "Phone Number:", citizen.getPhoneNumber());
        addDetailRow(contentPanel, gbc, row++, "Email:", citizen.getEmail());

        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 2;
        contentPanel.add(createSectionLabel("Address Information"), gbc);
        gbc.gridwidth = 1;

        addDetailRow(contentPanel, gbc, row++, "Division:", citizen.getDivision() != null ? citizen.getDivision() : "N/A");
        addDetailRow(contentPanel, gbc, row++, "District:", citizen.getDistrict() != null ? citizen.getDistrict() : "N/A");
        addDetailRow(contentPanel, gbc, row++, "Upazila:", citizen.getUpazila() != null ? citizen.getUpazila() : "N/A");
        addDetailRow(contentPanel, gbc, row++, "Postal Code:", citizen.getPostalCode() != null ? citizen.getPostalCode() : "N/A");

        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 2;
        contentPanel.add(createSectionLabel("NID Information"), gbc);
        gbc.gridwidth = 1;

        addDetailRow(contentPanel, gbc, row++, "NID Number:", citizen.getNidNumber() != null ? citizen.getNidNumber() : "Not Assigned");
        addDetailRow(contentPanel, gbc, row++, "Status:", citizen.getStatus());
        addDetailRow(contentPanel, gbc, row++, "Registration Date:", citizen.getRegistrationDate() != null ? citizen.getRegistrationDate().toString() : "N/A");

        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 15, 15, 15);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(new Color(100, 100, 100));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> detailsDialog.dispose());

        buttonPanel.add(closeButton);
        contentPanel.add(buttonPanel, gbc);

        detailsDialog.setVisible(true);
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(41, 128, 185));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        return label;
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row; gbc.gridx = 0;
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fieldLabel.setForeground(new Color(60, 60, 60));
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        JLabel valueLabel = new JLabel(value != null ? value : "N/A");
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueLabel.setForeground(Color.BLACK);

        if (label.equals("NID Number:") && !value.equals("Not Assigned")) {
            valueLabel.setForeground(new Color(0, 100, 0));
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        } else if (label.equals("Status:")) {

            switch (value) {
                case "Approved":
                    valueLabel.setForeground(new Color(0, 128, 0));
                    break;
                case "Rejected":
                    valueLabel.setForeground(new Color(200, 0, 0));
                    break;
                case "Pending":
                    valueLabel.setForeground(new Color(255, 140, 0));
                    break;
            }
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        } else if (label.equals("Full Name:")) {
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueLabel.setForeground(new Color(0, 0, 139));
        }

        panel.add(valueLabel, gbc);
    }

    private class StatusTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                return c;
            }

            if (column == 9 && value != null) {
                String status = value.toString();
                switch (status) {
                    case "Approved":
                        c.setBackground(new Color(220, 255, 220));
                        c.setForeground(new Color(0, 100, 0));
                        break;
                    case "Pending":
                        c.setBackground(new Color(255, 255, 200));
                        c.setForeground(new Color(153, 102, 0));
                        break;
                    case "Rejected":
                        c.setBackground(new Color(255, 220, 220));
                        c.setForeground(new Color(139, 0, 0));
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        break;
                }

                c.setFont(c.getFont().deriveFont(Font.BOLD));
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }
}