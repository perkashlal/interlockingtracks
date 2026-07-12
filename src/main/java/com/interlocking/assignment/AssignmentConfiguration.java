package com.interlocking.assignment;

import com.interlocking.assignment.adapter.xml.XmlAssignmentRequestParser;
import com.interlocking.assignment.application.assignment.AssignmentAuditRepository;
import com.interlocking.assignment.application.assignment.AssignmentService;
import com.interlocking.assignment.domain.service.assignment.TrackAssignmentEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssignmentConfiguration {

    @Bean
    TrackAssignmentEngine trackAssignmentEngine() {
        return new TrackAssignmentEngine();
    }

    @Bean
    AssignmentService assignmentService(
            TrackAssignmentEngine trackAssignmentEngine,
            AssignmentAuditRepository auditRepository) {
        return new AssignmentService(trackAssignmentEngine, auditRepository);
    }

    @Bean
    XmlAssignmentRequestParser xmlAssignmentRequestParser() {
        return new XmlAssignmentRequestParser();
    }
}
