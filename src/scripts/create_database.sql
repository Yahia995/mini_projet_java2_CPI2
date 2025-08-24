-- Base de données pour la gestion de stock ISIMM
CREATE DATABASE IF NOT EXISTS gestion_stock_isimm;
USE gestion_stock_isimm;

-- Table des types de produits
CREATE TABLE type_produit (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    est_consommable BOOLEAN NOT NULL
);

-- Table des locaux
CREATE TABLE locaux (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    type_local VARCHAR(50) NOT NULL,
    capacite INT DEFAULT 0,
    description TEXT
);

-- Table des fournisseurs
CREATE TABLE fournisseurs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    adresse TEXT,
    telephone VARCHAR(20),
    email VARCHAR(100),
    contact_personne VARCHAR(100)
);

-- Table des services consommateurs
CREATE TABLE services (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    responsable VARCHAR(100),
    telephone VARCHAR(20),
    email VARCHAR(100),
    budget_annuel DECIMAL(10,2)
);

-- Table des articles
CREATE TABLE articles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reference VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(200) NOT NULL,
    description TEXT,
    type_produit_id INT,
    stock_minimal INT DEFAULT 0,
    stock_actuel INT DEFAULT 0,
    prix_unitaire DECIMAL(10,2),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    est_critique BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (type_produit_id) REFERENCES type_produit(id)
);

-- Table de stockage (relation article-local)
CREATE TABLE stockage (
    id INT PRIMARY KEY AUTO_INCREMENT,
    article_id INT,
    local_id INT,
    quantite INT NOT NULL,
    date_peremption DATE,
    FOREIGN KEY (article_id) REFERENCES articles(id),
    FOREIGN KEY (local_id) REFERENCES locaux(id)
);

-- Table des commandes externes (approvisionnement)
CREATE TABLE commandes_externes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_commande VARCHAR(50) UNIQUE NOT NULL,
    fournisseur_id INT,
    date_commande DATE NOT NULL,
    date_livraison_prevue DATE,
    date_livraison_reelle DATE,
    statut ENUM('EN_ATTENTE', 'VALIDEE', 'LIVREE', 'ANNULEE') DEFAULT 'EN_ATTENTE',
    montant_total DECIMAL(10,2),
    FOREIGN KEY (fournisseur_id) REFERENCES fournisseurs(id)
);

-- Table des détails commandes externes
CREATE TABLE details_commande_externe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    commande_id INT,
    article_id INT,
    quantite_commandee INT NOT NULL,
    quantite_livree INT DEFAULT 0,
    prix_unitaire DECIMAL(10,2),
    FOREIGN KEY (commande_id) REFERENCES commandes_externes(id),
    FOREIGN KEY (article_id) REFERENCES articles(id)
);

-- Table des commandes internes (consommation)
CREATE TABLE commandes_internes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_commande VARCHAR(50) UNIQUE NOT NULL,
    service_id INT,
    date_commande DATE NOT NULL,
    date_livraison DATE,
    statut ENUM('EN_ATTENTE', 'VALIDEE', 'LIVREE', 'ANNULEE') DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (service_id) REFERENCES services(id)
);

-- Table des détails commandes internes
CREATE TABLE details_commande_interne (
    id INT PRIMARY KEY AUTO_INCREMENT,
    commande_id INT,
    article_id INT,
    quantite_demandee INT NOT NULL,
    quantite_livree INT DEFAULT 0,
    FOREIGN KEY (commande_id) REFERENCES commandes_internes(id),
    FOREIGN KEY (article_id) REFERENCES articles(id)
);

-- Table des mouvements de stock (traçabilité)
CREATE TABLE mouvements_stock (
    id INT PRIMARY KEY AUTO_INCREMENT,
    article_id INT,
    type_mouvement ENUM('ENTREE', 'SORTIE', 'TRANSFERT', 'INVENTAIRE') NOT NULL,
    quantite INT NOT NULL,
    local_source_id INT,
    local_destination_id INT,
    reference_document VARCHAR(100),
    date_mouvement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    utilisateur VARCHAR(100),
    commentaire TEXT,
    FOREIGN KEY (article_id) REFERENCES articles(id),
    FOREIGN KEY (local_source_id) REFERENCES locaux(id),
    FOREIGN KEY (local_destination_id) REFERENCES locaux(id)
);

-- Table des inventaires
CREATE TABLE inventaires (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date_inventaire DATE NOT NULL,
    local_id INT,
    statut ENUM('EN_COURS', 'TERMINE', 'VALIDE') DEFAULT 'EN_COURS',
    commentaire TEXT,
    FOREIGN KEY (local_id) REFERENCES locaux(id)
);

-- Table des détails inventaires
CREATE TABLE details_inventaire (
    id INT PRIMARY KEY AUTO_INCREMENT,
    inventaire_id INT,
    article_id INT,
    quantite_theorique INT,
    quantite_reelle INT,
    ecart INT,
    FOREIGN KEY (inventaire_id) REFERENCES inventaires(id),
    FOREIGN KEY (article_id) REFERENCES articles(id)
);
