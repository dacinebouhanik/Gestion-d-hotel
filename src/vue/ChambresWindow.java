package vue;

import controller.ChambresController;
import model.Chambre;
import model.Hotel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChambresWindow extends JPanel {
    private JTextField champRecherche;
    private JButton boutonRechercher;

    private JTable table;
    private DefaultTableModel model;

    private JTextField champNumeroChambre;
    private JTextField champEtage;
    private JComboBox<String> comboTypeChambre;
    private JTextField champPrix;
    private JLabel labelEtat;

    private JButton boutonAjouter;
    private JButton boutonSupprimer;
    private JButton boutonModifier;

    public ChambresWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panelRecherche = new JPanel();
        champRecherche = new JTextField(20);
        boutonRechercher = createRoundedButton("Rechercher");
        panelRecherche.add(new JLabel("Recherche :"));
        panelRecherche.add(champRecherche);
        panelRecherche.add(boutonRechercher);
        add(panelRecherche, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Numéro");
        model.addColumn("Étage");
        model.addColumn("Type");
        model.addColumn("Prix");

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

        JPanel panelBas = new JPanel(new GridLayout(5, 2, 10, 10));
        champNumeroChambre = new JTextField();
        champEtage = new JTextField();
        comboTypeChambre = new JComboBox<>(new String[]{"Simple", "Double", "Suite", "Présidentielle"});
        styleComboBoxArrow(comboTypeChambre);
        champPrix = new JTextField();
        labelEtat = new JLabel("Disponible");

        panelBas.add(new JLabel("Numéro chambre :"));
        panelBas.add(champNumeroChambre);
        panelBas.add(new JLabel("Étage :"));
        panelBas.add(champEtage);
        panelBas.add(new JLabel("Type chambre :"));
        panelBas.add(comboTypeChambre);
        panelBas.add(new JLabel("Prix :"));
        panelBas.add(champPrix);

        JPanel panelEtat = new JPanel();
        panelEtat.add(new JLabel("Disponibilité :"));
        panelEtat.add(labelEtat);

        JPanel panelBoutons = new JPanel();
        boutonAjouter = createRoundedButton("Ajouter");
        boutonSupprimer = createRoundedButton("Supprimer");
        boutonModifier = createRoundedButton("Modifier");
        panelBoutons.add(boutonAjouter);
        panelBoutons.add(boutonSupprimer);
        panelBoutons.add(boutonModifier);

        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.add(panelBas, BorderLayout.CENTER);
        panelSud.add(panelEtat, BorderLayout.NORTH);
        panelSud.add(panelBoutons, BorderLayout.SOUTH);

        add(panelSud, BorderLayout.SOUTH);
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

    private void styleComboBoxArrow(JComboBox<?> comboBox) {
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton("\u25BC");
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
                label.setOpaque(true);
                label.setBackground(isSelected ? new Color(245, 238, 229) : Color.WHITE);
                return label;
            }
        });

        comboBox.setBackground(Color.WHITE);
        comboBox.setOpaque(true);
        comboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }


    public JTextField getChampRecherche() { return champRecherche; }
    public JButton getBoutonRechercher() { return boutonRechercher; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JTextField getChampNumeroChambre() { return champNumeroChambre; }
    public JTextField getChampEtage() { return champEtage; }
    public JComboBox<String> getComboTypeChambre() { return comboTypeChambre; }
    public JTextField getChampPrix() { return champPrix; }
    public JLabel getLabelEtat() { return labelEtat; }
    public JButton getBoutonAjouter() { return boutonAjouter; }
    public JButton getBoutonSupprimer() { return boutonSupprimer; }
    public JButton getBoutonModifier() { return boutonModifier; }
}
