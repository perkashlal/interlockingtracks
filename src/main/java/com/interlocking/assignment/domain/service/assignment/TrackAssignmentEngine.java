package com.interlocking.assignment.domain.service.assignment;

import com.interlocking.assignment.domain.model.assignment.AssignmentDecision;
import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.track.Track;
import com.interlocking.assignment.domain.rule.track.TrackAvailabilityRule;
import com.interlocking.assignment.domain.rule.track.TrackOperationalStatusRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Chooses a safe available track for a train request.
 *
 * <p>Safety checks are gates. Rotation is used only after a track passes safety.
 */
public final class TrackAssignmentEngine {

    private final TrackOperationalStatusRule statusRule = new TrackOperationalStatusRule();
    private final TrackAvailabilityRule availabilityRule = new TrackAvailabilityRule();

    public AssignmentDecision assign(
            TrainRequest request,
            List<Track> tracks,
            Optional<String> lastAssignedTrackId) {
        Objects.requireNonNull(request, "train request is required");
        Objects.requireNonNull(tracks, "tracks are required");
        Objects.requireNonNull(lastAssignedTrackId, "last assigned track id is required");

        if (tracks.isEmpty()) {
            return AssignmentDecision.manualReview("NO_TRACKS_CONFIGURED");
        }

        for (var track : rotated(tracks, lastAssignedTrackId)) {
            if (isSafeForAutomaticAssignment(request, track)) {
                return AssignmentDecision.assigned(track.id());
            }
        }

        return AssignmentDecision.manualReview("NO_SAFE_TRACK_AVAILABLE");
    }

    private boolean isSafeForAutomaticAssignment(TrainRequest request, Track track) {
        return statusRule.isAssignable(track.operationalStatus())
                && availabilityRule.isAvailable(
                        request.requestedInterval(),
                        track.protectedOccupations());
    }

    private static List<Track> rotated(List<Track> tracks, Optional<String> lastAssignedTrackId) {
        var immutableTracks = List.copyOf(tracks);
        if (lastAssignedTrackId.isEmpty()) {
            return immutableTracks;
        }

        var lastIndex = -1;
        for (var index = 0; index < immutableTracks.size(); index++) {
            if (immutableTracks.get(index).id().equals(lastAssignedTrackId.get())) {
                lastIndex = index;
                break;
            }
        }

        if (lastIndex < 0) {
            return immutableTracks;
        }

        var rotated = new ArrayList<Track>(immutableTracks.size());
        for (var offset = 1; offset <= immutableTracks.size(); offset++) {
            rotated.add(immutableTracks.get((lastIndex + offset) % immutableTracks.size()));
        }
        return rotated;
    }
}
