package final_proj;

import javax.swing.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InventoryManagementSystem {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private DataManager dataManager;
    private SecurityAndPermissions securityAndPermissions;
    private OrderFulfillmentAndReplenishment orderFulfillmentAndReplenishment;
    private NotificationsAndAlerts notificationsAndAlerts;

    public InventoryManagementSystem() {
        dataManager = new DataManager();
        securityAndPermissions = new SecurityAndPermissions(dataManager.getUserList().stream()
                .map(user -> new SecurityAndPermissions.EnhancedUser(user.getUsername(), new ArrayList<>(),
                        new ArrayList<>()))
                .collect(Collectors.toList()));
        orderFulfillmentAndReplenishment = new OrderFulfillmentAndReplenishment(dataManager);
        notificationsAndAlerts = new NotificationsAndAlerts();
    }

    public void run() {
        frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

    
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");

        openItem.addActionListener(e -> {
            dataManager.loadData();
            JOptionPane.showMessageDialog(frame, "Data loaded successfully.");
        });

        saveItem.addActionListener(e -> {
            dataManager.saveData();
            JOptionPane.showMessageDialog(frame, "Data saved successfully.");
        });

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);

 
        tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);

        tabbedPane.addTab("User Management", new UserManagement(dataManager.getUserList().stream()
                .map(user -> new SecurityAndPermissions.EnhancedUser(user.getUsername(), new ArrayList<>(),
                        new ArrayList<>()))
                .collect(Collectors.toList())));
        tabbedPane.addTab("Product Management", new ProductManagement(dataManager.getProductList()));
        tabbedPane.addTab("Search and Reporting", new InventorySearchAndReporting());
        tabbedPane.addTab("Notifications and Alerts", notificationsAndAlerts);
        tabbedPane.addTab("Order Fulfillment and Replenishment", orderFulfillmentAndReplenishment);
        tabbedPane.addTab("Security and Permissions", securityAndPermissions);

        tabbedPane.addChangeListener(e -> {
            int tabIndex = tabbedPane.getSelectedIndex();
            if (tabIndex == 0) {
                System.out.println("User Management tab selected.");
            } else if (tabIndex == 1) {
                System.out.println("Product Management tab selected.");
            } else if (tabIndex == 2) {
                System.out.println("Search and Reporting tab selected.");
            } else if (tabIndex == 3) {
                System.out.println("Notifications and Alerts tab selected.");
            } else if (tabIndex == 4) {
                System.out.println("Order Fulfillment and Replenishment tab selected.");
            } else if (tabIndex == 5) {
                System.out.println("Security and Permissions tab selected.");
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new InventoryManagementSystem().run();
        });
    }
}
