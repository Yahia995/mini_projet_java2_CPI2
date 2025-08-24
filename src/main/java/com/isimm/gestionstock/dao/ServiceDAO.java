package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.Service;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ServiceDAO {

    public ObservableList<Service> getAllServices() {
        ObservableList<Service> services = FXCollections.observableArrayList();
        String query = "SELECT id, nom, responsable, telephone, email, budget_annuel FROM services ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("responsable"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getBigDecimal("budget_annuel")
                );
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public boolean insertService(Service service) {
        String query = "INSERT INTO services (nom, responsable, telephone, email, budget_annuel) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, service.getNom());
            stmt.setString(2, service.getResponsable());
            stmt.setString(3, service.getTelephone());
            stmt.setString(4, service.getEmail());
            stmt.setBigDecimal(5, service.getBudgetAnnuel());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateService(Service service) {
        String query = "UPDATE services SET nom=?, responsable=?, telephone=?, email=?, budget_annuel=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, service.getNom());
            stmt.setString(2, service.getResponsable());
            stmt.setString(3, service.getTelephone());
            stmt.setString(4, service.getEmail());
            stmt.setBigDecimal(5, service.getBudgetAnnuel());
            stmt.setInt(6, service.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteService(int id) {
        String query = "DELETE FROM services WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
