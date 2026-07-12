package com.interlocking.assignment.adapter.persistence;

import com.interlocking.assignment.application.assignment.AssignmentAuditRecord;
import com.interlocking.assignment.application.assignment.AssignmentAuditRepository;
import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAssignmentAuditRepository implements AssignmentAuditRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAssignmentAuditRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        ensureAuditTableExists();
    }

    @Override
    public void save(AssignmentAuditRecord record) {
        jdbcTemplate.update(
                """
                INSERT INTO assignment_audit
                    (request_id, outcome, assigned_track_id, reasons, recorded_at)
                VALUES (?, ?, ?, ?, ?)
                """,
                record.requestId(),
                record.outcome(),
                record.assignedTrackId(),
                record.reasons(),
                Timestamp.from(record.recordedAt()));
    }

    private void ensureAuditTableExists() {
        jdbcTemplate.execute(
                """
                CREATE TABLE IF NOT EXISTS assignment_audit (
                    id BIGSERIAL PRIMARY KEY,
                    request_id VARCHAR(200) NOT NULL,
                    outcome VARCHAR(50) NOT NULL,
                    assigned_track_id VARCHAR(100),
                    reasons TEXT NOT NULL,
                    recorded_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);
    }
}
