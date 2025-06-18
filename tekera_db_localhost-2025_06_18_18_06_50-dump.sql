--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: attributes; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.attributes (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    stock integer NOT NULL,
    variation_id uuid
);


ALTER TABLE public.attributes OWNER TO tekera_user;

--
-- Name: attributes_stock_attributes; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.attributes_stock_attributes (
    attribute_id uuid NOT NULL,
    key character varying(255),
    value character varying(255)
);


ALTER TABLE public.attributes_stock_attributes OWNER TO tekera_user;

--
-- Name: basket_item; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.basket_item (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    slug character varying(255),
    code character varying(255),
    brand_name character varying(255),
    quantity integer NOT NULL,
    model_name character varying(255),
    model_code character varying(255),
    price numeric,
    sku character varying(255),
    barcode character varying(255),
    image character varying(255),
    company_id character varying(255)
);


ALTER TABLE public.basket_item OWNER TO tekera_user;

--
-- Name: basket_item_attributes; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.basket_item_attributes (
    basket_item_id uuid NOT NULL,
    key character varying(255),
    value character varying(255)
);


ALTER TABLE public.basket_item_attributes OWNER TO tekera_user;

--
-- Name: category; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.category (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    image character varying(255)
);


ALTER TABLE public.category OWNER TO tekera_user;

--
-- Name: comment; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.comment (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    user_name character varying(255),
    rate double precision NOT NULL,
    message text
);


ALTER TABLE public.comment OWNER TO tekera_user;

--
-- Name: companies; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.companies (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    slug character varying(255),
    logo character varying(255),
    email character varying(255),
    gsm_number character varying(255),
    alternative_phone_number character varying(255),
    support_phone_number character varying(255),
    tax_number character varying(255),
    tax_office character varying(255),
    meris_number character varying(255),
    registration_date timestamp without time zone,
    contact_person_number character varying(255),
    contact_person_title character varying(255),
    rate double precision NOT NULL,
    is_verified boolean NOT NULL,
    is_active boolean NOT NULL,
    verification_status character varying(255)
);


ALTER TABLE public.companies OWNER TO tekera_user;

--
-- Name: companies_orders; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.companies_orders (
    company_id uuid NOT NULL,
    orders_id uuid NOT NULL
);


ALTER TABLE public.companies_orders OWNER TO tekera_user;

--
-- Name: companies_products; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.companies_products (
    company_id uuid NOT NULL,
    products_id uuid NOT NULL
);


ALTER TABLE public.companies_products OWNER TO tekera_user;

--
-- Name: companies_users; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.companies_users (
    company_id uuid NOT NULL,
    users_id uuid NOT NULL
);


ALTER TABLE public.companies_users OWNER TO tekera_user;

--
-- Name: company_address; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.company_address (
    company_id uuid NOT NULL,
    city character varying(255),
    street character varying(255),
    postal_code character varying(255),
    build_no character varying(255),
    door_number character varying(255),
    detail_address character varying(255),
    country character varying(255)
);


ALTER TABLE public.company_address OWNER TO tekera_user;

--
-- Name: company_bank_accounts; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.company_bank_accounts (
    company_id uuid NOT NULL,
    iban character varying(255),
    account_name character varying(255),
    bank_name character varying(255),
    is_active boolean
);


ALTER TABLE public.company_bank_accounts OWNER TO tekera_user;

--
-- Name: company_categories; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.company_categories (
    category_id uuid NOT NULL,
    company_id uuid NOT NULL
);


ALTER TABLE public.company_categories OWNER TO tekera_user;

--
-- Name: company_document_paths; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.company_document_paths (
    company_id uuid NOT NULL,
    document_title character varying(255),
    document_path character varying(255),
    verification_status smallint
);


ALTER TABLE public.company_document_paths OWNER TO tekera_user;

--
-- Name: digital_fashion_category; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.digital_fashion_category (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    image character varying(255)
);


ALTER TABLE public.digital_fashion_category OWNER TO tekera_user;

--
-- Name: digital_fashion_sub_category; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.digital_fashion_sub_category (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    image character varying(255),
    digital_fashion_category_id uuid
);


ALTER TABLE public.digital_fashion_sub_category OWNER TO tekera_user;

--
-- Name: fabrics; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.fabrics (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    fabric_name character varying(255),
    fabric_image character varying(255),
    fabric_price numeric,
    stock integer NOT NULL,
    color character varying(255)
);


