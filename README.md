# 🚀 Lovable Clone - Full Stack AI-Powered Web App Builder

A full-stack platform that revolutionizes web application development by providing AI-powered code generation and real-time collaboration features. Build web applications with intelligent assistance!

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

## 📁 Project Structure

```text
lovable-clone/
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

## 🚀 Getting Started

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

- [GitHub Repository](https://github.com/SahilTanwani/MajorLovableClone)
- [Issues & Bugs](https://github.com/SahilTanwani/MajorLovableClone/issues)
- [Discussions](https://github.com/SahilTanwani/MajorLovableClone/discussions)

## 📧 Support

For questions, bug reports, or feature requests:
- Open an issue on GitHub
- Check existing issues for solutions
- Review [SECURITY_GUIDE.md](SECURITY_GUIDE.md) for common questions

---

⭐ **If you find this project helpful, please star the repository!**

Made with ❤️ by Sahil Tanwani

