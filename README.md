# 🐛 BugTracker AI — Backend

> **Production-grade REST API for AI-powered bug tracking.**  
> Built with Java · Spring Boot · MySQL · JWT · Spring Security 7 · OpenAI API

🔗 **Frontend Repo:** [bugtracker-ai-frontend](https://github.com/yourusername/bugtracker-frontend)  
🌐 **Live App:** [your-live-url.com](https://your-live-url.com)

---

## ✨ Features

### 🔐 Security
- JWT-based stateless authentication
- Spring Security 7 with role-based authorization (Admin, Developer, Tester)
- CORS configured inside Spring Security filter chain
- Password encryption with BCrypt

### 🤖 AI Integration
- **Auto-suggest bug severity** from plain language descriptions via OpenAI API
- **Semantic duplicate detection** — compares new bug against existing ones
- **Reproduction step generation** — developer-ready steps from bug description

### ⚙️ Backend Architecture
- **Controller → Service → Repository** layered architecture
- **DTO pattern** for clean request/response separation
- **@Transactional** on service layer to prevent lazy loading issues
- **Pagination & filtering** on all list endpoints (50 records/load)
- **Activity logging** for full audit trail

---

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Java 17 | Core language |
| Spring Boot 3 | Application framework |
| Spring Security 7 | Auth + RBAC |
| JWT | Stateless authentication |
| MySQL 8 | Relational database |
| Spring Data JPA | ORM / data access |
| OpenAI API | AI triage features |
| Maven | Build tool |
| Render | Deployment |

---

## 📁 Project Structure

```
backend/
└── src/main/java/com/bugtracker/
    ├── controller/
    │   ├── AuthController.java
    │   ├── BugController.java
    │   ├── ProjectController.java
    │   ├── UserController.java
    │   └── AIController.java
    ├── service/
    │   ├── BugService.java
    │   ├── ProjectService.java
    │   ├── UserService.java
    │   └── AITriageService.java
    ├── repository/
    │   ├── BugRepository.java
    │   ├── ProjectRepository.java
    │   └── UserRepository.java
    ├── dto/
    │   ├── BugRequestDTO.java
    │   ├── BugResponseDTO.java
    │   └── AuthRequestDTO.java
    ├── model/
    │   ├── Bug.java
    │   ├── Project.java
    │   └── User.java
    └── security/
        ├── JwtFilter.java
        ├── JwtUtil.java
        └── SecurityConfig.java
```

---

## 🔑 API Endpoints

### Auth
```
POST   /api/auth/login              → Login, returns JWT token
```

### Bugs
```
GET    /api/bugs?page=0&size=10     → Paginated bug list (with filters)
GET    /api/bugs/{id}               → Get bug by ID
POST   /api/bugs                    → Report new bug
PUT    /api/bugs/{id}/status        → Update bug status
DELETE /api/bugs/{id}               → Delete bug (Admin only)
```

### Projects
```
GET    /api/projects                → List all projects
POST   /api/projects                → Create project (Admin only)
GET    /api/projects/{id}/bugs      → Get bugs for a project
```

### Users
```
GET    /api/users                   → List all users (Admin only)
POST   /api/users                   → Create user (Admin only)
PUT    /api/users/{id}/status       → Activate/deactivate user
```

### AI Triage
```
POST   /api/ai/suggest-priority     → AI severity suggestion
POST   /api/ai/check-duplicate      → AI duplicate detection
POST   /api/ai/generate-steps       → AI reproduction steps
```

---

## ⚡ Getting Started

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+
- OpenAI API key

### Installation

```bash
git clone https://github.com/yourusername/bugtracker-backend
cd bugtracker-backend
```

### Configuration

```bash
cp src/main/resources/application.example.properties src/main/resources/application.properties
```

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/bugtracker
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# JPA
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# OpenAI
openai.api.key=your_openai_api_key
```

### Database Setup

```sql
CREATE DATABASE bugtracker;
-- Tables auto-created by Spring JPA on first run
```

### Run

```bash
mvn spring-boot:run
```

API runs at `http://localhost:8080`

---

## 🔐 Role Permissions Matrix

| Endpoint | Admin | Developer | Tester |
|----------|-------|-----------|--------|
| View all bugs | ✅ | ✅ | ✅ |
| Report bug | ✅ | ✅ | ✅ |
| Update bug status | ✅ | ✅ | ❌ |
| Delete bug | ✅ | ❌ | ❌ |
| Manage users | ✅ | ❌ | ❌ |
| Create project | ✅ | ❌ | ❌ |
| AI triage | ✅ | ✅ | ✅ |

---

## 🔗 Related

- 🎨 **Frontend Repo:** [bugtracker-ai-frontend](https://github.com/vikash1311/bugtracker-frontend)
- 🌐 **Live App:** [your-live-url.com](https://your-live-url.com)
- 👤 **Portfolio:** [yourportfolio.com](https://yourportfolio.com)

---

## 👨‍💻 Author

**Vikash Gautam** — Full Stack Developer  
📧 gautam7.ven@gmail.com  
🔗 [LinkedIn](https://linkedin.com/in/yourprofile) · [Portfolio](https://yourportfolio.com) · [GitHub](https://github.com/yourusername)

---

## 📄 License

MIT License
