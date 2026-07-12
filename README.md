# Automatic Train Track and Platform Assignment

Java 25/Spring Boot service for automatic XML ingestion and concurrency-safe train
track/platform assignment.

![Project architecture and safety flow](docs/assets/project-architecture.svg)

## Current state: local working MVP

The project now contains a working local prototype for automatic train
track/platform assignment. It accepts XML or JSON, checks safety gates, selects a
safe available track, records the assignment decision for audit, and includes
separated unit, integration, and E2E test phases.

## Requirements

- JDK 25
- Maven 3.9+
- Docker or compatible container runtime for PostgreSQL integration tests

## Run the tests

```text
mvn test
mvn verify -Pintegration
mvn verify -Pe2e
```

Expected result: all test phases pass.

If your terminal is still using JDK 17, run tests with JDK 25:

```text
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
mvn test
```

## Current local MVP behavior

The project now contains a working local assignment prototype:

- accepts JSON assignment requests;
- accepts XML assignment requests;
- parses train request time windows;
- parses tracks, operational status and current occupations;
- rejects closed or unsafe tracks;
- skips occupied tracks;
- rotates to the next safe track after the last assigned track;
- returns either an assigned track or manual-review reason;
- records assignment decisions in the `assignment_audit` database table.

This is an academic decision-support prototype, not certified railway signalling
software.

## XML input format

Example file:

```text
src/main/resources/examples/assignment-request.xml
```

Example XML:

```xml
<assignmentRequest id="REQ-XML-1"
                   start="2026-08-05T10:00:00Z"
                   end="2026-08-05T10:20:00Z"
                   lastAssignedTrackId="T1">
  <tracks>
    <track id="T1" status="OPEN">
      <occupation start="2026-08-05T09:00:00Z" end="2026-08-05T09:30:00Z"/>
    </track>
    <track id="T2" status="OPEN"/>
    <track id="T3" status="CLOSED"/>
  </tracks>
</assignmentRequest>
```

## Run the application locally

Start PostgreSQL first:

```text
docker compose up -d postgres
```

Then start the service:

```text
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
mvn spring-boot:run
```

## Submit XML for automatic assignment

From another CMD window:

```text
curl -X POST http://localhost:8080/api/assignments ^
  -H "Content-Type: application/xml" ^
  -H "Accept: application/json" ^
  --data-binary @src/main/resources/examples/assignment-request.xml
```

Expected response:

```json
{
  "outcome": "ASSIGNED",
  "assignedTrackId": "T2",
  "reasons": ["SAFE_TRACK_ASSIGNED"]
}
```

## Submit JSON for automatic assignment

```text
curl -X POST http://localhost:8080/api/assignments ^
  -H "Content-Type: application/json" ^
  -d "{\"requestId\":\"REQ-1\",\"start\":\"2026-08-05T10:00:00Z\",\"end\":\"2026-08-05T10:20:00Z\",\"lastAssignedTrackId\":\"T1\",\"tracks\":[{\"id\":\"T1\",\"status\":\"OPEN\",\"occupations\":[]},{\"id\":\"T2\",\"status\":\"OPEN\",\"occupations\":[]}]}"
```

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

## Implemented architecture

```text
XML / JSON request
      ↓
AssignmentController
      ↓
AssignmentService
      ↓
TrackAssignmentEngine
      ↓
Safety rules:
  - operational status
  - time overlap
  - track availability
  - rotation after safety
      ↓
AssignmentDecision
      ↓
assignment_audit table
```

## Completed implementation sequence

1. Added interval-overlap tests and implementation.
2. Added clearance buffer logic.
3. Added track availability and operational-status safety rules.
4. Added assignment engine with safe-track selection and rotation.
5. Added XML parser for automatic request ingestion.
6. Added REST API for XML and JSON assignment requests.
7. Added audit persistence table and JDBC repository.
8. Added separated unit, integration, and E2E test phases.

## Safety Features

- Prevent assigning occupied tracks or platforms.
- Reject conflicting train routes before scoring or rotation.
- Apply clearance buffers around every occupied time interval.
- Treat missing, stale, unknown, or conflicting status as unsafe.
- Send unsafe or uncertain requests to manual review instead of auto-assigning.
- Keep priority as a scoring input only; priority must never override safety.
- Record assignment and rejection reasons for audit and thesis evidence.
