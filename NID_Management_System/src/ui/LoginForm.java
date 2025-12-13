package ui;

import dao.AdminDAO;
import dao.CitizenDAO;
import model.Admin;
import model.Citizen;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JPanel {
    private NIDAppGUI mainApp;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private JButton registerButton;

    public LoginForm(NIDAppGUI mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.getBackgroundColor());

        add(UIUtils.createHeaderPanel("NID Management System"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIUtils.getBackgroundColor());

        JPanel contentCard = new JPanel(new GridBagLayout());
        UIUtils.styleCardPanel(contentCard);
        contentCard.setPreferredSize(new Dimension(400, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Login to Your Account", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.FONT_SUBTITLE);
        titleLabel.setForeground(UIUtils.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentCard.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userTypeLabel = UIUtils.createFormLabel("User Type:");
        contentCard.add(userTypeLabel, gbc);

        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Citizen", "Admin"});
        UIUtils.styleComboBox(userTypeCombo);
        contentCard.add(userTypeCombo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel emailLabel = UIUtils.createFormLabel("Email/Username:");
        contentCard.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        UIUtils.styleTextField(emailField);
        contentCard.add(emailField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel passwordLabel = UIUtils.createFormLabel("Password:");
        contentCard.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        UIUtils.stylePasswordField(passwordField);
        contentCard.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 10, 20);
        loginButton = new JButton("Login");

        loginButton.setBackground(new Color(66, 103, 178));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(UIUtils.createHoverEffect(loginButton,
                new Color(53, 90, 160), new Color(66, 103, 178)));
        loginButton.addActionListener(new LoginButtonListener());
        contentCard.add(loginButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 10, 20);
        JSeparator separator = new JSeparator();
        separator.setForeground(UIUtils.BORDER_COLOR);
        contentCard.add(separator, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(10, 20, 20, 20);
        registerButton = new JButton("Register");

        registerButton.setBackground(new Color(72, 181, 62));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(UIUtils.createHoverEffect(registerButton,
                new Color(64, 160, 55), new Color(72, 181, 62)));
        registerButton.addActionListener(e -> mainApp.showRegisterScreen());
        contentCard.add(registerButton, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 20, 10, 20);
        JLabel footerLabel = new JLabel("Secure NID Management System © 2025", SwingConstants.CENTER);
        footerLabel.setFont(UIUtils.FONT_SMALL);
        footerLabel.setForeground(UIUtils.TEXT_SECONDARY);
        contentCard.add(footerLabel, gbc);

        centerPanel.add(contentCard);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void clearForm() {
        emailField.setText("");
        passwordField.setText("");
        userTypeCombo.setSelectedIndex(0);
        emailField.requestFocus();
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userType = (String) userTypeCombo.getSelectedItem();
            String username = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                UIUtils.showErrorMessage(LoginForm.this, "Please fill in all fields.");
                return;
            }

            if ("Citizen".equals(userType)) {
                CitizenDAO citizenDAO = new CitizenDAO();
                Citizen citizen = citizenDAO.loginCitizen(username, password);

                if (citizen != null) {
                    UIUtils.showSuccessMessage(LoginForm.this, "Login successful! Welcome " + citizen.getFullName());
                    mainApp.showUserDashboard(citizen.getCitizenId());
                } else {
                    UIUtils.showErrorMessage(LoginForm.this, "Invalid email or password.");
                }
            } else {
                AdminDAO adminDAO = new AdminDAO();
                Admin admin = adminDAO.loginAdmin(username, password);

                if (admin != null) {
                    UIUtils.showSuccessMessage(LoginForm.this, "Admin login successful! Welcome " + admin.getUsername());
                    mainApp.showAdminDashboard();
                } else {
                    UIUtils.showErrorMessage(LoginForm.this, "Invalid admin credentials.");
                }
            }
        }
    }
}