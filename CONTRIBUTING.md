# Contributing

## Development workflow

1. Create a focused branch from `main`.
2. Write or refine one behavioral test.
3. Run it and confirm the intended RED failure.
4. Implement the smallest GREEN change.
5. Refactor while the complete suite remains green.
6. Update architecture, XML/API contracts, and thesis evidence where applicable.
7. Open a pull request using the repository template.

## Commands

```text
./mvnw test
./mvnw verify
```

On Windows use `mvnw.cmd`.

## Commit style

Use short imperative subjects, for example:

```text
Add half-open interval overlap rule
Reject stale track availability updates
Prevent concurrent reservation overlap
```

Do not commit secrets, real operational railway data, generated build output, or
unsanitized production XML.

