package com.interlocking.assignment.domain.model.time;

import java.time.Instant;
import java.util.Objects;

/**
 * A half-open occupation interval: start is included and end is excluded.
 */
public record TimeInterval(Instant start, Instant end) {

    public TimeInterval {
        Objects.requireNonNull(start, "start is required");
        Objects.requireNonNull(end, "end is required");
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("start must be before end");
        }
    }
}

