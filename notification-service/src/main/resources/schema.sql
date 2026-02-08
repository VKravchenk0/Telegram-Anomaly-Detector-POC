CREATE TABLE IF NOT EXISTS notifications (
    id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    group_link VARCHAR(500) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    keyword VARCHAR(100),
    content TEXT,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notifications_timestamp ON notifications(timestamp DESC);
