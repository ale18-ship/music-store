CREATE TABLE artists (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    members TEXT[] -- Para grupos musicales
);

CREATE TABLE record_companies (
    name VARCHAR(255) PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(50) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    headquarters_country VARCHAR(100) NOT NULL
);

CREATE TABLE compact_discs (
    global_id UUID PRIMARY KEY,
    store_code VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    price_amount DECIMAL(10, 2) NOT NULL,
    price_currency VARCHAR(3) NOT NULL,
    artist_id UUID REFERENCES artists(id),
    production_year INTEGER NOT NULL,
    record_company_name VARCHAR(255) REFERENCES record_companies(name),
    stock INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE tracks (
    disc_id UUID REFERENCES compact_discs(global_id),
    position INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    duration_seconds INTEGER NOT NULL,
    PRIMARY KEY (disc_id, position)
);

CREATE TABLE collections (
    global_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price_amount DECIMAL(10, 2) NOT NULL,
    price_currency VARCHAR(3) NOT NULL,
    promoter VARCHAR(255)
);

CREATE TABLE collection_discs (
    collection_id UUID REFERENCES collections(global_id),
    disc_id UUID REFERENCES compact_discs(global_id),
    PRIMARY KEY (collection_id, disc_id)
);