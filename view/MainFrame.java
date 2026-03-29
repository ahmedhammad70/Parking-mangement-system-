package com.parking.view;

import com.parking.data.DataManager;
import com.parking.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main Frame with tabbed interface (Light Theme)
 */
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private DashboardPanel dashboardPanel;
    private UsersPanel usersPanel;
    private ReportsPanel reportsPanel;
    private SettingsPanel settingsPanel;
    private JPanel backupsPanel;

    private JLabel footerActiveLabel;
    private JLabel footerGateLabel;
    private JLabel footerUserLabel;
    private JLabel dateTimeLabel;
    private JLabel bannerLabel;
    private Timer dateTimeTimer;

    // Light theme colors
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color HEADER_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color TEXT_MUTED = new Color(128, 128, 128);
    private static final Color PRIMARY = new Color(33, 150, 243);
    private static final Color DANGER = new Color(244, 67, 54);

    public MainFrame() {
        initUI();
        applyPermissions();
        startDateTimeTimer();
    }

    private void initUI() {
        DataManager dm = DataManager.getInstance();
        String companyName = dm.getSettings().getCompanyName();

        setTitle(companyName + " - Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(HEADER_COLOR);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(HEADER_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Create panels
        dashboardPanel = new DashboardPanel();
        usersPanel = new UsersPanel();
        reportsPanel = new ReportsPanel();
        settingsPanel = new SettingsPanel();
        backupsPanel = createBackupsPanel();

        tabbedPane.addTab("Dashboard", dashboardPanel);
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Reports", reportsPanel);
        tabbedPane.addTab("Settings", settingsPanel);
        tabbedPane.addTab("Backups", backupsPanel);

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            String tabName = tabbedPane.getTitleAt(index);
            if (!dm.hasPermission(tabName)) {
                JOptionPane.showMessageDialog(this,
                        "Access Denied! You don't have permission to access " + tabName,
                        "Permission Denied", JOptionPane.WARNING_MESSAGE);
                tabbedPane.setSelectedIndex(0);
            } else {
                refreshCurrentTab();
            }
        });

        // Right side buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rightPanel.setBackground(HEADER_COLOR);

        dateTimeLabel = new JLabel();
        dateTimeLabel.setForeground(TEXT_MUTED);
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rightPanel.add(dateTimeLabel);

        JButton refreshBtn = createButton("Refresh", new Color(108, 117, 125));
        refreshBtn.addActionListener(e -> refreshCurrentTab());
        rightPanel.add(refreshBtn);

        JButton logoutBtn = createButton("Logout", DANGER);
        logoutBtn.addActionListener(e -> handleLogout());
        rightPanel.add(logoutBtn);

        topBar.add(tabbedPane, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(BG_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel headerLabel = new JLabel(companyName);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY);
        header.add(headerLabel, BorderLayout.WEST);

        bannerLabel = new JLabel();
        bannerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(bannerLabel, BorderLayout.EAST);

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 8));
        footer.setBackground(HEADER_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        footerActiveLabel = new JLabel("Active: 0");
        footerActiveLabel.setForeground(TEXT_MUTED);
        footer.add(footerActiveLabel);

        footerGateLabel = new JLabel("Gate: CLOSED");
        footerGateLabel.setForeground(TEXT_MUTED);
        footer.add(footerGateLabel);

        User user = dm.getCurrentUser();
        footerUserLabel = new JLabel(user.getUsername() + " (" + user.getRole() + ")");
        footerUserLabel.setForeground(TEXT_MUTED);
        footer.add(footerUserLabel);

        JLabel poweredBy = new JLabel("Powered by MEV Systems");
        poweredBy.setForeground(PRIMARY);
        footer.add(Box.createHorizontalGlue());
        footer.add(poweredBy);

        // Content wrapper
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.add(header, BorderLayout.NORTH);
        contentWrapper.add(tabbedPane, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(contentWrapper, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        updateFooter();
        updateBanner();
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createBackupsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Backups");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        JLabel msgLabel = new JLabel("Backup functionality coming soon...");
        msgLabel.setForeground(TEXT_MUTED);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(msgLabel);
        panel.add(Box.createVerticalStrut(20));

        JButton createBackupBtn = new JButton("Create Backup");
        createBackupBtn.setEnabled(false);
        panel.add(createBackupBtn);

        return panel;
    }

    private void applyPermissions() {
        DataManager dm = DataManager.getInstance();
        List<String> allowed = dm.getAllowedTabs();

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String tabName = tabbedPane.getTitleAt(i);
            if (!allowed.contains(tabName)) {
                tabbedPane.setEnabledAt(i, false);
            }
        }
    }

    private void refreshCurrentTab() {
        int index = tabbedPane.getSelectedIndex();
        switch (index) {
            case 0:
                dashboardPanel.refresh();
                break;
            case 1:
                usersPanel.refresh();
                break;
            case 2:
                reportsPanel.refresh();
                break;
            case 3:
                settingsPanel.refresh();
                break;
        }
        updateFooter();
        updateBanner();
    }

    public void updateFooter() {
        DataManager dm = DataManager.getInstance();
        footerActiveLabel.setText("Active: " + dm.getTotalParked());
        footerGateLabel.setText("Gate: " + (dm.isGateOpen() ? "OPEN" : "CLOSED"));
    }

    private void startDateTimeTimer() {
        dateTimeTimer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        });
        dateTimeTimer.start();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dateTimeTimer != null)
                dateTimeTimer.stop();
            DataManager.getInstance().logout();

            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            });
        }
    }

    private void updateBanner() {
        DataManager dm = DataManager.getInstance();
        String path = dm.getSettings().getBannerPath();
        if (path != null && !path.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0) {
                    // Scaling for the title section
                    // Size requested 3650*823
                    int targetHeight = 50; // Constrain height for title section
                    int targetWidth = (int) (targetHeight * (3650.0 / 823.0));
                    Image scaled = icon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                    bannerLabel.setIcon(new ImageIcon(scaled));
                    bannerLabel.setVisible(true);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bannerLabel.setVisible(false);
    }
}
