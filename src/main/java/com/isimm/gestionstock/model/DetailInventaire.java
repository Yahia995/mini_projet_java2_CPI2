package com.isimm.gestionstock.model;

import javafx.beans.property.*;

public class DetailInventaire {
    private final IntegerProperty id;
    private final IntegerProperty inventaireId;
    private final IntegerProperty articleId;
    private final StringProperty articleNom;
    private final IntegerProperty quantiteTheorique;
    private final IntegerProperty quantiteReelle;
    private final IntegerProperty ecart;

    public DetailInventaire() {
        this.id = new SimpleIntegerProperty();
        this.inventaireId = new SimpleIntegerProperty();
        this.articleId = new SimpleIntegerProperty();
        this.articleNom = new SimpleStringProperty();
        this.quantiteTheorique = new SimpleIntegerProperty();
        this.quantiteReelle = new SimpleIntegerProperty();
        this.ecart = new SimpleIntegerProperty();
    }

    public DetailInventaire(int id, int inventaireId, int articleId, String articleNom,
                            int quantiteTheorique, int quantiteReelle, int ecart) {
        this();
        setId(id);
        setInventaireId(inventaireId);
        setArticleId(articleId);
        setArticleNom(articleNom);
        setQuantiteTheorique(quantiteTheorique);
        setQuantiteReelle(quantiteReelle);
        setEcart(ecart);
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public int getInventaireId() { return inventaireId.get(); }
    public void setInventaireId(int inventaireId) { this.inventaireId.set(inventaireId); }
    public IntegerProperty inventaireIdProperty() { return inventaireId; }

    public int getArticleId() { return articleId.get(); }
    public void setArticleId(int articleId) { this.articleId.set(articleId); }
    public IntegerProperty articleIdProperty() { return articleId; }

    public String getArticleNom() { return articleNom.get(); }
    public void setArticleNom(String articleNom) { this.articleNom.set(articleNom); }
    public StringProperty articleNomProperty() { return articleNom; }

    public int getQuantiteTheorique() { return quantiteTheorique.get(); }
    public void setQuantiteTheorique(int quantiteTheorique) { this.quantiteTheorique.set(quantiteTheorique); }
    public IntegerProperty quantiteTheoriqueProperty() { return quantiteTheorique; }

    public int getQuantiteReelle() { return quantiteReelle.get(); }
    public void setQuantiteReelle(int quantiteReelle) { this.quantiteReelle.set(quantiteReelle); }
    public IntegerProperty quantiteReelleProperty() { return quantiteReelle; }

    public int getEcart() { return ecart.get(); }
    public void setEcart(int ecart) { this.ecart.set(ecart); }
    public IntegerProperty ecartProperty() { return ecart; }

    @Override
    public String toString() {
        return "DetailInventaire{" +
                "articleNom='" + getArticleNom() + '\'' +
                ", quantiteTheorique=" + getQuantiteTheorique() +
                ", quantiteReelle=" + getQuantiteReelle() +
                ", ecart=" + getEcart() +
                '}';
    }
}
