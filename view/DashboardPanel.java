package com.parking.view;

import com.parking.data.DataManager;
import com.parking.model.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.List;

/**
 * Dashboard Panel - Main parking operations (Light Theme)
 */
public class DashboardPanel extends JPanel {
    private JLabel staffCountLabel;
    private JLabel studentCountLabel;
    private JLabel remainingLabel;
    private JLabel gateStatusLabel;
    private JTextField entryPlateField;
    private JTextField entryNameField;
    private JTextField entryIdField;
    private String selectedPlate;
    private JToggleButton vipButton;
    private JToggleButton regularButton;
    private DefaultTableModel vehiclesTableModel;
    private JTextField searchField;
    // private Timer gateTimer;

    // Light theme colors
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color TEXT_MUTED = new Color(128, 128, 128);
    private static final Color PRIMARY = new Color(33, 150, 243);
    private static final Color SUCCESS = new Color(76, 175, 80);
    private static final Color DANGER = new Color(244, 67, 54);

    public DashboardPanel() {
        initUI();
        refresh();
    }

    private void initUI() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top section: Stats & Gate
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        topPanel.setOpaque(false);
        topPanel.add(createStatsPanel());
        topPanel.add(createGatePanel());
        add(topPanel, BorderLayout.NORTH);

        // Middle section: Entry/Exit forms & Active vehicles
        JPanel middlePanel = new JPanel(new BorderLayout(15, 15));
        middlePanel.setOpaque(false);

        // Forms panel
        JPanel formsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        formsPanel.setOpaque(false);
        formsPanel.add(createEntryPanel());
        formsPanel.add(createExitPanel());

        middlePanel.add(formsPanel, BorderLayout.NORTH);

        // Vehicles table
        middlePanel.add(createVehiclesTablePanel(), BorderLayout.CENTER);

        add(middlePanel, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel panel = createCard("Occupancy");
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(createLabel("Staff In:", false));
        staffCountLabel = createLabel("0", true);
        staffCountLabel.setForeground(PRIMARY);
        panel.add(staffCountLabel);

        panel.add(createLabel("Student In:", false));
        studentCountLabel = createLabel("0", true);
        studentCountLabel.setForeground(PRIMARY);
        panel.add(studentCountLabel);

        panel.add(createLabel("Remaining:", false));
        remainingLabel = createLabel("100", true);
        remainingLabel.setForeground(SUCCESS);
        panel.add(remainingLabel);

        return panel;
    }

