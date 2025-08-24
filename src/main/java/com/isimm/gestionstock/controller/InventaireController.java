package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.InventaireDAO;
import com.isimm.gestionstock.dao.ArticleDAO;
import com.isimm.gestionstock.dao.LocalDAO;
import com.isimm.gestionstock.model.Inventaire;
import com.isimm.gestionstock.model.DetailInventaire;
import com.isimm.gestionstock.model.Article;
import com.isimm.gestionstock.model.Local;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InventaireController implements Initializable {

    @FXML private TableView<Inventaire> tableInventaires;
    @FXML private TableColumn<Inventaire, Integer> colIdInventaire;
    @FXML private TableColumn<Inventaire, String> colNomInventaire;
    @FXML private TableColumn<Inventaire, LocalDate> colDateInventaire;
    @FXML private TableColumn<Inventaire, String> colStatutInventaire;

    @FXML private TableView<DetailInventaire> tableDetails;
    @FXML private TableColumn<DetailInventaire, String> colArticleNom;
    @FXML private TableColumn<DetailInventaire, Integer> colQuantiteTheorique;
    @FXML private TableColumn<DetailInventaire, Integer> colQuantiteReelle;
    @FXML private TableColumn<DetailInventaire, Integer> colEcart;

    @FXML private DatePicker dateInventaire;
    @FXML private TextField txtNomInventaire;
    @FXML private ComboBox<Local> comboLocal;
    @FXML private ComboBox<Article> comboArticle;
    @FXML private TextField txtQuantiteReelle;
    @FXML private TextField txtRecherche;

    private InventaireDAO inventaireDAO;
    private ArticleDAO articleDAO;
    private LocalDAO localDAO;
    private ObservableList<Inventaire> inventaires;
    private ObservableList<DetailInventaire> details;
    private Inventaire inventaireSelectionne;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventaireDAO = new InventaireDAO();
        articleDAO = new ArticleDAO();
        localDAO = new LocalDAO();

        inventaires = FXCollections.observableArrayList();
        details = FXCollections.observableArrayList();

        setupTableColumns();
        loadInventaires();
        loadLocaux();
        loadArticles();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        colIdInventaire.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomInventaire.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDateInventaire.setCellValueFactory(new PropertyValueFactory<>("dateInventaire"));
        colStatutInventaire.setCellValueFactory(new PropertyValueFactory<>("statut"));

        colArticleNom.setCellValueFactory(new PropertyValueFactory<>("articleNom"));
        colQuantiteTheorique.setCellValueFactory(new PropertyValueFactory<>("quantiteTheorique"));
        colQuantiteReelle.setCellValueFactory(new PropertyValueFactory<>("quantiteReelle"));
        colEcart.setCellValueFactory(new PropertyValueFactory<>("ecart"));

        tableInventaires.setItems(inventaires);
        tableDetails.setItems(details);
    }

    private void setupEventHandlers() {
        tableInventaires.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        inventaireSelectionne = newSelection;
                        populateFields(newSelection);
                        loadDetailsInventaire(newSelection.getId());
                    }
                }
        );

        txtRecherche.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                tableInventaires.setItems(inventaires);
            } else {
                ObservableList<Inventaire> filtered = inventaires.filtered(
                        inventaire -> inventaire.getLocalNom().toLowerCase().contains(newText.toLowerCase())
                );
                tableInventaires.setItems(filtered);
            }
        });
    }

    private void loadInventaires() {
        inventaires.setAll(inventaireDAO.getAllInventaires());
    }

    private void loadLocaux() {
        comboLocal.setItems(localDAO.getAllLocaux());
        comboLocal.setConverter(new StringConverter<Local>() {
            @Override
            public String toString(Local local) {
                return local != null ? local.getNom() : "";
            }

            @Override
            public Local fromString(String string) {
                return null;
            }
        });
    }

    private void loadArticles() {
        comboArticle.setItems(articleDAO.getAllArticles());
        comboArticle.setConverter(new StringConverter<Article>() {
            @Override
            public String toString(Article article) {
                return article != null ? article.getNom() : "";
            }

            @Override
            public Article fromString(String string) {
                return null;
            }
        });
    }

    private void loadDetailsInventaire(int inventaireId) {
        details.setAll(inventaireDAO.getDetailsInventaire(inventaireId));
    }

    private void populateFields(Inventaire inventaire) {
        dateInventaire.setValue(inventaire.getDateInventaire());
        for (Local local : comboLocal.getItems()) {
            if (local.getId() == inventaire.getLocalId()) {
                comboLocal.setValue(local);
                break;
            }
        }
    }

    private void clearFields() {
        txtNomInventaire.clear();
        dateInventaire.setValue(LocalDate.now());
        comboLocal.setValue(null);
        comboArticle.setValue(null);
        txtQuantiteReelle.clear();
        inventaireSelectionne = null;
    }

    @FXML
    private void handleNouvelInventaire() {
        clearFields();
    }

    @FXML
    private void handleCreerInventaire() {
        if (validateFields()) {
            Inventaire inventaire = new Inventaire();
            inventaire.setLocalNom(txtNomInventaire.getText());
            inventaire.setDateInventaire(dateInventaire.getValue());
            inventaire.setLocalId(comboLocal.getValue().getId());
            inventaire.setStatut("EN_COURS");
            inventaire.setCommentaire(""); // Commentaire vide par défaut

            if (inventaireDAO.insertInventaire(inventaire)) {
                loadInventaires();
                clearFields();
                showAlert("Succès", "Inventaire créé avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de la création de l'inventaire.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleAjouterArticle() {
        if (inventaireSelectionne != null && comboArticle.getValue() != null &&
                !txtQuantiteReelle.getText().isEmpty()) {

            try {
                Article article = comboArticle.getValue();
                int quantiteReelle = Integer.parseInt(txtQuantiteReelle.getText());

                DetailInventaire detail = new DetailInventaire();
                detail.setInventaireId(inventaireSelectionne.getId());
                detail.setArticleId(article.getId());
                detail.setArticleNom(article.getNom());
                detail.setQuantiteTheorique(article.getStockActuel());
                detail.setQuantiteReelle(quantiteReelle);
                detail.setEcart(quantiteReelle - article.getStockActuel());

                loadDetailsInventaire(inventaireSelectionne.getId());
                comboArticle.setValue(null);
                txtQuantiteReelle.clear();
                showAlert("Succès", "Article ajouté à l'inventaire!", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Quantité invalide!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un inventaire et un article!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleValiderInventaire() {
        if (inventaireSelectionne != null) {
            inventaireSelectionne.setStatut("VALIDE");
            if (inventaireDAO.updateInventaire(inventaireSelectionne)) {
                loadInventaires();
                showAlert("Succès", "Inventaire validé!", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void handleGenererRapport() {
        if (inventaireSelectionne != null) {
            // Logique pour générer un rapport d'inventaire
            showAlert("Info", "Rapport généré pour l'inventaire: " + inventaireSelectionne.getLocalNom(),
                    Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un inventaire!", Alert.AlertType.WARNING);
        }
    }

    private boolean validateFields() {
        if (txtNomInventaire.getText().isEmpty()) {
            showAlert("Erreur", "Le nom de l'inventaire est requis!", Alert.AlertType.WARNING);
            return false;
        }
        if (comboLocal.getValue() == null) {
            showAlert("Erreur", "Le local est requis!", Alert.AlertType.WARNING);
            return false;
        }
        if (dateInventaire.getValue() == null) {
            showAlert("Erreur", "La date d'inventaire est requise!", Alert.AlertType.WARNING);
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
