# Aura: High-Throughput BI Migration & Rationalization Engine

**Aura** is a resilient, strategy-driven migration framework built with **Spring Boot 3.4+** and **Java 21**. It orchestrates complex data transfers between BI vendors (Tableau, PowerBI, Looker) while maintaining high availability and data integrity.

## 🏗️ High-Level Design (HLD)

*The architecture demonstrates a decoupled, event-aware flow featuring Load Balancing, Rate Limiting, and specialized Extraction/Transformation services.*

## 🚀 SDE-2 Level Engineering Features

### 1\. Dynamic Strategy Pattern (LLD)

Aura eliminates brittle `if-else` blocks by using **Spring Map Injection**. The `MigrationServiceImpl` resolves dependencies (Auth, DB, OS, BI-Vendor) at runtime.

  * **Impact:** Adding a new BI vendor requires zero changes to core orchestration logic.

### 2\. Resilient Checkpointing & Watermarking

To handle multi-million record migrations without data loss:

  * **Atomic Upserts:** Uses PostgreSQL native `ON CONFLICT` logic to persist migration state.
  * **Idempotency:** The system can recover from crashes by resuming from the `lastProcessedId` stored in the `checkpoint` table.

### 3\. Backpressure & Concurrency Control

  * **Semaphore Guard:** Limits concurrent "bucket" processing to prevent `OutOfMemoryError`.
  * **Virtual Thread Ready:** Designed for non-blocking execution using `CompletableFuture`.

-----

## 🛠️ Tech Stack

  * **Language:** Java 21
  * **Framework:** Spring Boot 3.x (Web, Data JPA, Validation)
  * **Database:** PostgreSQL 16
  * **Optimization:** HikariCP Connection Pooling & Hibernate Batching (Size: 500)
  * **Testing:** JUnit 5, Mockito (TDD approach)

-----

## 🚦 Getting Started

### 1\. Spin up the Infrastructure

```yaml
# docker-compose.yml
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: auradb
      POSTGRES_USER: aura_user
      POSTGRES_PASSWORD: aura_password
    ports:
      - "5332:5432"
```

Run `docker-compose up -d`.

### 2\. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

### 3\. Trigger a Migration

```bash
curl -X POST http://localhost:8080/api/v1/migration/run \
-H "Content-Type: application/json" \
-d '{
  "vendor": "tableau",
  "auth": "google",
  "db": "postgresvalidator",
  "os": "win",
  "biValidator": "tableauvalidator",
  "uploader": "file"
}'
```

-----

## 🧪 Testing

The project follows **Test-Driven Development (TDD)**. Run the suite to verify strategy resolution:

```bash
mvn test
```

-----