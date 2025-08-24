package com.isimm.gestionstock.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Alerte {
    private final IntegerProperty id;
    private final StringProperty type;
    private final StringProperty message;
    private final StringProperty articleReference;
    private final StringProperty articleNom;
    private final IntegerProperty stockActuel;
    private final IntegerProperty stockMinimal;
    private final ObjectProperty<LocalDate> datePeremption;
    private final StringProperty priorite;
    private final BooleanProperty traitee;

    public Alerte() {
        this.id = new SimpleIntegerProperty();
        this.type = new SimpleStringProperty();
        this.message = new SimpleStringProperty();
        this.articleReference = new SimpleStringProperty();
        this.articleNom = new SimpleStringProperty();
        this.stockActuel = new SimpleIntegerProperty();
        this.stockMinimal = new SimpleIntegerProperty();
        this.datePeremption = new SimpleObjectProperty<>();
        this.priorite = new SimpleStringProperty();
        this.traitee = new SimpleBooleanProperty();
    }

    public Alerte(int id, String type, String message, String articleReference, String articleNom,
                  int stockActuel, int stockMinimal, LocalDate datePeremption, String priorite, boolean traitee) {
        this();
        setId(id);
        setType(type);
        setMessage(message);
        setArticleReference(articleReference);
        setArticleNom(articleNom);
        setStockActuel(stockActuel);
        setStockMinimal(stockMinimal);
        setDatePeremption(datePeremption);
        setPriorite(priorite);
        setTraitee(traitee);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }
    public StringProperty typeProperty() { return type; }

    public String getMessage() { return message.get(); }
    public void setMessage(String message) { this.message.set(message); }
    public StringProperty messageProperty() { return message; }

    public String getArticleReference() { return articleReference.get(); }
    public void setArticleReference(String articleReference) { this.articleReference.set(articleReference); }
    public StringProperty articleReferenceProperty() { return articleReference; }

    public String getArticleNom() { return articleNom.get(); }
    public void setArticleNom(String articleNom) { this.articleNom.set(articleNom); }
    public StringProperty articleNomProperty() { return articleNom; }

    public int getStockActuel() { return stockActuel.get(); }
    public void setStockActuel(int stockActuel) { this.stockActuel.set(stockActuel); }
    public IntegerProperty stockActuelProperty() { return stockActuel; }

    public int getStockMinimal() { return stockMinimal.get(); }
    public void setStockMinimal(int stockMinimal) { this.stockMinimal.set(stockMinimal); }
    public IntegerProperty stockMinimalProperty() { return stockMinimal; }

    public LocalDate getDatePeremption() { return datePeremption.get(); }
    public void setDatePeremption(LocalDate datePeremption) { this.datePeremption.set(datePeremption); }
    public ObjectProperty<LocalDate> datePeremptionProperty() { return datePeremption; }

    public String getPriorite() { return priorite.get(); }
    public void setPriorite(String priorite) { this.priorite.set(priorite); }
    public StringProperty prioriteProperty() { return priorite; }

    public boolean isTraitee() { return traitee.get(); }
    public void setTraitee(boolean traitee) { this.traitee.set(traitee); }
    public BooleanProperty traiteeProperty() { return traitee; }

    @Override
    public String toString() {
        return "Alerte{" +
                "type='" + getType() + '\'' +
                ", message='" + getMessage() + '\'' +
                ", articleReference='" + getArticleReference() + '\'' +
                ", priorite='" + getPriorite() + '\'' +
                '}';
    }
}
