package ui.artefact;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import dao.ArtefactDAO;
import model.Artefact;
import service.ArtefactService;

public class AddArtefactForm extends JFrame {

    private JTextField nameField, materialField;
    private JTextArea descriptionArea;
    private JLabel imageLabel;
    private String imagePath;

    private JComboBox<String> categoryBox, periodBox, regionBox;

    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> periodMap = new HashMap<>();
    private Map<String, Integer> regionMap = new HashMap<>();

    // Professional font setup
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 15);

    public AddArtefactForm() {

        setTitle("Add Artefact");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField();
        nameField.setFont(fieldFont);
        nameField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(nameField, gbc);

        // Material
        gbc.gridx = 0; gbc.gridy++;
        JLabel materialLabel = new JLabel("Material:");
        materialLabel.setFont(labelFont);
        formPanel.add(materialLabel, gbc);

        gbc.gridx = 1;
        materialField = new JTextField();
        materialField.setFont(fieldFont);
        materialField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(materialField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy++;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(labelFont);
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(fieldFont);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Category
        gbc.gridx = 0; gbc.gridy++;
        JLabel catLabel = new JLabel("Category:");
        catLabel.setFont(labelFont);
        formPanel.add(catLabel, gbc);

        gbc.gridx = 1;
        categoryBox = new JComboBox<>();
        categoryBox.setFont(fieldFont);
        formPanel.add(categoryBox, gbc);

        // Period
        gbc.gridx = 0; gbc.gridy++;
        JLabel periodLabel = new JLabel("Period:");
        periodLabel.setFont(labelFont);
        formPanel.add(periodLabel, gbc);

        gbc.gridx = 1;
        periodBox = new JComboBox<>();
        periodBox.setFont(fieldFont);
        formPanel.add(periodBox, gbc);

        // Region
        gbc.gridx = 0; gbc.gridy++;
        JLabel regionLabel = new JLabel("Region:");
        regionLabel.setFont(labelFont);
        formPanel.add(regionLabel, gbc);

        gbc.gridx = 1;
        regionBox = new JComboBox<>();
        regionBox.setFont(fieldFont);
        formPanel.add(regionBox, gbc);

        // Image
        gbc.gridx = 0; gbc.gridy++;
        JLabel imgLabel = new JLabel("Image:");
        imgLabel.setFont(labelFont);
        formPanel.add(imgLabel, gbc);

        gbc.gridx = 1;
        JPanel imagePanel = new JPanel(new BorderLayout());

        JButton imageBtn = new JButton("Select Image");
        imageBtn.setFont(buttonFont);

        imageLabel = new JLabel("No file selected");
        imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        imagePanel.add(imageBtn, BorderLayout.WEST);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        formPanel.add(imagePanel, gbc);

        imageBtn.addActionListener(e -> selectImage());

        // Submit Button
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton submitBtn = new JButton("Add Artefact");
        submitBtn.setFont(buttonFont);
        submitBtn.setPreferredSize(new Dimension(180, 40));

        formPanel.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> addArtefact());

        add(formPanel, BorderLayout.CENTER);

        loadDropdownData();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageLabel.setText(fileChooser.getSelectedFile().getName());
        }
    }

    private void loadDropdownData() {
        ArtefactDAO dao = new ArtefactDAO();

        categoryMap = dao.getCategories();
        for (String name : categoryMap.keySet()) {
            categoryBox.addItem(name);
        }

        periodMap = dao.getPeriods();
        for (String name : periodMap.keySet()) {
            periodBox.addItem(name);
        }

        regionMap = dao.getRegions();
        for (String name : regionMap.keySet()) {
            regionBox.addItem(name);
        }
    }

    private void addArtefact() {
        try {
            if (nameField.getText().trim().isEmpty()) {
                throw new RuntimeException("Name cannot be empty");
            }

            if (imagePath == null) {
                throw new RuntimeException("Please select an image");
            }

            Artefact a = new Artefact();

            a.setName(nameField.getText());
            a.setMaterial(materialField.getText());
            a.setDescription(descriptionArea.getText());

            String cat = (String) categoryBox.getSelectedItem();
            String per = (String) periodBox.getSelectedItem();
            String reg = (String) regionBox.getSelectedItem();

            a.setCategory_id(categoryMap.get(cat));
            a.setPeriod_id(periodMap.get(per));
            a.setRegion_id(regionMap.get(reg));
            a.setImagePath(imagePath);

            ArtefactService service = new ArtefactService();
            service.addArtefact(a);

            JOptionPane.showMessageDialog(this, "Artefact added successfully");

            resetForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void resetForm() {
        nameField.setText("");
        materialField.setText("");
        descriptionArea.setText("");
        imageLabel.setText("No file selected");
        imagePath = null;
    }
}