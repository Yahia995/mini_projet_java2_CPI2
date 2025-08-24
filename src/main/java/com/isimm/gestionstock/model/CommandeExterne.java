package com.isimm.gestionstock.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class CommandeExterne {
    private final IntegerProperty id;
    private final StringProperty numeroCommande;
    private final IntegerProperty fournisseurId;
    private final ObjectProperty<LocalDateTime> dateCommande;
    private final StringProperty statut;
    private final DoubleProperty montantTotal;
    private final StringProperty observations;

    public CommandeExterne() {
        this.id = new SimpleIntegerProperty();
        this.numeroCommande = new SimpleStringProperty();
        this.fournisseurId = new SimpleIntegerProperty();
        this.dateCommande = new SimpleObjectProperty<>();
        this.statut = new SimpleStringProperty();
        this.montantTotal = new SimpleDoubleProperty();
        this.observations = new SimpleStringProperty();
    }

    public CommandeExterne(int id, String numeroCommande, int fournisseurId,
                           LocalDateTime dateCommande, String statut, double montantTotal,
                           String observations) {
        this();
        setId(id);
        setNumeroCommande(numeroCommande);
        setFournisseurId(fournisseurId);
        setDateCommande(dateCommande);
        setStatut(statut);
        setMontantTotal(montantTotal);
        setObservations(observations);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNumeroCommande() { return numeroCommande.get(); }
    public void setNumeroCommande(String numeroCommande) { this.numeroCommande.set(numeroCommande); }
    public StringProperty numeroCommandeProperty() { return numeroCommande; }

    public int getFournisseurId() { return fournisseurId.get(); }
    public void setFournisseurId(int fournisseurId) { this.fournisseurId.set(fournisseurId); }
    public IntegerProperty fournisseurIdProperty() { return fournisseurId; }

    public LocalDateTime getDateCommande() { return dateCommande.get(); }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande.set(dateCommande); }
    public ObjectProperty<LocalDateTime> dateCommandeProperty() { return dateCommande; }

    public String getStatut() { return statut.get(); }
    public void setStatut(String statut) { this.statut.set(statut); }
    public StringProperty statutProperty() { return statut; }

    public double getMontantTotal() { return montantTotal.get(); }
    public void setMontantTotal(double montantTotal) { this.montantTotal.set(montantTotal); }
    public DoubleProperty montantTotalProperty() { return montantTotal; }

    public String getObservations() { return observations.get(); }
    public void setObservations(String observations) { this.observations.set(observations); }
    public StringProperty observationsProperty() { return observations; }
}
