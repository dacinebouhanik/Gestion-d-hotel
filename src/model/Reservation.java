package model;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Reservation {
    private int idReservation;
    private int nbrNuits;
    private float prix;
    private Date dateDebut;
    private Date dateFin;
    private Client client;
    private Chambre chambre;
    private Receptionniste receptionniste;
    private Sejour sejour;

    public Reservation(int idReservation, int nbrNuits, float prix, Date dateDebut, Date dateFin,
                       Client client, Chambre chambre, Receptionniste receptionniste, Sejour sejour) {
        this.idReservation = idReservation;
        this.nbrNuits = nbrNuits;
        this.prix = prix;
        this.dateDebut = normaliserDate(dateDebut);
        this.dateFin = normaliserDate(dateFin);
        this.client = client;
        this.chambre = chambre;
        this.receptionniste = receptionniste;
        this.sejour = sejour;
    }



    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public int getNbrNuits() { return nbrNuits; }
    public void setNbrNuits(int nbrNuits) { this.nbrNuits = nbrNuits; }

    public float getPrix() { return prix; }
    public void setPrix(float prix) { this.prix = prix; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = normaliserDate(dateDebut); }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = normaliserDate(dateFin); }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }

    public Receptionniste getReceptionniste() { return receptionniste; }
    public void setReceptionniste(Receptionniste receptionniste) { this.receptionniste = receptionniste; }

    public Sejour getSejour() { return sejour; }
    public void setSejour(Sejour sejour) { this.sejour = sejour; }



    public static Date normaliserDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }





    public static Vector<Chambre> chercherChambresDisponibles(Date debut, Date fin, String typeChambre,
                                                              Vector<Chambre> chambres, Vector<Reservation> reservations) {
        Vector<Chambre> disponibles = new Vector<>();
        Date d1 = normaliserDate(debut);
        Date d2 = normaliserDate(fin);

        for (Chambre ch : chambres) {
            if (!ch.getType().equalsIgnoreCase(typeChambre)) continue;

            boolean libre = true;
            for (Reservation r : reservations) {
                if (r.getChambre().getIdChambre() == ch.getIdChambre()) {
                    Date rd1 = normaliserDate(r.getDateDebut());
                    Date rd2 = normaliserDate(r.getDateFin());

                    if (!(d2.before(rd1) || d1.after(rd2))) {
                        libre = false;
                        break;
                    }
                }
            }
            if (libre) disponibles.add(ch);
        }
        return disponibles;
    }
}
