package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.ArticleDAO;
import com.isimm.gestionstock.dao.FournisseurDAO;
import com.isimm.gestionstock.dao.LocalDAO;
import com.isimm.gestionstock.dao.ServiceDAO;
import com.isimm.gestionstock.model.Article;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label totalArticlesLabel;
    @FXML private Label alertesStockLabel;
    @FXML private Label articlesCritiquesLabel;
    @FXML private Label totalFournisseursLabel;
    @FXML private Label totalLocauxLabel;
    @FXML private Label totalServicesLabel;

    @FXML private TableView<Article> alertesTable;
    @FXML private TableColumn<Article, String> alerteReferenceColumn;
    @FXML private TableColumn<Article, String> alerteNomColumn;
    @FXML private TableColumn<Article, Integer> alerteStockMinColumn;
    @FXML private TableColumn<Article, Integer> alerteStockActColumn;
    @FXML private TableColumn<Article, String> alerteStatutColumn;

    private ArticleDAO articleDAO;
    private FournisseurDAO fournisseurDAO;
    private LocalDAO localDAO;
    private ServiceDAO serviceDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        articleDAO = new ArticleDAO();
        fournisseurDAO = new FournisseurDAO();
        localDAO = new LocalDAO();
        serviceDAO = new ServiceDAO();

        // Configuration des colonnes du tableau d'alertes
        alerteReferenceColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        alerteNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        alerteStockMinColumn.setCellValueFactory(new PropertyValueFactory<>("stockMinimal"));
        alerteStockActColumn.setCellValueFactory(new PropertyValueFactory<>("stockActuel"));

        // Colonne statut personnalisée
        alerteStatutColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            if (article.getStockActuel() == 0) {
                return new javafx.beans.property.SimpleStringProperty("RUPTURE");
            } else if (article.getStockActuel() <= article.getStockMinimal()) {
                return new javafx.beans.property.SimpleStringProperty("ALERTE");
            } else {
                return new javafx.beans.property.SimpleStringProperty("OK");
            }
        });

        // Coloration des lignes selon le statut
        alertesTable.setRowFactory(tv -> new TableRow<Article>() {
            @Override
            protected void updateItem(Article item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.getStockActuel() == 0) {
                    setStyle("-fx-background-color: #ffcdd2;");
                } else if (item.getStockActuel() <= item.getStockMinimal()) {
                    setStyle("-fx-background-color: #fff3e0;");
                } else {
                    setStyle("");
                }
            }
        });

        // Chargement des données
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Chargement des statistiques
        ObservableList<Article> allArticles = articleDAO.getAllArticles();
        ObservableList<Article> alertesStock = articleDAO.getArticlesEnAlerte();
        ObservableList<Article> articlesCritiques = articleDAO.getArticlesCritiques();

        totalArticlesLabel.setText(String.valueOf(allArticles.size()));
        alertesStockLabel.setText(String.valueOf(alertesStock.size()));
        articlesCritiquesLabel.setText(String.valueOf(articlesCritiques.size()));

        totalFournisseursLabel.setText(String.valueOf(fournisseurDAO.getAllFournisseurs().size()));
        totalLocauxLabel.setText(String.valueOf(localDAO.getAllLocaux().size()));
        totalServicesLabel.setText(String.valueOf(serviceDAO.getAllServices().size()));

        // Chargement du tableau d'alertes
        alertesTable.setItems(alertesStock);
    }

    public void refreshData() {
        loadDashboardData();
    }
}
