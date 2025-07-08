CREATE TABLE fashion_collection (
                                    id              UUID        PRIMARY KEY,              -- BaseEntity.id
                                    collection_name VARCHAR(255) NOT NULL,
                                    slug            VARCHAR(255),                         -- NULL olabilir
                                    image           VARCHAR(255),
                                    description     TEXT,
                                    is_active       BOOLEAN      DEFAULT TRUE,
                                    created_at      TIMESTAMP    DEFAULT now(),
                                    updated_at      TIMESTAMP    DEFAULT now()
);

CREATE TABLE fashion_collection_products (
                                             fashion_collection_id UUID NOT NULL,
                                             product_id            UUID NOT NULL,

    /* Aynı ürün + koleksiyon ikilisi yalnızca 1 kez yer alabilir */
                                             PRIMARY KEY (fashion_collection_id, product_id),

    /* Koleksiyon silinirse ilgili satırlar da silinsin  */
                                             CONSTRAINT fk_fcp_collection
                                                 FOREIGN KEY (fashion_collection_id)
                                                     REFERENCES fashion_collection(id)
                                                     ON DELETE CASCADE,

    /* Ürün silinirse yine JOIN satırı silinsin           */
                                             CONSTRAINT fk_fcp_product
                                                 FOREIGN KEY (product_id)
                                                     REFERENCES products(id)
                                                     ON DELETE CASCADE
);

/* Performans için ek indeksler (opsiyonel ama tavsiye edilir) */
CREATE INDEX idx_fcp_collection ON fashion_collection_products(fashion_collection_id);
CREATE INDEX idx_fcp_product    ON fashion_collection_products(product_id);