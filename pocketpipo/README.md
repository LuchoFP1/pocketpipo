## Pocketpipo – Local setup

This project consists of:
- **Backend**: Spring Boot (`pocketpipo`)
- **Frontend**: React + Vite (`pocketpipo-ui`)
- **Local infra**: PostgreSQL + Kafka via `docker-compose`

---

## Prerequisites

- **Java 21** (JDK)
- **Maven** (or the included `mvnw` wrapper)
- **Node.js** 20+ (recommended) and **npm**
- **Docker Desktop** (or a compatible Docker engine, with Docker Compose)

Project structure:
- Backend: `pocketpipo/`
- Frontend: `pocketpipo-ui/`

---

## 1. Start infrastructure services (PostgreSQL + Kafka)

From the backend folder:

```bash
cd pocketpipo
docker compose up -d
```

This will start:
- **Postgres** at `localhost:5432`
  - DB: `pocketpipo_db`
  - User: `pipo_user`
  - Password: `nambiap1p02dge12k`
- **Kafka** at `localhost:9092`

To stop the services:

```bash
docker compose down
```

---

## 2. Backend configuration (Spring Boot)

The backend is configured in `pocketpipo/src/main/resources/application.yml`:
- **HTTP port**: `8080`
- **Database**:
  - URL: `jdbc:postgresql://localhost:5432/pocketpipo_db`
  - Username: `pipo_user`
  - Password: `nambiap1p02dge12k`
- **Kafka**:
  - `spring.kafka.bootstrap-servers: localhost:9092`
- **JWT**:
  - `app.jwt.secret` and expiration already defined in `application.yml`

If you need to change credentials/URLs for another environment, update them here or use environment-specific files (e.g. `application-*.yml`) and environment variables.

---

## 3. Run the backend

From the `pocketpipo` folder:

```bash
cd pocketpipo
mvn -DskipTests spring-boot:run
```

or using the Maven wrapper:

```bash
cd pocketpipo
./mvnw -DskipTests spring-boot:run      # macOS/Linux
.\mvnw -DskipTests spring-boot:run      # Windows PowerShell
```

The backend will be available at:
- `http://localhost:8080`

To build the JAR:

```bash
cd pocketpipo
mvn -DskipTests package
```

---

## 4. Run the frontend (React + Vite)

From the `pocketpipo-ui` folder:

```bash
cd pocketpipo-ui
npm install
npm run dev
```

By default, Vite runs at `http://localhost:5173` (or the port shown in the console).

The frontend is expected to talk to the backend API (make sure the backend at `http://localhost:8080` is running).

To create a production build:

```bash
cd pocketpipo-ui
npm run build
```

And to preview the production build:

```bash
npm run preview
```

---

## 5. Recommended order to run everything

1. **Start Docker services** (PostgreSQL + Kafka):
   - `cd pocketpipo`
   - `docker compose up -d`
2. **Start backend**:
   - `cd pocketpipo`
   - `mvn -DskipTests spring-boot:run`
3. **Start frontend**:
   - `cd pocketpipo-ui`
   - `npm install` (only the first time)
   - `npm run dev`

You should then have:
- Backend running at `http://localhost:8080`
- Frontend running at `http://localhost:5173`

---

## 6. Additional notes

- If you change ports or credentials in `docker-compose.yml`, remember to update `application.yml` accordingly.
- Make sure **Docker Desktop** is running before executing `docker compose up`.
- During development, it’s useful to keep backend logs visible to spot any connection issues with Kafka or Postgres.


