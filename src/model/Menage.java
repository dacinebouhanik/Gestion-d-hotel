package model;

import java.util.Date;

public class Menage {
    private Chambre chambre;
    private FemmeDeMenage femmeDeMenage;
    private Date dateMenage;

    public Menage(Chambre chambre, FemmeDeMenage femmeDeMenage, Date dateMenage) {
        this.chambre = chambre;
        this.femmeDeMenage = femmeDeMenage;
        this.dateMenage = dateMenage;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public FemmeDeMenage getFemmeDeMenage() {
        return femmeDeMenage;
    }

    public Date getDateMenage() {
        return dateMenage;
    }
}
