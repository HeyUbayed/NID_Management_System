package ui;

import dao.CitizenDAO;
import model.Citizen;
import util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class RegisterForm extends JPanel {
    private NIDAppGUI mainApp;
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
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

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

    public RegisterForm(NIDAppGUI mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.getBackgroundColor());

        add(UIUtils.createHeaderPanel("NID Registration"), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIUtils.getBackgroundColor());
        contentPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Citizen Registration Form");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        contentPanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        formPanel.add(createFormSection("PERSONAL INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(createFormField("Full Name", fullNameField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(createFormField("Father's Name", fatherNameField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(createFormField("Mother's Name", motherNameField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel dobPanel = new JPanel(new BorderLayout());
        dobPanel.setBackground(Color.WHITE);
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dobLabel.setForeground(new Color(60, 60, 60));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        SpinnerDateModel dateModel = new SpinnerDateModel(
                calendar.getTime(),
                null,
                null,
                Calendar.DAY_OF_MONTH
        );
        dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);

        JFormattedTextField spinnerTextField = ((JSpinner.DateEditor) dobSpinner.getEditor()).getTextField();
        spinnerTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        spinnerTextField.setBackground(Color.WHITE);

        dobPanel.add(dobLabel, BorderLayout.NORTH);
        dobPanel.add(dobSpinner, BorderLayout.CENTER);
        formPanel.add(dobPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel genderBloodPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        genderBloodPanel.setBackground(Color.WHITE);

        JPanel genderPanel = new JPanel(new BorderLayout());
        genderPanel.setBackground(Color.WHITE);
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        genderLabel.setForeground(new Color(60, 60, 60));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        UIUtils.styleComboBox(genderCombo);
        genderPanel.add(genderLabel, BorderLayout.NORTH);
        genderPanel.add(genderCombo, BorderLayout.CENTER);

        JPanel bloodPanel = new JPanel(new BorderLayout());
        bloodPanel.setBackground(Color.WHITE);
        JLabel bloodLabel = new JLabel("Blood Group");
        bloodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bloodLabel.setForeground(new Color(60, 60, 60));
        bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        UIUtils.styleComboBox(bloodGroupCombo);
        bloodPanel.add(bloodLabel, BorderLayout.NORTH);
        bloodPanel.add(bloodGroupCombo, BorderLayout.CENTER);

        genderBloodPanel.add(genderPanel);
        genderBloodPanel.add(bloodPanel);
        formPanel.add(genderBloodPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        formPanel.add(createFormField("Religion", religionField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        formPanel.add(createFormSection("CONTACT INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(createFormField("Phone Number", phoneNumberField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(createFormField("Email Address", emailField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        formPanel.add(createFormSection("ADDRESS INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel divisionDistrictPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        divisionDistrictPanel.setBackground(Color.WHITE);

        JPanel divisionPanel = new JPanel(new BorderLayout());
        divisionPanel.setBackground(Color.WHITE);
        JLabel divisionLabel = new JLabel("Division");
        divisionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        divisionLabel.setForeground(new Color(60, 60, 60));
        divisionCombo = new JComboBox<>(DIVISIONS);
        UIUtils.styleComboBox(divisionCombo);
        divisionCombo.addActionListener(e -> updateDistricts());
        divisionPanel.add(divisionLabel, BorderLayout.NORTH);
        divisionPanel.add(divisionCombo, BorderLayout.CENTER);

        JPanel districtPanel = new JPanel(new BorderLayout());
        districtPanel.setBackground(Color.WHITE);
        JLabel districtLabel = new JLabel("District");
        districtLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        districtLabel.setForeground(new Color(60, 60, 60));
        districtCombo = new JComboBox<>();
        UIUtils.styleComboBox(districtCombo);
        districtPanel.add(districtLabel, BorderLayout.NORTH);
        districtPanel.add(districtCombo, BorderLayout.CENTER);

        divisionDistrictPanel.add(divisionPanel);
        divisionDistrictPanel.add(districtPanel);
        formPanel.add(divisionDistrictPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        formPanel.add(createFormField("Upazila", upazilaField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(createFormField("Postal Code", postalCodeField = new JTextField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        formPanel.add(createFormSection("ACCOUNT INFORMATION"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(createFormField("Password", passwordField = new JPasswordField(25)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(createFormField("Confirm Password", confirmPasswordField = new JPasswordField(25)));

        contentPanel.add(formPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(UIUtils.getBackgroundColor());

        JButton registerButton = new JButton("Register");
        UIUtils.styleSuccessButton(registerButton);
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.addActionListener(new RegisterButtonListener());

        JButton backButton = new JButton("Back to Login");
        UIUtils.styleSecondaryButton(backButton);
        backButton.setPreferredSize(new Dimension(140, 40));
        backButton.addActionListener(e -> {
            clearForm();
            mainApp.showLoginScreen();
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel);

        updateDistricts();
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

    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setBorder(new EmptyBorder(0, 0, 5, 0));

        if (field instanceof JTextField) {
            UIUtils.styleTextField((JTextField) field);
        } else if (field instanceof JPasswordField) {
            UIUtils.stylePasswordField((JPasswordField) field);
        } else if (field instanceof JComboBox) {
            UIUtils.styleComboBox((JComboBox<?>) field);
        }

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
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

    public void clearForm() {
        fullNameField.setText("");
        fatherNameField.setText("");
        motherNameField.setText("");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        dobSpinner.setValue(calendar.getTime());

        genderCombo.setSelectedIndex(0);
        bloodGroupCombo.setSelectedIndex(0);
        religionField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        divisionCombo.setSelectedIndex(0);
        upazilaField.setText("");
        postalCodeField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        updateDistricts();
    }

    private LocalDate getSelectedDate() {
        java.util.Date date = (java.util.Date) dobSpinner.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    private boolean isUserAtLeast18() {
        LocalDate birthDate = getSelectedDate();
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() >= 18;
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateForm()) {
                return;
            }

            try {
                Citizen citizen = new Citizen(
                        fullNameField.getText().trim(),
                        fatherNameField.getText().trim(),
                        motherNameField.getText().trim(),
                        getSelectedDate(),
                        (String) genderCombo.getSelectedItem(),
                        (String) bloodGroupCombo.getSelectedItem(),
                        religionField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        emailField.getText().trim(),
                        (String) divisionCombo.getSelectedItem(),
                        (String) districtCombo.getSelectedItem(),
                        upazilaField.getText().trim(),
                        postalCodeField.getText().trim(),
                        new String(passwordField.getPassword())
                );

                CitizenDAO citizenDAO = new CitizenDAO();

                if (citizenDAO.isEmailExists(citizen.getEmail())) {
                    UIUtils.showErrorMessage(RegisterForm.this, "Email already registered. Please use a different email.");
                    return;
                }

                if (citizenDAO.isPhoneNumberExists(citizen.getPhoneNumber())) {
                    UIUtils.showErrorMessage(RegisterForm.this, "Phone number already registered. Please use a different phone number.");
                    return;
                }

                if (citizenDAO.registerCitizen(citizen)) {
                    UIUtils.showSuccessMessage(RegisterForm.this,
                            "Registration successful! Your application is pending approval.");
                    clearForm();
                    mainApp.showLoginScreen();
                } else {
                    UIUtils.showErrorMessage(RegisterForm.this, "Registration failed. Please try again.");
                }

            } catch (Exception ex) {
                UIUtils.showErrorMessage(RegisterForm.this, "Error during registration: " + ex.getMessage());
            }
        }

        private boolean validateForm() {

            if (fullNameField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter full name.");
                return false;
            }

            if (fatherNameField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter father's name.");
                return false;
            }

            if (motherNameField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter mother's name.");
                return false;
            }

            if (religionField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter religion.");
                return false;
            }


            if (phoneNumberField.getText().trim().isEmpty() ||
                    !phoneNumberField.getText().trim().matches("\\d{11}")) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter a valid 11-digit phone number.");
                return false;
            }


            if (emailField.getText().trim().isEmpty() ||
                    !emailField.getText().trim().contains("@")) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter a valid email address.");
                return false;
            }


            if (divisionCombo.getSelectedIndex() == -1) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please select a division.");
                return false;
            }

            if (districtCombo.getSelectedItem() == null) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please select a district.");
                return false;
            }

            if (upazilaField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter upazila.");
                return false;
            }

            if (postalCodeField.getText().trim().isEmpty()) {
                UIUtils.showErrorMessage(RegisterForm.this, "Please enter postal code.");
                return false;
            }


            if (!isUserAtLeast18()) {
                UIUtils.showErrorMessage(RegisterForm.this, "You must be at least 18 years old to register.");
                return false;
            }


            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (password.length() < 6) {
                UIUtils.showErrorMessage(RegisterForm.this, "Password must be at least 6 characters long.");
                return false;
            }

            if (!password.equals(confirmPassword)) {
                UIUtils.showErrorMessage(RegisterForm.this, "Passwords do not match.");
                return false;
            }

            return true;
        }
    }
}