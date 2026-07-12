package com.interlocking.assignment.application.assignment;

import com.interlocking.assignment.domain.model.assignment.AssignmentDecision;
import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import java.time.Instant;
import java.util.Objects;

public record AssignmentAuditRecord(
        String requestId,
        String outcome,
        String assignedTrackId,
        String reasons,
        Instant recordedAt) {

    public AssignmentAuditRecord {
        Objects.requireNonNull(requestId, "request id is required");
        Objects.requireNonNull(outcome, "outcome is required");
        Objects.requireNonNull(reasons, "reasons are required");
        Objects.requireNonNull(recordedAt, "recorded at is required");
    }

    public static AssignmentAuditRecord from(TrainRequest request, AssignmentDecision decision) {
        return new AssignmentAuditRecord(
                request.id(),
                decision.outcome().name(),
                decision.assignedTrackId().orElse(null),
                String.join(",", decision.reasons()),
                Instant.now());
    }
}
