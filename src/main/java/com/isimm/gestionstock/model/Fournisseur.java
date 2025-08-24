package com.isimm.gestionstock.model;

import javafx.beans.property.*;

public class Fournisseur {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty adresse;
    private final StringProperty telephone;
    private final StringProperty email;
    private final StringProperty contactPersonne;

    public Fournisseur() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.adresse = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.contactPersonne = new SimpleStringProperty();
    }

    public Fournisseur(int id, String nom, String adresse, String telephone,
                       String email, String contactPersonne) {
        this();
        setId(id);
        setNom(nom);
        setAdresse(adresse);
        setTelephone(telephone);
        setEmail(email);
        setContactPersonne(contactPersonne);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String adresse) { this.adresse.set(adresse); }
    public StringProperty adresseProperty() { return adresse; }

    public String getTelephone() { return telephone.get(); }
    public void setTelephone(String telephone) { this.telephone.set(telephone); }
    public StringProperty telephoneProperty() { return telephone; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getContactPersonne() { return contactPersonne.get(); }
    public void setContactPersonne(String contactPersonne) { this.contactPersonne.set(contactPersonne); }
    public StringProperty contactPersonneProperty() { return contactPersonne; }

    @Override
    public String toString() {
        return getNom();
    }
}
