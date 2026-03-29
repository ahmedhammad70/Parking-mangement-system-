package com.parking.view;

import com.parking.data.DataManager;
import com.parking.model.User;
import java.awt.*;
import java.util.List;
import javax.swing.*;


public class UsersPanel extends JPanel {
    private JPanel usersListPanel;

    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color PRIMARY = new Color(33, 150, 243);
    private static final Color DANGER = new Color(244, 67, 54);

    public UsersPanel() {
        initUI();
        refresh();
    }

    private void initUI() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Manage Users");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY);
        add(titleLabel, BorderLayout.NORTH);

        usersListPanel = new JPanel();
        usersListPanel.setLayout(new BoxLayout(usersListPanel, BoxLayout.Y_AXIS));
        usersListPanel.setBackground(CARD_COLOR);
        usersListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(usersListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        
        JButton addBtn = createButton("Add User", PRIMARY);
        addBtn.addActionListener(e -> showAddUserDialog());
        buttonPanel.add(addBtn);

        add(buttonPanel, BorderLayout.SOUTH);
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

    private void showAddUserDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New User", true);
        dialog.setSize(350, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(new JLabel("Role:"));
        String[] roles = {"Admin", "Manager", "Cashier", "Security"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(roleCombo);
        formPanel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(cancelBtn);

        JButton saveBtn = createButton("Save", PRIMARY);
        saveBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DataManager dm = DataManager.getInstance();
            if (dm.findUser(username) != null) {
                JOptionPane.showMessageDialog(dialog, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User(0, username, password, role);
            dm.addUser(user);
            dialog.dispose();
            refresh();
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        btnPanel.add(saveBtn);

        formPanel.add(btnPanel);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public void refresh() {
        usersListPanel.removeAll();
        
        List<User> users = DataManager.getInstance().getUsers();
        User currentUser = DataManager.getInstance().getCurrentUser();

        for (User user : users) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(CARD_COLOR);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            JLabel nameLabel = new JLabel(user.getUsername() + " - " + user.getRole());
            nameLabel.setForeground(TEXT_COLOR);
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            itemPanel.add(nameLabel, BorderLayout.WEST);

            if (!user.getUsername().equals(currentUser.getUsername())) {
                JButton deleteBtn = createButton("Delete", DANGER);
                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Delete user " + user.getUsername() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        DataManager.getInstance().deleteUser(user.getId());
                        refresh();
                    }
                });
                itemPanel.add(deleteBtn, BorderLayout.EAST);
            }

            usersListPanel.add(itemPanel);
        }

        usersListPanel.revalidate();
        usersListPanel.repaint();
    }
}
