package com.interlocking.assignment.domain.model.assignment;

import com.interlocking.assignment.domain.model.time.TimeInterval;
import java.util.Objects;

/** Request to reserve a track or platform for a train during a time interval. */
public record TrainRequest(String id, TimeInterval requestedInterval) {

    public TrainRequest {
        requireText(id, "request id");
        Objects.requireNonNull(requestedInterval, "requested interval is required");
    }

    private static void requireText(String value, String name) {
        Objects.requireNonNull(value, name + " is required");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
