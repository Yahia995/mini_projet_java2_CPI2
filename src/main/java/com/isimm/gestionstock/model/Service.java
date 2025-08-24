package com.isimm.gestionstock.model;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class Service {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty responsable;
    private final StringProperty telephone;
    private final StringProperty email;
    private final ObjectProperty<BigDecimal> budgetAnnuel;

    public Service() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.responsable = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.budgetAnnuel = new SimpleObjectProperty<>();
    }

    public Service(int id, String nom, String responsable,
                   String telephone, String email, BigDecimal budgetAnnuel) {
        this();
        setId(id);
        setNom(nom);
        setResponsable(responsable);
        setTelephone(telephone);
        setEmail(email);
        setBudgetAnnuel(budgetAnnuel);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getResponsable() { return responsable.get(); }
    public void setResponsable(String responsable) { this.responsable.set(responsable); }
    public StringProperty responsableProperty() { return responsable; }

    public String getTelephone() { return telephone.get(); }
    public void setTelephone(String telephone) { this.telephone.set(telephone); }
    public StringProperty telephoneProperty() { return telephone; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public BigDecimal getBudgetAnnuel() { return budgetAnnuel.get(); }
    public void setBudgetAnnuel(BigDecimal budgetAnnuel) { this.budgetAnnuel.set(budgetAnnuel); }
    public ObjectProperty<BigDecimal> budgetAnnuelProperty() { return budgetAnnuel; }

    @Override
    public String toString() {
        return getNom();
    }
}
