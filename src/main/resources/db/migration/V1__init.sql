CREATE TABLE address
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    city           VARCHAR(255),
    street         VARCHAR(255),
    postal_code    VARCHAR(255),
    build_no       VARCHAR(255),
    door_number    VARCHAR(255),
    detail_address VARCHAR(255),
    country        VARCHAR(255),
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE attributes
(
    id                 UUID    NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE,
    updated_at         TIMESTAMP WITHOUT TIME ZONE,
    price              DECIMAL,
    discount_price     DECIMAL,
    stock              INTEGER NOT NULL,
    max_purchase_stock INTEGER,
    sku                VARCHAR(255),
    barcode            VARCHAR(255),
    variation_id       UUID,
    CONSTRAINT "pk_attrıbutes" PRIMARY KEY (id)
);

CREATE TABLE attributes_stock_attributes
(
    attribute_id UUID NOT NULL,
    key          VARCHAR(255),
    value        VARCHAR(255)
);

CREATE TABLE basket_item
(
    id                  UUID    NOT NULL,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    product_id          VARCHAR(255),
    variation_id        VARCHAR(255),
    attribute_id        VARCHAR(255),
    name                VARCHAR(255),
    slug                VARCHAR(255),
    code                VARCHAR(255),
    brand_name          VARCHAR(255),
    quantity            INTEGER NOT NULL,
    model_code          VARCHAR(255),
    price               DECIMAL,
    sku                 VARCHAR(255),
    barcode             VARCHAR(255),
    image               VARCHAR(255),
    shipping_price      DECIMAL,
    seller_id           UUID,
    shipping_company_id UUID,
    CONSTRAINT "pk_basketıtem" PRIMARY KEY (id)
);

CREATE TABLE basket_item_attributes
(
    basket_item_id UUID NOT NULL,
    key            VARCHAR(255),
    value          VARCHAR(255)
);

CREATE TABLE buyer
(
    id              UUID NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    name            VARCHAR(255),
    surname         VARCHAR(255),
    email           VARCHAR(255),
    gsm_number      VARCHAR(255),
    identity_number VARCHAR(255),
    is_registered   BOOLEAN,
    CONSTRAINT pk_buyer PRIMARY KEY (id)
);

CREATE TABLE category
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    slug       VARCHAR(255),
    image      VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE comment
