package controller;

import model.Chambre;
import model.Hotel;
import vue.ChambresWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.*;

public class ChambresController {
    private ChambresWindow vue;
    private Hotel hotel;

    public ChambresController(ChambresWindow vue, Hotel hotel) {
        this.vue = vue;
        this.hotel = hotel;

        this.vue.getBoutonAjouter().addActionListener(e -> ajouterChambre());
        this.vue.getBoutonSupprimer().addActionListener(e -> supprimerChambre());
        this.vue.getBoutonModifier().addActionListener(e -> modifierChambre());
        this.vue.getBoutonRechercher().addActionListener(e -> rechercherChambre());
        this.vue.getComboTypeChambre().addActionListener(e -> mettrePrixAutomatique());
    }

    private void ajouterChambre() {
        try {
            int numero = Integer.parseInt(vue.getChampNumeroChambre().getText());
            if (Chambre.existeNumero(hotel.getChambres(), numero)) {
                JOptionPane.showMessageDialog(vue, "Ce numéro de chambre existe déjà !");
                return;
            }

            int id = genererIdChambre();
            int etage = Integer.parseInt(vue.getChampEtage().getText());
            String type = (String) vue.getComboTypeChambre().getSelectedItem();
            float prix = Float.parseFloat(vue.getChampPrix().getText());



            Chambre chambre = new Chambre(id, numero, etage, type, prix);
            hotel.getChambres().add(chambre);

            vue.getModel().addRow(new Object[]{id, numero, etage, type, prix});
            viderChamps();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vue, "Erreur : Numéro, Étage ou Prix doivent être des nombres !");
        }
    }


    private void supprimerChambre() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne != -1) {
            hotel.getChambres().remove(ligne);
            vue.getModel().removeRow(ligne);
        } else {
            JOptionPane.showMessageDialog(vue, "Sélectionnez une chambre à supprimer.");
        }
    }

    private void modifierChambre() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne != -1) {
            try {
                // Ancienne chambre
                Chambre ancienne = hotel.getChambres().get(ligne);

                int id = ancienne.getIdChambre();

                // Lire les champs ou conserver les anciennes valeurs
                int numero = vue.getChampNumeroChambre().getText().isBlank()
                        ? ancienne.getNumero()
                        : Integer.parseInt(vue.getChampNumeroChambre().getText());

                int etage = vue.getChampEtage().getText().isBlank()
                        ? ancienne.getEtage()
                        : Integer.parseInt(vue.getChampEtage().getText());

                float prix = vue.getChampPrix().getText().isBlank()
                        ? ancienne.getPrix()
                        : Float.parseFloat(vue.getChampPrix().getText());

                String type = (String) vue.getComboTypeChambre().getSelectedItem();


                Chambre chambre = new Chambre(id, numero, etage, type, prix);
                hotel.getChambres().set(ligne, chambre);

                vue.getModel().setValueAt(numero, ligne, 1);
                vue.getModel().setValueAt(etage, ligne, 2);
                vue.getModel().setValueAt(type, ligne, 3);
                vue.getModel().setValueAt(prix, ligne, 4);


                JOptionPane.showMessageDialog(vue, "Chambre modifiée !");
                viderChamps();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vue, "Erreur : Numéro, Étage ou Prix doivent être des nombres !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vue, "Erreur inattendue : " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(vue, "Sélectionnez une chambre à modifier.");
        }
    }


    private void rechercherChambre() {
        String recherche = vue.getChampRecherche().getText().toLowerCase();
        DefaultTableModel model = vue.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        vue.getTable().setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + recherche));
    }

    private void mettrePrixAutomatique() {
        String type = (String) vue.getComboTypeChambre().getSelectedItem();
        switch (type) {
            case "Simple": vue.getChampPrix().setText("50"); break;
            case "Double": vue.getChampPrix().setText("80"); break;
            case "Suite": vue.getChampPrix().setText("150"); break;
            case "Présidentielle": vue.getChampPrix().setText("300"); break;
            default: vue.getChampPrix().setText(""); break;
        }
    }
    public void afficherChambres() {
        DefaultTableModel model = vue.getModel();
        model.setRowCount(0);

        for (Chambre chambre : hotel.getChambres()) {
            model.addRow(new Object[]{
                    chambre.getIdChambre(),
                    chambre.getNumero(),
                    chambre.getEtage(),
                    chambre.getType(),
                    chambre.getPrix(),

            });
        }
    }

    private void viderChamps() {
        vue.getChampNumeroChambre().setText("");
        vue.getChampEtage().setText("");
        vue.getChampPrix().setText("");
        vue.getComboTypeChambre().setSelectedIndex(0);
        vue.getLabelEtat().setText("Disponible");
    }

    private int genererIdChambre() {
        int maxId = 0;
        for (Chambre chambre : hotel.getChambres()) {
            if (chambre.getIdChambre() > maxId) {
                maxId = chambre.getIdChambre();
            }
        }
        return maxId + 1;
    }
}
