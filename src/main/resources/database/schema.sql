-- Schema for technical test H2 database

-- Table: cities

CREATE SEQUENCE cities_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

CREATE TABLE cities (
    id BIGINT PRIMARY KEY DEFAULT nextval('cities_id_seq'),
    name character varying(140) NOT NULL,
    state character varying(140) NOT NULL,
    CONSTRAINT uc_cities_name_state UNIQUE (name, state)
);
ALTER SEQUENCE cities_id_seq OWNED BY cities.id;