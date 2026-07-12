package com.interlocking.assignment.adapter.xml;

import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.track.Track;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record XmlAssignmentInput(
        TrainRequest request,
        List<Track> tracks,
        Optional<String> lastAssignedTrackId) {

    public XmlAssignmentInput {
        Objects.requireNonNull(request, "request is required");
        Objects.requireNonNull(tracks, "tracks are required");
        Objects.requireNonNull(lastAssignedTrackId, "last assigned track id is required");
        tracks = List.copyOf(tracks);
    }
}
