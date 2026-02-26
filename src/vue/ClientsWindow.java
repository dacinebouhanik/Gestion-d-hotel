package vue;

import controller.ClientsController;
import model.Client;
import model.Hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientsWindow extends JPanel {
    private JTextField champRecherche;
    private JButton boutonRechercher;

    private JTable table;
    private DefaultTableModel model;

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champTel;
    private JTextField champEmail;

    private JButton boutonAjouter;
    private JButton boutonSupprimer;
    private JButton boutonModifier;

    public ClientsWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panelRecherche = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRecherche.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelRecherche.setBackground(Color.WHITE);
        champRecherche = new JTextField(25);
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
        model.addColumn("Nom");
        model.addColumn("Prénom");
        model.addColumn("Téléphone");
        model.addColumn("Email");

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
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);


        JPanel panelFormulaire = new JPanel(new GridLayout(2, 4, 15, 10));
        panelFormulaire.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelFormulaire.setBackground(Color.WHITE);

        champNom = new JTextField();
        champPrenom = new JTextField();
        champTel = new JTextField();
        champEmail = new JTextField();

        panelFormulaire.add(new JLabel("Nom :"));
        panelFormulaire.add(champNom);
        panelFormulaire.add(new JLabel("Prénom :"));
        panelFormulaire.add(champPrenom);
        panelFormulaire.add(new JLabel("Téléphone :"));
        panelFormulaire.add(champTel);
        panelFormulaire.add(new JLabel("Email :"));
        panelFormulaire.add(champEmail);


        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBoutons.setBackground(Color.WHITE);
        boutonAjouter = createRoundedButton("Ajouter");
        boutonSupprimer = createRoundedButton("Supprimer");
        boutonModifier = createRoundedButton("Modifier");
        panelBoutons.add(boutonAjouter);
        panelBoutons.add(boutonSupprimer);
        panelBoutons.add(boutonModifier);

        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.setBackground(Color.WHITE);
        panelSud.add(panelFormulaire, BorderLayout.CENTER);
        panelSud.add(panelBoutons, BorderLayout.SOUTH);
        add(panelSud, BorderLayout.SOUTH);
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isArmed() ? new Color(240, 240, 240) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 220, 220));
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
    public JTextField getChampNom() { return champNom; }
    public JTextField getChampPrenom() { return champPrenom; }
    public JTextField getChampTel() { return champTel; }
    public JTextField getChampEmail() { return champEmail; }
    public JButton getBoutonAjouter() { return boutonAjouter; }
    public JButton getBoutonSupprimer() { return boutonSupprimer; }
    public JButton getBoutonModifier() { return boutonModifier; }
}
