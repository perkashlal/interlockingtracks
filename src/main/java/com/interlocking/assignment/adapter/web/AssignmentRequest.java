package com.interlocking.assignment.adapter.web;

import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;
import java.time.Instant;
import java.util.List;

public record AssignmentRequest(
        String requestId,
        Instant start,
        Instant end,
        String lastAssignedTrackId,
        List<TrackCandidate> tracks) {

    public record TrackCandidate(
            String id,
            TrackOperationalStatus status,
            List<Occupation> occupations) {
    }

    public record Occupation(Instant start, Instant end) {
    }
}
