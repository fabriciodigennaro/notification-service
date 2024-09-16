CREATE TABLE email_templates (
    id UUID PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    template TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);