package com.interlocking.assignment.domain.model.track;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.util.List;
import java.util.Objects;

/** Track/platform candidate considered by the assignment engine. */
public record Track(
        String id,
        TrackOperationalStatus operationalStatus,
        List<TimeInterval> protectedOccupations) {

    public Track {
        requireText(id, "track id");
        Objects.requireNonNull(operationalStatus, "operational status is required");
        Objects.requireNonNull(protectedOccupations, "protected occupations are required");
        protectedOccupations = List.copyOf(protectedOccupations);
    }

    private static void requireText(String value, String name) {
        Objects.requireNonNull(value, name + " is required");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
