package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.Alerte;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class AlerteDAO {

    public ObservableList<Alerte> getAlertesStockFaible() {
        ObservableList<Alerte> alertes = FXCollections.observableArrayList();
        String sql = """
            SELECT a.id, a.reference, a.nom, a.stock_actuel, a.stock_minimal
            FROM articles a
            WHERE a.stock_actuel <= a.stock_minimal AND a.stock_minimal > 0
            ORDER BY (a.stock_actuel - a.stock_minimal) ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String message = "Stock faible: " + rs.getInt("stock_actuel") + " unités restantes (minimum: " + rs.getInt("stock_minimal") + ")";
                String priorite = rs.getInt("stock_actuel") == 0 ? "CRITIQUE" : "MOYENNE";

                Alerte alerte = new Alerte(
                        rs.getInt("id"),
                        "STOCK_FAIBLE",
                        message,
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getInt("stock_actuel"),
                        rs.getInt("stock_minimal"),
                        null,
                        priorite,
                        false
                );
                alertes.add(alerte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alertes;
    }

    public ObservableList<Alerte> getAlertesPeremption() {
        ObservableList<Alerte> alertes = FXCollections.observableArrayList();
        String sql = """
            SELECT a.id, a.reference, a.nom, s.date_peremption, s.quantite
            FROM articles a
            JOIN stockage s ON a.id = s.article_id
            WHERE s.date_peremption IS NOT NULL 
            AND s.date_peremption <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)
            ORDER BY s.date_peremption ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LocalDate datePeremption = rs.getDate("date_peremption").toLocalDate();
                long joursRestants = LocalDate.now().until(datePeremption).getDays();

                String message = "Péremption dans " + joursRestants + " jours (" + rs.getInt("quantite") + " unités)";
                String priorite = joursRestants <= 7 ? "CRITIQUE" : joursRestants <= 15 ? "HAUTE" : "MOYENNE";

                Alerte alerte = new Alerte(
                        rs.getInt("id"),
                        "PEREMPTION",
                        message,
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getInt("quantite"),
                        0,
                        datePeremption,
                        priorite,
                        false
                );
                alertes.add(alerte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alertes;
    }

    public ObservableList<Alerte> getAlertesArticlesCritiques() {
        ObservableList<Alerte> alertes = FXCollections.observableArrayList();
        String sql = """
            SELECT id, reference, nom, stock_actuel, stock_minimal
            FROM articles
            WHERE est_critique = TRUE
            ORDER BY stock_actuel ASC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String message = "Article critique nécessite une attention particulière";

                Alerte alerte = new Alerte(
                        rs.getInt("id"),
                        "ARTICLE_CRITIQUE",
                        message,
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getInt("stock_actuel"),
                        rs.getInt("stock_minimal"),
                        null,
                        "HAUTE",
                        false
                );
                alertes.add(alerte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alertes;
    }

    public ObservableList<Alerte> getToutesAlertes() {
        ObservableList<Alerte> toutesAlertes = FXCollections.observableArrayList();
        toutesAlertes.addAll(getAlertesStockFaible());
        toutesAlertes.addAll(getAlertesPeremption());
        toutesAlertes.addAll(getAlertesArticlesCritiques());
        return toutesAlertes;
    }

    public int getTotalAlertes() {
        return getToutesAlertes().size();
    }
}
