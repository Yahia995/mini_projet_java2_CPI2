package com.isimm.gestionstock.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class CommandeInterne {
    private final IntegerProperty id;
    private final StringProperty numeroCommande;
    private final IntegerProperty serviceId;
    private final StringProperty serviceNom;
    private final ObjectProperty<LocalDate> dateCommande;
    private final ObjectProperty<LocalDate> dateLivraison;
    private final StringProperty statut;

    public CommandeInterne() {
        this.id = new SimpleIntegerProperty();
        this.numeroCommande = new SimpleStringProperty();
        this.serviceId = new SimpleIntegerProperty();
        this.serviceNom = new SimpleStringProperty();
        this.dateCommande = new SimpleObjectProperty<>();
        this.dateLivraison = new SimpleObjectProperty<>();
        this.statut = new SimpleStringProperty();
    }

    public CommandeInterne(int id, String numeroCommande, int serviceId, String serviceNom,
                           LocalDate dateCommande, LocalDate dateLivraison, String statut) {
        this();
        setId(id);
        setNumeroCommande(numeroCommande);
        setServiceId(serviceId);
        setServiceNom(serviceNom);
        setDateCommande(dateCommande);
        setDateLivraison(dateLivraison);
        setStatut(statut);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNumeroCommande() { return numeroCommande.get(); }
    public void setNumeroCommande(String numeroCommande) { this.numeroCommande.set(numeroCommande); }
    public StringProperty numeroCommandeProperty() { return numeroCommande; }

    public int getServiceId() { return serviceId.get(); }
    public void setServiceId(int serviceId) { this.serviceId.set(serviceId); }
    public IntegerProperty serviceIdProperty() { return serviceId; }

    public String getServiceNom() { return serviceNom.get(); }
    public void setServiceNom(String serviceNom) { this.serviceNom.set(serviceNom); }
    public StringProperty serviceNomProperty() { return serviceNom; }

    public LocalDate getDateCommande() { return dateCommande.get(); }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande.set(dateCommande); }
    public ObjectProperty<LocalDate> dateCommandeProperty() { return dateCommande; }

    public LocalDate getDateLivraison() { return dateLivraison.get(); }
    public void setDateLivraison(LocalDate dateLivraison) { this.dateLivraison.set(dateLivraison); }
    public ObjectProperty<LocalDate> dateLivraisonProperty() { return dateLivraison; }

    public String getStatut() { return statut.get(); }
    public void setStatut(String statut) { this.statut.set(statut); }
    public StringProperty statutProperty() { return statut; }

    @Override
    public String toString() {
        return "CommandeInterne{" +
                "id=" + getId() +
                ", numeroCommande='" + getNumeroCommande() + '\'' +
                ", serviceNom='" + getServiceNom() + '\'' +
                ", dateCommande=" + getDateCommande() +
                ", statut='" + getStatut() + '\'' +
                '}';
    }
}
