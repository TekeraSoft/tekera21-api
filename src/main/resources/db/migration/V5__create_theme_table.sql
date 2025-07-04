CREATE TABLE themes
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    image      VARCHAR(255),
    CONSTRAINT pk_themes PRIMARY KEY (id)
);