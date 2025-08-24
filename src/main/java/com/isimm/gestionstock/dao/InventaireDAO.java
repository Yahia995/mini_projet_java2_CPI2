package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.Inventaire;
import com.isimm.gestionstock.model.DetailInventaire;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class InventaireDAO {

    public ObservableList<Inventaire> getAllInventaires() {
        ObservableList<Inventaire> inventaires = FXCollections.observableArrayList();
        String sql = "SELECT i.*, l.nom as local_nom FROM inventaires i " +
                "LEFT JOIN locaux l ON i.local_id = l.id ORDER BY i.date_inventaire DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventaire inventaire = new Inventaire(
                        rs.getInt("id"),
                        rs.getDate("date_inventaire").toLocalDate(),
                        rs.getInt("local_id"),
                        rs.getString("local_nom"),
                        rs.getString("statut"),
                        rs.getString("commentaire")
                );
                inventaires.add(inventaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventaires;
    }

    public boolean insertInventaire(Inventaire inventaire) {
        String sql = "INSERT INTO inventaires (date_inventaire, local_id, statut, commentaire) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(inventaire.getDateInventaire()));
            pstmt.setInt(2, inventaire.getLocalId());
            pstmt.setString(3, inventaire.getStatut());
            pstmt.setString(4, inventaire.getCommentaire());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInventaire(Inventaire inventaire) {
        String sql = "UPDATE inventaires SET date_inventaire = ?, local_id = ?, statut = ?, commentaire = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(inventaire.getDateInventaire()));
            pstmt.setInt(2, inventaire.getLocalId());
            pstmt.setString(3, inventaire.getStatut());
            pstmt.setString(4, inventaire.getCommentaire());
            pstmt.setInt(5, inventaire.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInventaire(int id) {
        String sql = "DELETE FROM inventaires WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<DetailInventaire> getDetailsInventaire(int inventaireId) {
        ObservableList<DetailInventaire> details = FXCollections.observableArrayList();
        String sql = "SELECT di.*, a.nom as article_nom FROM details_inventaire di " +
                "LEFT JOIN articles a ON di.article_id = a.id WHERE di.inventaire_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inventaireId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                DetailInventaire detail = new DetailInventaire(
                        rs.getInt("id"),
                        rs.getInt("inventaire_id"),
                        rs.getInt("article_id"),
                        rs.getString("article_nom"),
                        rs.getInt("quantite_theorique"),
                        rs.getInt("quantite_reelle"),
                        rs.getInt("ecart")
                );
                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }
}
