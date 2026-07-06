package com.interlocking.assignment.domain.rule.time;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.time.Duration;
import java.util.Objects;

/**
 * Expands an occupation interval with safety clearance before and after it.
 */
public final class TimeIntervalClearanceRule {

    public TimeInterval apply(
            TimeInterval occupation,
            Duration clearanceBefore,
            Duration clearanceAfter) {
        Objects.requireNonNull(occupation, "occupation interval is required");
        requireNonNegative(clearanceBefore, "clearance before");
        requireNonNegative(clearanceAfter, "clearance after");

        return new TimeInterval(
                occupation.start().minus(clearanceBefore),
                occupation.end().plus(clearanceAfter));
    }

    private static void requireNonNegative(Duration clearance, String name) {
        Objects.requireNonNull(clearance, name + " is required");
        if (clearance.isNegative()) {
            throw new IllegalArgumentException(name + " must not be negative");
        }
    }
}
