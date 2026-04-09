package ui.auth;

import javax.swing.*;
import java.awt.*;

import model.UserRole;
import util.Session;
import ui.dashboard.Dashboard;
import ui.dashboard.Dashboard;
import ui.publicview.PublicView;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    // Fonts
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public LoginPage() {

        setTitle("Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bgColor = new Color(244, 246, 248);
        getContentPane().setBackground(bgColor);

        // Title
        JLabel title = new JLabel("Museum Login", JLabel.CENTER);
        title.setFont(titleFont);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        add(title, BorderLayout.NORTH);

        // Form Panel (center card)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField();
        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(150, 28));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        formPanel.add(passwordField, gbc);

        // Button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(buttonFont);
        loginBtn.setBackground(new Color(44, 62, 80));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(120, 35));

        formPanel.add(loginBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action
        loginBtn.addActionListener(e -> handleLogin());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {
            Session.setRole(UserRole.ADMIN);
            JOptionPane.showMessageDialog(this, "Admin Login Successful");
            new Dashboard();
            dispose();
        } else {
            Session.setRole(UserRole.PUBLIC);
            JOptionPane.showMessageDialog(this, "Public Access");
            new PublicView();
            dispose();
        }
    }
}