package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sejour {

    private int idSejour;
    private Reservation reservation;
    private Map<Produit, Integer> consommations;

    public Sejour(int idSejour, Reservation reservation) {
        this.idSejour = idSejour;
        this.reservation = reservation;
        this.consommations = new HashMap<>();
    }

    public int getIdSejour() {
        return idSejour;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Map<Produit, Integer> getConsommations() {
        return consommations;
    }

    public void ajouterConsommation(Produit produit, int quantite) {
        consommations.put(produit, consommations.getOrDefault(produit, 0) + quantite);
    }

    public float calculerTotal() {

        long diff = reservation.getDateFin().getTime() - reservation.getDateDebut().getTime();
        int nbNuits = (int) Math.max(diff / (1000 * 60 * 60 * 24), 1);


        float prixNuits = nbNuits * reservation.getChambre().getPrix();


        float totalConso = 0;
        for (Map.Entry<Produit, Integer> entry : consommations.entrySet()) {
            totalConso += entry.getKey().getPrix() * entry.getValue();
        }

        return prixNuits + totalConso;
    }

    @Override
    public String toString() {
        return "Séjour  : " + idSejour;
    }
}
