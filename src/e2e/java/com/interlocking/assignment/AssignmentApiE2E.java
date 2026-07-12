package com.interlocking.assignment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.interlocking.assignment.adapter.web.AssignmentController;
import com.interlocking.assignment.adapter.xml.XmlAssignmentRequestParser;
import com.interlocking.assignment.application.assignment.AssignmentAuditRecord;
import com.interlocking.assignment.application.assignment.AssignmentAuditRepository;
import com.interlocking.assignment.application.assignment.AssignmentService;
import com.interlocking.assignment.domain.service.assignment.TrackAssignmentEngine;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AssignmentApiE2E {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new AssignmentController(
                    new AssignmentService(
                            new TrackAssignmentEngine(),
                            new NoOpAssignmentAuditRepository()),
                    new XmlAssignmentRequestParser()))
            .build();

    @Test
    void userCanSubmitXmlAndReceiveAssignmentDecision() throws Exception {
        var xml = """
                <assignmentRequest id="REQ-E2E"
                                   start="2026-08-05T10:00:00Z"
                                   end="2026-08-05T10:20:00Z"
                                   lastAssignedTrackId="T1">
                  <tracks>
                    <track id="T1" status="OPEN"/>
                    <track id="T2" status="OPEN"/>
                  </tracks>
                </assignmentRequest>
                """;

        mockMvc.perform(post("/api/assignments")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(xml))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome").value("ASSIGNED"))
                .andExpect(jsonPath("$.assignedTrackId").value("T2"));
    }

    private static final class NoOpAssignmentAuditRepository implements AssignmentAuditRepository {
        @Override
        public void save(AssignmentAuditRecord record) {
        }
    }
}
