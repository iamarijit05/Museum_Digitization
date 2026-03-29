package ui.publicview;
import java.awt.FlowLayout;

import javax.swing.*;

import ui.artefact.ArtefactListView;

public class PublicView extends JFrame {
    public PublicView() {
        setTitle("Public View");
        setSize(400, 300);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Welcome! Click to Enter Museum");
        JButton viewBtn = new JButton("View");
        viewBtn.addActionListener(e -> new ArtefactListView());

        add(label);
        add(viewBtn);

        setVisible(true);
    }
}
