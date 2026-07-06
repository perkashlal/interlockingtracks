package com.interlocking.assignment.domain.rule.track;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import com.interlocking.assignment.domain.rule.time.TimeIntervalOverlapRule;
import java.util.List;
import java.util.Objects;

/** Determines whether a track is free for a requested occupation interval. */
public final class TrackAvailabilityRule {

    private final TimeIntervalOverlapRule overlapRule = new TimeIntervalOverlapRule();

    public boolean isAvailable(
            TimeInterval requestedInterval,
            List<TimeInterval> protectedOccupations) {
        Objects.requireNonNull(requestedInterval, "requested interval is required");
        Objects.requireNonNull(protectedOccupations, "protected occupations are required");

        return protectedOccupations.stream()
                .noneMatch(occupation -> overlapRule.overlaps(requestedInterval, occupation));
    }
}
