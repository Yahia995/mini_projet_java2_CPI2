package com.isimm.gestionstock.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Inventaire {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> dateInventaire;
    private final IntegerProperty localId;
    private final StringProperty localNom;
    private final StringProperty statut;
    private final StringProperty commentaire;

    public Inventaire() {
        this.id = new SimpleIntegerProperty();
        this.dateInventaire = new SimpleObjectProperty<>();
        this.localId = new SimpleIntegerProperty();
        this.localNom = new SimpleStringProperty();
        this.statut = new SimpleStringProperty();
        this.commentaire = new SimpleStringProperty();
    }

    public Inventaire(int id, LocalDate dateInventaire, int localId, String localNom, String statut, String commentaire) {
        this();
        setId(id);
        setDateInventaire(dateInventaire);
        setLocalId(localId);
        setLocalNom(localNom);
        setStatut(statut);
        setCommentaire(commentaire);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public LocalDate getDateInventaire() { return dateInventaire.get(); }
    public void setDateInventaire(LocalDate dateInventaire) { this.dateInventaire.set(dateInventaire); }
    public ObjectProperty<LocalDate> dateInventaireProperty() { return dateInventaire; }

    public int getLocalId() { return localId.get(); }
    public void setLocalId(int localId) { this.localId.set(localId); }
    public IntegerProperty localIdProperty() { return localId; }

    public String getLocalNom() { return localNom.get(); }
    public void setLocalNom(String localNom) { this.localNom.set(localNom); }
    public StringProperty localNomProperty() { return localNom; }

    public String getStatut() { return statut.get(); }
    public void setStatut(String statut) { this.statut.set(statut); }
    public StringProperty statutProperty() { return statut; }

    public String getCommentaire() { return commentaire.get(); }
    public void setCommentaire(String commentaire) { this.commentaire.set(commentaire); }
    public StringProperty commentaireProperty() { return commentaire; }

    @Override
    public String toString() {
        return "Inventaire{" +
                "id=" + getId() +
                ", dateInventaire=" + getDateInventaire() +
                ", localNom='" + getLocalNom() + '\'' +
                ", statut='" + getStatut() + '\'' +
                '}';
    }
}
