# Hirex Portal Backend — Overview

## Goal
Hirex Portal Backend is a modular microservices backend for a hiring and recruitment platform.  
It provides:

- A Users service for managing user profiles.
- A Vacancy service for managing job vacancies.
- A Job Application service for managing candidate applications.
- An API Gateway that routes, authenticates and orchestrates requests.
- Service Discovery so services can find each other automatically.
- Observability stack for logs and tracing.
- Independent PostgreSQL databases per microservice.

The long-term goal is to support intelligent matching between candidates and vacancies using generative AI.

---

## 🧱 Architecture Overview

The solution follows a **decoupled microservices architecture**, composed of:

- **Backend (hirex-portal-backend)**  
  Java + Spring Boot 3 microservices running in Kubernetes.

All services communicate via HTTP using REST + JSON.  
Each microservice owns its own data and exposes operations only via APIs.

### Microservices (Backend)

| Service | Description | Port | Database |
|--------|-------------|------|----------|
| **discovery** | Eureka Server (Service Registry) | 8761 | — |
| **gateway** | Spring Cloud Gateway (Routing + JWT validation) | 8080 | — |
| **users** | Users domain (profiles, roles, accounts) | 8083 | PostgreSQL `usersdb` |
| **vacancy** | Vacancies domain (jobs, categories) | 8082 | PostgreSQL `vacancydb` |
| **job-application** | Applications domain (candidate → job) | 8084 | PostgreSQL `jobappdb` |

Each microservice includes:

- Domain layer
- DTOs + mappers (MapStruct)
- Repositories (Spring Data JPA)
- Services with business logic
- Controllers (REST)
- Global Exception Handling
- Flyway migrations
- Logging with traceId/spanId

---

## 🏗️ Tech Stack

### Language / Build
- **Java 17**
- **Maven**

### Frameworks
- **Spring Boot 3**
- **Spring Cloud**

### Microservices & Routing
- **Spring Cloud Gateway** (API Gateway)
- **Spring Cloud Eureka** (Discovery)
- **Spring Security OAuth2 Resource Server**

### Persistence
- **PostgreSQL** (one database per domain)
- **Spring Data JPA**
- **Flyway** (DB migrations)

### Mapping & Boilerplate
- **MapStruct**
- **Lombok**

### Packaging & Deploy
- **Docker**
- **Kubernetes (Docker Desktop cluster)**

### Security
- **Keycloak** (OIDC + JWT)
- Services validate JWT using JWKs exposed by Keycloak.

---

## 🔐 Security

All backend services (gateway, users, vacancy, job-application) use:

- OAuth2 Resource Server
- JWT validation (RS256)
- Realm: `hirex` (Keycloak)
- Gateway performs authentication at the edge.

The Gateway routes authenticated requests:

```yaml
/users/** → lb://users-service
/vacancy/** → lb://vacancy-service
/job-application/** → lb://job-application-service