package controller;

import model.*;
import vue.MenageWindow;
import vue.ChambresWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenageController {
    private MenageWindow vue;
    private Hotel hotel;
    private ChambresWindow chambresWindow;

    public MenageController(MenageWindow vue, Hotel hotel, ChambresWindow chambresWindow) {
        this.vue = vue;
        this.hotel = hotel;
        this.chambresWindow = chambresWindow;

        remplirComboFemmesDeMenage();
        remplirComboReservations();

        vue.getBoutonAssignerMenage().addActionListener(e -> assignerMenage());
    }

    public void remplirComboFemmesDeMenage() {
        vue.getComboFemmesDeMenage().removeAllItems();
        for (FemmeDeMenage f : hotel.getFemmesDeMenage()) {
            vue.getComboFemmesDeMenage().addItem(f);
        }
    }

    public void remplirComboReservations() {
        vue.getComboReservations().removeAllItems();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Reservation r : hotel.getReservations()) {
            String info = "Chambre " + r.getChambre().getNumero() + " - Sortie: " + sdf.format(r.getDateFin());
            vue.getComboReservations().addItem(info);
        }
    }

    private void assignerMenage() {
        FemmeDeMenage femme = (FemmeDeMenage) vue.getComboFemmesDeMenage().getSelectedItem();
        String reservationInfo = (String) vue.getComboReservations().getSelectedItem();

        if (femme == null || reservationInfo == null) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner une femme de ménage et une réservation !");
            return;
        }

        try {
            String[] parts = reservationInfo.split(" - Sortie: ");
            int numeroChambre = Integer.parseInt(parts[0].replace("Chambre ", "").trim());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFin = sdf.parse(parts[1]);

            Chambre chambre = null;
            for (Reservation r : hotel.getReservations()) {
                if (r.getChambre().getNumero() == numeroChambre) {
                    chambre = r.getChambre();
                    break;
                }
            }

            if (chambre == null) {
                JOptionPane.showMessageDialog(vue, "Erreur : chambre non trouvée !");
                return;
            }

            for (Menage m : hotel.getMenages()) {
                if (m.getChambre().getIdChambre() == chambre.getIdChambre()
                        && m.getDateMenage().equals(dateFin)) {
                    JOptionPane.showMessageDialog(vue, "Cette chambre est déjà assignée à un ménage pour ce jour !");
                    return;
                }
            }

            Menage menage = new Menage(chambre, femme, dateFin);
            hotel.getMenages().add(menage);

            vue.getModelMenages().addRow(new Object[]{
                    femme.getNom() + " " + femme.getPrenom(),
                    chambre.getNumero(),
                    sdf.format(dateFin)
            });

            mettreAJourNettoyageDansChambres(chambre.getIdChambre(), dateFin);

            JOptionPane.showMessageDialog(vue,
                    "Ménage assigné : " + femme.getNom() + " " + femme.getPrenom() +
                            " va nettoyer la chambre " + chambre.getNumero() + " !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vue, "Erreur lors de l'assignation : " + ex.getMessage());
        }
    }

    private void mettreAJourNettoyageDansChambres(int idChambre, Date dateFin) {
        try {
            DefaultTableModel model = chambresWindow.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                int id = (int) model.getValueAt(i, 0);
                if (id == idChambre) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    model.setValueAt("à nettoyer le " + sdf.format(dateFin), i, 6);
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vue, "Erreur lors de la mise à jour de l'état de nettoyage !");
        }
    }
}
