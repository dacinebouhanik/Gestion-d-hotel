package model;

public class Client extends Personne {
    private int idClient;

    public Client(int idClient, String nom, String prenom, String telephone, String email) {
        super(nom, prenom, telephone, email);
        this.idClient = idClient;
    }

    public int getIdClient() {
        return idClient;
    }

    @Override
    public String toString() {
        return getNom() + " " + getPrenom();
    }
}
