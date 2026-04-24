package ui.artefact;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import model.Artefact;
import service.ArtefactService;

public class ArtefactListView extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ArtefactListView() {
        setTitle("Artefact List");
        setSize(800, 400);
        setLayout(new BorderLayout());

        // Column names
        String[] columns = {
                "ID", "Name", "Material", "Description",
                "Category ID", "Period ID", "Region ID"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();  //load data from DB

        setVisible(true);
    }

    private void loadData() {
        try {
            ArtefactService service = new ArtefactService();
            List<Artefact> list = service.getAllArtefacts();

            // clear existing rows (important if reloaded)
            model.setRowCount(0);

            for (Artefact a : list) {
                Object[] row = {
                        a.getArtefactId(),
                        a.getName(),
                        a.getMaterial(),
                        a.getDescription(),
                        a.getCategory_id(),
                        a.getPeriod_id(),
                        a.getRegion_id()
                };

                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}