ALTER TABLE public.fabrics OWNER TO tekera_user;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO tekera_user;

--
-- Name: guest; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.guest (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    surname character varying(255),
    email character varying(255),
    gsm_number character varying(255),
    shipping_city character varying(255),
    shipping_street character varying(255),
    shipping_postal_code character varying(255),
    shipping_build_no character varying(255),
    shipping_door_number character varying(255),
    shipping_detail_address character varying(255),
    shipping_country character varying(255),
    billing_city character varying(255),
    billing_street character varying(255),
    billing_postal_code character varying(255),
    billing_build_no character varying(255),
    billing_door_number character varying(255),
    billing_detail_address character varying(255),
    billing_country character varying(255)
);


ALTER TABLE public.guest OWNER TO tekera_user;

--
-- Name: guest_orders; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.guest_orders (
    guest_id uuid NOT NULL,
    orders_id uuid NOT NULL
);


ALTER TABLE public.guest_orders OWNER TO tekera_user;

--
-- Name: orders; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.orders (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    user_id uuid,
    guest_id uuid,
    company_id uuid,
    payment_type smallint
);


ALTER TABLE public.orders OWNER TO tekera_user;

--
-- Name: orders_basket_items; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.orders_basket_items (
    order_id uuid NOT NULL,
    basket_items_id uuid NOT NULL
);


ALTER TABLE public.orders_basket_items OWNER TO tekera_user;

--
-- Name: product_attributes; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.product_attributes (
    product_id uuid NOT NULL,
    key character varying(255),
    value character varying(255)
);


ALTER TABLE public.product_attributes OWNER TO tekera_user;

--
-- Name: product_subcategories; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.product_subcategories (
    product_fk uuid NOT NULL,
    subcategory_id uuid NOT NULL
);


ALTER TABLE public.product_subcategories OWNER TO tekera_user;

--
-- Name: product_tags; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.product_tags (
    product_fk uuid NOT NULL,
    tags character varying(255)
);


ALTER TABLE public.product_tags OWNER TO tekera_user;

--
-- Name: products; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.products (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    slug character varying(255),
    code character varying(255),
    brand_name character varying(255),
    description text,
    category_id uuid,
    currency_type character varying(255),
    company_id uuid,
    product_type character varying(255),
    rate double precision NOT NULL,
    is_active boolean NOT NULL
);


ALTER TABLE public.products OWNER TO tekera_user;

--
-- Name: products_comments; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.products_comments (
    product_id uuid NOT NULL,
    comments_id uuid NOT NULL
);


ALTER TABLE public.products_comments OWNER TO tekera_user;

--
-- Name: sub_category; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.sub_category (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    name character varying(255),
    image character varying(255),
    category_id uuid
);


ALTER TABLE public.sub_category OWNER TO tekera_user;

--
-- Name: target_pictures; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.target_pictures (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    product_id character varying(255),
    target_pic character varying(255),
    mind_path character varying(255),
    default_content character varying(255),
    special_content character varying(255)
);


ALTER TABLE public.target_pictures OWNER TO tekera_user;

--
-- Name: user_fav_products; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.user_fav_products (
    user_id uuid NOT NULL,
    fav_products character varying(255)
);


ALTER TABLE public.user_fav_products OWNER TO tekera_user;

--
-- Name: user_permissions; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.user_permissions (
    user_id uuid NOT NULL,
    permissions character varying(255)
);


ALTER TABLE public.user_permissions OWNER TO tekera_user;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.user_roles (
    user_id uuid NOT NULL,
    roles character varying(255)
);


ALTER TABLE public.user_roles OWNER TO tekera_user;

--
-- Name: users; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    first_name character varying(255),
    last_name character varying(255),
    email character varying(255),
    hashed_password character varying(255),
    gender character varying(255),
    company_id uuid,
    phone_number character varying(255),
    address character varying(255),
    birth_date date,
    last_login timestamp without time zone,
    is_active boolean NOT NULL
);


ALTER TABLE public.users OWNER TO tekera_user;

--
-- Name: users_orders; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.users_orders (
    user_id uuid NOT NULL,
    orders_id uuid NOT NULL
);


ALTER TABLE public.users_orders OWNER TO tekera_user;

