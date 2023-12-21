package final_proj;

import javax.swing.*;
import java.awt.*;

public class NotificationsAndAlerts extends JPanel {
    public NotificationsAndAlerts() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBackground(new Color(85, 37, 130));

        JPanel notificationsPanel = new JPanel(new GridBagLayout());
        notificationsPanel.setBackground(new Color(216, 222, 231));
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        JLabel notifs = new JLabel("Notifications and Alerts Section:");
        notifs.setFont(new Font("Cambria", Font.BOLD, 18));
        notificationsPanel.add(notifs, c);

        JTextArea alertsTextArea = new JTextArea(10, 30);
        alertsTextArea.setEditable(false);
        alertsTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        alertsTextArea.setLineWrap(true);
        alertsTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(alertsTextArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 10.0;
        notificationsPanel.add(scrollPane, c);

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(notificationsPanel, c);
    }

    public void displayAlert(String alertMessage) {
        JTextArea alertsTextArea = (JTextArea) ((JScrollPane) getComponent(0)).getViewport().getView();
        alertsTextArea.append(alertMessage + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Notifications and Alerts Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new NotificationsAndAlerts());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
