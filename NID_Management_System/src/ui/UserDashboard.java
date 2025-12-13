package ui;

import dao.CitizenDAO;
import model.Citizen;
import util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class UserDashboard extends JPanel {
    private NIDAppGUI mainApp;
    private int citizenId;
    private Citizen citizen;
    private CitizenDAO citizenDAO;
    private JLabel nameLabel;
    private JLabel statusLabel;
    private boolean isEditMode = false;
    private JPanel infoPanel;
    private JButton editButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField fullNameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JSpinner dobSpinner;
    private JComboBox<String> genderCombo;
    private JComboBox<String> bloodGroupCombo;
    private JTextField religionField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JComboBox<String> divisionCombo;
    private JComboBox<String> districtCombo;
    private JTextField upazilaField;
    private JTextField postalCodeField;

    private final String[] DIVISIONS = {"Dhaka", "Chittagong", "Rajshahi", "Khulna", "Barisal", "Sylhet", "Rangpur", "Mymensingh"};
    private final String[][] DISTRICTS = {
            {"Dhaka", "Gazipur", "Narayanganj", "Tangail", "Kishoreganj"},
            {"Chittagong", "Cox's Bazar", "Rangamati", "Bandarban", "Khagrachari"},
            {"Rajshahi", "Bogra", "Pabna", "Sirajganj", "Naogaon"},
            {"Khulna", "Satkhira", "Jessore", "Bagerhat", "Narail"},
            {"Barisal", "Patuakhali", "Bhola", "Pirojpur", "Jhalokati"},
            {"Sylhet", "Moulvibazar", "Habiganj", "Sunamganj"},
            {"Rangpur", "Dinajpur", "Nilphamari", "Gaibandha", "Lalmonirhat"},
            {"Mymensingh", "Jamalpur", "Netrokona", "Sherpur"}
    };

    public UserDashboard(NIDAppGUI mainApp, int citizenId) {
        this.mainApp = mainApp;
        this.citizenId = citizenId;
        this.citizenDAO = new CitizenDAO();
        this.citizen = citizenDAO.getCitizenById(citizenId);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.getBackgroundColor());

        JPanel headerPanel = UIUtils.createHeaderPanel("Citizen Dashboard - NID Information");

        JButton logoutButton = new JButton("Logout");
        UIUtils.styleDangerButton(logoutButton);
        logoutButton.addActionListener(e -> mainApp.showLoginScreen());

        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIUtils.getBackgroundColor());
        contentPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(UIUtils.getBackgroundColor());
        welcomePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        nameLabel = new JLabel("Welcome, " + (citizen != null ? citizen.getFullName() : "User"));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(41, 128, 185));

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateStatusLabel();

        welcomePanel.add(nameLabel, BorderLayout.WEST);
        welcomePanel.add(statusLabel, BorderLayout.EAST);

        contentPanel.add(welcomePanel, BorderLayout.NORTH);

        createInfoPanel();
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void createInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        infoPanel.add(createFormSection("PERSONAL INFORMATION"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel personalInfoPanel = createTwoColumnPanel();

        JPanel personalCol1 = new JPanel();
        personalCol1.setLayout(new BoxLayout(personalCol1, BoxLayout.Y_AXIS));
        personalCol1.setBackground(Color.WHITE);

        personalCol1.add(createInfoField("Full Name", citizen != null ? citizen.getFullName() : "N/A"));
        personalCol1.add(Box.createRigidArea(new Dimension(0, 10)));
        personalCol1.add(createInfoField("Father's Name", citizen != null ? citizen.getFatherName() : "N/A"));
        personalCol1.add(Box.createRigidArea(new Dimension(0, 10)));
        personalCol1.add(createInfoField("Mother's Name", citizen != null ? citizen.getMotherName() : "N/A"));

        JPanel personalCol2 = new JPanel();
        personalCol2.setLayout(new BoxLayout(personalCol2, BoxLayout.Y_AXIS));
        personalCol2.setBackground(Color.WHITE);

        String dobFormatted = "N/A";
        if (citizen != null && citizen.getDateOfBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            dobFormatted = sdf.format(java.sql.Date.valueOf(citizen.getDateOfBirth()));
        }

        personalCol2.add(createInfoField("Date of Birth", dobFormatted));
        personalCol2.add(Box.createRigidArea(new Dimension(0, 10)));
        personalCol2.add(createInfoField("Gender", citizen != null ? citizen.getGender() : "N/A"));
        personalCol2.add(Box.createRigidArea(new Dimension(0, 10)));
        personalCol2.add(createInfoField("Blood Group",
                citizen != null && citizen.getBloodGroup() != null ? citizen.getBloodGroup() : "Not Specified"));
        personalCol2.add(Box.createRigidArea(new Dimension(0, 10)));
        personalCol2.add(createInfoField("Religion",
                citizen != null && citizen.getReligion() != null ? citizen.getReligion() : "Not Specified"));

        personalInfoPanel.add(personalCol1);
        personalInfoPanel.add(personalCol2);
        infoPanel.add(personalInfoPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        infoPanel.add(createFormSection("CONTACT INFORMATION"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel contactInfoPanel = createTwoColumnPanel();

        JPanel contactCol1 = new JPanel();
        contactCol1.setLayout(new BoxLayout(contactCol1, BoxLayout.Y_AXIS));
        contactCol1.setBackground(Color.WHITE);
        contactCol1.add(createInfoField("Phone Number", citizen != null ? citizen.getPhoneNumber() : "N/A"));

        JPanel contactCol2 = new JPanel();
        contactCol2.setLayout(new BoxLayout(contactCol2, BoxLayout.Y_AXIS));
        contactCol2.setBackground(Color.WHITE);
        contactCol2.add(createInfoField("Email Address", citizen != null ? citizen.getEmail() : "N/A"));

        contactInfoPanel.add(contactCol1);
        contactInfoPanel.add(contactCol2);
        infoPanel.add(contactInfoPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        infoPanel.add(createFormSection("ADDRESS INFORMATION"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel addressInfoPanel = createTwoColumnPanel();

        JPanel addressCol1 = new JPanel();
        addressCol1.setLayout(new BoxLayout(addressCol1, BoxLayout.Y_AXIS));
        addressCol1.setBackground(Color.WHITE);

        addressCol1.add(createInfoField("Division",
                citizen != null && citizen.getDivision() != null ? citizen.getDivision() : "N/A"));
        addressCol1.add(Box.createRigidArea(new Dimension(0, 10)));
        addressCol1.add(createInfoField("District",
                citizen != null && citizen.getDistrict() != null ? citizen.getDistrict() : "N/A"));

        JPanel addressCol2 = new JPanel();
        addressCol2.setLayout(new BoxLayout(addressCol2, BoxLayout.Y_AXIS));
        addressCol2.setBackground(Color.WHITE);

        addressCol2.add(createInfoField("Upazila",
                citizen != null && citizen.getUpazila() != null ? citizen.getUpazila() : "N/A"));
        addressCol2.add(Box.createRigidArea(new Dimension(0, 10)));
        addressCol2.add(createInfoField("Postal Code",
                citizen != null && citizen.getPostalCode() != null ? citizen.getPostalCode() : "N/A"));

        addressInfoPanel.add(addressCol1);
        addressInfoPanel.add(addressCol2);
        infoPanel.add(addressInfoPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        infoPanel.add(createFormSection("NID INFORMATION"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel nidInfoPanel = createTwoColumnPanel();

        JPanel nidCol1 = new JPanel();
        nidCol1.setLayout(new BoxLayout(nidCol1, BoxLayout.Y_AXIS));
        nidCol1.setBackground(Color.WHITE);

        JPanel nidPanel = createInfoField("NID Number",
                citizen != null && citizen.getNidNumber() != null ? citizen.getNidNumber() : "Not Assigned Yet");
        if (citizen != null && citizen.getNidNumber() != null) {

            Component[] components = nidPanel.getComponents();
            if (components.length > 1 && components[1] instanceof JLabel) {
                JLabel valueLabel = (JLabel) components[1];
                valueLabel.setForeground(new Color(0, 100, 0));
                valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        }
        nidCol1.add(nidPanel);

        JPanel nidCol2 = new JPanel();
        nidCol2.setLayout(new BoxLayout(nidCol2, BoxLayout.Y_AXIS));
        nidCol2.setBackground(Color.WHITE);

        String regDateFormatted = "N/A";
        if (citizen != null && citizen.getRegistrationDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            regDateFormatted = sdf.format(java.sql.Date.valueOf(citizen.getRegistrationDate()));
        }
        nidCol2.add(createInfoField("Registration Date", regDateFormatted));

        nidInfoPanel.add(nidCol1);
        nidInfoPanel.add(nidCol2);
        infoPanel.add(nidInfoPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        editButton = new JButton("Edit Information");
        UIUtils.stylePrimaryButton(editButton);
        editButton.setPreferredSize(new Dimension(150, 40));
        editButton.addActionListener(e -> enableEditMode());

        JButton printButton = new JButton("Print Information");
        UIUtils.styleSuccessButton(printButton);
        printButton.setPreferredSize(new Dimension(150, 40));
        printButton.addActionListener(e -> printInformation());

        buttonPanel.add(editButton);
        buttonPanel.add(printButton);
        infoPanel.add(buttonPanel);
    }

    private JPanel createTwoColumnPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 30, 0));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JPanel createFormSection(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(41, 128, 185));

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));

        panel.add(label, BorderLayout.NORTH);
        panel.add(separator, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInfoField(String labelText, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueLabel.setForeground(new Color(80, 80, 80));
        valueLabel.setBorder(new EmptyBorder(2, 5, 2, 5));

        panel.add(label, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private void updateStatusLabel() {
        if (citizen != null) {
            String status = citizen.getStatus();
            Color color;
            switch (status) {
                case "Approved":
                    color = new Color(0, 128, 0);
                    break;
                case "Rejected":
                    color = new Color(200, 0, 0);
                    break;
                default:
                    color = new Color(255, 140, 0);
                    break;
            }
            statusLabel.setText("Status: " + status);
            statusLabel.setForeground(color);
            statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        }
    }

    private void enableEditMode() {
        isEditMode = true;

        removeAll();
        createEditableInfoPanel();
        revalidate();
        repaint();
    }

    private void createEditableInfoPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.getBackgroundColor());

        JPanel headerPanel = UIUtils.createHeaderPanel("Citizen Dashboard - Edit Information");

        JButton logoutButton = new JButton("Logout");
        UIUtils.styleDangerButton(logoutButton);
        logoutButton.addActionListener(e -> mainApp.showLoginScreen());

        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIUtils.getBackgroundColor());
        contentPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(UIUtils.getBackgroundColor());
        welcomePanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel titleLabel = new JLabel("Edit Citizen Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel();
        updateStatusLabel();

        welcomePanel.add(titleLabel, BorderLayout.WEST);
        welcomePanel.add(statusLabel, BorderLayout.EAST);

        contentPanel.add(welcomePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        formPanel.add(createFormSection("PERSONAL INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel personalInfoPanel = createTwoColumnPanel();

        JPanel personalCol1 = new JPanel();
        personalCol1.setLayout(new BoxLayout(personalCol1, BoxLayout.Y_AXIS));
        personalCol1.setBackground(Color.WHITE);

        fullNameField = new JTextField(citizen != null ? citizen.getFullName() : "");
        personalCol1.add(createFormField("Full Name", fullNameField));
        personalCol1.add(Box.createRigidArea(new Dimension(0, 12)));

        fatherNameField = new JTextField(citizen != null ? citizen.getFatherName() : "");
        personalCol1.add(createFormField("Father's Name", fatherNameField));
        personalCol1.add(Box.createRigidArea(new Dimension(0, 12)));

        motherNameField = new JTextField(citizen != null ? citizen.getMotherName() : "");
        personalCol1.add(createFormField("Mother's Name", motherNameField));
        JPanel personalCol2 = new JPanel();
        personalCol2.setLayout(new BoxLayout(personalCol2, BoxLayout.Y_AXIS));
        personalCol2.setBackground(Color.WHITE);

        JPanel dobPanel = new JPanel(new BorderLayout());
        dobPanel.setBackground(Color.WHITE);
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dobLabel.setForeground(new Color(60, 60, 60));

        Calendar calendar = Calendar.getInstance();
        if (citizen != null && citizen.getDateOfBirth() != null) {
            calendar.setTime(java.sql.Date.valueOf(citizen.getDateOfBirth()));
        }

        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -100);
        Calendar maxDate = Calendar.getInstance();

        SpinnerDateModel dateModel = new SpinnerDateModel(
                calendar.getTime(),
                minDate.getTime(),
                maxDate.getTime(),
                Calendar.DAY_OF_MONTH
        );
        dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "dd MMM yyyy");
        dobSpinner.setEditor(dateEditor);

        JFormattedTextField spinnerTextField = ((JSpinner.DateEditor) dobSpinner.getEditor()).getTextField();
        spinnerTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        spinnerTextField.setBackground(Color.WHITE);
        spinnerTextField.setForeground(new Color(80, 80, 80));

        dobPanel.add(dobLabel, BorderLayout.NORTH);
        dobPanel.add(dobSpinner, BorderLayout.CENTER);
        personalCol2.add(dobPanel);
        personalCol2.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel genderPanel = new JPanel(new BorderLayout());
        genderPanel.setBackground(Color.WHITE);
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        genderLabel.setForeground(new Color(60, 60, 60));

        genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        if (citizen != null && citizen.getGender() != null) {
            genderCombo.setSelectedItem(citizen.getGender());
        }

        UIUtils.styleComboBox(genderCombo);
        genderPanel.add(genderLabel, BorderLayout.NORTH);
        genderPanel.add(genderCombo, BorderLayout.CENTER);
        personalCol2.add(genderPanel);
        personalCol2.add(Box.createRigidArea(new Dimension(0, 12)));

        bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        if (citizen != null && citizen.getBloodGroup() != null) {
            bloodGroupCombo.setSelectedItem(citizen.getBloodGroup());
        }
        personalCol2.add(createFormField("Blood Group", bloodGroupCombo));
        personalCol2.add(Box.createRigidArea(new Dimension(0, 12)));

        religionField = new JTextField(citizen != null && citizen.getReligion() != null ? citizen.getReligion() : "");
        personalCol2.add(createFormField("Religion", religionField));

        personalInfoPanel.add(personalCol1);
        personalInfoPanel.add(personalCol2);
        formPanel.add(personalInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        formPanel.add(createFormSection("CONTACT INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel contactInfoPanel = createTwoColumnPanel();

        JPanel contactCol1 = new JPanel();
        contactCol1.setLayout(new BoxLayout(contactCol1, BoxLayout.Y_AXIS));
        contactCol1.setBackground(Color.WHITE);

        phoneNumberField = new JTextField(citizen != null ? citizen.getPhoneNumber() : "");
        contactCol1.add(createFormField("Phone Number", phoneNumberField));

        JPanel contactCol2 = new JPanel();
        contactCol2.setLayout(new BoxLayout(contactCol2, BoxLayout.Y_AXIS));
        contactCol2.setBackground(Color.WHITE);

        emailField = new JTextField(citizen != null ? citizen.getEmail() : "");
        contactCol2.add(createFormField("Email Address", emailField));

        contactInfoPanel.add(contactCol1);
        contactInfoPanel.add(contactCol2);
        formPanel.add(contactInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        formPanel.add(createFormSection("ADDRESS INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel addressInfoPanel = createTwoColumnPanel();

        JPanel addressCol1 = new JPanel();
        addressCol1.setLayout(new BoxLayout(addressCol1, BoxLayout.Y_AXIS));
        addressCol1.setBackground(Color.WHITE);

        divisionCombo = new JComboBox<>(DIVISIONS);
        if (citizen != null && citizen.getDivision() != null) {
            divisionCombo.setSelectedItem(citizen.getDivision());
        }
        divisionCombo.addActionListener(e -> updateDistricts());
        addressCol1.add(createFormField("Division", divisionCombo));
        addressCol1.add(Box.createRigidArea(new Dimension(0, 12)));

        districtCombo = new JComboBox<>();
        if (citizen != null && citizen.getDistrict() != null) {
            districtCombo.setSelectedItem(citizen.getDistrict());
        }
        addressCol1.add(createFormField("District", districtCombo));

        JPanel addressCol2 = new JPanel();
        addressCol2.setLayout(new BoxLayout(addressCol2, BoxLayout.Y_AXIS));
        addressCol2.setBackground(Color.WHITE);

        upazilaField = new JTextField(citizen != null && citizen.getUpazila() != null ? citizen.getUpazila() : "");
        addressCol2.add(createFormField("Upazila", upazilaField));
        addressCol2.add(Box.createRigidArea(new Dimension(0, 12)));

        postalCodeField = new JTextField(citizen != null && citizen.getPostalCode() != null ? citizen.getPostalCode() : "");
        addressCol2.add(createFormField("Postal Code", postalCodeField));

        addressInfoPanel.add(addressCol1);
        addressInfoPanel.add(addressCol2);
        formPanel.add(addressInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        formPanel.add(createFormSection("NID INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel nidInfoPanel = createTwoColumnPanel();

        JPanel nidCol1 = new JPanel();
        nidCol1.setLayout(new BoxLayout(nidCol1, BoxLayout.Y_AXIS));
        nidCol1.setBackground(Color.WHITE);

        JPanel nidPanel = createInfoField("NID Number",
                citizen != null && citizen.getNidNumber() != null ? citizen.getNidNumber() : "Not Assigned");
        stylePermanentField(nidPanel);
        if (citizen != null && citizen.getNidNumber() != null) {
            Component[] components = nidPanel.getComponents();
            if (components.length > 1 && components[1] instanceof JLabel) {
                JLabel valueLabel = (JLabel) components[1];
                valueLabel.setForeground(new Color(0, 100, 0));
                valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        }
        nidCol1.add(nidPanel);

        JPanel nidCol2 = new JPanel();
        nidCol2.setLayout(new BoxLayout(nidCol2, BoxLayout.Y_AXIS));
        nidCol2.setBackground(Color.WHITE);

        String regDateFormatted = "N/A";
        if (citizen != null && citizen.getRegistrationDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            regDateFormatted = sdf.format(java.sql.Date.valueOf(citizen.getRegistrationDate()));
        }
        JPanel regDatePanel = createInfoField("Registration Date", regDateFormatted);
        stylePermanentField(regDatePanel);
        nidCol2.add(regDatePanel);

        nidInfoPanel.add(nidCol1);
        nidInfoPanel.add(nidCol2);
        formPanel.add(nidInfoPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        saveButton = new JButton("Save Changes");
        UIUtils.styleSuccessButton(saveButton);
        saveButton.setPreferredSize(new Dimension(140, 40));
        saveButton.addActionListener(e -> saveChanges());

        cancelButton = new JButton("Cancel");
        UIUtils.styleDangerButton(cancelButton);
        cancelButton.setPreferredSize(new Dimension(140, 40));
        cancelButton.addActionListener(e -> cancelEdit());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        updateDistricts();
    }

    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setBorder(new EmptyBorder(0, 0, 5, 0));

        if (field instanceof JTextField) {
            UIUtils.styleTextField((JTextField) field);
        } else if (field instanceof JComboBox) {
            UIUtils.styleComboBox((JComboBox<?>) field);
        }

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void stylePermanentField(JPanel fieldPanel) {
        fieldPanel.setBackground(new Color(245, 245, 245));
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        Component[] components = fieldPanel.getComponents();
        if (components.length > 1 && components[1] instanceof JLabel) {
            JLabel valueLabel = (JLabel) components[1];
            valueLabel.setForeground(new Color(120, 120, 120));
        }
    }

    private void updateDistricts() {
        int divisionIndex = divisionCombo.getSelectedIndex();
        if (divisionIndex >= 0 && divisionIndex < DISTRICTS.length) {
            districtCombo.removeAllItems();
            for (String district : DISTRICTS[divisionIndex]) {
                districtCombo.addItem(district);
            }
        }
    }

    private void saveChanges() {
        try {

            if (fullNameField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(this, "Full Name is required!");
                return;
            }
            if (fatherNameField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(this, "Father's Name is required!");
                return;
            }
            if (motherNameField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(this, "Mother's Name is required!");
                return;
            }
            if (phoneNumberField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(this, "Phone Number is required!");
                return;
            }
            if (emailField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(this, "Email is required!");
                return;
            }
            if (religionField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(this, "Religion is required!");
                return;
            }

            Date dobDate = (Date) dobSpinner.getValue();
            LocalDate dobLocalDate = dobDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();
            Period age = Period.between(dobLocalDate, today);
            if (age.getYears() < 18) {
                UIUtils.showErrorMessage(this, "You must be at least 18 years old to update your information!");
                return;
            }

            citizen.setFullName(fullNameField.getText().trim());
            citizen.setFatherName(fatherNameField.getText().trim());
            citizen.setMotherName(motherNameField.getText().trim());


            citizen.setDateOfBirth(dobLocalDate);

            citizen.setGender(genderCombo.getSelectedItem().toString());

            citizen.setPhoneNumber(phoneNumberField.getText().trim());
            citizen.setEmail(emailField.getText().trim());
            citizen.setBloodGroup(bloodGroupCombo.getSelectedItem().toString());
            citizen.setReligion(religionField.getText().trim());
            citizen.setDivision(divisionCombo.getSelectedItem().toString());
            citizen.setDistrict(districtCombo.getSelectedItem().toString());
            citizen.setUpazila(upazilaField.getText().trim());
            citizen.setPostalCode(postalCodeField.getText().trim());

            String previousNid = citizen.getNidNumber();
            citizen.setStatus("Pending");
            citizen.setNidNumber(previousNid);

            boolean success = citizenDAO.updateCitizen(citizen);

            if (success) {
                UIUtils.showSuccessMessage(this, "Information updated successfully! Status changed to Pending.");
                cancelEdit();
            } else {
                UIUtils.showErrorMessage(this, "Failed to update information.");
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Error updating information: " + e.getMessage());
        }
    }

    private void cancelEdit() {
        isEditMode = false;

        citizen = citizenDAO.getCitizenById(citizenId);

        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }

    private void printInformation() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("=============================================\n");
            sb.append("        NATIONAL ID CARD INFORMATION        \n");
            sb.append("=============================================\n\n");

            if (citizen != null) {
                sb.append(String.format("%-20s: %s\n", "Full Name", citizen.getFullName()));
                sb.append(String.format("%-20s: %s\n", "Father's Name", citizen.getFatherName()));
                sb.append(String.format("%-20s: %s\n", "Mother's Name", citizen.getMotherName()));
                sb.append(String.format("%-20s: %s\n", "Date of Birth",
                        citizen.getDateOfBirth() != null ? citizen.getDateOfBirth().toString() : "N/A"));
                sb.append(String.format("%-20s: %s\n", "Gender", citizen.getGender()));
                sb.append(String.format("%-20s: %s\n", "Blood Group",
                        citizen.getBloodGroup() != null ? citizen.getBloodGroup() : "Not Specified"));
                sb.append(String.format("%-20s: %s\n", "Religion",
                        citizen.getReligion() != null ? citizen.getReligion() : "Not Specified"));
                sb.append("\n");
                sb.append(String.format("%-20s: %s\n", "Phone Number", citizen.getPhoneNumber()));
                sb.append(String.format("%-20s: %s\n", "Email", citizen.getEmail()));
                sb.append("\n");
                sb.append(String.format("%-20s: %s\n", "Division",
                        citizen.getDivision() != null ? citizen.getDivision() : "N/A"));
                sb.append(String.format("%-20s: %s\n", "District",
                        citizen.getDistrict() != null ? citizen.getDistrict() : "N/A"));
                sb.append(String.format("%-20s: %s\n", "Upazila",
                        citizen.getUpazila() != null ? citizen.getUpazila() : "N/A"));
                sb.append(String.format("%-20s: %s\n", "Postal Code",
                        citizen.getPostalCode() != null ? citizen.getPostalCode() : "N/A"));
                sb.append("\n");
                sb.append(String.format("%-20s: %s\n", "NID Number",
                        citizen.getNidNumber() != null ? citizen.getNidNumber() : "Not Assigned"));
                sb.append(String.format("%-20s: %s\n", "Status", citizen.getStatus()));
                sb.append(String.format("%-20s: %s\n", "Registration Date",
                        citizen.getRegistrationDate() != null ? citizen.getRegistrationDate().toString() : "N/A"));
            }

            sb.append("\n=============================================\n");
            sb.append("         END OF DOCUMENT                   \n");
            sb.append("=============================================\n");

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 600));

            int result = JOptionPane.showConfirmDialog(
                    this,
                    scrollPane,
                    "Print NID Information",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                UIUtils.showSuccessMessage(this, "Information ready for printing!");
            }

        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Error preparing print: " + e.getMessage());
        }
    }
}