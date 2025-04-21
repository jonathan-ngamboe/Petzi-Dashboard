# PetziHook Module

## Description
`PetziHook` is the webhook handling module of the Petzi project. It is responsible for receiving and processing webhook requests, storing the received JSON data, and publishing it to a Kafka topic for real-time processing.

## Key Features
- Validates and processes incoming webhook requests.
- Persists JSON data in an H2 database.
- Publishes JSON payloads to a Kafka topic for further processing.

## Configuration
- **Database:** Configure the H2 database connection settings in the `application.properties` file.
- **Secret Key:** Set the secret key used for signature validation in `SignatureService.java`.
- **Kafka:** If needed, update Kafka broker details, topic names, and other Kafka-related settings in `application.properties`.

## Getting Started
To ensure `PetziHook` can communicate with Kafka, make sure Kafka brokers are running and accessible. Use the provided Docker configuration in the main project to start Kafka instances.

1. Start Kafka services by running `docker-compose up` from the Docker directory at the root of the project.
2. Update `PetziHook`'s configuration files to point to the correct Kafka brokers and topics, or use the default values.
3. Run the `PetziHookApplication.java` class to start the `PetziHook` module. Ensure Kafka is running and reachable for `PetziHook` to function properly.

## Usage
- **POST `/petzihook/json/save`**: Save and publish JSON data to Kafka.
  - When running locally: `http://localhost:8085/petzihook/json/save`
- **GET `/petzihook/json/get/{id}`**: Retrieve a specific JSON entry from the database.
  - When running locally: `http://localhost:8085/petzihook/json/get/{id}`

### Database Access
The project uses an embedded H2 database stored at `/PetziHook/database.mv.db`.  
To access the database console, use:  
```
http://localhost:8085/petzihook/h2-console
```

**Default connection settings:**
- **JDBC URL**: `jdbc:h2:file:./PetziHook/database`
- **User Name**: *(empty)*
- **Password**: *(empty)*

⚠️ **Note:** If you manually modify the database, changes will not be reflected in the dashboard immediately. You must restart the PetziHook module for data to be published to the Kafka topic again.

---

For more details on other modules and the overall project architecture, see the [main Petzi project README](https://github.com/Jonathanngamboe/petzi).