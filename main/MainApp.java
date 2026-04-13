package main;

import javax.swing.SwingUtilities;
import ui.auth.LoginPage;
import ui.publicview.*;
public class MainApp {

    public static void main(String[] args) {

        // Always run Swing UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            //new PublicView();
            new LoginPage();
        });

    }
}