package com.isimm.gestionstock.controller;

import com.isimm.gestionstock.dao.ServiceDAO;
import com.isimm.gestionstock.model.Service;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ServicesController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Service> servicesTable;
    @FXML private TableColumn<Service, Integer> idColumn;
    @FXML private TableColumn<Service, String> nomColumn;
    @FXML private TableColumn<Service, BigDecimal> budgetAnnuelColumn;
    @FXML private TableColumn<Service, String> responsableColumn;
    @FXML private TableColumn<Service, String> telephoneColumn;
    @FXML private TableColumn<Service, String> emailColumn;

    @FXML private TextField nomField;
    @FXML private TextField budgetAnnuelField;
    @FXML private TextField responsableField;
    @FXML private TextField telephoneField;
    @FXML private TextField emailField;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    private ServiceDAO serviceDAO;
    private ObservableList<Service> servicesList;
    private FilteredList<Service> filteredServices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceDAO = new ServiceDAO();

        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        budgetAnnuelColumn.setCellValueFactory(new PropertyValueFactory<>("budgetAnnuel"));
        responsableColumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadServices();
        setupSearch();
        setupTableSelection();
    }

    private void loadServices() {
        servicesList = serviceDAO.getAllServices();
        filteredServices = new FilteredList<>(servicesList, p -> true);
        servicesTable.setItems(filteredServices);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredServices.setPredicate(service -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return service.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        service.getResponsable().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void setupTableSelection() {
        servicesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                        updateButton.setDisable(false);
                        deleteButton.setDisable(false);
                    } else {
                        clearFields();
                        updateButton.setDisable(true);
                        deleteButton.setDisable(true);
                    }
                }
        );
    }

    private void populateFields(Service service) {
        nomField.setText(service.getNom());
        budgetAnnuelField.setText(service.getBudgetAnnuel() != null ? service.getBudgetAnnuel().toString() : "");
        responsableField.setText(service.getResponsable());
        telephoneField.setText(service.getTelephone());
        emailField.setText(service.getEmail());
    }

    @FXML
    private void handleAdd() {
        if (validateFields()) {
            Service service = new Service();
            service.setNom(nomField.getText());
            try {
                service.setBudgetAnnuel(new BigDecimal(budgetAnnuelField.getText()));
            } catch (NumberFormatException e) {
                service.setBudgetAnnuel(BigDecimal.ZERO);
            }
            service.setResponsable(responsableField.getText());
            service.setTelephone(telephoneField.getText());
            service.setEmail(emailField.getText());

            if (serviceDAO.insertService(service)) {
                loadServices();
                clearFields();
                showAlert("Succès", "Service ajouté avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout du service.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUpdate() {
        Service selectedService = servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null && validateFields()) {
            selectedService.setNom(nomField.getText());
            try {
                selectedService.setBudgetAnnuel(new BigDecimal(budgetAnnuelField.getText()));
            } catch (NumberFormatException e) {
                selectedService.setBudgetAnnuel(BigDecimal.ZERO);
            }
            selectedService.setResponsable(responsableField.getText());
            selectedService.setTelephone(telephoneField.getText());
            selectedService.setEmail(emailField.getText());

            if (serviceDAO.updateService(selectedService)) {
                loadServices();
                clearFields();
                showAlert("Succès", "Service modifié avec succès!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Erreur lors de la modification du service.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDelete() {
        Service selectedService = servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer le service");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce service ?");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                if (serviceDAO.deleteService(selectedService.getId())) {
                    loadServices();
                    clearFields();
                    showAlert("Succès", "Service supprimé avec succès!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression du service.", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
        servicesTable.getSelectionModel().clearSelection();
    }

    private void clearFields() {
        nomField.clear();
        budgetAnnuelField.clear();
        responsableField.clear();
        telephoneField.clear();
        emailField.clear();
    }

    private boolean validateFields() {
        if (nomField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le nom du service est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }
        if (responsableField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Le responsable est obligatoire.", Alert.AlertType.WARNING);
            return false;
        }
        if (!budgetAnnuelField.getText().trim().isEmpty()) {
            try {
                new BigDecimal(budgetAnnuelField.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le budget annuel doit être un nombre valide.", Alert.AlertType.WARNING);
                return false;
            }
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
