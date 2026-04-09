# MajorLovableClone

A full-stack-ready backend platform inspired by Lovable workflows, built with Spring Boot.

This project provides APIs for authentication, AI-assisted chat generation, project/file management, team collaboration, billing, and usage tracking.

## Features

### Core Features
- JWT-based authentication and protected APIs
- Project CRUD and workspace-style organization
- Project member invitation and role updates
- AI chat streaming endpoint using Server-Sent Events (SSE)
- File tree and file content APIs for project files
- Subscription, plans, Stripe checkout, and billing portal endpoints
- Usage tracking endpoints for daily usage and limits

### Technical Features
- Spring Security with stateless session handling
- PostgreSQL persistence with Spring Data JPA
- MinIO integration for object storage
- Stripe webhook handling for payment lifecycle events
- OpenAPI + Swagger UI documentation
- MapStruct + Lombok for cleaner mapping and boilerplate reduction

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.0.0
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring AI (OpenAI-compatible provider support)

### Data and Infra
- PostgreSQL (local Docker service in `services.docker-compose.yml`)
- MinIO (local Docker service in `services.docker-compose.yml`)

### Libraries and Tooling
- JWT (`jjwt`)
- Stripe Java SDK
- MinIO Java SDK
- MapStruct
- Lombok
- springdoc-openapi (Swagger UI)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Project Structure

```text
lovable-clone/
|- src/
|  |- main/
|  |  |- java/lovable_clone/
|  |  |  |- config/         # App, AI, CORS, payment, storage configuration
|  |  |  |- controller/     # REST controllers
|  |  |  |- dto/            # Request/response DTOs
|  |  |  |- entity/         # JPA entities
|  |  |  |- enums/          # Domain enums
|  |  |  |- error/          # Exception handling and API errors
|  |  |  |- llm/            # LLM-related components
|  |  |  |- mapper/         # MapStruct mappers
|  |  |  |- repository/     # Spring Data repositories
|  |  |  |- security/       # JWT filter + Spring Security config
|  |  |  |- service/        # Business logic
|  |  |- resources/
|  |     |- application.yaml
|  |- test/java/lovable_clone/
|     |- LovableCloneApplicationTests.java
|- services.docker-compose.yml
|- pom.xml
|- README.md
```

## Getting Started

### Prerequisites
- Java 21+
- Docker Desktop (or Docker Engine + Compose)
- Git

### 1) Clone Repository

```bash
git clone https://github.com/SahilTanwani/MajorLovableClone.git
cd MajorLovableClone
```

If your local folder name is different (for example `lovable-clone`), use that folder.

### 2) Start Local Services

```powershell
docker compose -f services.docker-compose.yml up -d
```

This starts:
- PostgreSQL (`localhost:9010`)
- MinIO API (`localhost:9000`) and Console (`localhost:9001`)

### 3) Configure Environment Variables (Recommended)

The project currently has default values in `src/main/resources/application.yaml`. For safer local setup, override sensitive values with environment variables.

```powershell
$env:STRIPE_API_SECRET="sk_test_your_secret"
$env:STRIPE_WEBHOOK_SECRET="whsec_your_webhook_secret"
$env:SPRING_AI_OPENAI_API_KEY="your_openai_or_openrouter_key"
$env:JWT_SECRET_KEY="replace_with_a_strong_secret"
```

### 4) Run the Application

```powershell
.\mvnw.cmd spring-boot:run
```

App URL (default): `http://localhost:8080`

### 5) Open API Docs

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Configuration Reference

Current defaults are in `src/main/resources/application.yaml`.

### Important Properties
- `spring.datasource.*` -> PostgreSQL connection
- `spring.ai.openai.*` -> AI provider key/base URL/model
- `jwt.secret-key` -> JWT signing secret
- `stripe.api.secret` -> Stripe API key (supports env override)
- `stripe.webhook.secret` -> Stripe webhook secret (supports env override)
- `minio.*` -> MinIO endpoint and credentials

## API Overview

### Auth
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /api/auth/me`

### Projects
- `GET /api/projects`
- `POST /api/projects`
- `GET /api/projects/{id}`
- `PATCH /api/projects/{id}`
- `DELETE /api/projects/{id}`

### Project Members
- `GET /api/projects/{projectId}/members`
- `POST /api/projects/{projectId}/members`
- `PATCH /api/projects/{projectId}/members/{memberId}`
- `DELETE /api/projects/{projectId}/members/{memberId}`

### Files
- `GET /api/projects/{projectId}/files`
- `GET /api/projects/{projectId}/files/content?path=<file-path>`

### Chat
- `POST /api/chat/stream` (SSE)
- `GET /api/chat/projects/{projectId}`

### Billing and Usage
- `GET /api/plans`
- `GET /api/me/subscription`
- `POST /api/payments/checkout`
- `POST /api/payments/portal`
- `POST /webhooks/payment`
- `GET /api/usage/today`
- `GET /api/usage/limits`

## Testing

Run tests with Maven Wrapper:

```powershell
.\mvnw.cmd test
```

Current test baseline includes Spring context load test in `src/test/java/lovable_clone/LovableCloneApplicationTests.java`.

## Development Notes

- Security config allows public access to:
  - `/api/auth/**`
  - `/webhooks/**`
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
- Other endpoints require authentication.

## Contributing

1. Fork this repository
2. Create a feature branch
3. Commit focused, reviewable changes
4. Open a pull request with a clear summary and test notes

## Author

Sahil Tanwani

- GitHub: `https://github.com/SahilTanwani`
- Repository: `https://github.com/SahilTanwani/MajorLovableClone`

## License

No explicit license file is currently present.
If you want open-source reuse terms, add a `LICENSE` file (for example MIT).
