package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.StatistiqueDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StatistiquesController implements Initializable {

    @FXML private PieChart pieChartCategories;
    @FXML private BarChart<String, Number> barChartConsommation;
    @FXML private LineChart<String, Number> lineChartEvolution;
    @FXML private CategoryAxis xAxisBar;
    @FXML private NumberAxis yAxisBar;
    @FXML private CategoryAxis xAxisLine;
    @FXML private NumberAxis yAxisLine;

    @FXML private TableView<StatistiqueItem> tableTopArticles;
    @FXML private TableColumn<StatistiqueItem, String> colTopArticleNom;
    @FXML private TableColumn<StatistiqueItem, Integer> colQuantiteConsommee;

    @FXML private TableView<StatistiqueItem> tableTopServices;
    @FXML private TableColumn<StatistiqueItem, String> colServiceNom;
    @FXML private TableColumn<StatistiqueItem, Integer> colNbCommandes;

    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private ComboBox<String> comboPeriode;
    @FXML private Label lblTotalArticles;
    @FXML private Label lblTotalCommandes;
    @FXML private Label lblValeurStock;

    private StatistiqueDAO statistiqueDAO;
    private ObservableList<StatistiqueItem> topArticles;
    private ObservableList<StatistiqueItem> topServices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statistiqueDAO = new StatistiqueDAO();
        topArticles = FXCollections.observableArrayList();
        topServices = FXCollections.observableArrayList();

        setupTableColumns();
        setupComboBox();
        setupDatePickers();
        loadStatistiques();
    }

    private void setupTableColumns() {
        colTopArticleNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colQuantiteConsommee.setCellValueFactory(new PropertyValueFactory<>("valeur"));

        colServiceNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNbCommandes.setCellValueFactory(new PropertyValueFactory<>("valeur"));

        tableTopArticles.setItems(topArticles);
        tableTopServices.setItems(topServices);
    }

    private void setupComboBox() {
        comboPeriode.setItems(FXCollections.observableArrayList(
                "Cette semaine", "Ce mois", "Ce trimestre", "Cette année", "Personnalisée"
        ));
        comboPeriode.setValue("Ce mois");
    }

    private void setupDatePickers() {
        dateFin.setValue(LocalDate.now());
        dateDebut.setValue(LocalDate.now().minusMonths(1));
    }

    private void loadStatistiques() {
        loadStatistiquesGenerales();
        loadTopArticles();
        loadTopServices();
        loadGraphiques();
    }

    private void loadStatistiquesGenerales() {
        var stats = statistiqueDAO.getStatistiquesGenerales();
        lblTotalArticles.setText(String.valueOf(stats.getOrDefault("totalArticles", 0)));
        lblTotalCommandes.setText(String.valueOf(stats.getOrDefault("commandesEnCours", 0)));
        lblValeurStock.setText("N/A");
    }

    private void loadTopArticles() {
        topArticles.clear();
        var articles = statistiqueDAO.getArticlesPlusConsommes();
        for (var article : articles) {
            topArticles.add(new StatistiqueItem(
                    (String) article.get("nom"),
                    (Integer) article.get("quantite")
            ));
        }
    }

    private void loadTopServices() {
        topServices.clear();
        var services = statistiqueDAO.getServicesPlusConsommateurs();
        for (var service : services) {
            topServices.add(new StatistiqueItem(
                    (String) service.get("nom"),
                    (Integer) service.get("commandes")
            ));
        }
    }

    private void loadGraphiques() {
        loadBarChart();
    }

    private void loadBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Articles les plus consommés");

        var articles = statistiqueDAO.getArticlesPlusConsommes();
        for (var article : articles) {
            series.getData().add(new XYChart.Data<>(
                    (String) article.get("nom"),
                    (Integer) article.get("quantite")
            ));
        }
        barChartConsommation.getData().clear();
        barChartConsommation.getData().add(series);
    }

    @FXML
    private void handleActualiser() {
        loadStatistiques();
    }

    @FXML
    private void handleExporterRapport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export");
        alert.setHeaderText(null);
        alert.setContentText("Rapport exporté avec succès!");
        alert.showAndWait();
    }

    @FXML
    private void handleFiltrerPeriode() {
        String periode = comboPeriode.getValue();
        if ("Personnalisée".equals(periode)) {
            dateDebut.setDisable(false);
            dateFin.setDisable(false);
        } else {
            dateDebut.setDisable(true);
            dateFin.setDisable(true);
            setPeriodeDates(periode);
        }
        loadStatistiques();
    }

    private void setPeriodeDates(String periode) {
        LocalDate fin = LocalDate.now();
        LocalDate debut;

        switch (periode) {
            case "Cette semaine":
                debut = fin.minusWeeks(1);
                break;
            case "Ce mois":
                debut = fin.minusMonths(1);
                break;
            case "Ce trimestre":
                debut = fin.minusMonths(3);
                break;
            case "Cette année":
                debut = fin.minusYears(1);
                break;
            default:
                debut = fin.minusMonths(1);
        }

        dateDebut.setValue(debut);
        dateFin.setValue(fin);
    }

    public static class StatistiqueItem {
        private String nom;
        private Integer valeur;

        public StatistiqueItem(String nom, Integer valeur) {
            this.nom = nom;
            this.valeur = valeur;
        }

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public Integer getValeur() { return valeur; }
        public void setValeur(Integer valeur) { this.valeur = valeur; }
    }
}
