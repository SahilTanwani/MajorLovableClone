# 🚀 PromptToApp - Full Stack AI-Powered Web App Builder

A full-stack platform that revolutionizes web application development by providing AI-powered code generation and real-time collaboration features. Build web applications with intelligent assistance!

> **PromptToApp** consists of two main repositories:
> - **Backend**: [PromptToAppBackend](https://github.com/SahilTanwani/PromptToAppBackend) (This Repository) - Spring Boot API
> - **Frontend**: [PromptToAppFrontend](https://github.com/SahilTanwani/PromptToAppFrontend) - React + Vite UI
>
> Make sure to clone and set up both repositories for the complete application!

[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.oracle.com/java)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)](https://www.postgresql.org)

## ✨ Features

### 🎯 Core Features
- 🤖 **AI Code Generation** - Generate complete application code from natural language prompts
- 📝 **Real-time Editor** - Monaco Editor with syntax highlighting and multiple language support
- 🔄 **Live Collaboration** - Real-time project updates and chat-based AI development
- 📁 **File Management** - Create, edit, and organize project files with tree view
- 👥 **Team Management** - Add team members and manage project access with role-based control
- 💬 **Chat Sessions** - Persistent chat history per project for continuous AI conversations
- 📊 **Usage Tracking** - Monitor token usage and API consumption with daily limits
- 💳 **Billing Integration** - Stripe-based subscription and payment management
- 🎨 **Project Preview** - Real-time preview of generated applications

### 🛠️ Technical Features
- Real-time SSE (Server-Sent Events) for streaming responses
- Redis caching for optimized performance
- JWT-based authentication and authorization
- MinIO integration for secure file storage
- AI-powered features using OpenAI-compatible providers (OpenRouter, etc.)
- pgVector integration for semantic search (future)
- Comprehensive logging and error handling
- OpenAPI + Swagger UI documentation
- Spring Security with stateless session handling

## 🏗️ Tech Stack

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 4.0.0
- **Runtime**: JVM
- **Database**: PostgreSQL 13+ with pgVector
- **Caching**: Redis
- **Authentication**: JWT (jjwt)
- **Build Tool**: Maven
- **Logging**: SLF4J with Logback

### Infrastructure & Services
- **Object Storage**: MinIO
- **API Payments**: Stripe
- **AI Providers**: OpenRouter (GPT-4, GPT-OSS), OpenAI-compatible models
- **API Documentation**: Swagger UI + OpenAPI 3.0

### Development Tools
- **Code Generation**: MapStruct
- **Boilerplate**: Lombok
- **Build**: Maven Wrapper (mvnw, mvnw.cmd)

## 🏛️ Architecture Overview

## 🏛️ Architecture Overview

The PromptToApp platform consists of multiple interconnected services:

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER LAYER                               │
│  ┌──────────────┐        ┌──────────────┐                       │
│  │ Browser      │        │ NGINX Ingress│                       │
│  └──────────────┘        └──────────────┘                       │
└──────────────────┬────────────────────────────────────────────┬─┘
                   │                                            │
        ┌──────────▼─────────────┐            ┌────────────────▼──┐
        │   FRONTEND SERVICE     │            │  PROXY SERVICE    │
        │  (React + Vite)        │            │  (Node.js)        │
        │  - Monaco Editor       │            │  - Subdomain      │
        │  - Project UI          │            │    Routing        │
        │  - Chat Interface      │            │  - Cache Layer    │
        └──────────┬─────────────┘            └────────────────┬──┘
                   │                                            │
                   └────────────────────┬─────────────────────┘
                                        │
        ┌───────────────────────────────▼───────────────────────────┐
        │         SPRING BOOT GATEWAY SERVICE                       │
        │  (Main Backend - Port 8080)                               │
        └─────┬─────────────┬──────────────┬───────────────────┬───┘
              │             │              │                   │
        ┌─────▼──┐  ┌──────▼────┐  ┌──────▼──┐      ┌────────▼────────┐
        │ Account │  │ Workspace │  │ Storage │      │ Intelligence    │
        │ Service │  │ Service   │  │ Service │      │ Service (LLM)   │
        └─────┬──┘  └──────┬────┘  └──────┬──┘      └────────┬────────┘
              │           │              │                   │
        ┌─────▼─────────────▼──────────────▼──────────────────▼────────┐
        │                    DATA LAYER                                │
        │  ┌────────────┐  ┌──────────────┐  ┌──────────────────────┐ │
        │  │ PostgreSQL │  │ Redis Cache  │  │ MinIO Object Storage │ │
        │  │ + pgVector │  │              │  │                      │ │
        │  └────────────┘  └──────────────┘  └──────────────────────┘ │
        └─────────────────────────────────────────────────────────────┘
```

#### Service Components

1. **Frontend Service**
   - React 19.1.0 application with Vite
   - Monaco Editor for code editing
   - Real-time project management UI
   - AI chat interface for prompts

2. **Proxy Service**
   - Node.js reverse proxy
   - Subdomain routing (*.app.domain.com)
   - Cache layer for improved performance
   - Preview service routing

3. **Spring Boot Backend**
   - Account & Authentication Service
   - Workspace/Project Management Service
   - File Storage & Management Service
   - Intelligence Service (LLM Integration)

4. **Data Layer**
   - **PostgreSQL**: Relational database with pgVector for semantic search
   - **Redis**: In-memory caching for performance
   - **MinIO**: S3-compatible object storage for files

---

### Database Schema

```
┌──────────────────────────────────────────────────────────────────┐
│                    CORE ENTITIES                                 │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  USER                                  PROJECT                  │
│  ├─ bigint id (PK)                     ├─ bigint id (PK)        │
│  ├─ string email (UK)                  ├─ string name           │
│  ├─ string password_hash                ├─ bigint owner_id (FK) │
│  ├─ string name                         ├─ bool is_public       │
│  ├─ string avatar_url                   ├─ timestamps           │
│  └─ timestamps                          └─ one active subscription
│          │                                     │
│          │ owns                               │ has members
│          │ triggers                           │ contains files
│          ▼                                    ▼
│  ┌────────────────────────────────────────────────────────┐   │
│  │ PROJECT_OWNERSHIP  │  PROJECT_MEMBER   │  PROJECT_FILE │   │
│  ├────────────────────┼──────────────────┼───────────────┤   │
│  │ bigint project_id  │ project_id (FK)  │ project_id    │   │
│  │ bigint user_id     │ user_id (FK)     │ string path   │   │
│  │ timestamps         │ role: EDITOR     │ string minio  │   │
│  └────────────────────┴──────────────────┴───────────────┘   │
│          │                                                     │
│          │ tracks                                              │
│          ▼                                                     │
│  USAGE_LOG                    SUBSCRIPTION    PLAN             │
│  ├─ user_id (FK)              ├─ id           ├─ id           │
│  ├─ project_id (FK)           ├─ user_id      ├─ name         │
│  ├─ tokens_used               ├─ plan_id      ├─ stripe_price │
│  ├─ action (enum)             ├─ status       ├─ max_projects │
│  ├─ metadata (JSON)           ├─ current_     ├─ max_tokens   │
│  └─ timestamps                │   period_start ├─ max_previews │
│                               └─ timestamps   └─ active: bool  │
│
│  CHAT_SESSION                              CHAT_MESSAGE
│  ├─ project_id (FK)                        ├─ session_id (FK)
│  ├─ user_id (FK)                           ├─ role: USER/ASSISTANT
│  ├─ namespace (string)                     ├─ content (text)
│  └─ timestamps                             ├─ tokens_used
│                                            └─ timestamps
│
│  PREVIEW                                   CHAT_EVENT
│  ├─ project_id (FK)                        ├─ message_id (FK)
│  ├─ pool_name (string)                     ├─ type: THOUGHT/
│  ├─ preview_url                            │  FILE_EDIT/MESSAGE
│  ├─ status                                 ├─ content
│  └─ timestamps                             ├─ file_path
│                                            ├─ sequence_order
│                                            └─ timestamps
│
└──────────────────────────────────────────────────────────────────┘

KEY RELATIONSHIPS:
• User → owns → Project (1:many)
• User → has → Subscription (1:1 or 1:many)
• Project → has → ProjectMember (1:many, RBAC)
• Project → has → ProjectFile (1:many)
• Project → has → ChatSession (1:many)
• ChatSession → has → ChatMessage (1:many)
• ChatMessage → generates → ChatEvent (1:many)
• User → tracks → UsageLog (1:many)
```

---

### LLM Integration Flow

```
┌──────────────────────────────────────────────────────────────┐
│  REACT FRONTEND                                              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  User Input (Natural Language Prompt)                │  │
│  │  Example: "Create a login form with validation"     │  │
│  └────────────────┬──────────────────────────────────────┘  │
└─────────────────┼──────────────────────────────────────────┘
                  │ POST /api/chat/stream
                  ▼
┌──────────────────────────────────────────────────────────────┐
│  SPRING BOOT BACKEND                                         │
│  ┌────────────────────────────────────────────────────────┐  │
│  │ 1. Parse User Prompt & Validate Request              │  │
│  │    - Check JWT token                                 │  │
│  │    - Verify project access                           │  │
│  │    - Check token usage limits                        │  │
│  └────────────────┬───────────────────────────────────────┘  │
│  ┌────────────────▼───────────────────────────────────────┐  │
│  │ 2. Build LLM Context                                  │  │
│  │    ├─ Get file tree from MinIO                        │  │
│  │    ├─ Read existing project files                     │  │
│  │    ├─ Include system prompt (guidelines)             │  │
│  │    └─ Add user message with context                 │  │
│  └────────────────┬───────────────────────────────────────┘  │
│  ┌────────────────▼───────────────────────────────────────┐  │
│  │ 3. Call LLM API                                       │  │
│  │    ├─ Provider: OpenRouter / OpenAI Compatible       │  │
│  │    ├─ Model: GPT-4, GPT-OSS-120b                    │  │
│  │    ├─ Config: Temperature 0.0, Max tokens 400       │  │
│  │    └─ Stream: Yes (for real-time response)          │  │
│  └────────────────┬───────────────────────────────────────┘  │
│  ┌────────────────▼───────────────────────────────────────┐  │
│  │ 4. Stream & Parse Response                            │  │
│  │    ├─ Buffer streaming chunks                         │  │
│  │    ├─ Parse response into events:                    │  │
│  │    │  ├─ <message>Text output</message>             │  │
│  │    │  ├─ <file path="src/App.tsx">code</file>       │  │
│  │    │  └─ <thought>reasoning</thought>               │  │
│  │    └─ Keep track of token usage                      │  │
│  └────────────────┬───────────────────────────────────────┘  │
│  ┌────────────────▼───────────────────────────────────────┐  │
│  │ 5. Save to Database & Storage                         │  │
│  │    ├─ Chat messages → PostgreSQL                      │  │
│  │    ├─ Generated files → MinIO storage                │  │
│  │    ├─ Chat events → PostgreSQL (thoughts/actions)   │  │
│  │    └─ Usage log → Record tokens consumed            │  │
│  └────────────────┬───────────────────────────────────────┘  │
└─────────────────┼──────────────────────────────────────────┘
                  │ SSE Stream: Server-Sent Events
                  ▼
┌──────────────────────────────────────────────────────────────┐
│  REACT FRONTEND                                              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │ Display Results:                                       │  │
│  │ ├─ Show streamed response in real-time               │  │
│  │ ├─ Update file tree with generated files             │  │
│  │ ├─ Display suggested code changes                    │  │
│  │ ├─ Show chat history                                 │  │
│  │ └─ Update usage statistics                           │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘

CIRCUIT BREAKER PATTERN:
• Detects LLM API failures
• Prevents cascading failures
• Automatically retries
• Falls back to cached responses when available
```

---

### Code Execution System Architecture

```
PROJECT DEPLOYMENT FLOW:
┌─────────────────────────────────────────────────────────────┐
│  FRONTEND (User clicks "Deploy")                           │
│  http://project-36.app.domain.com                          │
│  Reverse proxy → Redis cache check → app.domain.com        │
└──────────────┬────────────────────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────────────────────┐
│  SPRING BOOT BACKEND                                        │
│  /deploy/36                                                 │
│  ├─ Fetch project files from MinIO                        │
│  ├─ Prepare build context                                 │
│  └─ Send to Kubernetes Runner                             │
└──────────────┬────────────────────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────────────────────┐
│  KUBERNETES CLUSTER (Code Execution)                       │
│  Fabric8 Kubernetes client manages deployment              │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ POD 1 (Project-36)                                  │  │
│  │ ├─ Syncer                                           │  │
│  │ │  └─ Fetches files from MinIO                     │  │
│  │ ├─ npm install                                     │  │
│  │ ├─ npm run dev                                     │  │
│  │ │  └─ Starts dev server on internal IP             │  │
│  │ └─ Exposes on :5173 internally                     │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  Network Policy: "Don't allow pods to talk to each other" │
│  └─ Prevents interference between project deployments    │
│                                                             │
│  ┌──────────────────┐  ┌──────────────────┐               │
│  │ POD 2            │  │ POD 36           │               │
│  │ (Project-37)     │  │ (Project-36)     │               │
│  │ 192.244.1.14     │  │ 192.244.1.12     │               │
│  │ :5173            │  │ :5173            │               │
│  └──────────────────┘  └──────────────────┘               │
│                                                             │
│  Kubernetes DNS resolves:                                 │
│  project-36.app.domain.com → Pod 36 (:5173)             │
│  project-37.app.domain.com → Pod 37 (:5173)             │
└─────────────────────────────────────────────────────────────┘

BUILD PROCESS IN POD:
├─ Fetch code files from MinIO
├─ npm install (dependencies)
├─ npm run dev (Hot Module Reload enabled)
├─ Server listens on 0.0.0.0:5173
└─ Ready for traffic via subdomain routing
```

---

### Request Flow Diagram

```
1. USER MAKES PROMPT REQUEST
   ┌──────────────────────────────────┐
   │ Browser sends prompt to backend  │
   │ POST /api/chat/stream            │
   └────────────────┬─────────────────┘
                    │
2. CONTEXT PREPARATION
   ┌────────────────▼──────────────────────────────┐
   │ FileTreeContextAdvisor:                       │
   │ ├─ Fetches file tree from MinIO              │
   │ ├─ Reads file contents                        │
   │ ├─ Prepares context for LLM                   │
   │ └─ Checks circuit breaker status             │
   └────────────────┬──────────────────────────────┘
                    │
3. LLM STREAMING
   ┌────────────────▼──────────────────────────────┐
   │ ChatClient.stream() via Spring AI:           │
   │ ├─ System prompt (code generation guidelines)│
   │ ├─ User message + context                     │
   │ ├─ Tools available (file operations)         │
   │ └─ Streams response in real-time             │
   └────────────────┬──────────────────────────────┘
                    │
4. RESPONSE PARSING
   ┌────────────────▼──────────────────────────────┐
   │ LlmResponseParser:                            │
   │ ├─ Parse <message> tags                       │
   │ ├─ Extract <file path="">code</file>         │
   │ ├─ Parse thoughts/reasoning                   │
   │ └─ Create ChatEvents for each action         │
   └────────────────┬──────────────────────────────┘
                    │
5. DATA PERSISTENCE
   ┌────────────────▼──────────────────────────────┐
   │ Save Results:                                 │
   │ ├─ ChatMessage (user & assistant) in DB      │
   │ ├─ ProjectFiles (generated code) in MinIO    │
   │ ├─ ChatEvents (actions performed) in DB      │
   │ ├─ UsageLog (token tracking) in DB           │
   │ └─ Redis cache update                         │
   └────────────────┬──────────────────────────────┘
                    │
6. RESPONSE TO FRONTEND
   ┌────────────────▼──────────────────────────────┐
   │ Stream events back via SSE:                  │
   │ ├─ Real-time response chunks                 │
   │ ├─ File creation/update events               │
   │ └─ Usage statistics                          │
   └──────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```text
PromptToApp/
├── Backend Repository (This Repository)
│   └── https://github.com/SahilTanwani/PromptToAppBackend
│
├── Frontend Repository
│   └── https://github.com/SahilTanwani/PromptToAppFrontend
│
└── Backend Project Structure:
    ├── src/
    │   ├── main/
    │   │   ├── java/lovable_clone/
    │   │   │   ├── config/           # Spring configurations (AI, Security, Payment, Storage)
    │   │   │   ├── controller/       # REST API endpoints
    │   │   │   ├── dto/              # Data Transfer Objects
    │   │   │   ├── entity/           # JPA entities
    │   │   │   ├── enums/            # Enum definitions
    │   │   │   ├── error/            # Error handling & exceptions
    │   │   │   ├── llm/              # LLM integration & tools
    │   │   │   ├── mapper/           # MapStruct mappers
    │   │   │   ├── repository/       # Spring Data repositories
    │   │   │   ├── security/         # JWT filter & Security config
    │   │   │   └── service/          # Business logic services
    │   │   ├── resources/
    │   │   │   ├── application.yaml.example  # Configuration template
    │   │   │   └── static/
    │   │   └── templates/
    │   └── test/java/lovable_clone/
    │       └── LovableCloneApplicationTests.java
    ├── proxy/                        # Node.js proxy service
    │   ├── index.js
    │   └── package.json
    ├── k8s/                          # Kubernetes manifests
    │   ├── infra.yml
    │   ├── lovable-proxy.yml
    │   ├── policy.yml
    │   └── runner-pods.yml
    ├── services.docker-compose.yml   # Local services (PostgreSQL, MinIO)
    ├── pom.xml                       # Maven configuration
    ├── mvnw & mvnw.cmd              # Maven Wrapper
    ├── SECURITY_GUIDE.md            # Security best practices
    └── README.md                    # This file
```

---

## 🔗 Project Repositories

This is a full-stack application with separate repositories for backend and frontend:

| Component | Repository | Tech Stack |
|-----------|-----------|-----------|
| **Backend** | [PromptToAppBackend](https://github.com/SahilTanwani/PromptToAppBackend) | Java 21, Spring Boot 4.0, PostgreSQL |
| **Frontend** | [PromptToAppFrontend](https://github.com/SahilTanwani/PromptToAppFrontend) | React 19, Vite, Tailwind CSS |

**For Frontend Setup Instructions**: See [PromptToAppFrontend Repository](https://github.com/SahilTanwani/PromptToAppFrontend)

## 🚀 Getting Started

> 📌 **Full Stack Setup**: This is the **backend** repository. Don't forget to also set up the **frontend**!
> - Frontend Setup: [PromptToAppFrontend](https://github.com/SahilTanwani/PromptToAppFrontend)

### Prerequisites
- **Java 21+** with JDK
- **Maven 3.8+** (or use Maven Wrapper)
- **PostgreSQL 13+** with pgVector extension
- **Redis 6+** (optional, for caching)
- **Docker & Docker Compose** (for local services)
- **Git** for version control

### Installation & Setup

#### 1. Clone Repository

```bash
git clone https://github.com/SahilTanwani/MajorLovableClone.git
cd lovable-clone
```

#### 2. Start Local Services

```powershell
docker compose -f services.docker-compose.yml up -d
```

This starts:
- **PostgreSQL** (`localhost:9010`)
- **MinIO API** (`localhost:9000`) and **Console** (`localhost:9001`)

#### 3. Set Up Configuration

**Copy the example configuration:**
```bash
copy src\main\resources\application.yaml.example src\main\resources\application.yaml
```

**Edit `src/main/resources/application.yaml` with your local values** or use environment variables:

```powershell
# Set API Keys and Secrets
$env:SPRING_AI_OPENAI_API_KEY="your_openai_or_openrouter_key"
$env:JWT_SECRET_KEY="your_secure_jwt_secret_key"
$env:STRIPE_API_SECRET="sk_test_your_stripe_key"
$env:STRIPE_WEBHOOK_SECRET="whsec_your_webhook_secret"

# Database Configuration
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:9010/pgvector-test"
$env:SPRING_DATASOURCE_USERNAME="user"
$env:SPRING_DATASOURCE_PASSWORD="password"
```

⚠️ **Important**: See [SECURITY_GUIDE.md](SECURITY_GUIDE.md) for proper secret management!

#### 4. Run the Application

Using Maven Wrapper:
```powershell
.\mvnw.cmd spring-boot:run
```

Or with Maven installed:
```bash
mvn spring-boot:run
```

#### 5. Access the Application

- **Backend API**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **MinIO Console**: `http://localhost:9001`

## 🔌 API Endpoints

### Authentication
```
POST   /api/auth/signup        - Register new user
POST   /api/auth/login         - Login user
GET    /api/auth/me            - Get current user info
```

### Projects
```
GET    /api/projects           - List all projects
POST   /api/projects           - Create new project
GET    /api/projects/{id}      - Get project details
PATCH  /api/projects/{id}      - Update project
DELETE /api/projects/{id}      - Delete project
```

### Project Members
```
GET    /api/projects/{projectId}/members           - List members
POST   /api/projects/{projectId}/members           - Add member
PATCH  /api/projects/{projectId}/members/{id}      - Update member
DELETE /api/projects/{projectId}/members/{id}      - Remove member
```

### Files
```
GET    /api/projects/{projectId}/files             - List files
GET    /api/projects/{projectId}/files/content     - Get file content
```

### Chat & AI
```
POST   /api/chat/stream        - Stream AI responses (SSE)
GET    /api/chat/projects/{projectId}  - Get chat history
```

### Billing & Usage
```
GET    /api/plans              - Get available plans
GET    /api/me/subscription    - Get user subscription
POST   /api/payments/checkout  - Create checkout session
POST   /api/payments/portal    - Create billing portal
POST   /webhooks/payment       - Stripe webhook endpoint
GET    /api/usage/today        - Get today's usage
GET    /api/usage/limits       - Get usage limits
```

## 🔐 Security

### Authentication & Authorization
- **JWT-based authentication** with secure token signing
- **Role-based access control (RBAC)** for projects and features
- **Method-level security** with `@PreAuthorize` annotations
- **Stateless session handling** with Spring Security

### Sensitive Data Protection
⚠️ **All sensitive configuration is gitignored**
- `application.yaml` is excluded from git tracking
- Use `application.yaml.example` as template
- Store secrets in environment variables or secure vaults
- See [SECURITY_GUIDE.md](SECURITY_GUIDE.md) for detailed practices

### Protected Endpoints
- Endpoints require valid JWT token in `Authorization: Bearer <token>` header
- Public endpoints: `/api/auth/**`, `/webhooks/**`, `/swagger-ui/**`, `/v3/api-docs/**`

## 🧪 Testing

Run tests with Maven:

```powershell
.\mvnw.cmd test
```

Or with Maven installed:
```bash
mvn test
```

Current test suite includes Spring context load test and integration tests.

## 🐳 Docker & Kubernetes

### Docker Compose (Local Development)
```bash
# Start local services
docker compose -f services.docker-compose.yml up -d

# Stop services
docker compose -f services.docker-compose.yml down
```

### Kubernetes Deployment (Production)
```bash
# Apply infrastructure
kubectl apply -f k8s/infra.yml

# Apply proxy service
kubectl apply -f k8s/lovable-proxy.yml

# Apply network policies
kubectl apply -f k8s/policy.yml

# Deploy runner pods
kubectl apply -f k8s/runner-pods.yml

# Check deployment status
kubectl get pods
kubectl get services
kubectl logs -f deployment/lovable-clone
```

## 📊 Configuration Reference

Configuration can be set in `src/main/resources/application.yaml` or via environment variables:

```yaml
spring:
  application:
    name: lovable-clone
  
  datasource:
    url: jdbc:postgresql://localhost:9010/pgvector-test
    username: ${SPRING_DATASOURCE_USERNAME:user}
    password: ${SPRING_DATASOURCE_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
  
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
  
  ai:
    openai:
      api-key: ${SPRING_AI_OPENAI_API_KEY:your_api_key}
      base-url: https://openrouter.ai/api
      chat:
        options:
          model: openai/gpt-oss-120b:free
          temperature: 0.0
          max-tokens: 400

jwt:
  secret-key: ${JWT_SECRET_KEY:your_secret_key}

stripe:
  api:
    secret: ${STRIPE_API_SECRET:sk_test_placeholder}
  webhook:
    secret: ${STRIPE_WEBHOOK_SECRET:whsec_placeholder}

minio:
  url: ${MINIO_URL:http://localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin123}
  project-bucket: projects

logging:
  level:
    org.springframework.ai: DEBUG
    lovable_clone: INFO
```

## 📚 Documentation

- **[SECURITY_GUIDE.md](SECURITY_GUIDE.md)** - Security best practices and secret management
- **Swagger UI** - Interactive API documentation at `/swagger-ui/index.html`
- **Code Comments** - Javadoc for major classes and methods

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/your-feature-name`
3. **Commit** focused changes: `git commit -m "feat: add your feature"`
4. **Push** to your branch: `git push origin feature/your-feature-name`
5. **Open** a Pull Request with description and test notes

### Code Guidelines
- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed
- Use MapStruct for entity mapping

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Sahil Tanwani**
- **GitHub**: [@SahilTanwani](https://github.com/SahilTanwani)
- **Repository**: [MajorLovableClone](https://github.com/SahilTanwani/MajorLovableClone)
- **Email**: Contact via GitHub profile

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Powerful Java framework
- [Spring AI](https://spring.io/projects/spring-ai) - LLM integration
- [PostgreSQL](https://www.postgresql.org/) - Reliable database
- [MinIO](https://min.io/) - S3-compatible object storage
- [Stripe](https://stripe.com/) - Payment processing
- [OpenRouter](https://openrouter.ai/) - AI model aggregation
- [Swagger/OpenAPI](https://swagger.io/) - API documentation
- All contributors and supporters

## 🔗 Quick Links

- [Backend Repository](https://github.com/SahilTanwani/PromptToAppBackend)
- [Frontend Repository](https://github.com/SahilTanwani/PromptToAppFrontend)
- [Issues & Bugs](https://github.com/SahilTanwani/PromptToAppBackend/issues)
- [Discussions](https://github.com/SahilTanwani/PromptToAppBackend/discussions)

## 📧 Support

For questions, bug reports, or feature requests:
- Open an issue on GitHub
- Check existing issues for solutions
- Review [SECURITY_GUIDE.md](SECURITY_GUIDE.md) for common questions

---

⭐ **If you find this project helpful, please star the repository!**

Made with ❤️ by Sahil Tanwani

