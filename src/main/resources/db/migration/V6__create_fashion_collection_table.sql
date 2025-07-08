CREATE TABLE fashion_collection
(
    id              UUID    NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    collection_name VARCHAR(255),
    slug            VARCHAR(255) NOT NULL,
    image           VARCHAR(255),
    description     TEXT,
    is_active       BOOLEAN NOT NULL,
    CONSTRAINT "pk_fashıoncollectıon" PRIMARY KEY (id)
);

CREATE TABLE fashion_collection_products
(
    fashion_collection_id UUID NOT NULL,
    product_id            UUID NOT NULL
);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT "uc_fashıon_collectıon_products_product" UNIQUE (product_id);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT "fk_fascolpro_on_fashıon_collectıon" FOREIGN KEY (fashion_collection_id) REFERENCES fashion_collection (id);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT fk_fascolpro_on_product FOREIGN KEY (product_id) REFERENCES products (id);