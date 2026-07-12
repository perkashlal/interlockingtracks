CREATE TABLE assignment_audit (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(200) NOT NULL,
    outcome VARCHAR(50) NOT NULL,
    assigned_track_id VARCHAR(100),
    reasons TEXT NOT NULL,
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
