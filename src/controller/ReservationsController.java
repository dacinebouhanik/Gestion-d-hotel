package controller;

import model.*;
import vue.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class ReservationsController {
    private ReservationsWindow vue;
    private Hotel hotel;
    private ChambresWindow chambresWindow;

    public ReservationsController(ReservationsWindow vue, Hotel hotel, ChambresWindow chambresWindow) {
        this.vue = vue;
        this.hotel = hotel;
        this.chambresWindow = chambresWindow;

        rafraichirComboClientsChambresReceptionnistes();
        ajouterEcouteurTypeChambre();
        ajouterControleBoutonPlus();
        ajouterFiltreRecherche();
        ajouterFiltreRechercheReservation();

        vue.getBoutonAjouter().addActionListener(e -> ajouterReservation());
        vue.getBoutonAnnuler().addActionListener(e -> annulerReservation());
        vue.getBoutonModifier().addActionListener(e -> modifierReservation());
    }

    private void ajouterFiltreRechercheReservation() {
        vue.getChampRecherche().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String recherche = vue.getChampRecherche().getText().trim();
                DefaultTableModel model = vue.getModel();
                model.setRowCount(0);

                for (Reservation r : hotel.getReservations()) {
                    String idString = String.valueOf(r.getIdReservation());
                    if (idString.startsWith(recherche)) {
                        model.addRow(new Object[]{
                                r.getIdReservation(),
                                r.getClient(),
                                r.getChambre(),
                                new java.text.SimpleDateFormat("dd/MM/yyyy").format(r.getDateDebut()),
                                new java.text.SimpleDateFormat("dd/MM/yyyy").format(r.getDateFin()),
                                r.getNbrNuits(),
                                r.getReceptionniste()
                        });
                    }
                }
            }
        });
    }

    public void rafraichirComboClientsChambresReceptionnistes() {
        vue.getComboClient().removeAllItems();
        for (Client c : hotel.getClients()) vue.getComboClient().addItem(c);

        vue.getComboChambre().removeAllItems();
        for (Chambre ch : hotel.getChambres()) vue.getComboChambre().addItem(ch);

        vue.getComboReceptionniste().removeAllItems();
        for (Receptionniste r : hotel.getReceptionnistes()) vue.getComboReceptionniste().addItem(r);
    }

    private int getMaxPersonnesPourType(String typeChambre) {
        return switch (typeChambre) {
            case "Simple" -> 1;
            case "Double" -> 2;
            case "Suite" -> 4;
            case "Présidentielle" -> 6;
            default -> 1;
        };
    }

    private void ajusterNbPersonnes() {
        String typeChambre = (String) vue.getComboTypeChambre().getSelectedItem();
        int maxPersonnes = getMaxPersonnesPourType(typeChambre);
        int nbActuel = Integer.parseInt(vue.getLabelNbPersonnes().getText());
        if (nbActuel > maxPersonnes) vue.getLabelNbPersonnes().setText(String.valueOf(maxPersonnes));
    }

    private void ajouterEcouteurTypeChambre() {
        vue.getComboTypeChambre().addActionListener(e -> ajusterNbPersonnes());
    }

    private void ajouterControleBoutonPlus() {
        for (Component comp : vue.getComponents()) {
            if (comp instanceof JPanel panel) {
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JPanel subPanel) {
                        for (Component buttonComp : subPanel.getComponents()) {
                            if (buttonComp instanceof JButton bouton && bouton.getText().equals("+")) {
                                bouton.addActionListener(e -> {
                                    String typeChambre = (String) vue.getComboTypeChambre().getSelectedItem();
                                    int maxPersonnes = getMaxPersonnesPourType(typeChambre);
                                    int nbActuel = Integer.parseInt(vue.getLabelNbPersonnes().getText());
                                    if (nbActuel < maxPersonnes) {
                                        vue.getLabelNbPersonnes().setText(String.valueOf(nbActuel + 1));
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    private void ajouterFiltreRecherche() {
        vue.getBoutonRechercher().addActionListener(e -> filtrerChambresDisponibles());
    }

    private void filtrerChambresDisponibles() {
        String typeChambre = (String) vue.getComboTypeChambre().getSelectedItem();
        Date dateEntree = Reservation.normaliserDate((Date) vue.getSpinnerDateEntree().getValue());
        Date dateSortie = Reservation.normaliserDate((Date) vue.getSpinnerDateSortie().getValue());

        vue.getComboChambre().removeAllItems();

        Vector<Chambre> disponibles = Reservation.chercherChambresDisponibles(
                dateEntree, dateSortie, typeChambre,
                hotel.getChambres(), hotel.getReservations()
        );

        for (Chambre ch : disponibles) {
            vue.getComboChambre().addItem(ch);
        }

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Aucune chambre disponible pour cette période et ce type.");
        }
    }

    private void ajouterReservation() {
        try {
            int idReservation = genererIdReservation();
            Client clientChoisi = (Client) vue.getComboClient().getSelectedItem();
            Chambre chambreChoisie = (Chambre) vue.getComboChambre().getSelectedItem();
            Receptionniste receptionnisteChoisi = (Receptionniste) vue.getComboReceptionniste().getSelectedItem();
            int nbPersonnes = Integer.parseInt(vue.getLabelNbPersonnes().getText());

            Date dateEntree = Reservation.normaliserDate((Date) vue.getSpinnerDateEntree().getValue());
            Date dateSortie = Reservation.normaliserDate((Date) vue.getSpinnerDateSortie().getValue());


            if (dateEntree.after(dateSortie)) {
                JOptionPane.showMessageDialog(vue, "Erreur : la date d'entrée doit être avant la date de sortie !");
                return;
            }

            for (Reservation r : hotel.getReservations()) {
                Date rDebut = Reservation.normaliserDate(r.getDateDebut());
                Date rFin = Reservation.normaliserDate(r.getDateFin());

                if (r.getChambre() != null && r.getChambre().getIdChambre() == chambreChoisie.getIdChambre()) {
                    if (!(dateSortie.before(rDebut) || dateEntree.after(rFin))) {
                        JOptionPane.showMessageDialog(vue, "Erreur : Cette chambre est déjà réservée !");
                        return;
                    }
                }
            }

            Reservation r = new Reservation(idReservation, nbPersonnes, 0, dateEntree, dateSortie,
                    clientChoisi, chambreChoisie, receptionnisteChoisi, null);

            hotel.getReservations().add(r);

            rafraichirChambresWindow();

            vue.getModel().addRow(new Object[]{
                    r.getIdReservation(), clientChoisi, chambreChoisie,
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(r.getDateDebut()),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(r.getDateFin()),
                    nbPersonnes, receptionnisteChoisi
            });

            JOptionPane.showMessageDialog(vue, "Réservation ajoutée !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vue, "Erreur : " + ex.getMessage());
        }
    }

    private void annulerReservation() {
        int selectedRow = vue.getTable().getSelectedRow();
        if (selectedRow != -1) {
            Reservation r = hotel.getReservations().get(selectedRow);
            if (r.getChambre() != null) {

                rafraichirChambresWindow();
            }
            hotel.getReservations().remove(selectedRow);
            vue.getModel().removeRow(selectedRow);
            JOptionPane.showMessageDialog(vue, "Réservation supprimée !");
        } else {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner une réservation !");
        }
    }

    private void modifierReservation() {
        int selectedRow = vue.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner une réservation !");
            return;
        }

        try {
            int idReservation = Integer.parseInt(vue.getTable().getValueAt(selectedRow, 0).toString());
            Client clientChoisi = (Client) vue.getComboClient().getSelectedItem();
            Chambre chambreChoisie = (Chambre) vue.getComboChambre().getSelectedItem();
            Receptionniste receptionnisteChoisi = (Receptionniste) vue.getComboReceptionniste().getSelectedItem();
            int nbPersonnes = Integer.parseInt(vue.getLabelNbPersonnes().getText());

            Date dateEntree = Reservation.normaliserDate((Date) vue.getSpinnerDateEntree().getValue());
            Date dateSortie = Reservation.normaliserDate((Date) vue.getSpinnerDateSortie().getValue());


            if (dateEntree.after(dateSortie)) {
                JOptionPane.showMessageDialog(vue, "Erreur : la date d'entrée doit être avant la date de sortie !");
                return;
            }

            for (Reservation r : hotel.getReservations()) {
                Date rDebut = Reservation.normaliserDate(r.getDateDebut());
                Date rFin = Reservation.normaliserDate(r.getDateFin());

                if (r.getIdReservation() != idReservation &&
                        r.getChambre() != null &&
                        r.getChambre().getIdChambre() == chambreChoisie.getIdChambre()) {
                    if (!(dateSortie.before(rDebut) || dateEntree.after(rFin))) {
                        JOptionPane.showMessageDialog(vue, "Erreur : Chambre déjà réservée !");
                        return;
                    }
                }
            }

            Reservation reservationAModifier = hotel.getReservations().get(selectedRow);
            reservationAModifier.setClient(clientChoisi);
            reservationAModifier.setChambre(chambreChoisie);
            reservationAModifier.setReceptionniste(receptionnisteChoisi);
            reservationAModifier.setNbrNuits(nbPersonnes);
            reservationAModifier.setDateDebut(dateEntree);
            reservationAModifier.setDateFin(dateSortie);

            vue.getModel().setValueAt(clientChoisi, selectedRow, 1);
            vue.getModel().setValueAt(chambreChoisie, selectedRow, 2);
            vue.getModel().setValueAt(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dateEntree), selectedRow, 3);
            vue.getModel().setValueAt(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dateSortie), selectedRow, 4);
            vue.getModel().setValueAt(nbPersonnes, selectedRow, 5);
            vue.getModel().setValueAt(receptionnisteChoisi, selectedRow, 6);

            JOptionPane.showMessageDialog(vue, "Réservation modifiée !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vue, "Erreur lors de la modification : " + ex.getMessage());
        }
    }

    private void rafraichirChambresWindow() {
        if (chambresWindow != null) {
            DefaultTableModel model = chambresWindow.getModel();
            model.setRowCount(0);
            for (Chambre c : hotel.getChambres()) {
                model.addRow(new Object[]{
                        c.getIdChambre(),
                        c.getIdChambre(),
                        c.getEtage(),
                        c.getType(),
                        c.getPrix(),

                });
            }
        }
    }

    private int genererIdReservation() {
        int maxId = 0;
        for (Reservation r : hotel.getReservations()) {
            if (r.getIdReservation() > maxId) maxId = r.getIdReservation();
        }
        return maxId + 1;
    }
}
