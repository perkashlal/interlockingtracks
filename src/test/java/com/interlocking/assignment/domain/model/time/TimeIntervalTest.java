package com.interlocking.assignment.domain.model.time;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TimeIntervalTest {

    @ParameterizedTest
    @CsvSource({
        "2026-08-05T10:00:00Z, 2026-08-05T10:00:00Z",
        "2026-08-05T10:30:00Z, 2026-08-05T10:00:00Z"
    })
    void rejectsIntervalsWhoseEndIsNotAfterTheirStart(String start, String end) {
        assertThatThrownBy(() -> new TimeInterval(Instant.parse(start), Instant.parse(end)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("start must be before end");
    }
}