--
-- Name: variation_images; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.variation_images (
    variation_id uuid NOT NULL,
    images character varying(255)
);


ALTER TABLE public.variation_images OWNER TO tekera_user;

--
-- Name: variations; Type: TABLE; Schema: public; Owner: tekera_user
--

CREATE TABLE public.variations (
    id uuid NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    model_name character varying(255),
    model_code character varying(255),
    price numeric,
    discount_price numeric,
    sku character varying(255),
    barcode character varying(255),
    product_id uuid
);


ALTER TABLE public.variations OWNER TO tekera_user;

--
-- Data for Name: attributes; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.attributes (id, created_at, updated_at, stock, variation_id) FROM stdin;
\.


--
-- Data for Name: attributes_stock_attributes; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.attributes_stock_attributes (attribute_id, key, value) FROM stdin;
\.


--
-- Data for Name: basket_item; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.basket_item (id, created_at, updated_at, name, slug, code, brand_name, quantity, model_name, model_code, price, sku, barcode, image, company_id) FROM stdin;
\.


--
-- Data for Name: basket_item_attributes; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.basket_item_attributes (basket_item_id, key, value) FROM stdin;
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.category (id, created_at, updated_at, name, image) FROM stdin;
ddec3b65-a0ed-4286-a600-0c1a55a840a2	2025-06-18 14:58:25.721023	2025-06-18 14:58:25.721038	Elektronik	category/9db088ef-d2fb-4b18-9ec1-d154f1764e55.jpg
\.


--
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.comment (id, created_at, updated_at, user_name, rate, message) FROM stdin;
\.


--
-- Data for Name: companies; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.companies (id, created_at, updated_at, name, slug, logo, email, gsm_number, alternative_phone_number, support_phone_number, tax_number, tax_office, meris_number, registration_date, contact_person_number, contact_person_title, rate, is_verified, is_active, verification_status) FROM stdin;
011ade18-d066-405d-bd89-3c58c27ae603	2025-06-18 15:03:15.304255	2025-06-18 15:03:15.30427	ARZUAMBER MODA	arzuamber-moda-64116	/company/logo/arzuamber_moda/16e231ed-565e-496a-a721-e5946b441dca.jpeg	arzuamber@gmail.com	5300889507	5300889507	5300889507	94839283493	Düden Vergi Dairesi	5983922932092910	2025-05-21 14:30:00	5300889507	Manager	0	f	t	PENDING
\.


--
-- Data for Name: companies_orders; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.companies_orders (company_id, orders_id) FROM stdin;
\.


--
-- Data for Name: companies_products; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.companies_products (company_id, products_id) FROM stdin;
\.


--
-- Data for Name: companies_users; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.companies_users (company_id, users_id) FROM stdin;
\.


--
-- Data for Name: company_address; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.company_address (company_id, city, street, postal_code, build_no, door_number, detail_address, country) FROM stdin;
011ade18-d066-405d-bd89-3c58c27ae603	Antalya	Kışla	07080	8A	4	Kışla Mah. 40 sok. Reis Apt. Muratpaşa/Antalya	Turkey
\.


--
-- Data for Name: company_bank_accounts; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.company_bank_accounts (company_id, iban, account_name, bank_name, is_active) FROM stdin;
011ade18-d066-405d-bd89-3c58c27ae603	472319288283728	Arzu AMBER	Ziraat Bank	t
\.


--
-- Data for Name: company_categories; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.company_categories (category_id, company_id) FROM stdin;
ddec3b65-a0ed-4286-a600-0c1a55a840a2	011ade18-d066-405d-bd89-3c58c27ae603
\.


--
-- Data for Name: company_document_paths; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.company_document_paths (company_id, document_title, document_path, verification_status) FROM stdin;
011ade18-d066-405d-bd89-3c58c27ae603	Ramazan_Dizman_CV	/company/documents/arzuamber_moda/bb59d04c-4777-4bae-a35b-b15c4c4858bf.pdf	0
\.


--
-- Data for Name: digital_fashion_category; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.digital_fashion_category (id, created_at, updated_at, name, image) FROM stdin;
\.


--
-- Data for Name: digital_fashion_sub_category; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.digital_fashion_sub_category (id, created_at, updated_at, name, image, digital_fashion_category_id) FROM stdin;
\.


