package ui;

import javax.swing.*;
import java.awt.*;

public class NIDAppGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginForm loginForm;

    public NIDAppGUI() {
        setAppIcon();
        initializeUI();
    }

    private void setAppIcon() {
        try {

            ImageIcon icon = new ImageIcon("resources/logo.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());

        }
    }

    private void initializeUI() {
        setTitle("NID Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1000, 700));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(util.UIUtils.getBackgroundColor());

        loginForm = new LoginForm(this);
        RegisterForm registerForm = new RegisterForm(this);

        mainPanel.add(loginForm, "LOGIN");
        mainPanel.add(registerForm, "REGISTER");

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);

        showLoginScreen();
    }

    public void showLoginScreen() {
        loginForm.clearForm();
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showRegisterScreen() {
        cardLayout.show(mainPanel, "REGISTER");
    }

    public void showAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard(this);
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        cardLayout.show(mainPanel, "ADMIN_DASHBOARD");
    }

    public void showUserDashboard(int citizenId) {
        UserDashboard userDashboard = new UserDashboard(this, citizenId);
        mainPanel.add(userDashboard, "USER_DASHBOARD_" + citizenId);
        cardLayout.show(mainPanel, "USER_DASHBOARD_" + citizenId);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {

                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            new NIDAppGUI().setVisible(true);
        });
    }
}