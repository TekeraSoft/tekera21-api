CREATE TABLE attributes
(
    id             UUID    NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    price          DECIMAL,
    discount_price DECIMAL,
    stock          INTEGER NOT NULL,
    sku            VARCHAR(255),
    barcode        VARCHAR(255),
    variation_id   UUID,
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
    id         UUID    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    slug       VARCHAR(255),
    code       VARCHAR(255),
    brand_name VARCHAR(255),
    quantity   INTEGER NOT NULL,
    model_name VARCHAR(255),
    model_code VARCHAR(255),
    price      DECIMAL,
    sku        VARCHAR(255),
    barcode    VARCHAR(255),
    image      VARCHAR(255),
    company_id VARCHAR(255),
    CONSTRAINT "pk_basketıtem" PRIMARY KEY (id)
);

CREATE TABLE basket_item_attributes
(
    basket_item_id UUID NOT NULL,
    key            VARCHAR(255),
    value          VARCHAR(255)
);

CREATE TABLE category
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
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

CREATE TABLE companies
(
    id                       UUID             NOT NULL,
    created_at               TIMESTAMP WITHOUT TIME ZONE,
    updated_at               TIMESTAMP WITHOUT TIME ZONE,
    name                     VARCHAR(255),
    slug                     VARCHAR(255),
    logo                     VARCHAR(255),
    email                    VARCHAR(255),
    gsm_number               VARCHAR(255),
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
    CONSTRAINT "pk_companıes" PRIMARY KEY (id)
);

CREATE TABLE companies_orders
(
    company_id UUID NOT NULL,
    orders_id  UUID NOT NULL
);

CREATE TABLE companies_products
(
    company_id  UUID NOT NULL,
    products_id UUID NOT NULL
);

CREATE TABLE companies_users
(
    company_id UUID NOT NULL,
    users_id   UUID NOT NULL,
    CONSTRAINT "pk_companıes_users" PRIMARY KEY (company_id, users_id)
);

CREATE TABLE company_address
(
    company_id     UUID NOT NULL,
    city           VARCHAR(255),
    street         VARCHAR(255),
    postal_code    VARCHAR(255),
    build_no       VARCHAR(255),
    door_number    VARCHAR(255),
    detail_address VARCHAR(255),
    country        VARCHAR(255)
);

CREATE TABLE company_bank_accounts
(
    company_id   UUID NOT NULL,
    iban         VARCHAR(255),
    account_name VARCHAR(255),
    bank_name    VARCHAR(255),
    is_active    BOOLEAN
);

CREATE TABLE company_categories
(
    category_id UUID NOT NULL,
    company_id  UUID NOT NULL,
    CONSTRAINT "pk_company_categorıes" PRIMARY KEY (category_id, company_id)
);

CREATE TABLE company_document_paths
(
    company_id          UUID NOT NULL,
    document_title      VARCHAR(255),
    document_path       VARCHAR(255),
    verification_status SMALLINT
);

CREATE TABLE digital_fashion_category
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    image      VARCHAR(255),
    CONSTRAINT "pk_dıgıtalfashıoncategory" PRIMARY KEY (id)
);

CREATE TABLE digital_fashion_sub_category
(
    id                          UUID NOT NULL,
    created_at                  TIMESTAMP WITHOUT TIME ZONE,
    updated_at                  TIMESTAMP WITHOUT TIME ZONE,
    name                        VARCHAR(255),
    image                       VARCHAR(255),
    digital_fashion_category_id UUID,
    CONSTRAINT "pk_dıgıtalfashıonsubcategory" PRIMARY KEY (id)
);

CREATE TABLE fabrics
(
    id           UUID    NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    fabric_name  VARCHAR(255),
    fabric_image VARCHAR(255),
    fabric_price DECIMAL,
    stock        INTEGER NOT NULL,
    color        VARCHAR(255),
    CONSTRAINT "pk_fabrıcs" PRIMARY KEY (id)
);

CREATE TABLE guest
(
    id                      UUID NOT NULL,
    created_at              TIMESTAMP WITHOUT TIME ZONE,
    updated_at              TIMESTAMP WITHOUT TIME ZONE,
    name                    VARCHAR(255),
    surname                 VARCHAR(255),
    email                   VARCHAR(255),
    gsm_number              VARCHAR(255),
    shipping_city           VARCHAR(255),
    shipping_street         VARCHAR(255),
    shipping_postal_code    VARCHAR(255),
    shipping_build_no       VARCHAR(255),
    shipping_door_number    VARCHAR(255),
    shipping_detail_address VARCHAR(255),
    shipping_country        VARCHAR(255),
    billing_city            VARCHAR(255),
    billing_street          VARCHAR(255),
    billing_postal_code     VARCHAR(255),
    billing_build_no        VARCHAR(255),
    billing_door_number     VARCHAR(255),
    billing_detail_address  VARCHAR(255),
    billing_country         VARCHAR(255),
    CONSTRAINT pk_guest PRIMARY KEY (id)
);

CREATE TABLE guest_orders
(
    guest_id  UUID NOT NULL,
    orders_id UUID NOT NULL
);

