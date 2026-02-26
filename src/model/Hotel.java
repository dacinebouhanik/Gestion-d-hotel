package model;

import java.util.Vector;

public class Hotel {
    private Vector<Chambre> chambres;
    private Vector<Client> clients;
    private Vector<Reservation> reservations;
    private Vector<Sejour> sejours;
    private Vector<Receptionniste> receptionnistes;
    private Vector<FemmeDeMenage> femmesDeMenage;
    private Vector<Menage> menages;
    private Vector<Personne> employes = new Vector<>();

    public Hotel() {
        chambres = new Vector<>();
        clients = new Vector<>();
        reservations = new Vector<>();
        sejours = new Vector<>();
        receptionnistes = new Vector<>();
        femmesDeMenage = new Vector<>();
        menages = new Vector<>();
    }

    public Vector<Personne> getEmployes() {
        return employes;
    }

    public Vector<Chambre> getChambres() { return chambres; }
    public Vector<Client> getClients() { return clients; }
    public Vector<Reservation> getReservations() { return reservations; }
    public Vector<Sejour> getSejours() { return sejours; }
    public Vector<Receptionniste> getReceptionnistes() { return receptionnistes; }
    public Vector<FemmeDeMenage> getFemmesDeMenage() { return femmesDeMenage; }
    public Vector<Menage> getMenages() { return menages; }
}
