package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.CommandeInterneDAO;
import com.isimm.gestionstock.dao.ArticleDAO;
import com.isimm.gestionstock.dao.ServiceDAO;
import com.isimm.gestionstock.model.CommandeInterne;
import com.isimm.gestionstock.model.Article;
import com.isimm.gestionstock.model.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CommandesInternesController implements Initializable {

    @FXML private TableView<CommandeInterne> tableCommandesInternes;
    @FXML private TableColumn<CommandeInterne, Integer> colId;
    @FXML private TableColumn<CommandeInterne, String> colNumeroCommande;
    @FXML private TableColumn<CommandeInterne, LocalDate> colDateCommande;
    @FXML private TableColumn<CommandeInterne, String> colServiceDemandeur;
    @FXML private TableColumn<CommandeInterne, String> colStatut;

    @FXML private TextField txtNumeroCommande;
    @FXML private DatePicker dateCommande;
    @FXML private ComboBox<Service> cmbServiceDemandeur;
    @FXML private ComboBox<String> cmbStatut;

    private CommandeInterneDAO commandeInterneDAO;
    private ArticleDAO articleDAO;
    private ServiceDAO serviceDAO;
    private ObservableList<CommandeInterne> commandesInternesList;
    private CommandeInterne commandeSelectionnee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        commandeInterneDAO = new CommandeInterneDAO();
        articleDAO = new ArticleDAO();
        serviceDAO = new ServiceDAO();
        commandesInternesList = FXCollections.observableArrayList();

        setupTableColumns();
        setupComboBoxes();
        loadCommandesInternes();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumeroCommande.setCellValueFactory(new PropertyValueFactory<>("numeroCommande"));
        colDateCommande.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        colServiceDemandeur.setCellValueFactory(new PropertyValueFactory<>("serviceNom"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        tableCommandesInternes.setItems(commandesInternesList);
    }

    private void setupComboBoxes() {
        // Charger les services
        cmbServiceDemandeur.setItems(serviceDAO.getAllServices());

        // Statuts possibles
        ObservableList<String> statuts = FXCollections.observableArrayList(
                "En attente", "Validée", "En cours", "Livrée", "Annulée"
        );
        cmbStatut.setItems(statuts);
        cmbStatut.setValue("En attente");
    }

    private void loadCommandesInternes() {
        commandesInternesList.clear();
        commandesInternesList.addAll(commandeInterneDAO.getAllCommandesInternes());
    }

    private void setupEventHandlers() {
        tableCommandesInternes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        commandeSelectionnee = newSelection;
                        remplirFormulaire(newSelection);
                    }
                }
        );

        // Calculer automatiquement le montant total
        cmbServiceDemandeur.setOnAction(e -> calculerMontantTotal());
    }

    private void calculerMontantTotal() {
        // Logique de calcul supprimée car le modèle de service n'a pas de prixUnitaire
        // Cette méthode peut être implémentée ultérieurement lorsque la logique de tarification est ajoutée
    }

    private void remplirFormulaire(CommandeInterne commande) {
        txtNumeroCommande.setText(commande.getNumeroCommande());
        dateCommande.setValue(commande.getDateCommande());
        cmbStatut.setValue(commande.getStatut());
    }

    private void viderFormulaire() {
        txtNumeroCommande.clear();
        dateCommande.setValue(LocalDate.now());
        cmbServiceDemandeur.setValue(null);
        cmbStatut.setValue("En attente");
        commandeSelectionnee = null;
    }

    private void rechercherCommandesInternes(String critere) {
        // La fonctionnalité de recherche peut être mise en œuvre en filtrant la liste existante
        // car CommandeInterneDAO n'a pas de méthode rechercherCommandesInternes
    }

    @FXML
    private void ajouterCommandeInterne() {
        if (validerFormulaire()) {
            CommandeInterne nouvelleCommande = creerCommandeDepuisFormulaire();
            if (commandeInterneDAO.insertCommandeInterne(nouvelleCommande)) {
                loadCommandesInternes();
                viderFormulaire();
                showAlert("Succès", "Commande interne ajoutée avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout de la commande interne.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void modifierCommandeInterne() {
        if (commandeSelectionnee != null && validerFormulaire()) {
            CommandeInterne commandeModifiee = creerCommandeDepuisFormulaire();
            commandeModifiee.setId(commandeSelectionnee.getId());

            if (commandeInterneDAO.updateCommandeInterne(commandeModifiee)) {
                loadCommandesInternes();
                viderFormulaire();
                showAlert("Succès", "Commande interne modifiée avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de la modification de la commande interne.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner une commande interne à modifier.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void supprimerCommandeInterne() {
        if (commandeSelectionnee != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer la commande interne");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette commande interne ?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                if (commandeInterneDAO.deleteCommandeInterne(commandeSelectionnee.getId())) {
                    loadCommandesInternes();
                    viderFormulaire();
                    showAlert("Succès", "Commande interne supprimée avec succès!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression de la commande interne.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner une commande interne à supprimer.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void validerCommande() {
        if (commandeSelectionnee != null && "En attente".equals(commandeSelectionnee.getStatut())) {
            if (commandeInterneDAO.validerCommande(commandeSelectionnee.getId())) {
                loadCommandesInternes();
                viderFormulaire();
                showAlert("Succès", "Commande interne validée avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de la validation de la commande interne.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner une commande en attente à valider.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void annulerSaisie() {
        viderFormulaire();
    }

    private CommandeInterne creerCommandeDepuisFormulaire() {
        CommandeInterne commande = new CommandeInterne();
        commande.setNumeroCommande(txtNumeroCommande.getText());
        commande.setDateCommande(dateCommande.getValue());
        if (cmbServiceDemandeur.getValue() != null) {
            commande.setServiceId(cmbServiceDemandeur.getValue().getId());
            commande.setServiceNom(cmbServiceDemandeur.getValue().getNom());
        }
        commande.setStatut(cmbStatut.getValue());
        commande.setDateLivraison(null); // À déterminer à la livraison
        return commande;
    }

    private boolean validerFormulaire() {
        if (txtNumeroCommande.getText().isEmpty()) {
            showAlert("Erreur", "Le numéro de commande est obligatoire.", Alert.AlertType.ERROR);
            return false;
        }
        if (dateCommande.getValue() == null) {
            showAlert("Erreur", "La date de commande est obligatoire.", Alert.AlertType.ERROR);
            return false;
        }
        if (cmbServiceDemandeur.getValue() == null) {
            showAlert("Erreur", "Le service demandeur est obligatoire.", Alert.AlertType.ERROR);
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
