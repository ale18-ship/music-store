-- Artistas
INSERT INTO artists (id, name, country, genre, members) VALUES
('11111111-1111-1111-1111-111111111111', 'The Beatles', 'UK', 'ROCK', ARRAY['John Lennon', 'Paul McCartney', 'George Harrison', 'Ringo Starr']);

-- Compañías discográficas
INSERT INTO record_companies (name, street, number, postal_code, country, headquarters_country) VALUES
('EMI', 'Abbey Road', '3', 'NW8 9AY', 'UK', 'UK');

-- CDs
INSERT INTO compact_discs (global_id, store_code, title, price_amount, price_currency, artist_id, production_year, record_company_name, stock) VALUES
('22222222-2222-2222-2222-222222222222', 'CD-001', 'Abbey Road', 25.99, 'USD', '11111111-1111-1111-1111-111111111111', 1969, 'EMI', 10);

-- Pistas
INSERT INTO tracks (disc_id, position, title, duration_minutes, duration_seconds) VALUES
('22222222-2222-2222-2222-222222222222', 1, 'Come Together', 4, 20),
('22222222-2222-2222-2222-222222222222', 2, 'Something', 3, 3);