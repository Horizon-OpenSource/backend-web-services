# EcoTrack — Organization Management

## Backend Web Services — AV2

### 1. Overview

Organization Management is a bounded context of the EcoTrack platform responsible for registering, consulting, and updating organizations, as well as managing their associated sites and business units.

During TB1, the frontend interacted with a Mock REST API implemented with JSON Server.

For AV2, the organization registration, consultation, and update operations were integrated with the Spring Boot backend and MySQL database.

### 2. Technologies

- Java 27
- Spring Boot 4.1.2
- Spring Data JPA
- MySQL
- Maven
- Swagger / OpenAPI
- Lombok
- JUnit 5

### 3. Architecture

The bounded context follows the architectural principles of Domain-Driven Design (DDD), CQRS, and Hexagonal Architecture.

The implementation is organized into four main layers:

- **Domain:** Aggregates, commands, queries, and repository contracts.
- **Application:** Command and query services.
- **Infrastructure:** JPA persistence entities, repositories, adapters, and assemblers.
- **Interfaces:** REST controllers, resources, and transformations.

### 4. Implemented Features — AV2

| User Story | Feature | Status |
|---|---|---|
| US-06 | Register organization | Implemented and tested |
| US-09 | Retrieve organization information | Implemented and tested |
| US-10 | Update organization information | Implemented and tested |
| US-32 | Manage sites | Frontend implemented; backend integration pending |
| US-44 | Manage business units | Frontend implemented; backend integration pending |

### 5. REST API Endpoints

Base path: `/api/v1/organizations`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/organizations` | Register an organization |
| GET | `/api/v1/organizations` | Retrieve all organizations |
| GET | `/api/v1/organizations/{organizationId}` | Retrieve an organization |
| PATCH | `/api/v1/organizations/{organizationId}` | Update organization information |

### 6. Local Configuration

Create a MySQL database named `ecotrack_av2`.

The `org-local` Spring profile configures the local database connection through environment variables:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Database credentials must not be committed to the repository.

Run the backend from the project root using:

`.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=org-local"`

Swagger UI is available at:

`http://localhost:8080/swagger-ui/index.html`

### 7. Testing

The following operations were verified using Swagger UI:

- Organization registration: HTTP 201 Created.
- Organization retrieval: HTTP 200 OK.
- Organization update: HTTP 200 OK.
- Verification that updated information is retrieved successfully.

The `OrganizationPersistenceAssemblerTest` also verifies conversion between domain aggregates and persistence entities.

Run the test using:

`.\mvnw.cmd "-Dtest=OrganizationPersistenceAssemblerTest" test`

### 8. AV2 Scope and Pending Work

The purpose of AV2 is to demonstrate the transition from a Mock REST API to a real backend using Spring Boot, following the architecture introduced in the course.

The current AV2 scope focuses on organization registration, retrieval, and updates.

The following features remain pending for subsequent iterations:

- Backend persistence and REST endpoints for sites.
- Backend persistence and REST endpoints for business units.
- Migration of remaining Mock API operations.
- Additional automated testing and integration improvements.

### 9. Development

Bounded Context: Organization Management

Repository: `backend-web-services`

Development branch: `feature/organization-management-av2`

Course: Desarrollo de Aplicaciones Open Source — UPC