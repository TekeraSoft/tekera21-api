CREATE TABLE seller_verification
(
    id              UUID    NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    seller_user_id  UUID,
    supervisor_id   UUID,
    seller_id       UUID,
    checkesignature BOOLEAN NOT NULL,
    CONSTRAINT "pk_sellerverıfıcatıon" PRIMARY KEY (id)
);

CREATE TABLE seller_verification_documents
(
    seller_verification_id      UUID NOT NULL,
    check_document_verification VARCHAR(255)
);

CREATE TABLE seller_verification_extra_documents
(
    seller_verification_id      UUID NOT NULL,
    extra_document_verification VARCHAR(255)
);

ALTER TABLE seller_verification
    ADD CONSTRAINT FK_SELLERVERIFICATION_ON_SELLER FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_verification
    ADD CONSTRAINT FK_SELLERVERIFICATION_ON_SELLERUSER FOREIGN KEY (seller_user_id) REFERENCES users (id);

ALTER TABLE seller_verification
    ADD CONSTRAINT FK_SELLERVERIFICATION_ON_SUPERVISOR FOREIGN KEY (supervisor_id) REFERENCES users (id);

ALTER TABLE seller_verification_documents
    ADD CONSTRAINT "fk_seller_verıfıcatıon_documents_on_seller_verıfıcatıon" FOREIGN KEY (seller_verification_id) REFERENCES seller_verification (id);

ALTER TABLE seller_verification_extra_documents
    ADD CONSTRAINT "fk_seller_verıfıcatıon_extra_documents_on_seller_verıfıcatıon" FOREIGN KEY (seller_verification_id) REFERENCES seller_verification (id);