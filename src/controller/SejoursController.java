package controller;

import model.*;
import vue.SejoursWindow;

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SejoursController {
    private SejoursWindow vue;
    private Hotel hotel;
    private Vector<Reservation> reservations;
    private Vector<Sejour> sejours;
    private HashMap<String, Float> produitsPrix;

    public SejoursController(SejoursWindow vue, Hotel hotel) {
        this.vue = vue;
        this.hotel = hotel;
        this.reservations = hotel.getReservations();
        this.sejours = hotel.getSejours();
        this.produitsPrix = new HashMap<>();

        initialiserProduits();
        remplirComboReservations();
        ajouterEcouteurs();
        this.vue.getBoutonRechercher().addActionListener(e -> rechercherSejour());
        this.vue.getBoutonCreerSejour().addActionListener(e -> creerSejour());
        this.vue.getBoutonGenererFacture().addActionListener(e -> genererFacture());
        this.vue.getBoutonSupprimerSejour().addActionListener(e -> supprimerSejour());
    }

    private void initialiserProduits() {
        produitsPrix.put("Produit A", 10f);
        produitsPrix.put("Produit B", 20f);
        produitsPrix.put("Produit C", 30f);
    }

    public void remplirComboReservations() {
        vue.getComboIDReservation().removeAllItems();
        for (Reservation r : reservations) {
            vue.getComboIDReservation().addItem(String.valueOf(r.getIdReservation()));
        }
    }

    private void ajouterEcouteurs() {
        vue.getComboProduit().addActionListener(e -> calculerTotal());
        vue.getSpinnerQuantite().addChangeListener(e -> calculerTotal());
        vue.getComboIDReservation().addActionListener(e -> {
            mettreDatesReservation();
            calculerTotal();
        });
    }

    private void mettreDatesReservation() {
        try {
            String idRes = (String) vue.getComboIDReservation().getSelectedItem();
            if (idRes != null) {
                int idReservation = Integer.parseInt(idRes);
                Reservation reservation = trouverReservation(idReservation);
                if (reservation != null) {
                    vue.getSpinnerDateEntree().setValue(reservation.getDateDebut());
                    vue.getSpinnerDateSortie().setValue(reservation.getDateFin());
                }
            }
        } catch (Exception e) {

        }
    }

    private void calculerTotal() {
        try {
            String produit = (String) vue.getComboProduit().getSelectedItem();
            if (produit == null) return;

            Float prix = produitsPrix.get(produit.trim());
            if (prix == null) return;
            double prixProduit = prix;

            int quantite = (Integer) vue.getSpinnerQuantite().getValue();

            String idRes = (String) vue.getComboIDReservation().getSelectedItem();
            if (idRes == null) return;

            int idReservation = Integer.parseInt(idRes);
            Reservation reservation = trouverReservation(idReservation);

            if (reservation == null) return;

            double prixChambre = reservation.getChambre().getPrix();
            int nbNuits = reservation.getNbrNuits();

            double total = (prixProduit * quantite) + (prixChambre * nbNuits);
            vue.getChampTotal().setText(String.format("%.2f", total));
        } catch (Exception e) {
            vue.getChampTotal().setText("");
        }
    }
    private void rechercherSejour() {
        String texte = vue.getChampRecherche().getText().trim();
        if (texte.isEmpty()) {
            rafraichirTableau();
            return;
        }

        try {
            int idRecherche = Integer.parseInt(texte);
            vue.getModel().setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Sejour s : sejours) {
                if (s.getIdSejour() == idRecherche) {
                    Reservation r = s.getReservation();

                    for (Map.Entry<Produit, Integer> conso : s.getConsommations().entrySet()) {
                        vue.getModel().addRow(new Object[]{
                                s.getIdSejour(),
                                r.getIdReservation(),
                                sdf.format(r.getDateDebut()),
                                sdf.format(r.getDateFin()),
                                conso.getKey().getNomProduit(),
                                String.format("%.2f", s.calculerTotal())
                        });
                    }
                    return;
                }
            }

            JOptionPane.showMessageDialog(vue, "Aucun séjour trouvé avec ce numéro.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vue, "Veuillez entrer un numéro valide.");
        }
    }


    private void creerSejour() {
        try {
            int idReservation = Integer.parseInt((String) vue.getComboIDReservation().getSelectedItem());

            for (Sejour s : sejours) {
                if (s.getReservation().getIdReservation() == idReservation) {
                    JOptionPane.showMessageDialog(vue, "Un séjour existe déjà pour cette réservation !");
                    return;
                }
            }

            int idSejour = genererIdSejour();
            Reservation reservation = trouverReservation(idReservation);
            Sejour sejour = new Sejour(idSejour, reservation);

            String nomProduit = (String) vue.getComboProduit().getSelectedItem();
            int quantite = (Integer) vue.getSpinnerQuantite().getValue();
            Produit produit = new Produit(0, nomProduit, produitsPrix.get(nomProduit));

            sejour.ajouterConsommation(produit, quantite);
            sejours.add(sejour);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            vue.getModel().addRow(new Object[]{
                    idSejour,
                    idReservation,
                    sdf.format(reservation.getDateDebut()),
                    sdf.format(reservation.getDateFin()),
                    produit.getNomProduit(),
                    String.format("%.2f", sejour.calculerTotal())
            });

            JOptionPane.showMessageDialog(vue, "Séjour créé !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vue, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerSejour() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner un séjour à supprimer !");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vue, "Confirmer la suppression de ce séjour ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            sejours.remove(ligne);
            vue.getModel().removeRow(ligne);
            JOptionPane.showMessageDialog(vue, "Séjour supprimé !");
        }
    }
    private void rafraichirTableau() {
        vue.getModel().setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Sejour s : sejours) {
            Reservation r = s.getReservation();

            for (Map.Entry<Produit, Integer> conso : s.getConsommations().entrySet()) {
                vue.getModel().addRow(new Object[]{
                        s.getIdSejour(),
                        r.getIdReservation(),
                        sdf.format(r.getDateDebut()),
                        sdf.format(r.getDateFin()),
                        conso.getKey().getNomProduit(),
                        String.format("%.2f", s.calculerTotal())
                });
            }
        }
    }

    private void genererFacture() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne != -1) {
            String totalStr = (String) vue.getModel().getValueAt(ligne, 5);
            JOptionPane.showMessageDialog(vue, "Total du séjour : " + totalStr + " €");
        } else {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner un séjour !");
        }
    }

    private Reservation trouverReservation(int idReservation) {
        for (Reservation r : reservations) {
            if (r.getIdReservation() == idReservation) {
                return r;
            }
        }
        return null;
    }

    private int genererIdSejour() {
        int maxId = 0;
        for (Sejour s : sejours) {
            if (s.getIdSejour() > maxId) {
                maxId = s.getIdSejour();
            }
        }
        return maxId + 1;
    }
}
