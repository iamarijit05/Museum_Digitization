package ui.dashboard;

import javax.swing.*;
import java.awt.*;

import ui.artefact.AddArtefactForm;
import ui.artefact.ArtefactListView;
import ui.artefact.DeleteArtefactForm;
import ui.artefact.UpdateArtefactForm;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Admin Dashboard");
        setSize(550, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main background color
        Color bgColor = new Color(244, 246, 248);
        getContentPane().setBackground(bgColor);

        // Title
        JLabel title = new JLabel("Museum Admin Dashboard", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(44, 62, 80));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(title, BorderLayout.NORTH);

        // Button panel
        JPanel panel = new JPanel(new GridLayout(2, 2, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(bgColor);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);

        Color btnColor = new Color(44, 62, 80); // dark blue
        Color textColor = Color.WHITE;

        // Buttons
        JButton addBtn = createStyledButton("Add Artefact", btnFont, btnColor, textColor);
        JButton viewBtn = createStyledButton("View Artefacts", btnFont, btnColor, textColor);
        JButton updateBtn = createStyledButton("Update Artefact", btnFont, btnColor, textColor);
        JButton deleteBtn = createStyledButton("Delete Artefact", btnFont, btnColor, textColor);

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);

        add(panel, BorderLayout.CENTER);

        // Actions
        addBtn.addActionListener(e -> new AddArtefactForm());

        viewBtn.addActionListener(e -> new ArtefactListView());

        updateBtn.addActionListener(e -> new UpdateArtefactForm());

        deleteBtn.addActionListener(e -> new DeleteArtefactForm());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Reusable styled button method
    private JButton createStyledButton(String text, Font font, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }

}