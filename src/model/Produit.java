package model;

public class Produit {

    private  int id_produit;
    private double prix;
    private  String nomProduit;
    public Produit(int idProduit, String nomProduit, float prix) {
        this.id_produit = idProduit;
        this.nomProduit = nomProduit;
        this.prix = prix;
    }


    public String getNomProduit() {
        return nomProduit;
    }

    public int getId_produit() {
        return id_produit;
    }

    public double getPrix() {
        return prix;
    }
}
