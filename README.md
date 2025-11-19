### Hirex Portal Backend — Overview

### Goal
Hirex Portal Backend is a modular microservices backend for a hiring platform. It provides:
- A Users service for managing user profiles.
- A Vacancy service for managing job vacancies.
- An API Gateway that routes requests to the services.
- Service Discovery so services can find each other automatically.

### Tech Stack
- Language/Build: Java 17, Maven
- Frameworks: Spring Boot 3, Spring Cloud
- Microservices:
  - Spring Cloud Gateway (API Gateway)
  - Eureka Server/Client (Service Discovery)
- Persistence: Spring Data JPA, PostgreSQL
- DB Migrations: Flyway
- Security: Spring Security, OAuth2 Resource Server (JWT via Keycloak)
- Mapping/Boilerplate: MapStruct, Lombok


### Modules
- `discovery` (Eureka Server)
  - Port: `8761`
  - Central registry where services register themselves.

- `gateway` (API Gateway)
  - Port: `8080`
  - Routes:
    - `/users/**` → `lb://users-service`
    - `/vacancy/**` → `lb://vacancy-service`
  - Validates JWTs (issuer: `http://localhost:8081/realms/hirex`).

- `users` (Users Service)
  - Port: `8083`
  - DB: PostgreSQL `usersdb`
  - Registers with Eureka, secured via JWT.
  - Flyway migrations in `users/src/main/resources/db/migration`.

- `vacancy` (Vacancy Service)
  - Port: `8082`
  - DB: PostgreSQL `vacancydb`
  - Registers with Eureka, secured via JWT.
  - Flyway migrations in `vacancy/src/main/resources/db.migration`.

- `common`
  - Shared utilities to reuse code (e.g., roles, pagination helpers).

---

### Security
- All services are OAuth2 Resource Servers expecting JWTs.
- Issuer configured as `http://localhost:8081/realms/hirex` (Keycloak realm).
- The Gateway enforces auth at the edge; downstream services also validate tokens.

---

### Database & Migrations
- Users DB: `usersdb` (PostgreSQL)
- Vacancy DB: `vacancydb` (PostgreSQL)
- Flyway runs at startup applying migrations from each module.

---

### Testing
- Unit and slice tests present in `users` and `vacancy` modules under `src/test/java/...`.
- Run module tests:
```
mvn -pl users test
mvn -pl vacancy test
```

---
