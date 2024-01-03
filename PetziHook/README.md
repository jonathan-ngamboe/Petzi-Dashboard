# Projet Petzi

## Description
Petzi est un serveur Spring Boot conçu pour gérer les requêtes webhook de Petzi. Il offre des fonctionnalités pour enregistrer, récupérer et notifier en temps réel des informations JSON.

## Fonctionnalités
- Réception et traitement des requêtes webhook avec validation de signature.
- Stockage des données JSON reçues dans une base de données H2.
- Récupération des données JSON via une API REST.
- Notification en temps réel des clients via Server-Sent Events (SSE) lors de l'enregistrement de nouveaux JSON.

## Prérequis
- Java JDK 11 ou supérieur.
- Maven pour la gestion des dépendances et le build du projet.

## Installation et Configuration
1. **Cloner le projet** : Utilisez `git clone https://github.com/Jonathanngamboe/petzi` pour cloner le projet sur votre machine locale.
2. **Configuration de la base de données** : Assurez-vous que le fichier `application.properties` est configuré avec les paramètres corrects pour la base de données H2 ou laissez les paramètres par défaut.
3. **Configurer la clé secrète** : Définissez la clé secrète utilisée pour la validation de la signature dans `PetziApplication.java`.

## Utilisation
### Démarrer le serveur
Lancez le fichier `PetziApplication.java` pour démarrer le serveur.

### Utiliser l'API
- **Enregistrer JSON** : Envoyez une requête POST à `http://localhost:8080/json/save` avec un corps JSON et les en-têtes appropriés.
- **Récupérer JSON** : Envoyez une requête GET à `http://localhost:8080/json/get/{id}` pour récupérer les informations JSON enregistrées.
- **Recevoir des notifications SSE** : Connectez-vous à `http://localhost:8080/sse` pour s'abonner aux notifications SSE.

## Contact
- Dev : Jonathan Ngamboe
- E-mail : jonathan.ngamboe@he-arc.ch
