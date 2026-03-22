package com.usermgmt.ui;

import com.usermgmt.dao.UserDAO;
import com.usermgmt.model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class UserFormDialog extends JDialog {

    private static final Color BG_DARK    = new Color(13, 15, 20);
    private static final Color BG_CARD    = new Color(19, 23, 32);
    private static final Color BG_INPUT   = new Color(26, 31, 46);
    private static final Color ACCENT     = new Color(79, 140, 255);
    private static final Color TEXT_WHITE = new Color(232, 236, 244);
    private static final Color TEXT_GREY  = new Color(139, 149, 176);
    private static final Color BORDER_COL = new Color(37, 44, 61);
    private static final Color RED        = new Color(239, 68, 68);

    private JTextField nameField, emailField, phoneField, deptField, roleField;
    private boolean success = false;
    private UserDAO userDAO;
    private User existingUser;

    public UserFormDialog(Frame parent, String title, User user, UserDAO dao) {
        super(parent, title, true);
        this.userDAO = dao;
        this.existingUser = user;

        setSize(500, 460);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildHeader(title), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        if (user != null) populateFields(user);
    }

    private JPanel buildHeader(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(16, 20, 16, 20)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setForeground(TEXT_WHITE);
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_DARK);
        form.setBorder(new EmptyBorder(20, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        nameField  = createInput("e.g. Arjun Sharma");
        emailField = createInput("e.g. arjun@gmail.com");
        phoneField = createInput("10-digit number");
        deptField  = createInput("e.g. Engineering");
        roleField  = createInput("e.g. Developer");

        // Row 0 — Name + Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        form.add(buildFieldBox("Full Name *", nameField), gbc);
        gbc.gridx = 1;
        form.add(buildFieldBox("Email Address *", emailField), gbc);

        // Row 1 — Phone + Department
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(buildFieldBox("Phone Number *", phoneField), gbc);
        gbc.gridx = 1;
        form.add(buildFieldBox("Department", deptField), gbc);

        // Row 2 — Role full width
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(buildFieldBox("Role / Designation", roleField), gbc);

        return form;
    }

    private JPanel buildFieldBox(String label, JTextField field) {
        JPanel box = new JPanel(new BorderLayout(0, 5));
        box.setOpaque(false);
        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(TEXT_GREY);
        box.add(lbl, BorderLayout.NORTH);
        box.add(field, BorderLayout.CENTER);
        return box;
    }

    private JTextField createInput(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        f.setPreferredSize(new Dimension(0, 38));
        return f;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(BG_CARD);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("SansSerif", Font.BOLD, 12));
        cancel.setBackground(BG_INPUT);
        cancel.setForeground(TEXT_GREY);
        cancel.setFocusPainted(false);
        cancel.setBorderPainted(false);
        cancel.setOpaque(true);
        cancel.setBorder(new EmptyBorder(8, 18, 8, 18));
        cancel.addActionListener(e -> dispose());

        JButton save = new JButton(existingUser == null ? "Add User" : "Update User");
        save.setFont(new Font("SansSerif", Font.BOLD, 12));
        save.setBackground(ACCENT);
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.setBorderPainted(false);
        save.setOpaque(true);
        save.setBorder(new EmptyBorder(8, 18, 8, 18));
        save.addActionListener(e -> handleSave());

        footer.add(cancel);
        footer.add(save);
        return footer;
    }

    private void populateFields(User u) {
        nameField.setText(u.getName());
        emailField.setText(u.getEmail());
        phoneField.setText(u.getPhone());
        deptField.setText(u.getDepartment() != null ? u.getDepartment() : "");
        roleField.setText(u.getRole() != null ? u.getRole() : "");
    }

    private void handleSave() {
        // Validation
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String dept  = deptField.getText().trim();
        String role  = roleField.getText().trim();

        if (name.isEmpty()) {
            showError("Name is required!", nameField); return;
        }
        if (email.isEmpty() || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError("Valid email is required!", emailField); return;
        }
        if (phone.isEmpty() || !phone.matches("\\d{10}")) {
            showError("Phone must be exactly 10 digits!", phoneField); return;
        }

        int excludeId = existingUser != null ? existingUser.getId() : 0;
        if (userDAO.emailExists(email, excludeId)) {
            showError("This email already exists!", emailField); return;
        }

        User user = new User(name, email, phone, dept, role);

        boolean result;
        if (existingUser == null) {
            result = userDAO.addUser(user);
        } else {
            user.setId(existingUser.getId());
            result = userDAO.updateUser(user);
        }

        if (result) {
            success = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Database error. Try again.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showError(String msg, JTextField field) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error",
            JOptionPane.WARNING_MESSAGE);
        field.requestFocus();
        field.setBorder(new CompoundBorder(
            new LineBorder(RED, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    public boolean isSuccess() { return success; }
}
