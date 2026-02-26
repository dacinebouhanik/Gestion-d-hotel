package vue;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenageWindow extends JPanel {
    private JTable tableMenages;
    private DefaultTableModel modelMenages;
    private JComboBox<Object> comboFemmesDeMenage;
    private JComboBox<Object> comboReservations;
    private JButton boutonAssignerMenage;

    public MenageWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        modelMenages = new DefaultTableModel(new Object[]{"Femme de Ménage", "Chambre", "Date de nettoyage"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableMenages = new JTable(modelMenages);
        tableMenages.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        tableMenages.getTableHeader().setBackground(new Color(245, 238, 229));
        tableMenages.getTableHeader().setForeground(new Color(90, 60, 40));
        tableMenages.setRowHeight(28);
        tableMenages.setFont(new Font("SansSerif", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableMenages.getColumnCount(); i++) {
            tableMenages.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(new JScrollPane(tableMenages), BorderLayout.CENTER);

        comboFemmesDeMenage = new JComboBox<>();
        comboReservations = new JComboBox<>();
        styleComboBoxArrow(comboFemmesDeMenage);
        styleComboBoxArrow(comboReservations);

        boutonAssignerMenage = createRoundedButton("Assigner Ménage");

        JPanel panelBas = new JPanel();
        panelBas.setBackground(new Color(250, 250, 250));
        panelBas.add(new JLabel("Femme de Ménage :"));
        panelBas.add(comboFemmesDeMenage);
        panelBas.add(new JLabel("Réservation :"));
        panelBas.add(comboReservations);
        panelBas.add(boutonAssignerMenage);

        add(panelBas, BorderLayout.SOUTH);
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

    public JTable getTableMenages() { return tableMenages; }
    public DefaultTableModel getModelMenages() { return modelMenages; }
    public JComboBox<Object> getComboFemmesDeMenage() { return comboFemmesDeMenage; }
    public JComboBox<Object> getComboReservations() { return comboReservations; }
    public JButton getBoutonAssignerMenage() { return boutonAssignerMenage; }
}
