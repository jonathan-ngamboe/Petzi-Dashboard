# Module RTBI (Real-Time Business Intelligence)

## Description
Le module `RTBI` est conçu pour consommer et traiter les données JSON en temps réel via Kafka. Il permet une analyse en temps réel des événements et l'envoi des données en temps réel aux clients abonnés via SSE.

## Fonctionnalités Clés
- Consommation des données JSON depuis un topic Kafka.
- Calcul des statistiques en temps réel.
- Envoi des données en temps réel aux clients via SSE.

## Configuration
- **Kafka Consumer** : Configuration des détails du consumer Kafka dans `application.properties`, y compris les informations de connexion au broker Kafka et les paramètres de consommation.

## Démarrage
Le module `RTBI` nécessite que les brokers Kafka soient opérationnels.

1. Assurez-vous que les instances Kafka sur Docker sont en cours d'exécution comme décrit dans le [README global du projet Petzi](https://github.com/Jonathanngamboe/petzi).
2. Vérifiez que les containers Docker sont en cours d'exécution.
3. Pour lancer le module `RTBI`, exécutez la classe `RtbiApplication.java`. Ceci démarrera le consumer Kafka ainsi que le SSE et initiera le traitement des données.

## Utilisation
Le module `RTBI` fonctionne principalement en arrière-plan, en consommant et en traitant les données de Kafka. Les logs et les alertes générés seront le principal moyen d'interaction pour les utilisateurs et les systèmes aval.
Pour s'abonner aux données en temps réel, les clients peuvent se connecter au SSE via l'URL `http://localhost:8082/rtbi/sse`.

## Support et Contact
En cas de questions ou pour obtenir de l'aide technique, veuillez contacter :
- Dev : Jonathan Ngamboe
- E-mail : jonathan.ngamboe@he-arc.ch

---

Consultez le [README global du projet Petzi](https://github.com/Jonathanngamboe/petzi) pour plus d'informations sur l'architecture du projet et les autres modules.
