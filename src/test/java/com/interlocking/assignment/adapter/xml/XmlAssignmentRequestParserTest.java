package com.interlocking.assignment.adapter.xml;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class XmlAssignmentRequestParserTest {

    private final XmlAssignmentRequestParser parser = new XmlAssignmentRequestParser();

    @Test
    void parsesTrainRequestTracksOccupationsAndRotationState() {
        var xml = """
                <assignmentRequest id="REQ-1"
                                   start="2026-08-05T10:00:00Z"
                                   end="2026-08-05T10:20:00Z"
                                   lastAssignedTrackId="T1">
                  <tracks>
                    <track id="T1" status="OPEN">
                      <occupation start="2026-08-05T09:00:00Z" end="2026-08-05T09:30:00Z"/>
                    </track>
                    <track id="T2" status="OPEN"/>
                  </tracks>
                </assignmentRequest>
                """;

        var input = parser.parse(xml);

        assertThat(input.request().id()).isEqualTo("REQ-1");
        assertThat(input.request().requestedInterval().start())
                .isEqualTo(Instant.parse("2026-08-05T10:00:00Z"));
        assertThat(input.lastAssignedTrackId()).contains("T1");
        assertThat(input.tracks()).hasSize(2);
        assertThat(input.tracks().getFirst().id()).isEqualTo("T1");
        assertThat(input.tracks().getFirst().operationalStatus())
                .isEqualTo(TrackOperationalStatus.OPEN);
        assertThat(input.tracks().getFirst().protectedOccupations()).hasSize(1);
        assertThat(input.tracks().get(1).id()).isEqualTo("T2");
    }
}
