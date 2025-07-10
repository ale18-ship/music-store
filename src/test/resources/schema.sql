CREATE TABLE artists (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    genre VARCHAR(50),
    members TEXT[]
);

CREATE TABLE record_companies (
    name VARCHAR(255) PRIMARY KEY,
    street VARCHAR(255),
    number VARCHAR(50),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    headquarters_country VARCHAR(100)
);

CREATE TABLE compact_discs (
    global_id UUID PRIMARY KEY,
    store_code VARCHAR(50) UNIQUE,
    title VARCHAR(255) NOT NULL,
    price_amount DECIMAL(10,2),
    price_currency VARCHAR(3),
    artist_id UUID REFERENCES artists(id),
    production_year INTEGER,
    record_company_name VARCHAR(255) REFERENCES record_companies(name),
    stock INTEGER DEFAULT 0
);

CREATE TABLE tracks (
    disc_id UUID REFERENCES compact_discs(global_id),
    position INTEGER,
    title VARCHAR(255) NOT NULL,
    duration_minutes INTEGER,
    duration_seconds INTEGER,
    PRIMARY KEY (disc_id, position)
);

CREATE TABLE collections (
    global_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price_amount DECIMAL(10,2),
    price_currency VARCHAR(3),
    promoter VARCHAR(255)
);

CREATE TABLE collection_discs (
    collection_id UUID REFERENCES collections(global_id),
    disc_id UUID REFERENCES compact_discs(global_id),
    PRIMARY KEY (collection_id, disc_id)
);