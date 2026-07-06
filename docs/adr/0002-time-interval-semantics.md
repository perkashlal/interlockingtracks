# ADR 0002: Use Half-Open Time Intervals

- Status: Proposed
- Date: 2026-07-06

## Context

Resource occupation requires precise boundary semantics. Two bookings where one
ends exactly when the next starts should not overlap after all required clearance
buffers have already been included in their intervals.

## Decision

Represent occupation as `[start, end)`: start is included and end is excluded.

## Consequences

- Adjacent intervals do not overlap.
- Clearance and dwell buffers must be applied before constructing the reservation
  interval.
- Unit and database constraints must implement identical semantics.

