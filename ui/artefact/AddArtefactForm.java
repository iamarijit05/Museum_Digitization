package ui.artefact;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import dao.ArtefactDAO;
import model.Artefact;
import service.ArtefactService;

public class AddArtefactForm extends JFrame {

    private JTextField nameField, materialField;
    private JTextArea descriptionArea;

    private JComboBox<String> categoryBox, periodBox, regionBox;

    // 🔥 maps to store name → id
    private Map<String, Integer> categoryMap = new HashMap<>();
    private Map<String, Integer> periodMap = new HashMap<>();
    private Map<String, Integer> regionMap = new HashMap<>();

    public AddArtefactForm() {
        setTitle("Add Artefact");
        setSize(400, 450);
        setLayout(new GridLayout(8, 2, 10, 10));

        add(new JLabel("Name: "));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Material: "));
        materialField = new JTextField();
        add(materialField);

        add(new JLabel("Description: "));
        descriptionArea = new JTextArea();
        add(new JScrollPane(descriptionArea));

        // 🔥 CATEGORY DROPDOWN
        add(new JLabel("Category:"));
        categoryBox = new JComboBox<>();
        add(categoryBox);

        // 🔥 PERIOD DROPDOWN
        add(new JLabel("Period:"));
        periodBox = new JComboBox<>();
        add(periodBox);

        // 🔥 REGION DROPDOWN
        add(new JLabel("Region:"));
        regionBox = new JComboBox<>();
        add(regionBox);

        JButton submitBtn = new JButton("Add Artefact");
        add(submitBtn);

        submitBtn.addActionListener(e -> addArtefact());

        loadDropdownData(); // 🔥 important

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // 🔥 LOAD DATA FROM DB
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

    public void addArtefact() {
        try {
            if (nameField.getText().trim().isEmpty()) {
                throw new RuntimeException("Name Cannot be Empty");
            }

            Artefact a = new Artefact();

            a.setName(nameField.getText());
            a.setMaterial(materialField.getText());
            a.setDescription(descriptionArea.getText());

            // 🔥 GET SELECTED VALUES
            String cat = (String) categoryBox.getSelectedItem();
            String per = (String) periodBox.getSelectedItem();
            String reg = (String) regionBox.getSelectedItem();

            int categoryId = categoryMap.get(cat);
            int periodId = periodMap.get(per);
            int regionId = regionMap.get(reg);

            a.setCategory_id(categoryId);
            a.setPeriod_id(periodId);
            a.setRegion_id(regionId);

            ArtefactService service = new ArtefactService();
            service.addArtefact(a);

            JOptionPane.showMessageDialog(this, "Artefact added successfully!");

            // 🔄 reset
            nameField.setText("");
            materialField.setText("");
            descriptionArea.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}