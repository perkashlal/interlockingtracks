# Automatic Train Track and Platform Assignment

Java 25/Spring Boot service for automatic XML ingestion and concurrency-safe train
track/platform assignment.

![Project architecture and safety flow](docs/assets/project-architecture.svg)

## Current state: foundation baseline

The initial repository commit contains a green project foundation. The first TDD
increment will introduce a failing test for half-open occupation interval overlap,
then implement the smallest correct behavior.

## Requirements

- JDK 25
- Maven 3.9+
- Docker or compatible container runtime for PostgreSQL integration tests

## Run the tests

```text
mvn test
```

Expected result for the foundation commit: all bootstrap tests pass.

## Local PostgreSQL

```text
docker compose up -d postgres
```

The default connection is `jdbc:postgresql://localhost:5432/interlocking` with
username/password `interlocking`. Override it with `DATABASE_URL`,
`DATABASE_USER`, and `DATABASE_PASSWORD`.

## Initial package direction

```text
com.interlocking.assignment
  domain       pure business rules
  application  use-case orchestration
  adapter      XML, HTTP, persistence and file adapters
```

The assignment domain must not depend on Spring, XML, PostgreSQL, HTTP, or JavaFX.

## Test source sets

Tests are separated by scope:

```text
src/test/java  unit tests (*Test.java)
src/it/java    integration tests (*IT.java)
src/e2e/java   end-to-end tests (*E2E.java)
```

Run them independently:

```text
mvn test
mvn verify -Pintegration
mvn verify -Pe2e
```

Use lowercase directory names so local Windows builds and Linux CI use exactly
the same paths.

## First implementation sequence

1. Add the interval-overlap test and confirm the RED failure.
2. Implement the smallest correct half-open overlap rule and confirm GREEN.
3. Add boundary and symmetry tests.
4. Refactor while all tests remain green.
5. Continue with availability, compatibility, candidate selection, rotation, and
   atomic reservation tests.
## Safety Features

- Prevent assigning occupied tracks or platforms.
- Reject conflicting train routes before scoring or rotation.
- Apply clearance buffers around every occupied time interval.
- Treat missing, stale, unknown, or conflicting status as unsafe.
- Send unsafe or uncertain requests to manual review instead of auto-assigning.
- Keep priority as a scoring input only; priority must never override safety.
- Record assignment and rejection reasons for audit and thesis evidence.
