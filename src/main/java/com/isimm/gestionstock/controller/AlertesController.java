package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.AlerteDAO;
import com.isimm.gestionstock.model.Alerte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AlertesController implements Initializable {

    @FXML private TableView<Alerte> tableAlertes;
    @FXML private TableColumn<Alerte, Integer> colId;
    @FXML private TableColumn<Alerte, String> colType;
    @FXML private TableColumn<Alerte, String> colMessage;
    @FXML private TableColumn<Alerte, String> colNiveauGravite;
    @FXML private TableColumn<Alerte, LocalDateTime> colDateCreation;
    @FXML private TableColumn<Alerte, String> colStatut;

    @FXML private ComboBox<String> cmbFiltreType;
    @FXML private ComboBox<String> cmbFiltreGravite;
    @FXML private ComboBox<String> cmbFiltreStatut;
    @FXML private TextField txtRecherche;
    @FXML private Button btnActualiser;
    @FXML private Button btnMarquerLue;
    @FXML private Button btnMarquerResolue;
    @FXML private Button btnSupprimer;

    @FXML private TextArea txtDetailsAlerte;

    private AlerteDAO alerteDAO;
    private ObservableList<Alerte> alertesList;
    private Alerte alerteSelectionnee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alerteDAO = new AlerteDAO();
        alertesList = FXCollections.observableArrayList();

        setupTableColumns();
        setupFilters();
        loadAlertes();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colNiveauGravite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
        colStatut.setCellValueFactory(cellData -> {
            boolean traitee = cellData.getValue().isTraitee();
            return new javafx.beans.property.SimpleStringProperty(traitee ? "TRAITEE" : "NON_TRAITEE");
        });

        tableAlertes.setRowFactory(tv -> {
            TableRow<Alerte> row = new TableRow<>();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem == null) {
                    row.setStyle("");
                } else {
                    switch (newItem.getPriorite()) {
                        case "CRITIQUE":
                            row.setStyle("-fx-background-color: #ffebee;");
                            break;
                        case "ELEVEE":
                            row.setStyle("-fx-background-color: #fff3e0;");
                            break;
                        case "MOYENNE":
                            row.setStyle("-fx-background-color: #f3e5f5;");
                            break;
                        default:
                            row.setStyle("-fx-background-color: #e8f5e8;");
                            break;
                    }
                }
            });
            return row;
        });

        tableAlertes.setItems(alertesList);
    }

    private void setupFilters() {
        ObservableList<String> types = FXCollections.observableArrayList(
                "Tous", "STOCK_FAIBLE", "PEREMPTION_PROCHE", "ARTICLE_CRITIQUE", "COMMANDE_URGENTE"
        );
        cmbFiltreType.setItems(types);
        cmbFiltreType.setValue("Tous");

        ObservableList<String> gravites = FXCollections.observableArrayList(
                "Tous", "FAIBLE", "MOYENNE", "ELEVEE", "CRITIQUE"
        );
        cmbFiltreGravite.setItems(gravites);
        cmbFiltreGravite.setValue("Tous");

        ObservableList<String> statuts = FXCollections.observableArrayList(
                "Tous", "NON_TRAITEE", "TRAITEE"
        );
        cmbFiltreStatut.setItems(statuts);
        cmbFiltreStatut.setValue("Tous");
    }

    private void loadAlertes() {
        alertesList.clear();
        alertesList.addAll(alerteDAO.getToutesAlertes());
    }

    private void setupEventHandlers() {
        tableAlertes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        alerteSelectionnee = newSelection;
                        afficherDetailsAlerte(newSelection);
                    }
                }
        );

        cmbFiltreType.setOnAction(e -> appliquerFiltres());
        cmbFiltreGravite.setOnAction(e -> appliquerFiltres());
        cmbFiltreStatut.setOnAction(e -> appliquerFiltres());

        txtRecherche.textProperty().addListener((obs, oldText, newText) -> appliquerFiltres());
    }

    private void afficherDetailsAlerte(Alerte alerte) {
        StringBuilder details = new StringBuilder();
        details.append("Type: ").append(alerte.getType()).append("\n");
        details.append("Priorité: ").append(alerte.getPriorite()).append("\n");
        details.append("Statut: ").append(alerte.isTraitee() ? "TRAITEE" : "NON_TRAITEE").append("\n");
        details.append("Article: ").append(alerte.getArticleReference()).append(" - ").append(alerte.getArticleNom()).append("\n\n");
        details.append("Message:\n").append(alerte.getMessage());

        txtDetailsAlerte.setText(details.toString());
    }

    private void appliquerFiltres() {
        String typeFiltre = cmbFiltreType.getValue();
        String graviteFiltre = cmbFiltreGravite.getValue();
        String statutFiltre = cmbFiltreStatut.getValue();
        String rechercheTexte = txtRecherche.getText();

        // Charger toutes les alertes et filtrer localement puisque DAO n'a pas de methode filtreAlertes
        ObservableList<Alerte> toutesAlertes = alerteDAO.getToutesAlertes();
        ObservableList<Alerte> alertesFiltrees = FXCollections.observableArrayList();

        for (Alerte alerte : toutesAlertes) {
            boolean typeMatch = typeFiltre.equals("Tous") || alerte.getType().equals(typeFiltre);
            boolean graviteMatch = graviteFiltre.equals("Tous") || alerte.getPriorite().equals(graviteFiltre);
            boolean statutMatch = statutFiltre.equals("Tous") ||
                    (statutFiltre.equals("TRAITEE") && alerte.isTraitee()) ||
                    (statutFiltre.equals("NON_TRAITEE") && !alerte.isTraitee());
            boolean rechercheMatch = rechercheTexte.isEmpty() ||
                    alerte.getMessage().toLowerCase().contains(rechercheTexte.toLowerCase()) ||
                    alerte.getArticleNom().toLowerCase().contains(rechercheTexte.toLowerCase());

            if (typeMatch && graviteMatch && statutMatch && rechercheMatch) {
                alertesFiltrees.add(alerte);
            }
        }

        alertesList.clear();
        alertesList.addAll(alertesFiltrees);
    }

    @FXML
    private void actualiserAlertes() {
        loadAlertes();
        showAlert("Information", "Alertes actualisées avec succès!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void marquerCommeLue() {
        if (alerteSelectionnee != null && !alerteSelectionnee.isTraitee()) {
            alerteSelectionnee.setTraitee(true);
            loadAlertes();
            showAlert("Succès", "Alerte marquée comme traitée!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Attention", "Veuillez sélectionner une alerte non traitée.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void marquerCommeResolue() {
        marquerCommeLue();
    }

    @FXML
    private void supprimerAlerte() {
        if (alerteSelectionnee != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer l'alerte");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette alerte ?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                loadAlertes();
                txtDetailsAlerte.clear();
                showAlert("Succès", "Alerte supprimée avec succès!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner une alerte à supprimer.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
