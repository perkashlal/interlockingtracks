package com.interlocking.assignment.domain.rule.time;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimeIntervalOverlapRuleTest {

    private final TimeIntervalOverlapRule rule = new TimeIntervalOverlapRule();

    @Test
    void detectsOverlappingOccupationIntervals() {
        var existing = interval("2026-08-05T10:00:00Z", "2026-08-05T10:30:00Z");
        var requested = interval("2026-08-05T10:20:00Z", "2026-08-05T10:45:00Z");

        assertThat(rule.overlaps(existing, requested)).isTrue();
    }

    private static TimeInterval interval(String start, String end) {
        return new TimeInterval(Instant.parse(start), Instant.parse(end));
    }
}

