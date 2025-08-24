-- Insertion des données de base
USE gestion_stock_isimm;

-- Types de produits
INSERT INTO type_produit (nom, est_consommable) VALUES
('Bureautique', TRUE),
('Informatique Consommable', TRUE),
('Technologie Consommable', TRUE),
('Tirage', TRUE),
('Nettoyage', TRUE),
('Entretien', TRUE),
('Jardin', TRUE),
('Divers Consommable', TRUE),
('Meuble', FALSE),
('Informatique Durable', FALSE),
('Technologie Durable', FALSE),
('Divers Durable', FALSE);

-- Locaux
INSERT INTO locaux (nom, type_local, capacite, description) VALUES
('Bib1', 'Bibliothèque', 100, 'Bibliothèque principale'),
('Bib2', 'Bibliothèque', 50, 'Bibliothèque secondaire'),
('AA', 'Amphithéâtre', 200, 'Amphithéâtre A'),
('AB', 'Amphithéâtre', 150, 'Amphithéâtre B'),
('A11', 'Salle d\'enseignement', 30, 'Salle A11'),
('A12', 'Salle d\'enseignement', 30, 'Salle A12'),
('B11', 'Salle d\'enseignement', 25, 'Salle B11'),
('Bureau Secrétaire Général', 'Administration', 5, 'Bureau du secrétaire général'),
('Bureau1', 'Administration', 3, 'Bureau administratif 1'),
('Magasin1', 'Magasin', 500, 'Magasin principal'),
('Magasin2', 'Magasin', 200, 'Magasin secondaire'),
('Bureau Enseignant1', 'Bureau Enseignant', 2, 'Bureau enseignant 1');

-- Fournisseurs
INSERT INTO fournisseurs (nom, adresse, telephone, email, contact_personne) VALUES
('Papeterie Moderne', '123 Rue de la Papeterie, Tunis', '71234567', 'contact@papeterie-moderne.tn', 'Ahmed Ben Ali'),
('TechnoPlus', '456 Avenue Technologique, Sfax', '74567890', 'info@technoplus.tn', 'Fatma Trabelsi'),
('Bureau Service', '789 Boulevard des Affaires, Sousse', '73890123', 'vente@bureau-service.tn', 'Mohamed Gharbi');

-- Services
INSERT INTO services (nom, responsable, telephone, email, budget_annuel) VALUES
('Scolarité', 'Mme Amina Sassi', '71111111', 'scolarite@isimm.tn', 15000.00),
('Photocopie', 'M. Karim Mejri', '71222222', 'photocopie@isimm.tn', 8000.00),
('Bibliothèque', 'Mme Leila Bouaziz', '71333333', 'bibliotheque@isimm.tn', 12000.00),
('Maintenance', 'M. Slim Karray', '71444444', 'maintenance@isimm.tn', 20000.00),
('Direction', 'M. Directeur', '71555555', 'direction@isimm.tn', 25000.00);

-- Articles exemples
INSERT INTO articles (reference, nom, description, type_produit_id, stock_minimal, stock_actuel, prix_unitaire, est_critique) VALUES
('PAP001', 'Papier A4 80g', 'Ramette papier A4 80g/m²', 1, 50, 120, 8.50, FALSE),
('PAP002', 'Stylos bleus', 'Stylos à bille bleus', 1, 100, 250, 0.75, FALSE),
('INF001', 'Cartouche HP 305', 'Cartouche d\'encre HP 305 noire', 2, 10, 25, 45.00, TRUE),
('INF002', 'Clé USB 32GB', 'Clé USB 32GB USB 3.0', 2, 20, 35, 15.00, FALSE),
('NET001', 'Produit nettoyage sol', 'Détergent pour sol 1L', 5, 15, 40, 12.00, FALSE),
('MEB001', 'Chaise de bureau', 'Chaise de bureau ergonomique', 9, 5, 12, 150.00, FALSE),
('ORD001', 'Ordinateur portable', 'PC portable Dell Latitude', 10, 2, 8, 800.00, TRUE);
