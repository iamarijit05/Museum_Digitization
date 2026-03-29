package ui.artefact;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.*;

import model.Artefact;
import service.ArtefactService;

public class AddArtefactForm extends JFrame {
    private JTextField nameField, materialField, categoryField, periodField, regionField;

    private JTextArea descriptionArea;
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

        add(new JLabel("Category ID:"));
        categoryField = new JTextField();
        add(categoryField);

        add(new JLabel("Period ID:"));
        periodField = new JTextField();
        add(periodField);

        add(new JLabel("Region ID:"));
        regionField = new JTextField();
        add(regionField);

        JButton submitBtn = new JButton("Add Artefact");
        add(submitBtn);

        submitBtn.addActionListener(e -> addArtefact());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void addArtefact() {
        try {
            if(nameField.getText().trim().isEmpty()) {
                throw new RuntimeException("Name Cannot be Empty");
            }
            Artefact a = new Artefact();

            a.setName(nameField.getText());
            a.setMaterial(materialField.getText());
            a.setDescription(descriptionArea.getText());

            int categoryId = Integer.parseInt(categoryField.getText());
            int periodId = Integer.parseInt(periodField.getText());
            int regionId = Integer.parseInt(regionField.getText());

            a.setCategory_id(categoryId);
            a.setPeriod_id(periodId);
            a.setRegion_id(regionId);

            ArtefactService service = new ArtefactService();
            service.addArtefact(a);

            JOptionPane.showMessageDialog(this, "Artefact added successfully!");

            nameField.setText("");
            materialField.setText("");
            descriptionArea.setText("");
            categoryField.setText("");
            periodField.setText("");
            regionField.setText("");
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Must Be Numbers!");
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }
}
