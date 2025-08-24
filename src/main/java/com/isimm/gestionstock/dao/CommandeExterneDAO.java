package com.isimm.gestionstock.dao;

import com.isimm.gestionstock.model.CommandeExterne;
import com.isimm.gestionstock.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class CommandeExterneDAO {

    public ObservableList<CommandeExterne> getAllCommandesExternes() {
        ObservableList<CommandeExterne> commandes = FXCollections.observableArrayList();
        String query = "SELECT * FROM commandes_externes ORDER BY date_commande DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CommandeExterne commande = new CommandeExterne(
                        rs.getInt("id"),
                        rs.getString("numero_commande"),
                        rs.getInt("fournisseur_id"),
                        rs.getTimestamp("date_commande").toLocalDateTime(),
                        rs.getString("statut"),
                        rs.getDouble("montant_total"),
                        rs.getString("observations")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    public boolean insertCommandeExterne(CommandeExterne commande) {
        String query = "INSERT INTO commandes_externes (numero_commande, fournisseur_id, statut, montant_total, observations) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, commande.getNumeroCommande());
            stmt.setInt(2, commande.getFournisseurId());
            stmt.setString(3, commande.getStatut());
            stmt.setDouble(4, commande.getMontantTotal());
            stmt.setString(5, commande.getObservations());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCommandeExterne(CommandeExterne commande) {
        String query = "UPDATE commandes_externes SET numero_commande=?, fournisseur_id=?, statut=?, montant_total=?, observations=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, commande.getNumeroCommande());
            stmt.setInt(2, commande.getFournisseurId());
            stmt.setString(3, commande.getStatut());
            stmt.setDouble(4, commande.getMontantTotal());
            stmt.setString(5, commande.getObservations());
            stmt.setInt(6, commande.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCommandeExterne(int id) {
        String query = "DELETE FROM commandes_externes WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validerCommande(int commandeId) {
        String query = "UPDATE commandes_externes SET statut='VALIDEE' WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, commandeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
