package com.interlocking.assignment.application.assignment;

import com.interlocking.assignment.domain.model.assignment.AssignmentDecision;
import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.track.Track;
import com.interlocking.assignment.domain.service.assignment.TrackAssignmentEngine;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AssignmentService {

    private final TrackAssignmentEngine assignmentEngine;
    private final AssignmentAuditRepository auditRepository;

    public AssignmentService(
            TrackAssignmentEngine assignmentEngine,
            AssignmentAuditRepository auditRepository) {
        this.assignmentEngine = Objects.requireNonNull(assignmentEngine, "assignment engine is required");
        this.auditRepository = Objects.requireNonNull(auditRepository, "audit repository is required");
    }

    public AssignmentDecision assign(
            TrainRequest request,
            List<Track> tracks,
            Optional<String> lastAssignedTrackId) {
        var decision = assignmentEngine.assign(request, tracks, lastAssignedTrackId);
        auditRepository.save(AssignmentAuditRecord.from(request, decision));
        return decision;
    }
}
