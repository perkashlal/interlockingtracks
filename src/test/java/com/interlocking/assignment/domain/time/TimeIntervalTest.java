package com.interlocking.assignment.domain.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimeIntervalTest {

    @Test
    void detectsOverlappingOccupationIntervals() {
        var existing = interval("2026-08-05T10:00:00Z", "2026-08-05T10:30:00Z");
        var requested = interval("2026-08-05T10:20:00Z", "2026-08-05T10:45:00Z");

        assertThat(existing.overlaps(requested)).isTrue();
    }

    private static TimeInterval interval(String start, String end) {
        return new TimeInterval(Instant.parse(start), Instant.parse(end));
    }
}
