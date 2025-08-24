package com.isimm.gestionstock.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private BorderPane mainBorderPane;
    @FXML private MenuBar menuBar;
    @FXML private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusLabel.setText("Application démarrée - Prêt");
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadView("/fxml/DashboardView.fxml");
        statusLabel.setText("Tableau de bord");
    }

    @FXML
    private void showArticles() {
        loadView("/fxml/ArticlesView.fxml");
        statusLabel.setText("Gestion des articles");
    }

    @FXML
    private void showLocaux() {
        loadView("/fxml/LocauxView.fxml");
        statusLabel.setText("Gestion des locaux");
    }

    @FXML
    private void showFournisseurs() {
        loadView("/fxml/FournisseursView.fxml");
        statusLabel.setText("Gestion des fournisseurs");
    }

    @FXML
    private void showServices() {
        loadView("/fxml/ServicesView.fxml");
        statusLabel.setText("Gestion des services");
    }

    @FXML
    private void showCommandesExternes() {
        loadView("/fxml/CommandesExternesView.fxml");
        statusLabel.setText("Commandes externes");
    }

    @FXML
    private void showCommandesInternes() {
        loadView("/fxml/CommandesInternesView.fxml");
        statusLabel.setText("Commandes internes");
    }

    @FXML
    private void showInventaire() {
        loadView("/fxml/InventaireView.fxml");
        statusLabel.setText("Inventaire");
    }

    @FXML
    private void showStatistiques() {
        loadView("/fxml/StatistiquesView.fxml");
        statusLabel.setText("Statistiques");
    }

    @FXML
    private void showAlertes() {
        loadView("/fxml/AlertesView.fxml");
        statusLabel.setText("Alertes stock");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue : " + fxmlPath);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Gestion de Stock ISIMM");
        alert.setContentText("Application de gestion de stock pour l'ISIMM\nVersion 1.0\nAnnée Universitaire 2024/2025");
        alert.showAndWait();
    }
}