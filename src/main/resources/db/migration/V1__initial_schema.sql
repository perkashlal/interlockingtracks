CREATE TABLE processed_message (
    id BIGSERIAL PRIMARY KEY,
    source VARCHAR(100) NOT NULL,
    message_key VARCHAR(200) NOT NULL,
    checksum VARCHAR(64) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_processed_message_source_key UNIQUE (source, message_key)
);

