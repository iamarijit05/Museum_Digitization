package ui.auth;
import java.awt.GridLayout;

import javax.swing.*;
import model.UserRole;
import util.Session;
import ui.dashboard.Dashboard;
import ui.publicview.PublicView;

public class LoginPage extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Username: "));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password: "));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(e -> handleLogin());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(username.equals("admin") && password.equals("admin123")) {
            Session.setRole(UserRole.ADMIN);
            JOptionPane.showMessageDialog(this, "Admin Login Successful");
            new Dashboard(); // redirect to admin dashboard
            dispose();
        } else {
            Session.setRole(UserRole.PUBLIC);
            JOptionPane.showMessageDialog(this, "Public Access");
            new PublicView();
            dispose();
        }
    }
}
