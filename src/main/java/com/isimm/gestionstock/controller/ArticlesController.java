package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.ArticleDAO;
import com.isimm.gestionstock.model.Article;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class ArticlesController implements Initializable {

    @FXML private TableView<Article> articlesTable;
    @FXML private TableColumn<Article, String> referenceColumn;
    @FXML private TableColumn<Article, String> nomColumn;
    @FXML private TableColumn<Article, String> descriptionColumn;
    @FXML private TableColumn<Article, Integer> stockMinimalColumn;
    @FXML private TableColumn<Article, Integer> stockActuelColumn;
    @FXML private TableColumn<Article, Double> prixColumn;
    @FXML private TableColumn<Article, Boolean> critiqueColumn;

    @FXML private TextField referenceField;
    @FXML private TextField nomField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Integer> typeProduitCombo;
    @FXML private TextField stockMinimalField;
    @FXML private TextField stockActuelField;
    @FXML private TextField prixField;
    @FXML private CheckBox critiqueCheckBox;
    @FXML private TextField rechercheField;

    @FXML private Button ajouterButton;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;
    @FXML private Button viderButton;

    private ArticleDAO articleDAO;
    private ObservableList<Article> articlesList;
    private Article selectedArticle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        articleDAO = new ArticleDAO();

        // Configuration des colonnes
        referenceColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        stockMinimalColumn.setCellValueFactory(new PropertyValueFactory<>("stockMinimal"));
        stockActuelColumn.setCellValueFactory(new PropertyValueFactory<>("stockActuel"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        critiqueColumn.setCellValueFactory(new PropertyValueFactory<>("estCritique"));

        // Formatage des colonnes
        prixColumn.setCellFactory(column -> new TableCell<Article, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f DT", item));
                }
            }
        });

        // Coloration des lignes selon le stock
        articlesTable.setRowFactory(tv -> new TableRow<Article>() {
            @Override
            protected void updateItem(Article item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.isStockFaible()) {
                    setStyle("-fx-background-color: #ffebee;");
                } else {
                    setStyle("");
                }
            }
        });

        // Sélection d'article
        articlesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedArticle = newSelection;
                    populateFields();
                    updateButtonStates();
                }
        );

        // Initialisation des types de produits (a adapter selon votre base)
        typeProduitCombo.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        typeProduitCombo.setValue(1);

        // Chargement des données
        loadArticles();
        updateButtonStates();

        // Recherche en temps reel
        rechercheField.textProperty().addListener((obs, oldText, newText) -> filterArticles(newText));
    }

    private void loadArticles() {
        articlesList = articleDAO.getAllArticles();
        articlesTable.setItems(articlesList);
    }

    private void populateFields() {
        if (selectedArticle != null) {
            referenceField.setText(selectedArticle.getReference());
            nomField.setText(selectedArticle.getNom());
            descriptionArea.setText(selectedArticle.getDescription());
            typeProduitCombo.setValue(selectedArticle.getTypeProduitId());
            stockMinimalField.setText(String.valueOf(selectedArticle.getStockMinimal()));
            stockActuelField.setText(String.valueOf(selectedArticle.getStockActuel()));
            prixField.setText(String.valueOf(selectedArticle.getPrixUnitaire()));
            critiqueCheckBox.setSelected(selectedArticle.isEstCritique());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        referenceField.clear();
        nomField.clear();
        descriptionArea.clear();
        typeProduitCombo.setValue(1);
        stockMinimalField.clear();
        stockActuelField.clear();
        prixField.clear();
        critiqueCheckBox.setSelected(false);
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedArticle != null;
        modifierButton.setDisable(!hasSelection);
        supprimerButton.setDisable(!hasSelection);
    }

    @FXML
    private void handleAjouter() {
        if (validateFields()) {
            Article article = new Article();
            article.setReference(referenceField.getText());
            article.setNom(nomField.getText());
            article.setDescription(descriptionArea.getText());
            article.setTypeProduitId(typeProduitCombo.getValue());
            article.setStockMinimal(Integer.parseInt(stockMinimalField.getText()));
            article.setStockActuel(Integer.parseInt(stockActuelField.getText()));
            article.setPrixUnitaire(Double.parseDouble(prixField.getText()));
            article.setEstCritique(critiqueCheckBox.isSelected());
            article.setDateCreation(LocalDateTime.now());

            if (articleDAO.insertArticle(article)) {
                showAlert("Succès", "Article ajouté avec succès", Alert.AlertType.INFORMATION);
                loadArticles();
                clearFields();
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout de l'article", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedArticle != null && validateFields()) {
            selectedArticle.setReference(referenceField.getText());
            selectedArticle.setNom(nomField.getText());
            selectedArticle.setDescription(descriptionArea.getText());
            selectedArticle.setTypeProduitId(typeProduitCombo.getValue());
            selectedArticle.setStockMinimal(Integer.parseInt(stockMinimalField.getText()));
            selectedArticle.setStockActuel(Integer.parseInt(stockActuelField.getText()));
            selectedArticle.setPrixUnitaire(Double.parseDouble(prixField.getText()));
            selectedArticle.setEstCritique(critiqueCheckBox.isSelected());

            if (articleDAO.updateArticle(selectedArticle)) {
                showAlert("Succès", "Article modifié avec succès", Alert.AlertType.INFORMATION);
                loadArticles();
            } else {
                showAlert("Erreur", "Erreur lors de la modification de l'article", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedArticle != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer l'article");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet article ?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (articleDAO.deleteArticle(selectedArticle.getId())) {
                    showAlert("Succès", "Article supprimé avec succès", Alert.AlertType.INFORMATION);
                    loadArticles();
                    clearFields();
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression de l'article", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void handleVider() {
        clearFields();
        articlesTable.getSelectionModel().clearSelection();
    }

    private void filterArticles(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            articlesTable.setItems(articlesList);
        } else {
            ObservableList<Article> filteredList = articlesList.filtered(article ->
                    article.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                            article.getReference().toLowerCase().contains(searchText.toLowerCase()) ||
                            (article.getDescription() != null && article.getDescription().toLowerCase().contains(searchText.toLowerCase()))
            );
            articlesTable.setItems(filteredList);
        }
    }

    private boolean validateFields() {
        if (referenceField.getText().trim().isEmpty()) {
            showAlert("Erreur", "La référence est obligatoire", Alert.AlertType.WARNING);
            return false;
        }
        if (nomField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom est obligatoire", Alert.AlertType.WARNING);
            return false;
        }
        try {
            Integer.parseInt(stockMinimalField.getText());
            Integer.parseInt(stockActuelField.getText());
            Double.parseDouble(prixField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir des valeurs numériques valides", Alert.AlertType.WARNING);
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
