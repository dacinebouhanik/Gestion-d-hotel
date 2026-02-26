package vue;

import model.Client;
import model.Chambre;
import model.Receptionniste;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservationsWindow extends JPanel {
    private JTextField champRecherche;
    private JButton boutonRechercher;

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<Client> comboClient;
    private JComboBox<Chambre> comboChambre;
    private JComboBox<Receptionniste> comboReceptionniste;
    private JLabel labelNbPersonnes;
    private int nbPersonnes = 1;

    private JComboBox<String> comboTypeChambre;
    private JSpinner spinnerDateEntree;
    private JSpinner spinnerDateSortie;

    private JButton boutonAjouter;
    private JButton boutonAnnuler;
    private JButton boutonModifier;

    public ReservationsWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);


        JPanel panelRecherche = new JPanel();
        champRecherche = new JTextField(15);
        boutonRechercher = createRoundedButton("Recherche chambres disponibles");

        spinnerDateEntree = new JSpinner(new SpinnerDateModel());
        spinnerDateEntree.setEditor(new JSpinner.DateEditor(spinnerDateEntree, "dd/MM/yyyy"));
        spinnerDateSortie = new JSpinner(new SpinnerDateModel());
        spinnerDateSortie.setEditor(new JSpinner.DateEditor(spinnerDateSortie, "dd/MM/yyyy"));

        comboTypeChambre = new JComboBox<>(new String[]{"Simple", "Double", "Suite", "Présidentielle"});
        styleComboBoxArrow(comboTypeChambre);

        panelRecherche.add(new JLabel("Recherche :"));
        panelRecherche.add(champRecherche);
        panelRecherche.add(new JLabel("Date entrée :"));
        panelRecherche.add(spinnerDateEntree);
        panelRecherche.add(new JLabel("Date sortie :"));
        panelRecherche.add(spinnerDateSortie);
        panelRecherche.add(new JLabel("Type chambre :"));
        panelRecherche.add(comboTypeChambre);
        panelRecherche.add(boutonRechercher);
        add(panelRecherche, BorderLayout.NORTH);


        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Client");
        model.addColumn("Chambre");
        model.addColumn("Date entrée");
        model.addColumn("Date sortie");
        model.addColumn("Nb personnes");
        model.addColumn("Réceptionniste");

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

        comboClient = new JComboBox<>();
        styleComboBoxArrow(comboClient);
        comboChambre = new JComboBox<>();
        styleComboBoxArrow(comboChambre);
        comboReceptionniste = new JComboBox<>();
        styleComboBoxArrow(comboReceptionniste);

        labelNbPersonnes = new JLabel(String.valueOf(nbPersonnes));
        labelNbPersonnes.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton boutonMoins = createRoundedButton("-");
        JButton boutonPlus = createRoundedButton("+");

        boutonMoins.addActionListener(e -> {
            if (nbPersonnes > 1) {
                nbPersonnes--;
                labelNbPersonnes.setText(String.valueOf(nbPersonnes));
            }
        });

        boutonPlus.addActionListener(e -> {
            nbPersonnes++;
            labelNbPersonnes.setText(String.valueOf(nbPersonnes));
        });

        JPanel panelNbPersonnes = new JPanel();
        panelNbPersonnes.add(boutonMoins);
        panelNbPersonnes.add(labelNbPersonnes);
        panelNbPersonnes.add(boutonPlus);

        JPanel panelBas = new JPanel(new GridLayout(5, 2, 10, 10));
        panelBas.add(new JLabel("Client :"));
        panelBas.add(comboClient);
        panelBas.add(new JLabel("Chambre :"));
        panelBas.add(comboChambre);
        panelBas.add(new JLabel("Nb personnes :"));
        panelBas.add(panelNbPersonnes);
        panelBas.add(new JLabel("Réceptionniste :"));
        panelBas.add(comboReceptionniste);


        boutonAjouter = createRoundedButton("Ajouter");
        boutonAnnuler = createRoundedButton("Annuler");
        boutonModifier = createRoundedButton("Modifier");

        JPanel panelBoutons = new JPanel();
        panelBoutons.add(boutonAjouter);
        panelBoutons.add(boutonAnnuler);
        panelBoutons.add(boutonModifier);

        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.add(panelBas, BorderLayout.CENTER);
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


    public JTextField getChampRecherche() { return champRecherche; }
    public JButton getBoutonRechercher() { return boutonRechercher; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JComboBox<Client> getComboClient() { return comboClient; }
    public JComboBox<Chambre> getComboChambre() { return comboChambre; }
    public JComboBox<Receptionniste> getComboReceptionniste() { return comboReceptionniste; }
    public JLabel getLabelNbPersonnes() { return labelNbPersonnes; }
    public JComboBox<String> getComboTypeChambre() { return comboTypeChambre; }
    public JSpinner getSpinnerDateEntree() { return spinnerDateEntree; }
    public JSpinner getSpinnerDateSortie() { return spinnerDateSortie; }
    public JButton getBoutonAjouter() { return boutonAjouter; }
    public JButton getBoutonAnnuler() { return boutonAnnuler; }
    public JButton getBoutonModifier() { return boutonModifier; }
}
