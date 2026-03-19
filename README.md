PocketPipo – Personal Expense Manager
=====================================

PocketPipo is a full-stack personal expense management application designed to handle monthly budgeting with a focus on backend robustness and real-world engineering concerns such as authentication, idempotency, and event-driven processing.

This project was built to simulate production-like scenarios and demonstrate backend best practices using Java + Spring Boot.


Architecture Overview
---------------------

The backend follows a layered architecture:

- **Controller Layer** → Handles HTTP requests and responses
- **Service Layer** → Contains business logic
- **Repository Layer** → Data access via JPA/Hibernate

**Key architectural components:**

- **JWT Authentication**
  - Token-based stateless authentication
  - Custom authentication flow via `/auth/login`

- **API Gateway / Filter**
  - Intercepts incoming requests
  - Validates JWT tokens
  - Ensures protected routes are secured

- **Idempotency Handling**
  - Prevents duplicate expense creation
  - Ensures safe retries in case of network failures

- **Event-Driven Processing (Kafka)**
  - Produces events on specific actions (e.g., expense creation)
  - Consumer processes events asynchronously

- **Scheduler**
  - Periodic background tasks (e.g., future notifications / summaries)


Tech Stack
----------

**Backend**

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Apache Kafka

**Frontend**

- React (Vite)

**Infrastructure**

- Docker (Postgres + Kafka via `docker-compose`)


Key Features
------------

- User authentication with JWT
- Expense CRUD operations
- Monthly expense filtering (year + month)
- Idempotent expense creation (via `Idempotency-Key`)
- Event-driven architecture using Kafka
- Background scheduled jobs


Engineering Considerations
--------------------------

This project includes several real-world backend concerns:

- **Idempotency**  
  Handles duplicate requests safely to avoid duplicated data (e.g., double expense creation due to retries).

- **Concurrency Awareness**  
  Designed to avoid inconsistent states in concurrent environments (e.g., multiple requests hitting the same endpoint).

- **API Design**
  - RESTful endpoints
  - Proper HTTP status codes
  - Clear separation between layers

- **Error Handling**  
  Consistent error responses to improve frontend integration and debugging.


API Overview
------------

**Authentication**

- `POST /auth/login`

**Expenses**

- `GET /expense/expenses?year=YYYY&month=MM`
- `POST /expense/expenses`
- `GET /expense/expenses/{id}`
- `PUT /expense/expenses/{id}`


Local Setup
-----------

This project consists of:

- Backend: Spring Boot (`pocketpipo`)
- Frontend: React + Vite (`pocketpipo-ui`)
- Local infra: PostgreSQL + Kafka via `docker-compose`

**Prerequisites**

- Java 21 (JDK)
- Maven (or the included `mvnw` wrapper)
- Node.js 20+ (recommended) and npm
- Docker Desktop (or compatible engine with Docker Compose)

**Project structure:**

- Backend: `pocketpipo/`
- Frontend: `pocketpipo-ui/`


1. Start infrastructure services (PostgreSQL + Kafka)
----------------------------------------------------

```bash
cd pocketpipo
docker compose up -d
```

**Services:**

- Postgres → `localhost:5432`
  - DB: `pocketpipo_db`
  - User: `pipo_user`
  - Password: `nambiap1p02dge12k`

- Kafka → `localhost:9092`

To stop services:

```bash
docker compose down
```


2. Backend configuration
------------------------

Located at:

- `pocketpipo/src/main/resources/application.yml`

Main configs:

- Port: `8080`
- DB: PostgreSQL connection
- Kafka: `localhost:9092`
- JWT: secret + expiration


3. Run the backend
------------------

```bash
cd pocketpipo
mvn -DskipTests spring-boot:run
```

Or on Windows (PowerShell):

```bash
.\mvnw -DskipTests spring-boot:run
```

Backend URL:

- `http://localhost:8080`


4. Run the frontend
-------------------

```bash
cd pocketpipo-ui
npm install
npm run dev
```

Frontend URL:

- `http://localhost:5173`


5. Recommended startup order
----------------------------

1. Docker (Postgres + Kafka)
2. Backend
3. Frontend


Notes
-----

- Update `application.yml` if ports/credentials change
- Ensure Docker is running before starting services
- Check backend logs for Kafka/DB connection issues


Purpose of the Project
----------------------

This project is part of a backend engineering learning path focused on:

- Building production-like systems
- Practicing clean architecture
- Handling real-world backend challenges (auth, concurrency, async processing)
- Preparing for backend technical interviews (Java / Spring Boot)