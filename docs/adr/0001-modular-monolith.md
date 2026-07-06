# ADR 0001: Start as a Modular Monolith

- Status: Accepted
- Date: 2026-07-06

## Context

The assignment and multi-resource reservation workflow requires strong
transactional consistency and extensive testing. The team does not yet have
measured scaling or independent deployment requirements.

## Decision

Build one Spring Boot application with explicit domain, application, and adapter
boundaries. Keep the pure assignment domain free from framework dependencies.

## Consequences

- Atomic PostgreSQL transactions remain straightforward.
- Local development and end-to-end testing require fewer moving parts.
- Modules may be extracted later only when measured needs justify distribution.

