package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.FournisseurDAO;
import com.isimm.gestionstock.model.Fournisseur;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FournisseursController implements Initializable {

    @FXML private TableView<Fournisseur> fournisseursTable;
    @FXML private TableColumn<Fournisseur, String> nomColumn;
    @FXML private TableColumn<Fournisseur, String> adresseColumn;
    @FXML private TableColumn<Fournisseur, String> telephoneColumn;
    @FXML private TableColumn<Fournisseur, String> emailColumn;
    @FXML private TableColumn<Fournisseur, String> contactColumn;

    @FXML private TextField nomField;
    @FXML private TextArea adresseArea;
    @FXML private TextField telephoneField;
    @FXML private TextField emailField;
    @FXML private TextField contactPersonneField;
    @FXML private TextField rechercheField;

    @FXML private Button ajouterButton;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;
    @FXML private Button viderButton;

    private FournisseurDAO fournisseurDAO;
    private ObservableList<Fournisseur> fournisseursList;
    private Fournisseur selectedFournisseur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fournisseurDAO = new FournisseurDAO();

        // Configuration des colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactPersonne"));

        // Sélection de fournisseur
        fournisseursTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedFournisseur = newSelection;
                    populateFields();
                    updateButtonStates();
                }
        );

        // Chargement des données
        loadFournisseurs();
        updateButtonStates();

        // Recherche en temps réel
        rechercheField.textProperty().addListener((obs, oldText, newText) -> filterFournisseurs(newText));
    }

    private void loadFournisseurs() {
        fournisseursList = fournisseurDAO.getAllFournisseurs();
        fournisseursTable.setItems(fournisseursList);
    }

    private void populateFields() {
        if (selectedFournisseur != null) {
            nomField.setText(selectedFournisseur.getNom());
            adresseArea.setText(selectedFournisseur.getAdresse());
            telephoneField.setText(selectedFournisseur.getTelephone());
            emailField.setText(selectedFournisseur.getEmail());
            contactPersonneField.setText(selectedFournisseur.getContactPersonne());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        nomField.clear();
        adresseArea.clear();
        telephoneField.clear();
        emailField.clear();
        contactPersonneField.clear();
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedFournisseur != null;
        modifierButton.setDisable(!hasSelection);
        supprimerButton.setDisable(!hasSelection);
    }

    @FXML
    private void handleAjouter() {
        if (validateFields()) {
            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setNom(nomField.getText());
            fournisseur.setAdresse(adresseArea.getText());
            fournisseur.setTelephone(telephoneField.getText());
            fournisseur.setEmail(emailField.getText());
            fournisseur.setContactPersonne(contactPersonneField.getText());

            if (fournisseurDAO.insertFournisseur(fournisseur)) {
                showAlert("Succès", "Fournisseur ajouté avec succès", Alert.AlertType.INFORMATION);
                loadFournisseurs();
                clearFields();
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout du fournisseur", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedFournisseur != null && validateFields()) {
            selectedFournisseur.setNom(nomField.getText());
            selectedFournisseur.setAdresse(adresseArea.getText());
            selectedFournisseur.setTelephone(telephoneField.getText());
            selectedFournisseur.setEmail(emailField.getText());
            selectedFournisseur.setContactPersonne(contactPersonneField.getText());

            if (fournisseurDAO.updateFournisseur(selectedFournisseur)) {
                showAlert("Succès", "Fournisseur modifié avec succès", Alert.AlertType.INFORMATION);
                loadFournisseurs();
            } else {
                showAlert("Erreur", "Erreur lors de la modification du fournisseur", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedFournisseur != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer le fournisseur");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce fournisseur ?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (fournisseurDAO.deleteFournisseur(selectedFournisseur.getId())) {
                    showAlert("Succès", "Fournisseur supprimé avec succès", Alert.AlertType.INFORMATION);
                    loadFournisseurs();
                    clearFields();
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression du fournisseur", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void handleVider() {
        clearFields();
        fournisseursTable.getSelectionModel().clearSelection();
    }

    private void filterFournisseurs(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            fournisseursTable.setItems(fournisseursList);
        } else {
            ObservableList<Fournisseur> filteredList = fournisseursList.filtered(fournisseur ->
                    fournisseur.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                            (fournisseur.getEmail() != null && fournisseur.getEmail().toLowerCase().contains(searchText.toLowerCase())) ||
                            (fournisseur.getContactPersonne() != null && fournisseur.getContactPersonne().toLowerCase().contains(searchText.toLowerCase()))
            );
            fournisseursTable.setItems(filteredList);
        }
    }

    private boolean validateFields() {
        if (nomField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom est obligatoire", Alert.AlertType.WARNING);
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
