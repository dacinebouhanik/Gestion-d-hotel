package vue;

import controller.*;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenuWindow extends JFrame {
    private ClientsWindow clientsWindow;
    private ChambresWindow chambresWindow;
    private ReservationsWindow reservationsWindow;
    private SejoursWindow sejoursWindow;
    private EmployesWindow employesWindow;
    private MenageWindow menageWindow;
    private ReservationsController reservationsController;
    private SejoursController sejoursController;
    private EmployesController employesController;
    private MenageController menageController;

    private Hotel hotel;
    private JPanel contentPanel;

    public MainMenuWindow(Hotel hotel) {
        this.hotel = hotel;
        setTitle("Menu Principal - Hôtel Dream");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        clientsWindow = new ClientsWindow();
        new ClientsController(clientsWindow, hotel);

        chambresWindow = new ChambresWindow();
        new ChambresController(chambresWindow, hotel).afficherChambres();

        reservationsWindow = new ReservationsWindow();
        reservationsController = new ReservationsController(reservationsWindow, hotel, chambresWindow);

        sejoursWindow = new SejoursWindow();
        sejoursController = new SejoursController(sejoursWindow, hotel);
        sejoursWindow.setController(sejoursController);

        employesWindow = new EmployesWindow();
        employesController = new EmployesController(employesWindow, hotel, this);
        employesController.afficherEmployes();

        menageWindow = new MenageWindow();
        menageController = new MenageController(menageWindow, hotel,chambresWindow);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(245, 239, 229));
        sidePanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        sidePanel.setPreferredSize(new Dimension(300, getHeight()));

        JLabel titleLabel = new JLabel("Hôtel Dream");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 30));
        titleLabel.setForeground(new Color(90, 60, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel starsLabel = new JLabel("⭐⭐⭐⭐");
        starsLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
        starsLabel.setForeground(new Color(200, 170, 80));
        starsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(titleLabel);
        sidePanel.add(starsLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        sidePanel.add(createNavButton("Gérer les Clients", "👤", () -> setContent(clientsWindow)));
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(createNavButton("Gérer les Chambres", "🛏", () -> setContent(chambresWindow)));
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(createNavButton("Gérer les Réservations", "📅", () -> {
            reservationsController.rafraichirComboClientsChambresReceptionnistes();
            setContent(reservationsWindow);
        }));
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(createNavButton("Gérer les Séjours", "🕓", () -> {
            sejoursWindow.getController().remplirComboReservations();
            setContent(sejoursWindow);
        }));
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(createNavButton("Gérer les Employés", "👥", () -> setContent(employesWindow)));
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(createNavButton("Gérer les Ménages", "🧹", () -> {
            menageController.remplirComboReservations();
            setContent(menageWindow);
        }));

        sidePanel.add(Box.createVerticalGlue());
        JButton btnQuitter = createNavButton("Quitter", "🚪", () -> System.exit(0));
        btnQuitter.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(btnQuitter);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(250, 246, 240));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setContent(clientsWindow);
    }

    private JButton createNavButton(String text, String icon, Runnable onClick) {
        JButton button = new JButton(icon + "  " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isArmed() ? new Color(230, 230, 230) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setForeground(new Color(60, 60, 60));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setMaximumSize(new Dimension(260, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(e -> onClick.run());
        return button;
    }

    private void setContent(Container content) {
        contentPanel.removeAll();
        contentPanel.add(content, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void rafraichirMenageWindow() {
        menageController.remplirComboFemmesDeMenage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Hotel hotel = new Hotel();

            hotel.getChambres().add(new Chambre(1, 101, 1, "Simple", 50.0f));
            hotel.getChambres().add(new Chambre(2, 102, 1, "Double", 80.0f));
            hotel.getChambres().add(new Chambre(3, 201, 2, "Suite", 150.0f));
            hotel.getChambres().add(new Chambre(4, 202, 2, "Présidentielle", 300.0f));

            Receptionniste r1 = new Receptionniste("Paul", "Lemoine", "0600000001", "paul.lemoine@hotel.com", 1);
            Receptionniste r2 = new Receptionniste("Camille", "Girard", "0600000002", "camille.girard@hotel.com", 2);
            Receptionniste r3 = new Receptionniste("Antoine", "Roux", "0600000003", "antoine.roux@hotel.com", 3);
            Receptionniste r4 = new Receptionniste("Sophie", "Perez", "0600000004", "sophie.perez@hotel.com", 4);
            hotel.getEmployes().add(r1);
            hotel.getEmployes().add(r2);
            hotel.getEmployes().add(r3);
            hotel.getEmployes().add(r4);
            hotel.getReceptionnistes().add(r1);
            hotel.getReceptionnistes().add(r2);
            hotel.getReceptionnistes().add(r3);
            hotel.getReceptionnistes().add(r4);

            FemmeDeMenage f1 = new FemmeDeMenage("Julie", "Durand", "0600000005", "julie.durand@hotel.com", 5);
            FemmeDeMenage f2 = new FemmeDeMenage("Nina", "Leroy", "0600000006", "nina.leroy@hotel.com", 6);
            FemmeDeMenage f3 = new FemmeDeMenage("Emma", "Benoit", "0600000007", "emma.benoit@hotel.com", 7);
            FemmeDeMenage f4 = new FemmeDeMenage("Claire", "Marchand", "0600000008", "claire.marchand@hotel.com", 8);
            hotel.getEmployes().add(f1);
            hotel.getEmployes().add(f2);
            hotel.getEmployes().add(f3);
            hotel.getEmployes().add(f4);
            hotel.getFemmesDeMenage().add(f1);
            hotel.getFemmesDeMenage().add(f2);
            hotel.getFemmesDeMenage().add(f3);
            hotel.getFemmesDeMenage().add(f4);

            new MainMenuWindow(hotel).setVisible(true);
        });
    }
}
