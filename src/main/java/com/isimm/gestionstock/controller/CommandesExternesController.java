package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.CommandeExterneDAO;
import com.isimm.gestionstock.dao.FournisseurDAO;
import com.isimm.gestionstock.model.CommandeExterne;
import com.isimm.gestionstock.model.Fournisseur;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CommandesExternesController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<CommandeExterne> commandesTable;
    @FXML private TableColumn<CommandeExterne, Integer> idColumn;
    @FXML private TableColumn<CommandeExterne, String> numeroColumn;
    @FXML private TableColumn<CommandeExterne, Integer> fournisseurColumn;
    @FXML private TableColumn<CommandeExterne, LocalDateTime> dateColumn;
    @FXML private TableColumn<CommandeExterne, String> statutColumn;
    @FXML private TableColumn<CommandeExterne, Double> montantColumn;

    @FXML private TextField numeroField;
    @FXML private ComboBox<Fournisseur> fournisseurCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private TextField montantField;
    @FXML private TextArea observationsField;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button validerButton;

    private CommandeExterneDAO commandeDAO;
    private FournisseurDAO fournisseurDAO;
    private ObservableList<CommandeExterne> commandesList;
    private FilteredList<CommandeExterne> filteredCommandes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        commandeDAO = new CommandeExterneDAO();
        fournisseurDAO = new FournisseurDAO();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numeroCommande"));
        fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("fournisseurId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));

        // Format de la colonne date
        dateColumn.setCellFactory(column -> new TableCell<CommandeExterne, LocalDateTime>() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        setupComboBoxes();
        loadCommandes();
        setupSearch();
        setupTableSelection();
    }

    private void setupComboBoxes() {
        // Statuts possibles
        statutCombo.getItems().addAll("EN_ATTENTE", "VALIDEE", "LIVREE", "ANNULEE");
        statutCombo.setValue("EN_ATTENTE");

        // Chargement des fournisseurs
        fournisseurCombo.setItems(fournisseurDAO.getAllFournisseurs());
    }

    private void loadCommandes() {
        commandesList = commandeDAO.getAllCommandesExternes();
        filteredCommandes = new FilteredList<>(commandesList, p -> true);
        commandesTable.setItems(filteredCommandes);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCommandes.setPredicate(commande -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return commande.getNumeroCommande().toLowerCase().contains(lowerCaseFilter) ||
                        commande.getStatut().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void setupTableSelection() {
        commandesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                        updateButton.setDisable(false);
                        deleteButton.setDisable(false);
                        validerButton.setDisable(!"EN_ATTENTE".equals(newValue.getStatut()));
                    } else {
                        clearFields();
                        updateButton.setDisable(true);
                        deleteButton.setDisable(true);
                        validerButton.setDisable(true);
                    }
                }
        );
    }

    private void populateFields(CommandeExterne commande) {
        numeroField.setText(commande.getNumeroCommande());
        statutCombo.setValue(commande.getStatut());
        montantField.setText(String.valueOf(commande.getMontantTotal()));
        observationsField.setText(commande.getObservations());

        // Sélectionner le fournisseur correspondant
        for (Fournisseur f : fournisseurCombo.getItems()) {
            if (f.getId() == commande.getFournisseurId()) {
                fournisseurCombo.setValue(f);
                break;
            }
        }
    }

    @FXML
    private void handleAdd() {
        if (validateFields()) {
            CommandeExterne commande = new CommandeExterne();
            commande.setNumeroCommande(numeroField.getText());
            commande.setFournisseurId(fournisseurCombo.getValue().getId());
            commande.setDateCommande(LocalDateTime.now());
            commande.setStatut(statutCombo.getValue());
            commande.setMontantTotal(Double.parseDouble(montantField.getText()));
            commande.setObservations(observationsField.getText());

            if (commandeDAO.insertCommandeExterne(commande)) {
                loadCommandes();
                clearFields();
                showAlert("Succès", "Commande ajoutée avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout de la commande.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        CommandeExterne selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null && validateFields()) {
            selectedCommande.setNumeroCommande(numeroField.getText());
            selectedCommande.setFournisseurId(fournisseurCombo.getValue().getId());
            selectedCommande.setStatut(statutCombo.getValue());
            selectedCommande.setMontantTotal(Double.parseDouble(montantField.getText()));
            selectedCommande.setObservations(observationsField.getText());

            if (commandeDAO.updateCommandeExterne(selectedCommande)) {
                loadCommandes();
                clearFields();
                showAlert("Succès", "Commande modifiée avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de la modification de la commande.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDelete() {
        CommandeExterne selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer la commande");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette commande ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                if (commandeDAO.deleteCommandeExterne(selectedCommande.getId())) {
                    loadCommandes();
                    clearFields();
                    showAlert("Succès", "Commande supprimée avec succès!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression de la commande.", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void handleValider() {
        CommandeExterne selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null && "EN_ATTENTE".equals(selectedCommande.getStatut())) {
            if (commandeDAO.validerCommande(selectedCommande.getId())) {
                loadCommandes();
                clearFields();
                showAlert("Succès", "Commande validée avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de la validation de la commande.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
        commandesTable.getSelectionModel().clearSelection();
    }

    private void clearFields() {
        numeroField.clear();
        fournisseurCombo.setValue(null);
        statutCombo.setValue("EN_ATTENTE");
        montantField.clear();
        observationsField.clear();
    }

    private boolean validateFields() {
        if (numeroField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le numéro de commande est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }
        if (fournisseurCombo.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un fournisseur.", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Double.parseDouble(montantField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.", Alert.AlertType.WARNING);
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
