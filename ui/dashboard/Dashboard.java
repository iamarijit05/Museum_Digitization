package ui.dashboard;
import java.awt.FlowLayout;

import javax.swing.*;

import ui.artefact.AddArtefactForm;
import ui.artefact.ArtefactListView;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setLayout(new FlowLayout());

        JButton addBtn = new JButton("Add Artefact");
        JButton viewBtn = new JButton("View Artefacts");

        addBtn.addActionListener(e -> new AddArtefactForm());
        viewBtn.addActionListener(e-> new ArtefactListView());

        add(addBtn);
        add(viewBtn);

        setVisible(true);
    }
}
