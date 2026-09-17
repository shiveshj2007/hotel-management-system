package hotelmanagement;

import hotelmanagement.gui.MainFrame;
import hotelmanagement.manager.*;
import hotelmanagement.storage.FileManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        FileManager fileManager = new FileManager();
        GuestManager guestManager = new GuestManager(fileManager);
        RoomManager roomManager = new RoomManager(fileManager);
        ReservationManager reservationManager = new ReservationManager(fileManager, roomManager, guestManager);
        BillingManager billingManager = new BillingManager(fileManager);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(roomManager, guestManager, reservationManager, billingManager);
            mainFrame.setVisible(true);
        });
    }
}
