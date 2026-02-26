package model;
import java.util.Vector;
public class Chambre {
    private int idChambre;
    private int numero;
    private int etage;
    private String type;
    private float prix;


    public Chambre(int idChambre, int numero, int etage, String type, float prix) {
        this.idChambre = idChambre;
        this.numero = numero;
        this.etage = etage;
        this.type = type;
        this.prix = prix;

    }


    public int getIdChambre() {
        return idChambre;
    }

    public int getNumero() {
        return numero;
    }

    public int getEtage() {
        return etage;
    }

    public String getType() {
        return type;
    }

    public float getPrix() {
        return prix;
    }


    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }




    @Override
    public String toString() {
        return "Chambre " + numero + " (" + type + ")";
    }
    public static boolean existeNumero(Vector<Chambre> chambres, int numero) {
        for (Chambre c : chambres) {
            if (c.getNumero() == numero) return true;
        }
        return false;
    }

}