    private JPanel createGatePanel() {
        JPanel panel = createCard("Gate Control");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(CARD_COLOR);
        JLabel statusLabel = new JLabel("Status: ");
        statusLabel.setForeground(TEXT_COLOR);
        statusPanel.add(statusLabel);
        gateStatusLabel = new JLabel("CLOSED");
        gateStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gateStatusLabel.setForeground(DANGER);
        statusPanel.add(gateStatusLabel);
        panel.add(statusPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(CARD_COLOR);

        JButton openBtn = createButton("Open Gate", SUCCESS);
        openBtn.addActionListener(e -> setGate(true));
        buttonsPanel.add(openBtn);

        JButton closeBtn = createButton("Close Gate", DANGER);
        closeBtn.addActionListener(e -> setGate(false));
        buttonsPanel.add(closeBtn);

        panel.add(buttonsPanel);

        // JLabel hint = new JLabel("Gate auto-closes after 10s");
        // hint.setForeground(TEXT_MUTED);
        // hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        // hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        // panel.add(hint);

        return panel;
    }

    private JPanel createEntryPanel() {
        JPanel panel = createCard("New Entry");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Plate input
        JPanel platePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        platePanel.setBackground(CARD_COLOR);
        JLabel plateLabel = new JLabel("Plate: ");
        plateLabel.setForeground(TEXT_COLOR);
        platePanel.add(plateLabel);
        entryPlateField = new JTextField(15);
        platePanel.add(entryPlateField);
        panel.add(platePanel);

        // Name input
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        namePanel.setBackground(CARD_COLOR);
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(TEXT_COLOR);
        namePanel.add(nameLabel);
        entryNameField = new JTextField(15);
        namePanel.add(entryNameField);
        panel.add(namePanel);

        // ID input
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        idPanel.setBackground(CARD_COLOR);
        JLabel idLabel = new JLabel("ID:      ");
        idLabel.setForeground(TEXT_COLOR);
        idPanel.add(idLabel);
        entryIdField = new JTextField(15);
        idPanel.add(entryIdField);
        panel.add(idPanel);

        // Type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        typePanel.setBackground(CARD_COLOR);
        JLabel typeLabel = new JLabel("Type: ");
        typeLabel.setForeground(TEXT_COLOR);
        typePanel.add(typeLabel);

        ButtonGroup typeGroup = new ButtonGroup();
        vipButton = new JToggleButton("Staff");
        regularButton = new JToggleButton("Student");
        vipButton.setSelected(true);
        styleToggle(vipButton, true);
        styleToggle(regularButton, false);

        vipButton.addActionListener(e -> {
            styleToggle(vipButton, true);
            styleToggle(regularButton, false);
        });
        regularButton.addActionListener(e -> {
            styleToggle(vipButton, false);
            styleToggle(regularButton, true);
        });

        typeGroup.add(vipButton);
        typeGroup.add(regularButton);
        typePanel.add(vipButton);
        typePanel.add(Box.createHorizontalStrut(10));
        typePanel.add(regularButton);
        panel.add(typePanel);

        // Record button
        JButton recordBtn = createButton("Record Entry", PRIMARY);
        recordBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        recordBtn.addActionListener(e -> handleEntry());
        panel.add(Box.createVerticalStrut(10));
        panel.add(recordBtn);

        return panel;
    }

    private JPanel createExitPanel() {
        JPanel panel = createCard("Record Exit");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Record button
        JButton recordBtn = createButton("Record Exit & Calculate Fee", DANGER);
        recordBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        recordBtn.addActionListener(e -> handleExit());
        panel.add(Box.createVerticalStrut(10));
        panel.add(recordBtn);

        return panel;
    }

    private JPanel createVehiclesTablePanel() {
        JPanel panel = createCard("Active Vehicles");
        panel.setLayout(new BorderLayout());

        String[] columns = { "#", "Code", "Plate", "Name", "ID", "Type", "Entry Time", "Attendant" };
        vehiclesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(vehiclesTableModel);
        table.setBackground(CARD_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(CARD_COLOR);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                refresh();
            }

            public void removeUpdate(DocumentEvent e) {
                refresh();
            }

            public void insertUpdate(DocumentEvent e) {
                refresh();
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String plate = (String) table.getValueAt(row, 2);
                    selectedPlate = plate;
                } else {
                    selectedPlate = null;
                }
            }
        });

        return panel;
    }

    private JPanel createCard(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        title,
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        PRIMARY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return panel;
    }

    private JLabel createLabel(String text, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setForeground(isBold ? TEXT_COLOR : TEXT_MUTED);
        label.setFont(new Font("Segoe UI", isBold ? Font.BOLD : Font.PLAIN, isBold ? 20 : 13));
        return label;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 35));
        return btn;
    }

    private void styleToggle(JToggleButton btn, boolean selected) {
        if (selected) {
            btn.setBackground(PRIMARY);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(230, 230, 230));
            btn.setForeground(TEXT_COLOR);
        }
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
    }

    private void handleEntry() {
        String plate = entryPlateField.getText().trim().toUpperCase();
        if (plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a plate number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataManager dm = DataManager.getInstance();

        if (dm.findVehicle(plate) != null) {
            JOptionPane.showMessageDialog(this, "Vehicle already in parking!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dm.getRemainingSpots() <= 0) {
            JOptionPane.showMessageDialog(this, "Parking lot is FULL!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = entryNameField.getText().trim();
        String id = entryIdField.getText().trim();

        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Name and ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = vipButton.isSelected() ? "Staff" : "Student";
        Vehicle vehicle = new Vehicle(plate, type, name, id, dm.getCurrentUser().getUsername());
        dm.addVehicle(vehicle);

        entryPlateField.setText("");
        entryNameField.setText("");
        entryIdField.setText("");
        refresh();

        JOptionPane.showMessageDialog(this, "Vehicle " + plate + " checked in!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleExit() {
        if (selectedPlate == null) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle from the table!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataManager dm = DataManager.getInstance();
        Vehicle vehicle = dm.findVehicle(selectedPlate);

        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate fee
        Settings settings = dm.getSettings();
        double rate = settings.getRate(vehicle.getType());
        double fee = vehicle.calculateFee(rate);
        long hours = vehicle.getHoursParked();

        // Remove and add to history
        dm.removeVehicle(selectedPlate);
        HistoryRecord record = new HistoryRecord(vehicle, fee, dm.getCurrentUser().getUsername());
        dm.addHistoryRecord(record);

        refresh();

        String message = String.format("Vehicle %s (%s - %s) checked out!\nDuration: %d hour(s)\nFee: %.2f %s",
                selectedPlate, vehicle.getOwnerName(), vehicle.getOwnerId(), hours, fee, settings.getCurrency());
        JOptionPane.showMessageDialog(this, message, "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);

        selectedPlate = null;
    }

    private void setGate(boolean open) {
        DataManager dm = DataManager.getInstance();
        dm.setGateOpen(open);
        updateGateStatus();
    }

    private void updateGateStatus() {
        boolean isOpen = DataManager.getInstance().isGateOpen();
        gateStatusLabel.setText(isOpen ? "OPEN" : "CLOSED");
        gateStatusLabel.setForeground(isOpen ? SUCCESS : DANGER);
    }

    public void refresh() {
        DataManager dm = DataManager.getInstance();

        staffCountLabel.setText(String.valueOf(dm.getStaffCount()));
        studentCountLabel.setText(String.valueOf(dm.getStudentCount()));
        remainingLabel.setText(String.valueOf(dm.getRemainingSpots()));

        int remaining = dm.getRemainingSpots();
        if (remaining == 0) {
            remainingLabel.setForeground(DANGER);
        } else if (remaining < 20) {
            remainingLabel.setForeground(new Color(255, 152, 0));
        } else {
            remainingLabel.setForeground(SUCCESS);
        }

        updateGateStatus();

        vehiclesTableModel.setRowCount(0);
        List<Vehicle> vehicles = dm.getVehicles();
        String searchText = searchField != null ? searchField.getText().toLowerCase().trim() : "";

        int i = 1;
        for (Vehicle v : vehicles) {
            boolean matches = searchText.isEmpty() ||
                    v.getPlate().toLowerCase().contains(searchText) ||
                    v.getOwnerName().toLowerCase().contains(searchText) ||
                    v.getOwnerId().toLowerCase().contains(searchText) ||
                    v.getCode().toLowerCase().contains(searchText);

            if (matches) {
                vehiclesTableModel.addRow(new Object[] {
                        i++, v.getCode(), v.getPlate(), v.getOwnerName(), v.getOwnerId(), v.getType(),
                        v.getFormattedEntryTime(), v.getAttendant()
                });
            }
        }
    }
}
