package com.isimm.gestionstock.model;

import javafx.beans.property.*;

public class Local {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty typeLocal;
    private final IntegerProperty capacite;
    private final StringProperty description;

    public Local() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.typeLocal = new SimpleStringProperty();
        this.capacite = new SimpleIntegerProperty();
        this.description = new SimpleStringProperty();
    }

    public Local(int id, String nom, String typeLocal, int capacite, String description) {
        this();
        setId(id);
        setNom(nom);
        setTypeLocal(typeLocal);
        setCapacite(capacite);
        setDescription(description);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getTypeLocal() { return typeLocal.get(); }
    public void setTypeLocal(String typeLocal) { this.typeLocal.set(typeLocal); }
    public StringProperty typeLocalProperty() { return typeLocal; }

    public int getCapacite() { return capacite.get(); }
    public void setCapacite(int capacite) { this.capacite.set(capacite); }
    public IntegerProperty capaciteProperty() { return capacite; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    @Override
    public String toString() {
        return getNom() + " (" + getTypeLocal() + ")";
    }
}