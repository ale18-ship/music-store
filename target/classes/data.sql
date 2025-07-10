-- Limpiar tablas (opcional, solo si necesitas reiniciar)
DELETE FROM collection_discs;
DELETE FROM tracks;
DELETE FROM compact_discs;
DELETE FROM collections;
DELETE FROM artists;
DELETE FROM record_companies;

-- Compañías discográficas
INSERT INTO record_companies (name, street, number, postal_code, country, headquarters_country)
VALUES
    ('Sony Music', 'Calle de la Música', '123', '28001', 'España', 'Estados Unidos'),
    ('Universal Music', 'Avenida del Arte', '456', '08001', 'España', 'Países Bajos'),
    ('Warner Music', 'Plaza del Sonido', '789', '41001', 'España', 'Estados Unidos');

-- Insertar datos con UUIDs correctamente formateados
INSERT INTO artists (id, name, country, genre, members)
VALUES
    (CAST('11111111-1111-1111-1111-111111111111' AS UUID), 'Coldplay', 'Reino Unido', 'ROCK', ARRAY['Chris Martin', 'Jonny Buckland', 'Guy Berryman', 'Will Champion']),
    (CAST('22222222-2222-2222-2222-222222222222' AS UUID), 'Rosalía', 'España', 'FLAMENCO', ARRAY['Rosalía Vila']),
    (CAST('33333333-3333-3333-3333-333333333333' AS UUID), 'Metallica', 'Estados Unidos', 'ROCK', ARRAY['James Hetfield', 'Lars Ulrich', 'Kirk Hammett', 'Robert Trujillo']),
    (CAST('44444444-4444-4444-4444-444444444444' AS UUID), 'Taylor Swift', 'Estados Unidos', 'POP', ARRAY['Taylor Swift']);

-- Insertar en compact_discs con UUIDs correctos
INSERT INTO compact_discs (global_id, store_code, title, price_amount, price_currency, artist_id, production_year, record_company_name, stock)
VALUES
    (CAST('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' AS UUID), 'CD001', 'A Rush of Blood to the Head', 15.99, 'EUR', CAST('11111111-1111-1111-1111-111111111111' AS UUID), 2002, 'Sony Music', 50),
    (CAST('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' AS UUID), 'CD002', 'El Mal Querer', 12.99, 'EUR', CAST('22222222-2222-2222-2222-222222222222' AS UUID), 2018, 'Sony Music', 30),
    (CAST('cccccccc-cccc-cccc-cccc-cccccccccccc' AS UUID), 'CD003', 'Master of Puppets', 18.99, 'EUR', CAST('33333333-3333-3333-3333-333333333333' AS UUID), 1986, 'Universal Music', 20),
    (CAST('dddddddd-dddd-dddd-dddd-dddddddddddd' AS UUID), 'CD004', '1989', 14.99, 'EUR', CAST('44444444-4444-4444-4444-444444444444' AS UUID), 2014, 'Universal Music', 40),
    (CAST('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee' AS UUID), 'CD005', 'Parachutes', 13.99, 'EUR', CAST('11111111-1111-1111-1111-111111111111' AS UUID), 2000, 'Sony Music', 10);

-- Pistas (tracks) para los CDs
INSERT INTO tracks (disc_id, position, title, duration_minutes, duration_seconds)
VALUES
    -- Coldplay - A Rush of Blood to the Head
    (CAST('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' AS UUID), 1, 'Politik', 5, 18),
    (CAST('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' AS UUID), 2, 'In My Place', 3, 48),
    (CAST('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' AS UUID), 3, 'God Put a Smile upon Your Face', 4, 57),

    -- Rosalía - El Mal Querer
    (CAST('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' AS UUID), 1, 'Malamente', 2, 30),
    (CAST('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' AS UUID), 2, 'Que no salga la luna', 2, 48),
    (CAST('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' AS UUID), 3, 'Pienso en tu mirá', 3, 13),

    -- Metallica - Master of Puppets
    (CAST('cccccccc-cccc-cccc-cccc-cccccccccccc' AS UUID), 1, 'Battery', 5, 12),
    (CAST('cccccccc-cccc-cccc-cccc-cccccccccccc' AS UUID), 2, 'Master of Puppets', 8, 35),
    (CAST('cccccccc-cccc-cccc-cccc-cccccccccccc' AS UUID), 3, 'The Thing That Should Not Be', 6, 36),

    -- Taylor Swift - 1989
    (CAST('dddddddd-dddd-dddd-dddd-dddddddddddd' AS UUID), 1, 'Welcome to New York', 3, 32),
    (CAST('dddddddd-dddd-dddd-dddd-dddddddddddd' AS UUID), 2, 'Blank Space', 3, 51),
    (CAST('dddddddd-dddd-dddd-dddd-dddddddddddd' AS UUID), 3, 'Style', 3, 51),

    -- Coldplay - Parachutes
    (CAST('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee' AS UUID), 1, 'Don''t Panic', 2, 17),
    (CAST('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee' AS UUID), 2, 'Shiver', 5, 0),
    (CAST('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee' AS UUID), 3, 'Spies', 5, 18);

-- Colecciones
INSERT INTO collections (global_id, name, price_amount, price_currency, promoter)
VALUES
    (CAST('ffffffff-ffff-ffff-ffff-ffffffffffff' AS UUID), 'Lo Mejor del Rock', 45.99, 'EUR', 'Tienda de Música XYZ'),
    (CAST('gggggggg-gggg-gggg-gggg-gggggggggggg' AS UUID), 'Éxitos Internacionales', 39.99, 'EUR', 'Promociones Musicales');

-- Relación CDs-Colecciones
INSERT INTO collection_discs (collection_id, disc_id)
VALUES
    -- Lo Mejor del Rock
    (CAST('ffffffff-ffff-ffff-ffff-ffffffffffff' AS UUID), CAST('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' AS UUID)), -- Coldplay
    (CAST('ffffffff-ffff-ffff-ffff-ffffffffffff' AS UUID), CAST('cccccccc-cccc-cccc-cccc-cccccccccccc' AS UUID)), -- Metallica

    -- Éxitos Internacionales
    (CAST('gggggggg-gggg-gggg-gggg-gggggggggggg' AS UUID), CAST('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' AS UUID)), -- Rosalía
    (CAST('gggggggg-gggg-gggg-gggg-gggggggggggg' AS UUID), CAST('dddddddd-dddd-dddd-dddd-dddddddddddd' AS UUID)); -- Taylor Swift