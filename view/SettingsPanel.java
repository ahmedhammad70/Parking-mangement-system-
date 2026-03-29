package com.parking.view;

import com.parking.data.DataManager;
import com.parking.model.Settings;
import javax.swing.*;
import java.awt.*;

/**
 * Settings Panel - System configuration (Light Theme)
 */
public class SettingsPanel extends JPanel {
    private JTextField companyNameField;
    private JSpinner capacitySpinner;
    private JSpinner staffRateSpinner;
    private JSpinner studentRateSpinner;
    private JTextField bannerPathField;
    private JTextField logoPathField;

    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color PRIMARY = new Color(33, 150, 243);

    public SettingsPanel() {
        initUI();
        refresh();
    }

    private void initUI() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Company Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Company/Parking Name:"), gbc);
        gbc.gridx = 1;
        companyNameField = new JTextField(25);
        formPanel.add(companyNameField, gbc);

        // Capacity
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Total Capacity:"), gbc);
        gbc.gridx = 1;
        capacitySpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        formPanel.add(capacitySpinner, gbc);

        // Staff Rate
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Staff Hourly Rate (EGP):"), gbc);
        gbc.gridx = 1;
        staffRateSpinner = new JSpinner(new SpinnerNumberModel(20.0, 1.0, 1000.0, 1.0));
        formPanel.add(staffRateSpinner, gbc);

        // Students Rate
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("Students Hourly Rate (EGP):"), gbc);
        gbc.gridx = 1;
        studentRateSpinner = new JSpinner(new SpinnerNumberModel(10.0, 1.0, 1000.0, 1.0));
        formPanel.add(studentRateSpinner, gbc);

        // Currency (fixed)
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createLabel("Currency:"), gbc);
        gbc.gridx = 1;
        JTextField currencyField = new JTextField("EGP");
        currencyField.setEditable(false);
        currencyField.setBackground(new Color(240, 240, 240));
        formPanel.add(currencyField, gbc);

        // Banner Image
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(createLabel("Banner Image:"), gbc);
        gbc.gridx = 1;
        JPanel bannerPanel = new JPanel(new BorderLayout(5, 0));
        bannerPanel.setOpaque(false);
        bannerPathField = new JTextField(20);
        bannerPathField.setEditable(false);
        JButton browseBtn = new JButton("Browse");
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                bannerPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        bannerPanel.add(bannerPathField, BorderLayout.CENTER);
        bannerPanel.add(browseBtn, BorderLayout.EAST);
        formPanel.add(bannerPanel, gbc);

        // Logo Image
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(createLabel("Logo:"), gbc);
        gbc.gridx = 1;
        JPanel logoPanel = new JPanel(new BorderLayout(5, 0));
        logoPanel.setOpaque(false);
        logoPathField = new JTextField(20);
        logoPathField.setEditable(false);
        JButton logoBrowseBtn = new JButton("Browse");
        logoBrowseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                logoPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        logoPanel.add(logoPathField, BorderLayout.CENTER);
        logoPanel.add(logoBrowseBtn, BorderLayout.EAST);
        formPanel.add(logoPanel, gbc);

        // Save button
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        JButton saveBtn = createButton("Save Settings", PRIMARY);
        saveBtn.addActionListener(e -> saveSettings());
        formPanel.add(saveBtn, gbc);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(formPanel);

        add(wrapperPanel, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        return btn;
    }

    private void saveSettings() {
        DataManager dm = DataManager.getInstance();
        Settings settings = dm.getSettings();

        settings.setCompanyName(companyNameField.getText().trim());
        settings.setCapacity((Integer) capacitySpinner.getValue());
        settings.setStaffRate((Double) staffRateSpinner.getValue());
        settings.setStudentRate((Double) studentRateSpinner.getValue());
        settings.setBannerPath(bannerPathField.getText().trim());
        settings.setLogoPath(logoPathField.getText().trim());

        dm.setSettings(settings);

        JOptionPane.showMessageDialog(this,
                "Settings saved successfully!\nRestart app to see name changes.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        Settings settings = DataManager.getInstance().getSettings();
        companyNameField.setText(settings.getCompanyName());
        capacitySpinner.setValue(settings.getCapacity());
        staffRateSpinner.setValue(settings.getStaffRate());
        studentRateSpinner.setValue(settings.getStudentRate());
        bannerPathField.setText(settings.getBannerPath());
        logoPathField.setText(settings.getLogoPath());
    }
}
