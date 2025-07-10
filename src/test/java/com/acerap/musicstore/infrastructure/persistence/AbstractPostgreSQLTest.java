package com.acerap.musicstore.infrastructure.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractPostgreSQLTest {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    protected JdbcTemplate jdbcTemplate;
    protected DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = DataSourceBuilder.create()
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();

        jdbcTemplate = new JdbcTemplate(dataSource);

        // Crear esquema limpio para cada test
        createSchema();
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    private void createSchema() {
        // Ejecutar DDL para crear tablas
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS artists (
                id UUID PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                country VARCHAR(100),
                genre VARCHAR(50),
                members TEXT[]
            )
            """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS record_companies (
                name VARCHAR(255) PRIMARY KEY,
                street VARCHAR(255),
                number VARCHAR(50),
                postal_code VARCHAR(20),
                country VARCHAR(100),
                headquarters_country VARCHAR(100)
            )
            """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS compact_discs (
                global_id UUID PRIMARY KEY,
                store_code VARCHAR(50) UNIQUE,
                title VARCHAR(255) NOT NULL,
                price_amount DECIMAL(10,2),
                price_currency VARCHAR(3),
                artist_id UUID REFERENCES artists(id) ON DELETE CASCADE,
                production_year INTEGER,
                record_company_name VARCHAR(255) REFERENCES record_companies(name) ON DELETE CASCADE,
                stock INTEGER DEFAULT 0
            )
            """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS tracks (
                disc_id UUID REFERENCES compact_discs(global_id) ON DELETE CASCADE,
                position INTEGER,
                title VARCHAR(255) NOT NULL,
                duration_minutes INTEGER,
                duration_seconds INTEGER,
                PRIMARY KEY (disc_id, position)
            )
            """);
    }

    @AfterEach
    void tearDown() {
        // Limpiar datos pero mantener la estructura
        jdbcTemplate.execute("TRUNCATE TABLE tracks, compact_discs, artists, record_companies CASCADE");
    }
}
