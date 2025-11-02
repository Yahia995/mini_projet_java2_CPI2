# 📦 Système de Gestion de Stock - ISIMM

Une application JavaFX complète pour la gestion de stock du magasin de l'Institut Supérieur d'Informatique et de Mathématiques de Monastir (ISIMM).

## 🎯 Objectifs du Projet

Cette application de gestion de stock a été conçue pour répondre aux besoins spécifiques de l'ISIMM en matière de :
- Gestion rigoureuse des stocks avec traçabilité complète
- Suivi des distributions et achats de produits
- Gestion des clients (services de l'ISIMM) et fournisseurs
- Inventaire automatisé et rapports statistiques
- Interface utilisateur intuitive et réactive

## ✨ Fonctionnalités Principales

### 📋 Gestion des Articles
- **CRUD complet** : Créer, modifier, supprimer des articles
- **Types d'articles** : Consommables (bureautique, informatique, nettoyage) et durables (meubles, équipements)
- **Recherche avancée** : Par référence, date de péremption, mouvement de stock
- **Alertes automatiques** : Stock minimal, péremption proche, articles critiques
- **Affectation aux locaux** : Totale ou partielle

### 🏢 Gestion des Locaux
- **Types de locaux** : Bibliothèques, amphithéâtres, salles d'enseignement, administration, magasins, bureaux
- **Recherche** : Par caractéristiques et localisation
- **Affectation d'articles** : Suivi par local de stockage

### 🤝 Gestion des Fournisseurs
- **Informations complètes** : Contact, adresse, spécialités
- **Historique des commandes** : Suivi des relations commerciales
- **Recherche multicritères** : Par nom, spécialité, localisation

### 🏛️ Gestion des Services
- **Services de l'ISIMM** : Scolarité, photocopie, bibliothèque, etc.
- **Budget annuel** : Suivi des allocations budgétaires
- **Consommation** : Historique des demandes par service

### 📦 Commandes Externes (Approvisionnement)
- **Création et validation** : Workflow complet de commande
- **Stockage automatique** : Mise à jour des stocks lors de la validation
- **Traçabilité** : Historique complet des interventions
- **Suivi fournisseur** : Liaison avec les fournisseurs

### 📤 Commandes Internes (Distribution)
- **Demandes des services** : Gestion des besoins internes
- **Déstockage automatique** : Mise à jour des stocks lors de la validation
- **Contrôle des stocks** : Vérification de la disponibilité
- **Traçabilité complète** : Suivi des mouvements

### 📊 Inventaire
- **Inventaire périodique** : Création d'inventaires par local
- **Comparaison** : Quantités théoriques vs réelles
- **Rapports détaillés** : Écarts et ajustements
- **Bilan annuel** : Stocks par service et local

### 🚨 Système d'Alertes
- **Alertes automatiques** : Stock faible, péremption proche
- **Articles critiques** : Identification des produits sensibles
- **Filtrage** : Par type, priorité, statut
- **Traitement** : Marquage des alertes traitées

### 📈 Statistiques et Rapports
- **Produits les plus consommés** : Analyse des tendances
- **Services les plus actifs** : Statistiques de consommation
- **Évolution des stocks** : Graphiques temporels
- **Rapports personnalisés** : Export et impression

## 🛠️ Technologies Utilisées

- **Java 21** : Langage de programmation principal
- **JavaFX 21** : Interface utilisateur moderne
- **MySQL 8.0+** : Base de données relationnelle
- **Maven** : Gestionnaire de dépendances
- **CSS** : Stylisation de l'interface

## 📋 Prérequis

- **Java Development Kit (JDK) 21**
- **MySQL Server 8.0** ou supérieur
- **Maven 3.6** ou supérieur
- **IDE** : IntelliJ IDEA, Eclipse, ou VS Code avec extensions Java

## 🚀 Installation

### 1. Cloner le Projet
\`\`\`bash
git clone https://github.com/votre-username/gestion-stock-isimm.git
cd gestion-stock-isimm
\`\`\`

### 2. Configuration de la Base de Données
\`\`\`bash
# Démarrer MySQL
mysql -u root -p

# Exécuter les scripts SQL
mysql -u root -p < scripts/create_database.sql
mysql -u root -p < scripts/insert_sample_data.sql
\`\`\`

### 3. Configuration de la Connexion
Modifier le fichier `src/main/java/com/isimm/gestionstock/util/DatabaseConnection.java` :
\`\`\`java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock_isimm";
private static final String USERNAME = "votre_username";
private static final String PASSWORD = "votre_password";
\`\`\`

### 4. Compilation et Exécution
\`\`\`bash
# Compiler le projet
mvn clean compile

# Lancer l'application
mvn javafx:run
\`\`\`

## 📁 Structure du Projet

\`\`\`
src/

├── main/

│   ├── java/com/isimm/gestionstock/

│   │   ├── Main.java                    # Point d'entrée

│   │   ├── controller/                  # Contrôleurs MVC

│   │   │   ├── MainController.java

│   │   │   ├── ArticlesController.java

│   │   │   ├── DashboardController.java

│   │   │   └── ...

│   │   ├── model/                       # Modèles de données

│   │   │   ├── Article.java

│   │   │   ├── Local.java

│   │   │   ├── Fournisseur.java

│   │   │   └── ...

│   │   ├── dao/                         # Accès aux données

│   │   │   ├── ArticleDAO.java

│   │   │   ├── LocalDAO.java

│   │   │   └── ...

│   │   └── util/

│   │       └── DatabaseConnection.java  # Connexion DB

│   │── resources/

│   │   ├── fxml/                        # Interfaces utilisateur

│   │   │    │   ├── MainView.fxml


│   │   │    │   ├── ArticlesView.fxml

│   │   │    │   └── ...

│   │   │    └── css/

│   │   │        └── application.css          # Styles CSS

│   └── scripts/

│       ├── create_database.sql              # Création DB

│       └── insert_sample_data.sql           # Données d'exemple

└── pom.xml                              # Configuration Maven
\`\`\`

## 🎨 Interface Utilisateur

L'application dispose d'une interface moderne et intuitive avec :
- **Tableau de bord** : Vue d'ensemble des statistiques
- **Navigation latérale** : Accès rapide aux modules
- **Tableaux interactifs** : Tri, recherche, pagination
- **Formulaires validés** : Saisie sécurisée des données
- **Graphiques** : Visualisation des statistiques
- **Alertes visuelles** : Notifications en temps réel

## 🔧 Configuration Avancée

### Variables d'Environnement
\`\`\`bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=gestion_stock_isimm
export DB_USER=votre_username
export DB_PASSWORD=votre_password
\`\`\`

### Personnalisation CSS
Modifier `src/main/resources/css/application.css` pour personnaliser l'apparence.

## 📊 Base de Données

### Tables Principales
- **articles** : Produits et leurs caractéristiques
- **locaux** : Lieux de stockage
- **fournisseurs** : Partenaires commerciaux
- **services** : Départements de l'ISIMM
- **commandes_externes** : Approvisionnements
- **commandes_internes** : Distributions
- **inventaires** : Contrôles périodiques
- **alertes** : Notifications système

## 👥 Équipe de Développement

- **Développeur Principal** : Achouri Yahia
- **Institution** : ISIMM - Institut Supérieur d'Informatique et de Mathématiques de Monastir
- **Année Académique** : 2024/2025

---

**Développé avec ❤️ pour l'ISIMM**
