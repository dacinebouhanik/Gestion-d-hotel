package model;

public class FemmeDeMenage extends Personne {
    private int idEmploye;


    public FemmeDeMenage(String nom, String prenom, String telephone, String email, int idEmploye) {
        super(nom, prenom, telephone, email);
        this.idEmploye = idEmploye;

    }

    public int getIdEmploye() {
        return idEmploye;
    }


    @Override
    public String toString() {
        return getNom() + " " + getPrenom();
    }

}