(
    id         UUID             NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    user_name  VARCHAR(255),
    rate       DOUBLE PRECISION NOT NULL,
    message    TEXT,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE fashion_collection
(
    id              UUID    NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    collection_name VARCHAR(255),
    slug            VARCHAR(255),
    image           VARCHAR(255),
    description     TEXT,
    seller_id       UUID,
    is_active       BOOLEAN NOT NULL,
    CONSTRAINT "pk_fashıoncollectıon" PRIMARY KEY (id)
);

CREATE TABLE fashion_collection_products
(
    fashion_collection_id UUID NOT NULL,
    product_id            UUID NOT NULL,
    CONSTRAINT "pk_fashıon_collectıon_products" PRIMARY KEY (fashion_collection_id, product_id)
);

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

CREATE TABLE order_basket_items
(
    basket_item_id UUID NOT NULL,
    order_id       UUID NOT NULL
);

CREATE TABLE orders
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    order_no       VARCHAR(255),
    shipping_price DECIMAL,
    total_price    DECIMAL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE orders_seller_order
(
    order_id        UUID NOT NULL,
    seller_order_id UUID NOT NULL
);

CREATE TABLE product_attributes
(
    product_id UUID NOT NULL,
    key        VARCHAR(255),
    value      VARCHAR(255)
);

CREATE TABLE product_subcategories
(
    product_fk     UUID NOT NULL,
    subcategory_id UUID NOT NULL,
    CONSTRAINT "pk_product_subcategorıes" PRIMARY KEY (product_fk, subcategory_id)
);

CREATE TABLE product_tags
(
    product_fk UUID NOT NULL,
    tags       VARCHAR(255)
);

CREATE TABLE products
(
    id                        UUID             NOT NULL,
    created_at                TIMESTAMP WITHOUT TIME ZONE,
    updated_at                TIMESTAMP WITHOUT TIME ZONE,
    name                      VARCHAR(255),
    slug                      VARCHAR(255),
    code                      VARCHAR(255),
    brand_name                VARCHAR(255),
    description               TEXT,
    category_id               UUID,
    currency_type             VARCHAR(255),
    seller_id                 UUID,
    shipping_preparation_days INTEGER,
    product_type              VARCHAR(255),
    rate                      DOUBLE PRECISION NOT NULL,
    video_url                 VARCHAR(255),
    is_active                 BOOLEAN          NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE products_comments
(
    product_id  UUID NOT NULL,
    comments_id UUID NOT NULL
);

CREATE TABLE seller_bank_accounts
(
    seller_id    UUID NOT NULL,
    iban         VARCHAR(255),
    account_name VARCHAR(255),
    bank_name    VARCHAR(255),
    is_active    BOOLEAN
);

CREATE TABLE seller_categories
(
    category_id UUID NOT NULL,
    seller_id   UUID NOT NULL,
    CONSTRAINT "pk_seller_categorıes" PRIMARY KEY (category_id, seller_id)
);

CREATE TABLE seller_document_paths
(
    seller_id                   UUID NOT NULL,
    document_title              VARCHAR(255),
    document_path               VARCHAR(255),
    verification_status         SMALLINT,
    faulty_document_description VARCHAR(255)
);

CREATE TABLE seller_orders
(
    id                  UUID NOT NULL,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    user_id             UUID,
    buyer_id            UUID,
    shipping_address_id UUID,
    billing_address_id  UUID,
    total_price         DECIMAL,
    shipping_price      DECIMAL,
    payment_type        SMALLINT,
    payment_status      SMALLINT,
    order_id            UUID,
    CONSTRAINT pk_seller_orders PRIMARY KEY (id)
);

CREATE TABLE seller_shipping_companies
(
    seller_id           UUID NOT NULL,
    shipping_company_id UUID NOT NULL,
    CONSTRAINT "pk_seller_shıppıng_companıes" PRIMARY KEY (seller_id, shipping_company_id)
);

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

CREATE TABLE sellers
(
    id                       UUID             NOT NULL,
    created_at               TIMESTAMP WITHOUT TIME ZONE,
    updated_at               TIMESTAMP WITHOUT TIME ZONE,
    name                     VARCHAR(255),
    slug                     VARCHAR(255),
    logo                     VARCHAR(255),
    email                    VARCHAR(255),
    gsm_number               VARCHAR(255),
    estimated_delivery_time  VARCHAR(255),
    alternative_phone_number VARCHAR(255),
    support_phone_number     VARCHAR(255),
    tax_number               VARCHAR(255),
    tax_office               VARCHAR(255),
    meris_number             VARCHAR(255),
    registration_date        TIMESTAMP WITHOUT TIME ZONE,
    contact_person_number    VARCHAR(255),
    contact_person_title     VARCHAR(255),
    rate                     DOUBLE PRECISION NOT NULL,
    is_verified              BOOLEAN          NOT NULL,
    is_active                BOOLEAN          NOT NULL,
    verification_status      VARCHAR(255),
    CONSTRAINT pk_sellers PRIMARY KEY (id)
);

CREATE TABLE sellers_address
(
    seller_id  UUID NOT NULL,
    address_id UUID NOT NULL
);

CREATE TABLE sellers_seller_orders
(
    seller_id        UUID NOT NULL,
    seller_orders_id UUID NOT NULL
);

CREATE TABLE sellers_users
(
    seller_id UUID NOT NULL,
    users_id  UUID NOT NULL,
    CONSTRAINT pk_sellers_users PRIMARY KEY (seller_id, users_id)
);

CREATE TABLE shipping_company
(
    id               UUID    NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    name             VARCHAR(255),
    gsm_number       VARCHAR(255),
    email            VARCHAR(255),
    price            DECIMAL,
    min_delivery_day INTEGER NOT NULL,
    max_delivery_day INTEGER NOT NULL,
    CONSTRAINT "pk_shıppıng_company" PRIMARY KEY (id)
);

CREATE TABLE shipping_company_orders
(
    shipping_company_id UUID NOT NULL,
    orders_id           UUID NOT NULL
);

CREATE TABLE sub_category
(
    id          UUID NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    name        VARCHAR(255),
    slug        VARCHAR(255),
    image       VARCHAR(255),
    category_id UUID,
    parent_id   UUID,
    CONSTRAINT pk_subcategory PRIMARY KEY (id)
);

CREATE TABLE target_pictures
(
    id              UUID NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    product_id      VARCHAR(255),
    target_pic      VARCHAR(255),
    mind_path       VARCHAR(255),
    default_content VARCHAR(255),
    special_content VARCHAR(255),
    CONSTRAINT "pk_target_pıctures" PRIMARY KEY (id)
);

CREATE TABLE themes
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    image      VARCHAR(255),
    CONSTRAINT pk_themes PRIMARY KEY (id)
);

CREATE TABLE user_fav_products
(
    user_id      UUID NOT NULL,
    fav_products VARCHAR(255)
);

CREATE TABLE user_permissions
(
    user_id     UUID NOT NULL,
    permissions VARCHAR(255)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE user_topics
(
    user_id        UUID NOT NULL,
    related_topics VARCHAR(255)
);

CREATE TABLE users
(
    id              UUID NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    email           VARCHAR(255),
    hashed_password VARCHAR(255),
    gender          VARCHAR(255),
    gsm_number      VARCHAR(255),
    birth_date      date,
    last_login      TIMESTAMP WITHOUT TIME ZONE,
    assign_count    INTEGER,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_address
(
    user_id    UUID NOT NULL,
    address_id UUID NOT NULL
);

CREATE TABLE users_follow_sellers
(
    user_id           UUID NOT NULL,
    follow_sellers_id UUID NOT NULL
);

CREATE TABLE variation_images
(
    variation_id UUID NOT NULL,
    images       VARCHAR(255)
);

CREATE TABLE variations
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    model_name VARCHAR(255),
    model_code VARCHAR(255),
    color      VARCHAR(255),
    product_id UUID,
    position   INTEGER,
    CONSTRAINT "pk_varıatıons" PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE order_basket_items
    ADD CONSTRAINT "uc_order_basket_ıtems_basket_ıtem" UNIQUE (basket_item_id);

ALTER TABLE orders_seller_order
    ADD CONSTRAINT uc_orders_seller_order_sellerorder UNIQUE (seller_order_id);

ALTER TABLE products_comments
    ADD CONSTRAINT uc_products_comments_comments UNIQUE (comments_id);

ALTER TABLE seller_orders
    ADD CONSTRAINT uc_seller_orders_buyer UNIQUE (buyer_id);

ALTER TABLE sellers_address
    ADD CONSTRAINT uc_sellers_address_address UNIQUE (address_id);

ALTER TABLE sellers_seller_orders
    ADD CONSTRAINT uc_sellers_seller_orders_sellerorders UNIQUE (seller_orders_id);

ALTER TABLE sellers_users
    ADD CONSTRAINT uc_sellers_users_users UNIQUE (users_id);

ALTER TABLE shipping_company_orders
    ADD CONSTRAINT "uc_shıppıng_company_orders_orders" UNIQUE (orders_id);

ALTER TABLE users_address
    ADD CONSTRAINT uc_users_address_address UNIQUE (address_id);

ALTER TABLE users_follow_sellers
    ADD CONSTRAINT uc_users_follow_sellers_followsellers UNIQUE (follow_sellers_id);

ALTER TABLE attributes
    ADD CONSTRAINT FK_ATTRIBUTES_ON_VARIATION FOREIGN KEY (variation_id) REFERENCES variations (id);

ALTER TABLE basket_item
    ADD CONSTRAINT FK_BASKETITEM_ON_SELLER FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE basket_item
    ADD CONSTRAINT FK_BASKETITEM_ON_SHIPPING_COMPANY FOREIGN KEY (shipping_company_id) REFERENCES shipping_company (id);

ALTER TABLE fashion_collection
    ADD CONSTRAINT FK_FASHIONCOLLECTION_ON_SELLER FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_verification
    ADD CONSTRAINT FK_SELLERVERIFICATION_ON_SELLER FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_verification
    ADD CONSTRAINT FK_SELLERVERIFICATION_ON_SELLERUSER FOREIGN KEY (seller_user_id) REFERENCES users (id);

ALTER TABLE seller_verification
    ADD CONSTRAINT FK_SELLERVERIFICATION_ON_SUPERVISOR FOREIGN KEY (supervisor_id) REFERENCES users (id);

ALTER TABLE seller_orders
    ADD CONSTRAINT FK_SELLER_ORDERS_ON_BILLINGADDRESS FOREIGN KEY (billing_address_id) REFERENCES address (id);

ALTER TABLE seller_orders
    ADD CONSTRAINT FK_SELLER_ORDERS_ON_BUYER FOREIGN KEY (buyer_id) REFERENCES buyer (id);

ALTER TABLE seller_orders
    ADD CONSTRAINT FK_SELLER_ORDERS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE seller_orders
    ADD CONSTRAINT FK_SELLER_ORDERS_ON_SHIPPINGADDRESS FOREIGN KEY (shipping_address_id) REFERENCES address (id);

ALTER TABLE seller_orders
    ADD CONSTRAINT FK_SELLER_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE sub_category
    ADD CONSTRAINT FK_SUBCATEGORY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE sub_category
    ADD CONSTRAINT FK_SUBCATEGORY_ON_PARENT FOREIGN KEY (parent_id) REFERENCES sub_category (id);

ALTER TABLE variations
    ADD CONSTRAINT FK_VARIATIONS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE attributes_stock_attributes
    ADD CONSTRAINT "fk_attrıbutes_stockattrıbutes_on_attrıbute" FOREIGN KEY (attribute_id) REFERENCES attributes (id);

ALTER TABLE basket_item_attributes
    ADD CONSTRAINT "fk_basket_ıtem_attrıbutes_on_basket_ıtem" FOREIGN KEY (basket_item_id) REFERENCES basket_item (id);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT "fk_fascolpro_on_fashıon_collectıon" FOREIGN KEY (fashion_collection_id) REFERENCES fashion_collection (id);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT fk_fascolpro_on_product FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE order_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_basket_ıtem" FOREIGN KEY (basket_item_id) REFERENCES basket_item (id);

ALTER TABLE order_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_seller_order" FOREIGN KEY (order_id) REFERENCES seller_orders (id);

ALTER TABLE orders_seller_order
    ADD CONSTRAINT fk_ordselord_on_order FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE orders_seller_order
    ADD CONSTRAINT fk_ordselord_on_seller_order FOREIGN KEY (seller_order_id) REFERENCES seller_orders (id);

ALTER TABLE products_comments
    ADD CONSTRAINT fk_procom_on_comment FOREIGN KEY (comments_id) REFERENCES comment (id);

ALTER TABLE products_comments
    ADD CONSTRAINT fk_procom_on_product FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE product_attributes
    ADD CONSTRAINT "fk_product_attrıbutes_on_product" FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE product_tags
    ADD CONSTRAINT fk_product_tags_on_product FOREIGN KEY (product_fk) REFERENCES products (id);

ALTER TABLE product_subcategories
    ADD CONSTRAINT fk_prosub_on_product FOREIGN KEY (product_fk) REFERENCES products (id);

ALTER TABLE product_subcategories
    ADD CONSTRAINT fk_prosub_on_sub_category FOREIGN KEY (subcategory_id) REFERENCES sub_category (id);

ALTER TABLE sellers_address
    ADD CONSTRAINT fk_seladd_on_address FOREIGN KEY (address_id) REFERENCES address (id);

ALTER TABLE sellers_address
    ADD CONSTRAINT fk_seladd_on_seller FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_categories
    ADD CONSTRAINT fk_selcat_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE seller_categories
    ADD CONSTRAINT fk_selcat_on_seller FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_bank_accounts
    ADD CONSTRAINT fk_seller_bank_accounts_on_seller FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_document_paths
    ADD CONSTRAINT fk_seller_document_paths_on_seller FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_verification_documents
    ADD CONSTRAINT "fk_seller_verıfıcatıon_documents_on_seller_verıfıcatıon" FOREIGN KEY (seller_verification_id) REFERENCES seller_verification (id);

ALTER TABLE seller_verification_extra_documents
    ADD CONSTRAINT "fk_seller_verıfıcatıon_extra_documents_on_seller_verıfıcatıon" FOREIGN KEY (seller_verification_id) REFERENCES seller_verification (id);

ALTER TABLE sellers_seller_orders
    ADD CONSTRAINT fk_selselord_on_seller FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE sellers_seller_orders
    ADD CONSTRAINT fk_selselord_on_seller_order FOREIGN KEY (seller_orders_id) REFERENCES seller_orders (id);

ALTER TABLE seller_shipping_companies
    ADD CONSTRAINT "fk_selshıcom_on_seller" FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE seller_shipping_companies
    ADD CONSTRAINT "fk_selshıcom_on_shıppıng_company" FOREIGN KEY (shipping_company_id) REFERENCES shipping_company (id);

ALTER TABLE sellers_users
    ADD CONSTRAINT fk_seluse_on_seller FOREIGN KEY (seller_id) REFERENCES sellers (id);

ALTER TABLE sellers_users
    ADD CONSTRAINT fk_seluse_on_user FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE shipping_company_orders
    ADD CONSTRAINT "fk_shıcomord_on_order" FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE shipping_company_orders
    ADD CONSTRAINT "fk_shıcomord_on_shıppıng_company" FOREIGN KEY (shipping_company_id) REFERENCES shipping_company (id);

ALTER TABLE users_address
    ADD CONSTRAINT fk_useadd_on_address FOREIGN KEY (address_id) REFERENCES address (id);

ALTER TABLE users_address
    ADD CONSTRAINT fk_useadd_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_follow_sellers
    ADD CONSTRAINT fk_usefolsel_on_seller FOREIGN KEY (follow_sellers_id) REFERENCES sellers (id);

ALTER TABLE users_follow_sellers
    ADD CONSTRAINT fk_usefolsel_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_fav_products
    ADD CONSTRAINT fk_user_fav_products_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_permissions
    ADD CONSTRAINT "fk_user_permıssıons_on_user" FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_topics
    ADD CONSTRAINT "fk_user_topıcs_on_user" FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE variation_images
    ADD CONSTRAINT "fk_varıatıon_ımages_on_varıatıon" FOREIGN KEY (variation_id) REFERENCES variations (id);