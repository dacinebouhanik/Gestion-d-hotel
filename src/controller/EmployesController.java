package controller;

import model.*;
import vue.EmployesWindow;
import vue.MainMenuWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class EmployesController {
    private EmployesWindow vue;
    private Hotel hotel;
    private MainMenuWindow mainMenuWindow;

    public EmployesController(EmployesWindow vue, Hotel hotel, MainMenuWindow mainMenuWindow) {
        this.vue = vue;
        this.hotel = hotel;
        this.mainMenuWindow = mainMenuWindow;

        initController();
        ajouterRechercheDynamique();
    }

    public void afficherEmployes() {
        DefaultTableModel model = vue.getModel();
        model.setRowCount(0);

        int indexRec = 1;
        for (Receptionniste r : hotel.getReceptionnistes()) {
            model.addRow(new Object[]{
                    "REC-" + indexRec++,
                    r.getNom(),
                    r.getPrenom(),
                    r.getTelephone(),
                    r.getEmail(),
                    "Réceptionniste"
            });
        }

        int indexFem = 1;
        for (FemmeDeMenage f : hotel.getFemmesDeMenage()) {
            model.addRow(new Object[]{
                    "FEM-" + indexFem++,
                    f.getNom(),
                    f.getPrenom(),
                    f.getTelephone(),
                    f.getEmail(),
                    "Femme de Ménage"
            });
        }
    }

    private void ajouterRechercheDynamique() {
        vue.getChampRecherche().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String recherche = vue.getChampRecherche().getText().toLowerCase();
                DefaultTableModel model = vue.getModel();
                model.setRowCount(0);

                int indexRec = 1;
                for (Receptionniste r : hotel.getReceptionnistes()) {
                    String id = "rec-" + indexRec;
                    if (id.contains(recherche)
                            || r.getNom().toLowerCase().contains(recherche)
                            || r.getPrenom().toLowerCase().contains(recherche)
                            || r.getEmail().toLowerCase().contains(recherche)) {
                        model.addRow(new Object[]{
                                "REC-" + indexRec,
                                r.getNom(),
                                r.getPrenom(),
                                r.getTelephone(),
                                r.getEmail(),
                                "Réceptionniste"
                        });
                    }
                    indexRec++;
                }

                int indexFem = 1;
                for (FemmeDeMenage f : hotel.getFemmesDeMenage()) {
                    String id = "fem-" + indexFem;
                    if (id.contains(recherche)
                            || f.getNom().toLowerCase().contains(recherche)
                            || f.getPrenom().toLowerCase().contains(recherche)
                            || f.getEmail().toLowerCase().contains(recherche)) {
                        model.addRow(new Object[]{
                                "FEM-" + indexFem,
                                f.getNom(),
                                f.getPrenom(),
                                f.getTelephone(),
                                f.getEmail(),
                                "Femme de Ménage"
                        });
                    }
                    indexFem++;
                }
            }
        });
    }

    private void initController() {
        vue.getBoutonAjouter().addActionListener(e -> ajouterEmploye(e));
        vue.getBoutonModifier().addActionListener(e -> modifierEmploye(e));
    }

    private void ajouterEmploye(ActionEvent e) {
        String nom = vue.getChampNom().getText();
        String prenom = vue.getChampPrenom().getText();
        String tel = vue.getChampTel().getText();
        String email = vue.getChampEmail().getText();
        String role = (String) vue.getComboRole().getSelectedItem();

        if (role.equals("Réceptionniste")) {
            Receptionniste r = new Receptionniste(nom, prenom, tel, email, hotel.getReceptionnistes().size() + 1);
            hotel.getReceptionnistes().add(r);
        } else if (role.equals("Femme de Ménage")) {
            FemmeDeMenage f = new FemmeDeMenage(nom, prenom, tel, email, hotel.getFemmesDeMenage().size() + 1);
            hotel.getFemmesDeMenage().add(f);
        }

        rafraichirTable();
        viderChamps();
        mainMenuWindow.rafraichirMenageWindow();
    }

    private void modifierEmploye(ActionEvent e) {
        int selectedRow = vue.getTable().getSelectedRow();
        if (selectedRow == -1) return;

        String ancienRole = (String) vue.getModel().getValueAt(selectedRow, 5);
        String nouveauRole = vue.getComboRole().getSelectedItem().toString();

        String ancienNom = (String) vue.getModel().getValueAt(selectedRow, 1);
        String ancienPrenom = (String) vue.getModel().getValueAt(selectedRow, 2);
        String ancienTel = (String) vue.getModel().getValueAt(selectedRow, 3);
        String ancienEmail = (String) vue.getModel().getValueAt(selectedRow, 4);

        String nom = vue.getChampNom().getText().isBlank() ? ancienNom : vue.getChampNom().getText();
        String prenom = vue.getChampPrenom().getText().isBlank() ? ancienPrenom : vue.getChampPrenom().getText();
        String tel = vue.getChampTel().getText().isBlank() ? ancienTel : vue.getChampTel().getText();
        String email = vue.getChampEmail().getText().isBlank() ? ancienEmail : vue.getChampEmail().getText();

        if (ancienRole.equals(nouveauRole)) {
            if (ancienRole.equals("Réceptionniste")) {
                Receptionniste r = hotel.getReceptionnistes().get(selectedRowReceptionniste(selectedRow));
                r.setNom(nom);
                r.setPrenom(prenom);
                r.setTelephone(tel);
                r.setEmail(email);
            } else {
                FemmeDeMenage f = hotel.getFemmesDeMenage().get(selectedRowFemmeDeMenage(selectedRow));
                f.setNom(nom);
                f.setPrenom(prenom);
                f.setTelephone(tel);
                f.setEmail(email);
            }

            rafraichirTable();
            JOptionPane.showMessageDialog(vue, "Employé modifié !");
        } else {
            if (ancienRole.equals("Réceptionniste")) {
                Receptionniste r = hotel.getReceptionnistes().remove(selectedRowReceptionniste(selectedRow));
                int id = r.getIdEmploye();
                hotel.getFemmesDeMenage().add(new FemmeDeMenage(nom, prenom, tel, email, id));
            } else {
                FemmeDeMenage f = hotel.getFemmesDeMenage().remove(selectedRowFemmeDeMenage(selectedRow));
                int id = f.getIdEmploye();
                hotel.getReceptionnistes().add(new Receptionniste(nom, prenom, tel, email, id));
            }

            rafraichirTable();
            JOptionPane.showMessageDialog(vue, "Employé modifié et rôle mis à jour !");
        }
    }

    private void rafraichirTable() {
        vue.getModel().setRowCount(0);

        int indexRec = 1;
        for (Receptionniste r : hotel.getReceptionnistes()) {
            vue.getModel().addRow(new Object[]{
                    "REC-" + indexRec++,
                    r.getNom(),
                    r.getPrenom(),
                    r.getTelephone(),
                    r.getEmail(),
                    "Réceptionniste"
            });
        }

        int indexFem = 1;
        for (FemmeDeMenage f : hotel.getFemmesDeMenage()) {
            vue.getModel().addRow(new Object[]{
                    "FEM-" + indexFem++,
                    f.getNom(),
                    f.getPrenom(),
                    f.getTelephone(),
                    f.getEmail(),
                    "Femme de Ménage"
            });
        }
    }

    private void viderChamps() {
        vue.getChampNom().setText("");
        vue.getChampPrenom().setText("");
        vue.getChampTel().setText("");
        vue.getChampEmail().setText("");
        vue.getComboRole().setSelectedIndex(0);
    }

    private int selectedRowReceptionniste(int tableRow) {
        int countReceptionnistes = hotel.getReceptionnistes().size();
        if (tableRow < countReceptionnistes) return tableRow;
        else return -1;
    }

    private int selectedRowFemmeDeMenage(int tableRow) {
        int countReceptionnistes = hotel.getReceptionnistes().size();
        return tableRow - countReceptionnistes;
    }
}
