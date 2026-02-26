package controller;

import model.Client;
import model.Hotel;
import vue.ClientsWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

public class ClientsController {
    private ClientsWindow vue;
    private Hotel hotel;

    public ClientsController(ClientsWindow vue, Hotel hotel) {
        this.vue = vue;
        this.hotel = hotel;

        this.vue.getBoutonAjouter().addActionListener(e -> ajouterClient());
        this.vue.getBoutonSupprimer().addActionListener(e -> supprimerClient());
        this.vue.getBoutonModifier().addActionListener(e -> modifierClient());
        this.vue.getBoutonRechercher().addActionListener(e -> rechercherClient());

        afficherTousLesClients();
    }

    private void ajouterClient() {
        try {
            int id = genererIdClient();
            String nom = vue.getChampNom().getText();
            String prenom = vue.getChampPrenom().getText();
            String tel = vue.getChampTel().getText();
            String email = vue.getChampEmail().getText();

            Client c = new Client(id, nom, prenom, tel, email);
            hotel.getClients().add(c);

            vue.getModel().addRow(new Object[]{id, nom, prenom, tel, email});
            viderChamps();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vue, "Erreur lors de l'ajout du client !");
        }
    }

    private void supprimerClient() {
        int vueIndex = vue.getTable().getSelectedRow();
        if (vueIndex != -1) {
            int modelIndex = vue.getTable().convertRowIndexToModel(vueIndex);
            hotel.getClients().remove(modelIndex);
            vue.getModel().removeRow(modelIndex);
        } else {
            JOptionPane.showMessageDialog(vue, "Sélectionnez un client à supprimer.");
        }
    }

    private void modifierClient() {
        int vueIndex = vue.getTable().getSelectedRow();
        if (vueIndex == -1) {
            JOptionPane.showMessageDialog(vue, "Sélectionnez un client à modifier.");
            return;
        }

        try {
            int modelIndex = vue.getTable().convertRowIndexToModel(vueIndex);
            Client ancien = hotel.getClients().get(modelIndex);

            String nom = vue.getChampNom().getText().isBlank() ? ancien.getNom() : vue.getChampNom().getText();
            String prenom = vue.getChampPrenom().getText().isBlank() ? ancien.getPrenom() : vue.getChampPrenom().getText();
            String tel = vue.getChampTel().getText().isBlank() ? ancien.getTelephone() : vue.getChampTel().getText();
            String email = vue.getChampEmail().getText().isBlank() ? ancien.getEmail() : vue.getChampEmail().getText();

            int id = ancien.getIdClient();
            Client nouveau = new Client(id, nom, prenom, tel, email);

            hotel.getClients().set(modelIndex, nouveau);
            vue.getModel().setValueAt(nom, modelIndex, 1);
            vue.getModel().setValueAt(prenom, modelIndex, 2);
            vue.getModel().setValueAt(tel, modelIndex, 3);
            vue.getModel().setValueAt(email, modelIndex, 4);

            JOptionPane.showMessageDialog(vue, "Client modifié !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vue, "Erreur lors de la modification.");
        }
    }

    private void rechercherClient() {
        String recherche = vue.getChampRecherche().getText().toLowerCase();
        DefaultTableModel model = vue.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        vue.getTable().setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + recherche));
    }

    private void afficherTousLesClients() {
        DefaultTableModel model = vue.getModel();
        model.setRowCount(0);
        for (Client c : hotel.getClients()) {
            model.addRow(new Object[]{c.getIdClient(), c.getNom(), c.getPrenom(), c.getTelephone(), c.getEmail()});
        }
    }

    private void viderChamps() {
        vue.getChampNom().setText("");
        vue.getChampPrenom().setText("");
        vue.getChampTel().setText("");
        vue.getChampEmail().setText("");
    }

    private int genererIdClient() {
        int maxId = 0;
        for (Client client : hotel.getClients()) {
            if (client.getIdClient() > maxId) {
                maxId = client.getIdClient();
            }
        }
        return maxId + 1;
    }
}
