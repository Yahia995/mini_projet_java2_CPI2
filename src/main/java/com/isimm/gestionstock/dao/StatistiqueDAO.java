package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatistiqueDAO {

    public Map<String, Integer> getStatistiquesGenerales() {
        Map<String, Integer> stats = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Total articles
            String sql1 = "SELECT COUNT(*) as total FROM articles";
            try (PreparedStatement pstmt = conn.prepareStatement(sql1);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalArticles", rs.getInt("total"));
                }
            }

            // Articles en alerte
            String sql2 = "SELECT COUNT(*) as total FROM articles WHERE stock_actuel <= stock_minimal";
            try (PreparedStatement pstmt = conn.prepareStatement(sql2);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("articlesEnAlerte", rs.getInt("total"));
                }
            }

            // Commandes en cours
            String sql3 = "SELECT COUNT(*) as total FROM commandes_externes WHERE statut = 'EN_ATTENTE'";
            try (PreparedStatement pstmt = conn.prepareStatement(sql3);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("commandesEnCours", rs.getInt("total"));
                }
            }

            // Total services
            String sql4 = "SELECT COUNT(*) as total FROM services";
            try (PreparedStatement pstmt = conn.prepareStatement(sql4);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalServices", rs.getInt("total"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public ObservableList<Map<String, Object>> getArticlesPlusConsommes() {
        ObservableList<Map<String, Object>> articles = FXCollections.observableArrayList();
        String sql = "SELECT a.nom, SUM(dci.quantite_livree) as total_consomme " +
                "FROM articles a " +
                "JOIN details_commande_interne dci ON a.id = dci.article_id " +
                "JOIN commandes_internes ci ON dci.commande_id = ci.id " +
                "WHERE ci.statut = 'LIVREE' " +
                "GROUP BY a.id, a.nom " +
                "ORDER BY total_consomme DESC LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> article = new HashMap<>();
                article.put("nom", rs.getString("nom"));
                article.put("quantite", rs.getInt("total_consomme"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public ObservableList<Map<String, Object>> getServicesPlusConsommateurs() {
        ObservableList<Map<String, Object>> services = FXCollections.observableArrayList();
        String sql = "SELECT s.nom, COUNT(ci.id) as nb_commandes " +
                "FROM services s " +
                "JOIN commandes_internes ci ON s.id = ci.service_id " +
                "WHERE ci.statut = 'LIVREE' " +
                "GROUP BY s.id, s.nom " +
                "ORDER BY nb_commandes DESC LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> service = new HashMap<>();
                service.put("nom", rs.getString("nom"));
                service.put("commandes", rs.getInt("nb_commandes"));
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    public ObservableList<Map<String, Object>> getMouvementsStock(int mois, int annee) {
        ObservableList<Map<String, Object>> mouvements = FXCollections.observableArrayList();
        String sql = "SELECT DATE(date_mouvement) as date, type_mouvement, COUNT(*) as nb_mouvements " +
                "FROM mouvements_stock " +
                "WHERE MONTH(date_mouvement) = ? AND YEAR(date_mouvement) = ? " +
                "GROUP BY DATE(date_mouvement), type_mouvement " +
                "ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, mois);
            pstmt.setInt(2, annee);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> mouvement = new HashMap<>();
                mouvement.put("date", rs.getDate("date"));
                mouvement.put("type", rs.getString("type_mouvement"));
                mouvement.put("nombre", rs.getInt("nb_mouvements"));
                mouvements.add(mouvement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mouvements;
    }
}
