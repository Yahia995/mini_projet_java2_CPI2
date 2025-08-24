package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.Article;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class ArticleDAO {

    public ObservableList<Article> getAllArticles() {
        ObservableList<Article> articles = FXCollections.observableArrayList();
        String query = "SELECT * FROM articles ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("type_produit_id"),
                        rs.getInt("stock_minimal"),
                        rs.getInt("stock_actuel"),
                        rs.getDouble("prix_unitaire"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getBoolean("est_critique")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public boolean insertArticle(Article article) {
        String query = "INSERT INTO articles (reference, nom, description, type_produit_id, " +
                "stock_minimal, stock_actuel, prix_unitaire, est_critique) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, article.getReference());
            stmt.setString(2, article.getNom());
            stmt.setString(3, article.getDescription());
            stmt.setInt(4, article.getTypeProduitId());
            stmt.setInt(5, article.getStockMinimal());
            stmt.setInt(6, article.getStockActuel());
            stmt.setDouble(7, article.getPrixUnitaire());
            stmt.setBoolean(8, article.isEstCritique());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateArticle(Article article) {
        String query = "UPDATE articles SET reference=?, nom=?, description=?, type_produit_id=?, " +
                "stock_minimal=?, stock_actuel=?, prix_unitaire=?, est_critique=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, article.getReference());
            stmt.setString(2, article.getNom());
            stmt.setString(3, article.getDescription());
            stmt.setInt(4, article.getTypeProduitId());
            stmt.setInt(5, article.getStockMinimal());
            stmt.setInt(6, article.getStockActuel());
            stmt.setDouble(7, article.getPrixUnitaire());
            stmt.setBoolean(8, article.isEstCritique());
            stmt.setInt(9, article.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteArticle(int id) {
        String query = "DELETE FROM articles WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Article> getArticlesEnAlerte() {
        ObservableList<Article> articles = FXCollections.observableArrayList();
        String query = "SELECT * FROM articles WHERE stock_actuel <= stock_minimal ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("type_produit_id"),
                        rs.getInt("stock_minimal"),
                        rs.getInt("stock_actuel"),
                        rs.getDouble("prix_unitaire"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getBoolean("est_critique")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public ObservableList<Article> getArticlesCritiques() {
        ObservableList<Article> articles = FXCollections.observableArrayList();
        String query = "SELECT * FROM articles WHERE est_critique = true ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("reference"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getInt("type_produit_id"),
                        rs.getInt("stock_minimal"),
                        rs.getInt("stock_actuel"),
                        rs.getDouble("prix_unitaire"),
                        rs.getTimestamp("date_creation").toLocalDateTime(),
                        rs.getBoolean("est_critique")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public void updateStock(int articleId, int nouvelleQuantite) {
        String query = "UPDATE articles SET stock_actuel = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, articleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}