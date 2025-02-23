package com.example.location.model;
public class Offre {
    private String titre;
    private String description;
    private double prix;

    // Constructeur vide requis pour Firestore
    public Offre() {}

    public Offre(String titre, String description, double prix) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
    }

    // Getters
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }
}