--
-- Data for Name: fabrics; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.fabrics (id, created_at, updated_at, fabric_name, fabric_image, fabric_price, stock, color) FROM stdin;
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	init	SQL	V1__init.sql	-71500816	tekera_user	2025-06-18 14:52:04.596557	176	t
\.


--
-- Data for Name: guest; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.guest (id, created_at, updated_at, name, surname, email, gsm_number, shipping_city, shipping_street, shipping_postal_code, shipping_build_no, shipping_door_number, shipping_detail_address, shipping_country, billing_city, billing_street, billing_postal_code, billing_build_no, billing_door_number, billing_detail_address, billing_country) FROM stdin;
\.


--
-- Data for Name: guest_orders; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.guest_orders (guest_id, orders_id) FROM stdin;
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.orders (id, created_at, updated_at, user_id, guest_id, company_id, payment_type) FROM stdin;
\.


--
-- Data for Name: orders_basket_items; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.orders_basket_items (order_id, basket_items_id) FROM stdin;
\.


--
-- Data for Name: product_attributes; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.product_attributes (product_id, key, value) FROM stdin;
\.


--
-- Data for Name: product_subcategories; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.product_subcategories (product_fk, subcategory_id) FROM stdin;
\.


--
-- Data for Name: product_tags; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.product_tags (product_fk, tags) FROM stdin;
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.products (id, created_at, updated_at, name, slug, code, brand_name, description, category_id, currency_type, company_id, product_type, rate, is_active) FROM stdin;
\.


--
-- Data for Name: products_comments; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.products_comments (product_id, comments_id) FROM stdin;
\.


--
-- Data for Name: sub_category; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.sub_category (id, created_at, updated_at, name, image, category_id) FROM stdin;
cf07527f-a94f-4e05-91ae-61c2fb3273c0	2025-06-18 14:59:43.41196	2025-06-18 14:59:43.412004	Kadın T-Shirt	sub-category/8d1873fc-fc13-4e50-8a68-320ec94b3e2e.webp	ddec3b65-a0ed-4286-a600-0c1a55a840a2
\.


--
-- Data for Name: target_pictures; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.target_pictures (id, created_at, updated_at, product_id, target_pic, mind_path, default_content, special_content) FROM stdin;
\.


--
-- Data for Name: user_fav_products; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.user_fav_products (user_id, fav_products) FROM stdin;
\.


--
-- Data for Name: user_permissions; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.user_permissions (user_id, permissions) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.user_roles (user_id, roles) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.users (id, created_at, updated_at, first_name, last_name, email, hashed_password, gender, company_id, phone_number, address, birth_date, last_login, is_active) FROM stdin;
\.


--
-- Data for Name: users_orders; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.users_orders (user_id, orders_id) FROM stdin;
\.


--
-- Data for Name: variation_images; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.variation_images (variation_id, images) FROM stdin;
\.


--
-- Data for Name: variations; Type: TABLE DATA; Schema: public; Owner: tekera_user
--

COPY public.variations (id, created_at, updated_at, model_name, model_code, price, discount_price, sku, barcode, product_id) FROM stdin;
\.


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: attributes pk_attrıbutes; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.attributes
    ADD CONSTRAINT "pk_attrıbutes" PRIMARY KEY (id);


--
-- Name: basket_item pk_basketıtem; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.basket_item
    ADD CONSTRAINT "pk_basketıtem" PRIMARY KEY (id);


--
-- Name: category pk_category; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT pk_category PRIMARY KEY (id);


--
-- Name: comment pk_comment; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT pk_comment PRIMARY KEY (id);


--
-- Name: company_categories pk_company_categorıes; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.company_categories
    ADD CONSTRAINT "pk_company_categorıes" PRIMARY KEY (category_id, company_id);


--
-- Name: companies pk_companıes; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies
    ADD CONSTRAINT "pk_companıes" PRIMARY KEY (id);


--
-- Name: companies_users pk_companıes_users; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_users
    ADD CONSTRAINT "pk_companıes_users" PRIMARY KEY (company_id, users_id);


--
-- Name: digital_fashion_category pk_dıgıtalfashıoncategory; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.digital_fashion_category
    ADD CONSTRAINT "pk_dıgıtalfashıoncategory" PRIMARY KEY (id);


