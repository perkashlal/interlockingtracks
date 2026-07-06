package com.interlocking.assignment.domain.rule.time;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TimeIntervalClearanceRuleTest {

    private final TimeIntervalClearanceRule rule = new TimeIntervalClearanceRule();

    @Test
    void expandsOccupationIntervalByClearanceBeforeAndAfter() {
        var occupation = new TimeInterval(
                Instant.parse("2026-08-05T10:00:00Z"),
                Instant.parse("2026-08-05T10:30:00Z"));

        var protectedInterval = rule.apply(
                occupation,
                Duration.ofMinutes(5),
                Duration.ofMinutes(10));

        assertThat(protectedInterval.start())
                .isEqualTo(Instant.parse("2026-08-05T09:55:00Z"));
        assertThat(protectedInterval.end())
                .isEqualTo(Instant.parse("2026-08-05T10:40:00Z"));
    }
}

