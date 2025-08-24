package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.Local;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class LocalDAO {

    public ObservableList<Local> getAllLocaux() {
        ObservableList<Local> locaux = FXCollections.observableArrayList();
        String query = "SELECT * FROM locaux ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Local local = new Local(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("type_local"),
                        rs.getInt("capacite"),
                        rs.getString("description")
                );
                locaux.add(local);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locaux;
    }

    public boolean insertLocal(Local local) {
        String query = "INSERT INTO locaux (nom, type_local, capacite, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, local.getNom());
            stmt.setString(2, local.getTypeLocal());
            stmt.setInt(3, local.getCapacite());
            stmt.setString(4, local.getDescription());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLocal(Local local) {
        String query = "UPDATE locaux SET nom=?, type_local=?, capacite=?, description=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, local.getNom());
            stmt.setString(2, local.getTypeLocal());
            stmt.setInt(3, local.getCapacite());
            stmt.setString(4, local.getDescription());
            stmt.setInt(5, local.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLocal(int id) {
        String query = "DELETE FROM locaux WHERE id=?";

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
