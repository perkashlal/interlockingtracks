package com.interlocking.assignment.domain.rule.track;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class TrackAvailabilityRuleTest {

    private final TrackAvailabilityRule rule = new TrackAvailabilityRule();

    @Test
    void marksTrackUnavailableWhenRequestOverlapsProtectedOccupation() {
        var requestedInterval = interval(
                "2026-08-05T10:20:00Z",
                "2026-08-05T10:40:00Z");
        var protectedOccupation = interval(
                "2026-08-05T10:00:00Z",
                "2026-08-05T10:30:00Z");

        var available = rule.isAvailable(
                requestedInterval,
                List.of(protectedOccupation));

        assertThat(available).isFalse();
    }

    @Test
    void marksTrackUnavailableWhenAnyProtectedOccupationOverlaps() {
        var requestedInterval = interval(
                "2026-08-05T10:20:00Z",
                "2026-08-05T10:40:00Z");
        var earlierOccupation = interval(
                "2026-08-05T09:00:00Z",
                "2026-08-05T09:30:00Z");
        var conflictingOccupation = interval(
                "2026-08-05T10:30:00Z",
                "2026-08-05T10:50:00Z");
        var laterOccupation = interval(
                "2026-08-05T11:00:00Z",
                "2026-08-05T11:30:00Z");

        var available = rule.isAvailable(
                requestedInterval,
                List.of(earlierOccupation, conflictingOccupation, laterOccupation));

        assertThat(available).isFalse();
    }

    private static TimeInterval interval(String start, String end) {
        return new TimeInterval(Instant.parse(start), Instant.parse(end));
    }
}
