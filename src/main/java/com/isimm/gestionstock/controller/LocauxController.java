package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.LocalDAO;
import com.isimm.gestionstock.model.Local;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocauxController implements Initializable {

    @FXML private TableView<Local> locauxTable;
    @FXML private TableColumn<Local, String> nomColumn;
    @FXML private TableColumn<Local, String> typeColumn;
    @FXML private TableColumn<Local, Integer> capaciteColumn;
    @FXML private TableColumn<Local, String> descriptionColumn;

    @FXML private TextField nomField;
    @FXML private ComboBox<String> typeLocalCombo;
    @FXML private TextField capaciteField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField rechercheField;

    @FXML private Button ajouterButton;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;
    @FXML private Button viderButton;

    private LocalDAO localDAO;
    private ObservableList<Local> locauxList;
    private Local selectedLocal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localDAO = new LocalDAO();

        // Configuration des colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeLocal"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Sélection de local
        locauxTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedLocal = newSelection;
                    populateFields();
                    updateButtonStates();
                }
        );

        // Initialisation des types de locaux
        typeLocalCombo.getItems().addAll(
                "Bibliothèque", "Amphithéâtre", "Salle d'enseignement",
                "Administration", "Magasin", "Bureau enseignant"
        );

        // Chargement des données
        loadLocaux();
        updateButtonStates();

        // Recherche en temps réel
        rechercheField.textProperty().addListener((obs, oldText, newText) -> filterLocaux(newText));
    }

    private void loadLocaux() {
        locauxList = localDAO.getAllLocaux();
        locauxTable.setItems(locauxList);
    }

    private void populateFields() {
        if (selectedLocal != null) {
            nomField.setText(selectedLocal.getNom());
            typeLocalCombo.setValue(selectedLocal.getTypeLocal());
            capaciteField.setText(String.valueOf(selectedLocal.getCapacite()));
            descriptionArea.setText(selectedLocal.getDescription());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        nomField.clear();
        typeLocalCombo.setValue(null);
        capaciteField.clear();
        descriptionArea.clear();
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedLocal != null;
        modifierButton.setDisable(!hasSelection);
        supprimerButton.setDisable(!hasSelection);
    }

    @FXML
    private void handleAjouter() {
        if (validateFields()) {
            Local local = new Local();
            local.setNom(nomField.getText());
            local.setTypeLocal(typeLocalCombo.getValue());
            local.setCapacite(Integer.parseInt(capaciteField.getText()));
            local.setDescription(descriptionArea.getText());

            if (localDAO.insertLocal(local)) {
                showAlert("Succès", "Local ajouté avec succès", Alert.AlertType.INFORMATION);
                loadLocaux();
                clearFields();
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout du local", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedLocal != null && validateFields()) {
            selectedLocal.setNom(nomField.getText());
            selectedLocal.setTypeLocal(typeLocalCombo.getValue());
            selectedLocal.setCapacite(Integer.parseInt(capaciteField.getText()));
            selectedLocal.setDescription(descriptionArea.getText());

            if (localDAO.updateLocal(selectedLocal)) {
                showAlert("Succès", "Local modifié avec succès", Alert.AlertType.INFORMATION);
                loadLocaux();
            } else {
                showAlert("Erreur", "Erreur lors de la modification du local", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedLocal != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer le local");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce local ?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (localDAO.deleteLocal(selectedLocal.getId())) {
                    showAlert("Succès", "Local supprimé avec succès", Alert.AlertType.INFORMATION);
                    loadLocaux();
                    clearFields();
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression du local", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void handleVider() {
        clearFields();
        locauxTable.getSelectionModel().clearSelection();
    }

    private void filterLocaux(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            locauxTable.setItems(locauxList);
        } else {
            ObservableList<Local> filteredList = locauxList.filtered(local ->
                    local.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                            local.getTypeLocal().toLowerCase().contains(searchText.toLowerCase()) ||
                            (local.getDescription() != null && local.getDescription().toLowerCase().contains(searchText.toLowerCase()))
            );
            locauxTable.setItems(filteredList);
        }
    }

    private boolean validateFields() {
        if (nomField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom est obligatoire", Alert.AlertType.WARNING);
            return false;
        }
        if (typeLocalCombo.getValue() == null) {
            showAlert("Erreur", "Le type de local est obligatoire", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Integer.parseInt(capaciteField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir une capacité valide", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