--
-- Name: digital_fashion_sub_category pk_dıgıtalfashıonsubcategory; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.digital_fashion_sub_category
    ADD CONSTRAINT "pk_dıgıtalfashıonsubcategory" PRIMARY KEY (id);


--
-- Name: fabrics pk_fabrıcs; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.fabrics
    ADD CONSTRAINT "pk_fabrıcs" PRIMARY KEY (id);


--
-- Name: guest pk_guest; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.guest
    ADD CONSTRAINT pk_guest PRIMARY KEY (id);


--
-- Name: orders pk_orders; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT pk_orders PRIMARY KEY (id);


--
-- Name: product_subcategories pk_product_subcategorıes; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.product_subcategories
    ADD CONSTRAINT "pk_product_subcategorıes" PRIMARY KEY (product_fk, subcategory_id);


--
-- Name: products pk_products; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT pk_products PRIMARY KEY (id);


--
-- Name: sub_category pk_subcategory; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.sub_category
    ADD CONSTRAINT pk_subcategory PRIMARY KEY (id);


--
-- Name: target_pictures pk_target_pıctures; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.target_pictures
    ADD CONSTRAINT "pk_target_pıctures" PRIMARY KEY (id);


--
-- Name: users pk_users; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);


--
-- Name: variations pk_varıatıons; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.variations
    ADD CONSTRAINT "pk_varıatıons" PRIMARY KEY (id);


--
-- Name: users uc_74165e195b2f7b25de690d14a; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);


--
-- Name: companies_orders uc_companıes_orders_orders; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_orders
    ADD CONSTRAINT "uc_companıes_orders_orders" UNIQUE (orders_id);


--
-- Name: companies_products uc_companıes_products_products; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_products
    ADD CONSTRAINT "uc_companıes_products_products" UNIQUE (products_id);


--
-- Name: companies_users uc_companıes_users_users; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_users
    ADD CONSTRAINT "uc_companıes_users_users" UNIQUE (users_id);


--
-- Name: guest_orders uc_guest_orders_orders; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.guest_orders
    ADD CONSTRAINT uc_guest_orders_orders UNIQUE (orders_id);


--
-- Name: orders_basket_items uc_orders_basket_ıtems_basketıtems; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders_basket_items
    ADD CONSTRAINT "uc_orders_basket_ıtems_basketıtems" UNIQUE (basket_items_id);


--
-- Name: products_comments uc_products_comments_comments; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.products_comments
    ADD CONSTRAINT uc_products_comments_comments UNIQUE (comments_id);


