package com.usermgmt.ui;

import com.usermgmt.dao.UserDAO;
import com.usermgmt.model.User;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class UserManagementApp extends JFrame {

    // ─── Colors ───────────────────────────────────────────────────
    private static final Color BG_DARK    = new Color(13, 15, 20);
    private static final Color BG_CARD    = new Color(19, 23, 32);
    private static final Color BG_ROW     = new Color(26, 31, 46);
    private static final Color ACCENT     = new Color(79, 140, 255);
    private static final Color ACCENT2    = new Color(59, 111, 212);
    private static final Color TEXT_WHITE = new Color(232, 236, 244);
    private static final Color TEXT_GREY  = new Color(139, 149, 176);
    private static final Color RED        = new Color(239, 68, 68);
    private static final Color GREEN      = new Color(34, 197, 94);
    private static final Color BORDER_COL = new Color(37, 44, 61);

    // ─── Components ───────────────────────────────────────────────
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel totalLabel;
    private JLabel statusLabel;
    private UserDAO userDAO;

    // ─── Constructor ──────────────────────────────────────────────
    public UserManagementApp() {
        userDAO = new UserDAO();
        userDAO.createTableIfNotExists();

        setTitle("UserVault — User Management System");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 550));

        initUI();
        loadUsers();

        setVisible(true);
    }

    // ─── Build UI ─────────────────────────────────────────────────
    private void initUI() {
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ─── Header ───────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        header.setPreferredSize(new Dimension(0, 68));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        brand.setOpaque(false);

        JLabel logo = new JLabel("UM");
        logo.setFont(new Font("SansSerif", Font.BOLD, 14));
        logo.setForeground(Color.WHITE);
        logo.setOpaque(true);
        logo.setBackground(ACCENT);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(42, 42));
        logo.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        JLabel title = new JLabel("UserVault");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(TEXT_WHITE);
        JLabel sub = new JLabel("User Management System");
        sub.setFont(new Font("Monospaced", Font.PLAIN, 10));
        sub.setForeground(TEXT_GREY);
        titleBox.add(title);
        titleBox.add(sub);

        brand.add(logo);
        brand.add(titleBox);

        // Stats
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        stats.setOpaque(false);
        totalLabel = new JLabel("0 Users");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        totalLabel.setForeground(ACCENT);
        totalLabel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(5, 14, 5, 14)
        ));
        totalLabel.setOpaque(true);
        totalLabel.setBackground(BG_ROW);
        stats.add(totalLabel);

        header.add(brand, BorderLayout.WEST);
        header.add(stats, BorderLayout.EAST);
        return header;
    }

    // ─── Center Panel ─────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setBackground(BG_DARK);
        center.setBorder(new EmptyBorder(20, 20, 10, 20));

        center.add(buildToolbar(), BorderLayout.NORTH);
        center.add(buildTable(), BorderLayout.CENTER);
        return center;
    }

    // ─── Toolbar ──────────────────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);

        // Search
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBackground(BG_CARD);
        searchField.setForeground(TEXT_WHITE);
        searchField.setCaretColor(TEXT_WHITE);
        searchField.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(8, 14, 8, 14)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Search by name or email...");

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String q = searchField.getText().trim();
                if (q.isEmpty()) loadUsers();
                else searchUsers(q);
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton addBtn    = createButton("+ Add User",    ACCENT,  ACCENT2);
        JButton editBtn   = createButton("✎ Edit",        BG_ROW,  BG_CARD);
        JButton deleteBtn = createButton("⌫ Delete",      RED,     new Color(185, 28, 28));
        JButton refreshBtn= createButton("↺ Refresh",     BG_ROW,  BG_CARD);

        editBtn.setForeground(TEXT_GREY);
        refreshBtn.setForeground(TEXT_GREY);

        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> openEditDialog());
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        refreshBtn.addActionListener(e -> { loadUsers(); setStatus("Refreshed!", GREEN); });

        btnPanel.add(refreshBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(addBtn);

        toolbar.add(searchPanel, BorderLayout.CENTER);
        toolbar.add(btnPanel, BorderLayout.EAST);
        return toolbar;
    }

    // ─── Table ────────────────────────────────────────────────────
    private JScrollPane buildTable() {
        String[] cols = {"ID", "Name", "Email", "Phone", "Department", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        userTable = new JTable(tableModel);
        userTable.setBackground(BG_CARD);
        userTable.setForeground(TEXT_WHITE);
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userTable.setRowHeight(42);
        userTable.setShowGrid(false);
        userTable.setIntercellSpacing(new Dimension(0, 1));
        userTable.setSelectionBackground(new Color(79, 140, 255, 60));
        userTable.setSelectionForeground(TEXT_WHITE);
        userTable.setFocusable(false);

        // Header
        JTableHeader header = userTable.getTableHeader();
        header.setBackground(BG_ROW);
        header.setForeground(TEXT_GREY);
        header.setFont(new Font("Monospaced", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COL));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.LEFT);

        // Column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(220);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        userTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        userTable.getColumnModel().getColumn(5).setPreferredWidth(140);

        // Row renderer
        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 14));
                if (sel) {
                    setBackground(new Color(79, 140, 255, 40));
                } else {
                    setBackground(row % 2 == 0 ? BG_CARD : BG_ROW);
                }
                setForeground(col == 2 ? TEXT_GREY : TEXT_WHITE);
                if (col == 2) setFont(new Font("Monospaced", Font.PLAIN, 12));
                return this;
            }
        });

        // Double click to edit
        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openEditDialog();
            }
        });

        JScrollPane scroll = new JScrollPane(userTable);
        scroll.setBackground(BG_CARD);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(new LineBorder(BORDER_COL, 1, true));
        return scroll;
    }

    // ─── Status Bar ───────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        bar.setBackground(BG_CARD);
        bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COL));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_GREY);
        bar.add(statusLabel);
        return bar;
    }

    // ─── Load All Users ───────────────────────────────────────────
    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getId(), u.getName(), u.getEmail(),
                u.getPhone(), u.getDepartment(), u.getRole()
            });
        }
        updateTotal(users.size());
    }

    // ─── Search Users ─────────────────────────────────────────────
    private void searchUsers(String q) {
        tableModel.setRowCount(0);
        List<User> users = userDAO.searchUsers(q);
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getId(), u.getName(), u.getEmail(),
                u.getPhone(), u.getDepartment(), u.getRole()
            });
        }
        updateTotal(users.size());
        setStatus("Found " + users.size() + " result(s) for: " + q, ACCENT);
    }

    // ─── Add User Dialog ──────────────────────────────────────────
    private void openAddDialog() {
        UserFormDialog dialog = new UserFormDialog(this, "Add New User", null, userDAO);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            loadUsers();
            setStatus("✓ User added successfully!", GREEN);
        }
    }

    // ─── Edit User Dialog ─────────────────────────────────────────
    private void openEditDialog() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        User user = userDAO.getUserById(id);
        if (user == null) return;

        UserFormDialog dialog = new UserFormDialog(this, "Edit User", user, userDAO);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            loadUsers();
            setStatus("✓ User updated successfully!", GREEN);
        }
    }

    // ─── Delete User ──────────────────────────────────────────────
    private void deleteSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = (String) tableModel.getValueAt(row, 1);
        int id      = (int)    tableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete \"" + name + "\"?\nThis cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(id)) {
                loadUsers();
                setStatus("✓ User \"" + name + "\" deleted.", RED);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────
    private void updateTotal(int count) {
        totalLabel.setText(count + " User" + (count != 1 ? "s" : ""));
    }

    private void setStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
        Timer t = new Timer(3000, e -> {
            statusLabel.setText("Ready");
            statusLabel.setForeground(TEXT_GREY);
        });
        t.setRepeats(false);
        t.start();
    }

    private JButton createButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    // ─── Main ─────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(UserManagementApp::new);
    }
}
