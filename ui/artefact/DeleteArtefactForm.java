package ui.artefact;

import javax.swing.*;
import java.awt.*;
import service.ArtefactService;

public class DeleteArtefactForm extends JFrame {

    private JTextField idField;
    private JLabel statusLabel;

    // Fonts
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
    private Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public DeleteArtefactForm() {

        setTitle("Delete Artefact");
        setSize(400, 270);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bgColor = new Color(244, 246, 248);
        getContentPane().setBackground(bgColor);

        // Title
        JLabel title = new JLabel("Delete Artefact", JLabel.CENTER);
        title.setFont(titleFont);
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Form panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Artefact ID:");
        idLabel.setFont(labelFont);
        panel.add(idLabel, gbc);

        // Field
        gbc.gridx = 1;
        idField = new JTextField();
        idField.setFont(fieldFont);
        idField.setPreferredSize(new Dimension(150, 30));
        panel.add(idField, gbc);

        // Button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(buttonFont);
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setPreferredSize(new Dimension(120, 35));

        panel.add(deleteBtn, gbc);

        // STATUS LABEL 
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;

        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panel.add(statusLabel, gbc);

        add(panel, BorderLayout.CENTER);

        // Action
        deleteBtn.addActionListener(e -> deleteArtefact());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void deleteArtefact() {
        try {
            if (idField.getText().trim().isEmpty()) {
                throw new RuntimeException("ID cannot be empty");
            }

            int id = Integer.parseInt(idField.getText());

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this artefact?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            ArtefactService service = new ArtefactService();

            //  This will throw error if not found (based on DAO fix)
            service.deleteArtefact(id);

            // SUCCESS
            statusLabel.setText("Artefact deleted successfully");
            statusLabel.setForeground(new Color(39, 174, 96));

            idField.setText("");

        } catch (NumberFormatException e) {
            statusLabel.setText("ID must be a number");
            statusLabel.setForeground(Color.RED);

        } catch (Exception e) {
            // THIS WILL SHOW "Artefact not found"
            statusLabel.setText(e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
}