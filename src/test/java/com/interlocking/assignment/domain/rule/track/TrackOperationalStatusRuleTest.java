package com.interlocking.assignment.domain.rule.track;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;
import org.junit.jupiter.api.Test;

class TrackOperationalStatusRuleTest {

    private final TrackOperationalStatusRule rule = new TrackOperationalStatusRule();

    @Test
    void rejectsClosedTrackForAutomaticAssignment() {
        var assignable = rule.isAssignable(TrackOperationalStatus.CLOSED);

        assertThat(assignable).isFalse();
    }
}