CREATE TABLE orders
(
    id           UUID NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    user_id      UUID,
    guest_id     UUID,
    company_id   UUID,
    payment_type SMALLINT,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE orders_basket_items
(
    order_id        UUID NOT NULL,
    basket_items_id UUID NOT NULL
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
    id            UUID             NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    name          VARCHAR(255),
    slug          VARCHAR(255),
    code          VARCHAR(255),
    brand_name    VARCHAR(255),
    description   TEXT,
    category_id   UUID,
    currency_type VARCHAR(255),
    company_id    UUID,
    product_type  VARCHAR(255),
    rate          DOUBLE PRECISION NOT NULL,
    is_active     BOOLEAN          NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE products_comments
(
    product_id  UUID NOT NULL,
    comments_id UUID NOT NULL
);

CREATE TABLE sub_category
(
    id          UUID NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    name        VARCHAR(255),
    image       VARCHAR(255),
    category_id UUID,
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

CREATE TABLE users
(
    id              UUID    NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    email           VARCHAR(255),
    hashed_password VARCHAR(255),
    gender          VARCHAR(255),
    company_id      UUID,
    phone_number    VARCHAR(255),
    address         VARCHAR(255),
    birth_date      date,
    last_login      TIMESTAMP WITHOUT TIME ZONE,
    is_active       BOOLEAN NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_orders
(
    user_id   UUID NOT NULL,
    orders_id UUID NOT NULL
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
    CONSTRAINT "pk_varıatıons" PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE companies_orders
    ADD CONSTRAINT "uc_companıes_orders_orders" UNIQUE (orders_id);

ALTER TABLE companies_products
    ADD CONSTRAINT "uc_companıes_products_products" UNIQUE (products_id);

ALTER TABLE companies_users
    ADD CONSTRAINT "uc_companıes_users_users" UNIQUE (users_id);

ALTER TABLE guest_orders
    ADD CONSTRAINT uc_guest_orders_orders UNIQUE (orders_id);

ALTER TABLE orders_basket_items
    ADD CONSTRAINT "uc_orders_basket_ıtems_basketıtems" UNIQUE (basket_items_id);

ALTER TABLE products_comments
    ADD CONSTRAINT uc_products_comments_comments UNIQUE (comments_id);

ALTER TABLE users_orders
    ADD CONSTRAINT uc_users_orders_orders UNIQUE (orders_id);

ALTER TABLE attributes
    ADD CONSTRAINT FK_ATTRIBUTES_ON_VARIATION FOREIGN KEY (variation_id) REFERENCES variations (id);

ALTER TABLE digital_fashion_sub_category
    ADD CONSTRAINT FK_DIGITALFASHIONSUBCATEGORY_ON_DIGITAL_FASHION_CATEGORY FOREIGN KEY (digital_fashion_category_id) REFERENCES digital_fashion_category (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_GUEST FOREIGN KEY (guest_id) REFERENCES guest (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE sub_category
    ADD CONSTRAINT FK_SUBCATEGORY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_COMPANY FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE variations
    ADD CONSTRAINT FK_VARIATIONS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE attributes_stock_attributes
    ADD CONSTRAINT "fk_attrıbutes_stockattrıbutes_on_attrıbute" FOREIGN KEY (attribute_id) REFERENCES attributes (id);

ALTER TABLE basket_item_attributes
    ADD CONSTRAINT "fk_basket_ıtem_attrıbutes_on_basket_ıtem" FOREIGN KEY (basket_item_id) REFERENCES basket_item (id);

ALTER TABLE company_categories
    ADD CONSTRAINT fk_comcat_on_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE company_categories
    ADD CONSTRAINT fk_comcat_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE companies_orders
    ADD CONSTRAINT fk_comord_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE companies_orders
    ADD CONSTRAINT fk_comord_on_order FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE company_address
    ADD CONSTRAINT fk_company_address_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE company_bank_accounts
    ADD CONSTRAINT fk_company_bank_accounts_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE company_document_paths
    ADD CONSTRAINT fk_company_document_paths_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE companies_products
    ADD CONSTRAINT fk_compro_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE companies_products
    ADD CONSTRAINT fk_compro_on_product FOREIGN KEY (products_id) REFERENCES products (id);

ALTER TABLE companies_users
    ADD CONSTRAINT fk_comuse_on_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE companies_users
    ADD CONSTRAINT fk_comuse_on_user FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE guest_orders
    ADD CONSTRAINT fk_gueord_on_guest FOREIGN KEY (guest_id) REFERENCES guest (id);

ALTER TABLE guest_orders
    ADD CONSTRAINT fk_gueord_on_order FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE orders_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_basket_ıtem" FOREIGN KEY (basket_items_id) REFERENCES basket_item (id);

ALTER TABLE orders_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_order" FOREIGN KEY (order_id) REFERENCES orders (id);

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

ALTER TABLE users_orders
    ADD CONSTRAINT fk_useord_on_order FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE users_orders
    ADD CONSTRAINT fk_useord_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_fav_products
    ADD CONSTRAINT fk_user_fav_products_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_permissions
    ADD CONSTRAINT "fk_user_permıssıons_on_user" FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE variation_images
    ADD CONSTRAINT "fk_varıatıon_ımages_on_varıatıon" FOREIGN KEY (variation_id) REFERENCES variations (id);