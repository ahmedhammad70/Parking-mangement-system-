package com.parking.view;

import javax.swing.*;
import java.awt.*;

/**
 * Reports Panel - Placeholder (Light Theme)
 */
public class ReportsPanel extends JPanel {

    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color PRIMARY = new Color(33, 150, 243);
    private static final Color TEXT_MUTED = new Color(128, 128, 128);

    public ReportsPanel() {
        initUI();
    }

    private void initUI() {
        setBackground(BG_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(20));

        JLabel msgLabel = new JLabel("Reports functionality coming soon...");
        msgLabel.setForeground(TEXT_MUTED);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(msgLabel);
        add(Box.createVerticalStrut(20));

        JButton createReportBtn = new JButton("Generate Report");
        createReportBtn.setEnabled(false);
        add(createReportBtn);
    }

    public void refresh() {
        // No-op
    }
}
