CREATE TABLE news
(
    id         UUID    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    head       VARCHAR(255),
    sub_title  VARCHAR(255),
    image      VARCHAR(255),
    body       TEXT,
    is_active  BOOLEAN NOT NULL,
    CONSTRAINT pk_news PRIMARY KEY (id)
);