package com.interlocking.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import com.interlocking.assignment.adapter.xml.XmlAssignmentRequestParser;
import com.interlocking.assignment.domain.model.assignment.AssignmentOutcome;
import com.interlocking.assignment.domain.service.assignment.TrackAssignmentEngine;
import org.junit.jupiter.api.Test;

class XmlAssignmentFlowIT {

    private final XmlAssignmentRequestParser parser = new XmlAssignmentRequestParser();
    private final TrackAssignmentEngine engine = new TrackAssignmentEngine();

    @Test
    void parsesXmlAndAssignsSafeTrackEndToEndInsideDomainFlow() {
        var xml = """
                <assignmentRequest id="REQ-IT"
                                   start="2026-08-05T10:10:00Z"
                                   end="2026-08-05T10:25:00Z">
                  <tracks>
                    <track id="T1" status="OPEN">
                      <occupation start="2026-08-05T10:00:00Z" end="2026-08-05T10:20:00Z"/>
                    </track>
                    <track id="T2" status="OPEN"/>
                  </tracks>
                </assignmentRequest>
                """;

        var input = parser.parse(xml);
        var decision = engine.assign(
                input.request(),
                input.tracks(),
                input.lastAssignedTrackId());

        assertThat(decision.outcome()).isEqualTo(AssignmentOutcome.ASSIGNED);
        assertThat(decision.assignedTrackId()).contains("T2");
    }
}
