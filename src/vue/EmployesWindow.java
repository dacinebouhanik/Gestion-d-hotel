package vue;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployesWindow extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton boutonAjouter;
    private JButton boutonModifier;
    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champTel;
    private JTextField champEmail;
    private JComboBox<String> comboRole;
    private JTextField champRecherche;

    public EmployesWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        champRecherche = new JTextField(20);
        JPanel panelNord = new JPanel();
        panelNord.setBackground(Color.WHITE);
        panelNord.add(new JLabel("Recherche :"));
        panelNord.add(champRecherche);
        add(panelNord, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Téléphone", "Email", "Rôle"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(245, 238, 229));
        table.getTableHeader().setForeground(new Color(90, 60, 40));
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        champNom = new JTextField(10);
        champPrenom = new JTextField(10);
        champTel = new JTextField(10);
        champEmail = new JTextField(10);
        comboRole = new JComboBox<>(new String[]{"Réceptionniste", "Femme de Ménage"});
        styleComboBoxArrow(comboRole);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(champNom);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(champPrenom);
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(champTel);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(champEmail);
        formPanel.add(new JLabel("Rôle:"));
        formPanel.add(comboRole);

        boutonAjouter = createRoundedButton("Ajouter Employé");
        boutonModifier = createRoundedButton("Modifier Employé");

        JPanel panelBoutons = new JPanel();
        panelBoutons.setBackground(Color.WHITE);
        panelBoutons.add(boutonAjouter);
        panelBoutons.add(boutonModifier);

        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.add(formPanel, BorderLayout.CENTER);
        panelSud.add(panelBoutons, BorderLayout.SOUTH);

        add(panelSud, BorderLayout.SOUTH);
    }

    private void styleComboBoxArrow(JComboBox<?> comboBox) {
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton("▼");
                arrow.setFont(new Font("SansSerif", Font.PLAIN, 12));
                arrow.setForeground(Color.BLACK);
                arrow.setBackground(new Color(245, 238, 229));
                arrow.setBorder(BorderFactory.createEmptyBorder());
                arrow.setFocusable(false);
                return arrow;
            }
        });

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? new Color(245, 238, 229) : Color.WHITE);
                return label;
            }
        });

        comboBox.setBackground(Color.WHITE);
        comboBox.setOpaque(true);
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isArmed() ? new Color(220, 220, 220) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }


    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JButton getBoutonAjouter() { return boutonAjouter; }
    public JButton getBoutonModifier() { return boutonModifier; }
    public JTextField getChampNom() { return champNom; }
    public JTextField getChampPrenom() { return champPrenom; }
    public JTextField getChampTel() { return champTel; }
    public JTextField getChampEmail() { return champEmail; }
    public JComboBox<String> getComboRole() { return comboRole; }
    public JTextField getChampRecherche() { return champRecherche; }
}
