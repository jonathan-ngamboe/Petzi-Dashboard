# Module PetziHook

## Description
`PetziHook` est le module du projet Petzi, dédié à la réception et au traitement des webhooks. Il enregistre les données JSON reçues et les publie sur un topic Kafka pour un traitement en temps réel.

## Fonctionnalités Clés
- Validation et traitement des requêtes webhook.
- Stockage persistant des données JSON dans la base de données H2.
- Publication des données JSON sur un topic Kafka pour le traitement en temps réel.

## Configuration
- **Base de données** : Configurez `application.properties` avec les paramètres corrects pour la base de données H2.
- **Clé secrète** : Définissez la clé secrète pour la validation de la signature dans `SignatureService.java`.
- **Kafka** : Si nécessaire, configurez les détails du broker Kafka, le nom du topic, et d'autres paramètres relatifs à Kafka dans `application.properties`.

## Démarrage
Pour que `PetziHook` puisse communiquer avec Kafka, vous devez vous assurer que les brokers Kafka sont opérationnels et accessibles. Les fichiers Docker fournis dans le projet global doivent être utilisés pour démarrer les instances de Kafka nécessaires.

1. Démarrez les services Kafka en exécutant `docker-compose up` dans le répertoire Docker à la racine du projet si ce n'est pas déjà fait
2. Mettez à jour les fichiers de configuration de `PetziHook` pour pointer vers les brokers Kafka et les topics appropriés si nécessaire ou utilisez les valeurs par défaut.
3. Exécutez `PetziHookApplication.java` pour lancer le module `PetziHook`. Assurez-vous que Kafka est en cours d'exécution et accessible pour que `PetziHook` fonctionne correctement.

## Utilisation
- **POST `/petzihook/json/save`** : Pour enregistrer et publier les données JSON sur Kafka.
  - Si le programme est exécuté en local, vous pouvez utiliser l'URL suivante : `http://localhost:8085/petzihook/json/save`.
- **GET `/petzihook/json/get/{id}`** : Pour récupérer des données JSON spécifiques stockées dans la base de données.
  - Si le programme est exécuté en local, vous pouvez utiliser l'URL suivante : `http://localhost:8085/petzihook/json/get/{id}`.

## Support et Contact
Pour toute question ou soutien technique, veuillez contacter :
- Dev : Jonathan Ngamboe
- E-mail : jonathan.ngamboe@he-arc.ch

---

Pour plus d'informations sur les autres modules et l'architecture globale du projet, veuillez consulter le [README global du projet Petzi](https://github.com/Jonathanngamboe/petzi).
