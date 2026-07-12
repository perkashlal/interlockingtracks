package com.interlocking.assignment.domain.service.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.assignment.AssignmentOutcome;
import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.time.TimeInterval;
import com.interlocking.assignment.domain.model.track.Track;
import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TrackAssignmentEngineTest {

    private final TrackAssignmentEngine engine = new TrackAssignmentEngine();

    @Test
    void assignsFirstOpenTrackWhenItIsFree() {
        var request = request("REQ-1", "2026-08-05T10:00:00Z", "2026-08-05T10:20:00Z");
        var track = track("T1", TrackOperationalStatus.OPEN);

        var result = engine.assign(request, List.of(track), Optional.empty());

        assertThat(result.outcome()).isEqualTo(AssignmentOutcome.ASSIGNED);
        assertThat(result.assignedTrackId()).contains("T1");
    }

    @Test
    void rejectsClosedTracksEvenWhenTimeIsFree() {
        var request = request("REQ-1", "2026-08-05T10:00:00Z", "2026-08-05T10:20:00Z");
        var closedTrack = track("T1", TrackOperationalStatus.CLOSED);

        var result = engine.assign(request, List.of(closedTrack), Optional.empty());

        assertThat(result.outcome()).isEqualTo(AssignmentOutcome.MANUAL_REVIEW);
        assertThat(result.assignedTrackId()).isEmpty();
        assertThat(result.reasons()).contains("NO_SAFE_TRACK_AVAILABLE");
    }

    @Test
    void skipsOccupiedTrackAndAssignsNextAvailableTrack() {
        var request = request("REQ-1", "2026-08-05T10:10:00Z", "2026-08-05T10:25:00Z");
        var occupiedTrack = track(
                "T1",
                TrackOperationalStatus.OPEN,
                interval("2026-08-05T10:00:00Z", "2026-08-05T10:20:00Z"));
        var freeTrack = track("T2", TrackOperationalStatus.OPEN);

        var result = engine.assign(request, List.of(occupiedTrack, freeTrack), Optional.empty());

        assertThat(result.outcome()).isEqualTo(AssignmentOutcome.ASSIGNED);
        assertThat(result.assignedTrackId()).contains("T2");
    }

    @Test
    void rotatesToTrackAfterLastAssignedTrackWhenMultipleTracksAreSafe() {
        var request = request("REQ-1", "2026-08-05T10:00:00Z", "2026-08-05T10:20:00Z");
        var tracks = List.of(
                track("T1", TrackOperationalStatus.OPEN),
                track("T2", TrackOperationalStatus.OPEN),
                track("T3", TrackOperationalStatus.OPEN));

        var result = engine.assign(request, tracks, Optional.of("T1"));

        assertThat(result.outcome()).isEqualTo(AssignmentOutcome.ASSIGNED);
        assertThat(result.assignedTrackId()).contains("T2");
    }

    private static TrainRequest request(String id, String start, String end) {
        return new TrainRequest(id, interval(start, end));
    }

    private static Track track(String id, TrackOperationalStatus status, TimeInterval... occupations) {
        return new Track(id, status, List.of(occupations));
    }

    private static TimeInterval interval(String start, String end) {
        return new TimeInterval(Instant.parse(start), Instant.parse(end));
    }
}
