package com.isimm.gestionstock.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Article {
    private final IntegerProperty id;
    private final StringProperty reference;
    private final StringProperty nom;
    private final StringProperty description;
    private final IntegerProperty typeProduitId;
    private final IntegerProperty stockMinimal;
    private final IntegerProperty stockActuel;
    private final DoubleProperty prixUnitaire;
    private final ObjectProperty<LocalDateTime> dateCreation;
    private final BooleanProperty estCritique;

    public Article() {
        this.id = new SimpleIntegerProperty();
        this.reference = new SimpleStringProperty();
        this.nom = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.typeProduitId = new SimpleIntegerProperty();
        this.stockMinimal = new SimpleIntegerProperty();
        this.stockActuel = new SimpleIntegerProperty();
        this.prixUnitaire = new SimpleDoubleProperty();
        this.dateCreation = new SimpleObjectProperty<>();
        this.estCritique = new SimpleBooleanProperty();
    }

    public Article(int id, String reference, String nom, String description,
                   int typeProduitId, int stockMinimal, int stockActuel,
                   double prixUnitaire, LocalDateTime dateCreation, boolean estCritique) {
        this();
        setId(id);
        setReference(reference);
        setNom(nom);
        setDescription(description);
        setTypeProduitId(typeProduitId);
        setStockMinimal(stockMinimal);
        setStockActuel(stockActuel);
        setPrixUnitaire(prixUnitaire);
        setDateCreation(dateCreation);
        setEstCritique(estCritique);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getReference() { return reference.get(); }
    public void setReference(String reference) { this.reference.set(reference); }
    public StringProperty referenceProperty() { return reference; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public int getTypeProduitId() { return typeProduitId.get(); }
    public void setTypeProduitId(int typeProduitId) { this.typeProduitId.set(typeProduitId); }
    public IntegerProperty typeProduitIdProperty() { return typeProduitId; }

    public int getStockMinimal() { return stockMinimal.get(); }
    public void setStockMinimal(int stockMinimal) { this.stockMinimal.set(stockMinimal); }
    public IntegerProperty stockMinimalProperty() { return stockMinimal; }

    public int getStockActuel() { return stockActuel.get(); }
    public void setStockActuel(int stockActuel) { this.stockActuel.set(stockActuel); }
    public IntegerProperty stockActuelProperty() { return stockActuel; }

    public double getPrixUnitaire() { return prixUnitaire.get(); }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire.set(prixUnitaire); }
    public DoubleProperty prixUnitaireProperty() { return prixUnitaire; }

    public LocalDateTime getDateCreation() { return dateCreation.get(); }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation.set(dateCreation); }
    public ObjectProperty<LocalDateTime> dateCreationProperty() { return dateCreation; }

    public boolean isEstCritique() { return estCritique.get(); }
    public void setEstCritique(boolean estCritique) { this.estCritique.set(estCritique); }
    public BooleanProperty estCritiqueProperty() { return estCritique; }

    public boolean isStockFaible() {
        return getStockActuel() <= getStockMinimal();
    }
}