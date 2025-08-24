package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.CommandeInterne;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CommandeInterneDAO {

    public ObservableList<CommandeInterne> getAllCommandesInternes() {
        ObservableList<CommandeInterne> commandes = FXCollections.observableArrayList();
        String sql = """
            SELECT ci.id, ci.numero_commande, ci.service_id, s.nom as service_nom,
                   ci.date_commande, ci.date_livraison, ci.statut
            FROM commandes_internes ci
            LEFT JOIN services s ON ci.service_id = s.id
            ORDER BY ci.date_commande DESC
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CommandeInterne commande = new CommandeInterne(
                        rs.getInt("id"),
                        rs.getString("numero_commande"),
                        rs.getInt("service_id"),
                        rs.getString("service_nom"),
                        rs.getDate("date_commande") != null ? rs.getDate("date_commande").toLocalDate() : null,
                        rs.getDate("date_livraison") != null ? rs.getDate("date_livraison").toLocalDate() : null,
                        rs.getString("statut")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    public boolean insertCommandeInterne(CommandeInterne commande) {
        String sql = "INSERT INTO commandes_internes (numero_commande, service_id, date_commande, date_livraison, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, commande.getNumeroCommande());
            stmt.setInt(2, commande.getServiceId());
            stmt.setDate(3, commande.getDateCommande() != null ? Date.valueOf(commande.getDateCommande()) : null);
            stmt.setDate(4, commande.getDateLivraison() != null ? Date.valueOf(commande.getDateLivraison()) : null);
            stmt.setString(5, commande.getStatut());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCommandeInterne(CommandeInterne commande) {
        String sql = "UPDATE commandes_internes SET numero_commande = ?, service_id = ?, date_commande = ?, date_livraison = ?, statut = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, commande.getNumeroCommande());
            stmt.setInt(2, commande.getServiceId());
            stmt.setDate(3, commande.getDateCommande() != null ? Date.valueOf(commande.getDateCommande()) : null);
            stmt.setDate(4, commande.getDateLivraison() != null ? Date.valueOf(commande.getDateLivraison()) : null);
            stmt.setString(5, commande.getStatut());
            stmt.setInt(6, commande.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCommandeInterne(int id) {
        String sql = "DELETE FROM commandes_internes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validerCommande(int id) {
        String sql = "UPDATE commandes_internes SET statut = 'VALIDEE' WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalCommandesInternes() {
        String sql = "SELECT COUNT(*) FROM commandes_internes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
