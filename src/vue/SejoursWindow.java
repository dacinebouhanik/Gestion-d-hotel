package vue;

import controller.SejoursController;
import model.Hotel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SejoursWindow extends JPanel {
    private JTextField champRecherche;
    private JButton boutonRechercher;

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> comboIDReservation;
    private JSpinner spinnerDateEntree;
    private JSpinner spinnerDateSortie;

    private JComboBox<String> comboProduit;
    private JSpinner spinnerQuantite;
    private JTextField champTotal;

    private JButton boutonCreerSejour;
    private JButton boutonGenererFacture;
    private JButton boutonSupprimerSejour;

    private SejoursController controller;

    public SejoursWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);


        JPanel panelRecherche = new JPanel();
        champRecherche = new JTextField(20);
        boutonRechercher = createRoundedButton("Rechercher");
        panelRecherche.add(new JLabel("Numéro Séjour :"));
        panelRecherche.add(champRecherche);
        panelRecherche.add(boutonRechercher);
        add(panelRecherche, BorderLayout.NORTH);


        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID Séjour");
        model.addColumn("ID Réservation");
        model.addColumn("Date entrée");
        model.addColumn("Date sortie");
        model.addColumn("Produit consommé");
        model.addColumn("Total");

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
        add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel panelBas = new JPanel(new GridLayout(2, 6, 10, 10));
        panelBas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBas.setBackground(new Color(250, 250, 250));

        comboIDReservation = new JComboBox<>();
        styleComboBoxArrow(comboIDReservation);

        comboProduit = new JComboBox<>(new String[]{"Produit A", "Produit B", "Produit C"});
        styleComboBoxArrow(comboProduit);

        spinnerDateEntree = new JSpinner(new SpinnerDateModel());
        spinnerDateEntree.setEditor(new JSpinner.DateEditor(spinnerDateEntree, "dd/MM/yyyy"));
        styleSpinner(spinnerDateEntree);
        spinnerDateEntree.setEnabled(false);

        spinnerDateSortie = new JSpinner(new SpinnerDateModel());
        spinnerDateSortie.setEditor(new JSpinner.DateEditor(spinnerDateSortie, "dd/MM/yyyy"));
        styleSpinner(spinnerDateSortie);
        spinnerDateSortie.setEnabled(false);

        spinnerQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        styleSpinner(spinnerQuantite);

        champTotal = new JTextField(10);

        panelBas.add(new JLabel("ID Réservation :"));
        panelBas.add(comboIDReservation);
        panelBas.add(new JLabel("Date entrée :"));
        panelBas.add(spinnerDateEntree);
        panelBas.add(new JLabel("Date sortie :"));
        panelBas.add(spinnerDateSortie);
        panelBas.add(new JLabel("Produit :"));
        panelBas.add(comboProduit);
        panelBas.add(new JLabel("Quantité :"));
        panelBas.add(spinnerQuantite);
        panelBas.add(new JLabel("Total :"));
        panelBas.add(champTotal);


        JPanel panelBoutons = new JPanel();
        panelBoutons.setBackground(new Color(250, 250, 250));
        boutonCreerSejour = createRoundedButton("Créer Séjour");
        boutonGenererFacture = createRoundedButton("Générer Facture");
        boutonSupprimerSejour = createRoundedButton("Supprimer Séjour");
        panelBoutons.add(boutonCreerSejour);
        panelBoutons.add(boutonGenererFacture);
        panelBoutons.add(boutonSupprimerSejour);

        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.add(panelBas, BorderLayout.CENTER);
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

    private void styleSpinner(JSpinner spinner) {
        spinner.setUI(new BasicSpinnerUI() {
            @Override
            protected Component createNextButton() {
                Component c = super.createNextButton();
                if (c instanceof BasicArrowButton) c.setBackground(new Color(245, 238, 229));
                return c;
            }

            @Override
            protected Component createPreviousButton() {
                Component c = super.createPreviousButton();
                if (c instanceof BasicArrowButton) c.setBackground(new Color(245, 238, 229));
                return c;
            }
        });
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


    public JTextField getChampRecherche() { return champRecherche; }
    public JButton getBoutonRechercher() { return boutonRechercher; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JComboBox<String> getComboIDReservation() { return comboIDReservation; }
    public JSpinner getSpinnerDateEntree() { return spinnerDateEntree; }
    public JSpinner getSpinnerDateSortie() { return spinnerDateSortie; }
    public JComboBox<String> getComboProduit() { return comboProduit; }
    public JSpinner getSpinnerQuantite() { return spinnerQuantite; }
    public JTextField getChampTotal() { return champTotal; }
    public JButton getBoutonCreerSejour() { return boutonCreerSejour; }
    public JButton getBoutonGenererFacture() { return boutonGenererFacture; }
    public JButton getBoutonSupprimerSejour() { return boutonSupprimerSejour; }
    public void setController(SejoursController controller) { this.controller = controller; }
    public SejoursController getController() { return controller; }
}