--
-- Name: users_orders uc_users_orders_orders; Type: CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.users_orders
    ADD CONSTRAINT uc_users_orders_orders UNIQUE (orders_id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: tekera_user
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: attributes fk_attributes_on_variation; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.attributes
    ADD CONSTRAINT fk_attributes_on_variation FOREIGN KEY (variation_id) REFERENCES public.variations(id);


--
-- Name: attributes_stock_attributes fk_attrıbutes_stockattrıbutes_on_attrıbute; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.attributes_stock_attributes
    ADD CONSTRAINT "fk_attrıbutes_stockattrıbutes_on_attrıbute" FOREIGN KEY (attribute_id) REFERENCES public.attributes(id);


--
-- Name: basket_item_attributes fk_basket_ıtem_attrıbutes_on_basket_ıtem; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.basket_item_attributes
    ADD CONSTRAINT "fk_basket_ıtem_attrıbutes_on_basket_ıtem" FOREIGN KEY (basket_item_id) REFERENCES public.basket_item(id);


--
-- Name: company_categories fk_comcat_on_category; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.company_categories
    ADD CONSTRAINT fk_comcat_on_category FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: company_categories fk_comcat_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.company_categories
    ADD CONSTRAINT fk_comcat_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: companies_orders fk_comord_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_orders
    ADD CONSTRAINT fk_comord_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: companies_orders fk_comord_on_order; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_orders
    ADD CONSTRAINT fk_comord_on_order FOREIGN KEY (orders_id) REFERENCES public.orders(id);


--
-- Name: company_address fk_company_address_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.company_address
    ADD CONSTRAINT fk_company_address_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: company_bank_accounts fk_company_bank_accounts_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.company_bank_accounts
    ADD CONSTRAINT fk_company_bank_accounts_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: company_document_paths fk_company_document_paths_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.company_document_paths
    ADD CONSTRAINT fk_company_document_paths_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: companies_products fk_compro_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_products
    ADD CONSTRAINT fk_compro_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: companies_products fk_compro_on_product; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_products
    ADD CONSTRAINT fk_compro_on_product FOREIGN KEY (products_id) REFERENCES public.products(id);


--
-- Name: companies_users fk_comuse_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_users
    ADD CONSTRAINT fk_comuse_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: companies_users fk_comuse_on_user; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.companies_users
    ADD CONSTRAINT fk_comuse_on_user FOREIGN KEY (users_id) REFERENCES public.users(id);


--
-- Name: digital_fashion_sub_category fk_digitalfashionsubcategory_on_digital_fashion_category; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.digital_fashion_sub_category
    ADD CONSTRAINT fk_digitalfashionsubcategory_on_digital_fashion_category FOREIGN KEY (digital_fashion_category_id) REFERENCES public.digital_fashion_category(id);


--
-- Name: guest_orders fk_gueord_on_guest; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.guest_orders
    ADD CONSTRAINT fk_gueord_on_guest FOREIGN KEY (guest_id) REFERENCES public.guest(id);


--
-- Name: guest_orders fk_gueord_on_order; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.guest_orders
    ADD CONSTRAINT fk_gueord_on_order FOREIGN KEY (orders_id) REFERENCES public.orders(id);


--
-- Name: orders_basket_items fk_ordbasıte_on_basket_ıtem; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_basket_ıtem" FOREIGN KEY (basket_items_id) REFERENCES public.basket_item(id);


--
-- Name: orders_basket_items fk_ordbasıte_on_order; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_order" FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- Name: orders fk_orders_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_orders_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: orders fk_orders_on_guest; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_orders_on_guest FOREIGN KEY (guest_id) REFERENCES public.guest(id);


--
-- Name: orders fk_orders_on_user; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_orders_on_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: products_comments fk_procom_on_comment; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.products_comments
    ADD CONSTRAINT fk_procom_on_comment FOREIGN KEY (comments_id) REFERENCES public.comment(id);


--
-- Name: products_comments fk_procom_on_product; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.products_comments
    ADD CONSTRAINT fk_procom_on_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: product_attributes fk_product_attrıbutes_on_product; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.product_attributes
    ADD CONSTRAINT "fk_product_attrıbutes_on_product" FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: product_tags fk_product_tags_on_product; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.product_tags
    ADD CONSTRAINT fk_product_tags_on_product FOREIGN KEY (product_fk) REFERENCES public.products(id);


--
-- Name: products fk_products_on_category; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk_products_on_category FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: products fk_products_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk_products_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: product_subcategories fk_prosub_on_product; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.product_subcategories
    ADD CONSTRAINT fk_prosub_on_product FOREIGN KEY (product_fk) REFERENCES public.products(id);


--
-- Name: product_subcategories fk_prosub_on_sub_category; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.product_subcategories
    ADD CONSTRAINT fk_prosub_on_sub_category FOREIGN KEY (subcategory_id) REFERENCES public.sub_category(id);


--
-- Name: sub_category fk_subcategory_on_category; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.sub_category
    ADD CONSTRAINT fk_subcategory_on_category FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: users_orders fk_useord_on_order; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.users_orders
    ADD CONSTRAINT fk_useord_on_order FOREIGN KEY (orders_id) REFERENCES public.orders(id);


--
-- Name: users_orders fk_useord_on_user; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.users_orders
    ADD CONSTRAINT fk_useord_on_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_fav_products fk_user_fav_products_on_user; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.user_fav_products
    ADD CONSTRAINT fk_user_fav_products_on_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_permissions fk_user_permıssıons_on_user; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.user_permissions
    ADD CONSTRAINT "fk_user_permıssıons_on_user" FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_roles fk_user_roles_on_user; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: users fk_users_on_company; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk_users_on_company FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- Name: variations fk_variations_on_product; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.variations
    ADD CONSTRAINT fk_variations_on_product FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: variation_images fk_varıatıon_ımages_on_varıatıon; Type: FK CONSTRAINT; Schema: public; Owner: tekera_user
--

ALTER TABLE ONLY public.variation_images
    ADD CONSTRAINT "fk_varıatıon_ımages_on_varıatıon" FOREIGN KEY (variation_id) REFERENCES public.variations(id);


--
-- PostgreSQL database dump complete
--

