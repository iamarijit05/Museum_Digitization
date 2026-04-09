package ui.artefact;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import dao.ArtefactDAO;
import model.Artefact;
import service.ArtefactService;

public class UpdateArtefactForm extends JFrame {
    private JTextField idField, nameField, materialField;
    private JTextArea descriptionArea;
    private JLabel imageLabel;
    private String imagePath;

    private JComboBox<String> categoryBox, periodBox, regionBox;
    
    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> periodMap = new HashMap<>();
    private Map<String, Integer> regionMap = new HashMap<>();
    private Font font = new Font("Segoe UI", Font.PLAIN, 14);

    public UpdateArtefactForm() {
        setTitle("Update Artefact");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //background
        getContentPane().setBackground(new Color(244, 246, 248));

        //title
        JLabel title = new JLabel("Update Artefact", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        
        JPanel panel = new JPanel(new GridBagLayout()); panel.setBackground(Color.WHITE); panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); 
        
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID + Load
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Artefact ID:"), gbc);

        gbc.gridx = 1;
        idField = new JTextField();
        idField.setFont(font);
        panel.add(idField, gbc);

        gbc.gridx = 2;
        JButton loadBtn = new JButton("Load");
        panel.add(loadBtn, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        nameField = new JTextField();
        nameField.setFont(font);
        panel.add(nameField, gbc);

        // Material
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Material:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        materialField = new JTextField();
        materialField.setFont(font);
        panel.add(materialField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(font);
        panel.add(new JScrollPane(descriptionArea), gbc);

        // Category
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        categoryBox = new JComboBox<>();
        panel.add(categoryBox, gbc);

        // Period
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Period:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        periodBox = new JComboBox<>();
        panel.add(periodBox, gbc);

        // Region
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Region:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        regionBox = new JComboBox<>();
        panel.add(regionBox, gbc);

        // Image
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Image:"), gbc);

        gbc.gridx = 1;
        JButton imageBtn = new JButton("Change");
        panel.add(imageBtn, gbc);

        gbc.gridx = 2;
        imageLabel = new JLabel("No file");
        panel.add(imageLabel, gbc);

        // Update Button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton updateBtn = new JButton("Update Artefact");
        updateBtn.setBackground(new Color(39, 174, 96));
        updateBtn.setForeground(Color.WHITE);

        panel.add(updateBtn, gbc);

        add(panel, BorderLayout.CENTER);

        loadDropdownData();

        // Actions
        loadBtn.addActionListener(e -> loadArtefact());
        imageBtn.addActionListener(e -> selectImage());
        updateBtn.addActionListener(e -> updateArtefact());

        setVisible(true);
    }

    private void loadDropdownData() {
        ArtefactDAO dao = new ArtefactDAO();
        categoryMap = dao.getCategories();
    
        for (String name : categoryMap.keySet()) categoryBox.addItem(name);

        periodMap = dao.getPeriods();
        for (String name : periodMap.keySet()) periodBox.addItem(name);

        regionMap = dao.getRegions();
        for (String name : regionMap.keySet()) regionBox.addItem(name);
    }

    private void loadArtefact() {
        try {
            int id = Integer.parseInt(idField.getText());
            Artefact a = new ArtefactDAO().getArtefactById(id);
            if(a == null) {
                JOptionPane.showMessageDialog(this, "Artefaact not Found");
                return;
            }
            nameField.setText(a.getName());
            materialField.setText(a.getMaterial());
            descriptionArea.setText(a.getDescription());
            imagePath = a.getImagePath();
            imageLabel.setText("Loaded");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID");
        }
    }

    private void selectImage() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagePath = fc.getSelectedFile().getAbsolutePath();
            imageLabel.setText(fc.getSelectedFile().getName());
        }
    }

    private void updateArtefact() {
        try {
            int id = Integer.parseInt(idField.getText());
            Artefact a = new Artefact();
            a.setArtefactId(id);
            a.setName(nameField.getText());
            a.setMaterial(materialField.getText());
            a.setDescription(descriptionArea.getText());
            a.setImagePath(imagePath);

            String cat = (String) categoryBox.getSelectedItem();
            String per = (String) periodBox.getSelectedItem();
            String reg = (String) regionBox.getSelectedItem();

            a.setCategory_id(categoryMap.get(cat));
            a.setPeriod_id(periodMap.get(per));
            a.setRegion_id(regionMap.get(reg));

            new ArtefactService().updateArtefact(a);

            JOptionPane.showMessageDialog(this, "Updated Successfully");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
