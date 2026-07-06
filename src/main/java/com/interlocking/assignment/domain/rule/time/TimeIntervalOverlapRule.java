package com.interlocking.assignment.domain.rule.time;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.util.Objects;

/**
 * Determines whether two half-open occupation intervals intersect.
 */
public final class TimeIntervalOverlapRule {

    public boolean overlaps(TimeInterval first, TimeInterval second) {
        Objects.requireNonNull(first, "first interval is required");
        Objects.requireNonNull(second, "second interval is required");
        return first.start().isBefore(second.end())
                && second.start().isBefore(first.end());
    }
}

