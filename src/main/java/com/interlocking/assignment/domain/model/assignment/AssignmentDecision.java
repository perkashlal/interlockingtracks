package com.interlocking.assignment.domain.model.assignment;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Result produced by the automatic assignment engine. */
public record AssignmentDecision(
        AssignmentOutcome outcome,
        Optional<String> assignedTrackId,
        List<String> reasons) {

    public AssignmentDecision {
        Objects.requireNonNull(outcome, "outcome is required");
        Objects.requireNonNull(assignedTrackId, "assigned track id is required");
        Objects.requireNonNull(reasons, "reasons are required");
        reasons = List.copyOf(reasons);
    }

    public static AssignmentDecision assigned(String trackId) {
        Objects.requireNonNull(trackId, "track id is required");
        return new AssignmentDecision(
                AssignmentOutcome.ASSIGNED,
                Optional.of(trackId),
                List.of("SAFE_TRACK_ASSIGNED"));
    }

    public static AssignmentDecision manualReview(String reason) {
        Objects.requireNonNull(reason, "reason is required");
        return new AssignmentDecision(
                AssignmentOutcome.MANUAL_REVIEW,
                Optional.empty(),
                List.of(reason));
    }
}
