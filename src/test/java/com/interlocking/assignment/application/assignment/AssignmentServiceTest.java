package com.interlocking.assignment.application.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.assignment.AssignmentOutcome;
import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.time.TimeInterval;
import com.interlocking.assignment.domain.model.track.Track;
import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;
import com.interlocking.assignment.domain.service.assignment.TrackAssignmentEngine;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AssignmentServiceTest {

    private final RecordingAuditRepository auditRepository = new RecordingAuditRepository();
    private final AssignmentService service = new AssignmentService(
            new TrackAssignmentEngine(),
            auditRepository);

    @Test
    void recordsAuditAfterAutomaticAssignment() {
        var request = new TrainRequest(
                "REQ-AUDIT",
                interval("2026-08-05T10:00:00Z", "2026-08-05T10:20:00Z"));
        var track = new Track("T1", TrackOperationalStatus.OPEN, List.of());

        var decision = service.assign(request, List.of(track), Optional.empty());

        assertThat(decision.outcome()).isEqualTo(AssignmentOutcome.ASSIGNED);
        assertThat(auditRepository.records).hasSize(1);
        assertThat(auditRepository.records.getFirst().requestId()).isEqualTo("REQ-AUDIT");
        assertThat(auditRepository.records.getFirst().outcome()).isEqualTo("ASSIGNED");
        assertThat(auditRepository.records.getFirst().assignedTrackId()).isEqualTo("T1");
    }

    private static TimeInterval interval(String start, String end) {
        return new TimeInterval(Instant.parse(start), Instant.parse(end));
    }

    private static final class RecordingAuditRepository implements AssignmentAuditRepository {
        private final List<AssignmentAuditRecord> records = new ArrayList<>();

        @Override
        public void save(AssignmentAuditRecord record) {
            records.add(record);
        }
    }
}
