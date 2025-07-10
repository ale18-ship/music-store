# music-store



# music-store


## Documentación del Sistema de Gestión de Tienda de Música





### Tabla de Contenidos
    1. Requisitos
    2. Configuración Inicial
    3. Ejecución del Proyecto
    4. API Endpoints
    5. Ejemplo de Uso
    6. Estructura del Proyecto
    7. Troubleshooting


### Requisitos
    Java 17
    PostgreSQL 13+
    Maven 3.8+
    4GB RAM mínimo
    2GB de espacio en disco

    
--Configuración Inicial
1. Instalación de PostgreSQL
```bash
# Instalar PostgreSQL en Windows (usando Homebrew)
brew install postgresql@15
--Iniciar el servicio
brew services start postgresql@15
--Crear usuario y base de datos
psql -U postgres -c "CREATE DATABASE music_store_db;"
psql -U postgres -c "CREATE USER user_music_store WITH PASSWORD 'password_music_store';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE music_store_db TO user_music_store;"
```

2. Configuración de la Base de Datos
Ejecutar el script SQL inicial:
```bash
psql -U user_music_store -d music_store_db -f src/main/resources/schema.sql
psql -U user_music_store -d music_store_db -f src/main/resources/data.sql
```

3. Configuración de la Aplicación
Editar src/main/resources/application.properties:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/music_store_db
spring.datasource.username=user_music_store
spring.datasource.password=password_music_store
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=never
```

### Ejecución del Proyecto
```bash
# Compilar y ejecutar
mvn clean install
mvn spring-boot:run
# Alternativamente desde tu IDE:
# Ejecutar la clase MusicStoreApplication
```

### API Endpoints
Discos Compactos (CDs)
    POST /api/discs - Crear un nuevo disco
    GET /api/discs/{id} - Obtener disco por ID
    GET /api/discs?genre=ROCK - Buscar discos por género
    PUT /api/discs/{id}/stock - Actualizar stock

Artistas
    GET /api/artists - Listar todos los artistas
    GET /api/artists/{id} - Obtener artista por ID

Colecciones
    POST /api/collections - Crear nueva colección
    GET /api/collections/{id} - Obtener colección por ID

Ejemplo de Uso
Creación de un Disco (POST /api/discs)
Request:
```json
curl -X POST "http://localhost:8080/api/discs" \
-H "Content-Type: application/json" \
-d 
'{
  "storeCode": "CD006",
  "title": "Disco de prueba",
  "artist": {
    "name": "Coldplay"
  },
  "price": {
    "amount": 15.99,
    "currency": "EUR"
  },
  "productionYear": 2002,
  "recordCompany": {
    "name": "Sony Music"
  },
  "tracks": [
    {
      "position": 1,
      "title": "Politik PRUEBA",
      "duration": {
        "minutes": 5,
        "seconds": 18
      }
    },
    {
      "position": 2,
      "title": "In My Place PRUEBA",
      "duration": {
        "minutes": 5,
        "seconds": 18
      }
    }
  ],
  "stock": 50
}'
```

Response (201 Created):
```json
{
    "globalId": "dcb150e6-016c-4eb8-84ed-f6949fa4332a",
    "storeCode": "CD006",
    "title": "Disco de prueba",
    "price": {
        "amount": 15.99,
        "currency": "EUR"
    },
    "artist": {
        "id": "11111111-1111-1111-1111-111111111111",
        "name": "Coldplay",
        "country": "Reino Unido",
        "genre": "ROCK",
        "members": [
            "Chris Martin",
            "Jonny Buckland",
            "Guy Berryman",
            "Will Champion"
        ]
    },
    "productionYear": 2002,
    "recordCompany": {
        "name": "Sony Music",
        "address": {
            "street": "Calle de la Música",
            "number": "123",
            "postalCode": "28001",
            "country": "España"
        },
        "headquartersCountry": "Estados Unidos"
    },
    "tracks": [
        {
            "position": 1,
            "title": "Politik PRUEBA",
            "duration": "5:18"
        },
        {
            "position": 2,
            "title": "In My Place PRUEBA",
            "duration": "5:18"
        }
    ],
    "stock": 50
}
```


### Estructura del Proyecto
```text
music-store/
├── src/
│   ├── main/
│   │   ├── java/coma/acerap/musicstore/
│   │   │   ├── application/          # Capa de aplicación
│   │   │   │   ├── dto/              # DTOs
│   │   │   │   │   ├── CompactDiscDTO.java
│   │   │   │   │   ├── TrackDTO.java
│   │   │   │   │   └── CollectionDTO.java
│   │   │   │   ├── service/          # Servicios de aplicación
│   │   │   │   │   ├── CompactDiscServiceImpl.java
│   │   │   │   │   ├── CollectionServiceImpl.java
│   │   │   │   │   └── InventoryAlertServiceImpl.java
│   │   │   │   └── web/              # Controladores REST
│   │   │   │       ├── CompactDiscController.java
│   │   │   │       ├── CollectionController.java
│   │   │   │       └── ArtistController.java
│   │   │   ├── domain/               # Dominio (core)
│   │   │   │   ├── model/            # Entidades
│   │   │   │   │   ├── Artist.java
│   │   │   │   │   ├── CompactDisc.java
│   │   │   │   │   ├── Collection.java
│   │   │   │   │   ├── RecordCompany.java
│   │   │   │   │   └── Track.java
│   │   │   │   ├── valueobjects/     # Value Objects
│   │   │   │   │   ├── Address.java
│   │   │   │   │   ├── Duration.java
│   │   │   │   │   └── Money.java
│   │   │   │   ├── enums/            # Enums
│   │   │   │   │   └── MusicGenre.java
│   │   │   │   ├── ports/            # Puertos (interfaces)
│   │   │   │   │   ├── in/           # Puertos de entrada
│   │   │   │   │   │   ├── CompactDiscService.java
│   │   │   │   │   │   ├── CollectionService.java
│   │   │   │   │   │   └── InventoryAlertService.java
│   │   │   │   │   └── out/          # Puertos de salida
│   │   │   │   │       ├── CompactDiscRepository.java
│   │   │   │   │       ├── CollectionRepository.java
│   │   │   │   │       └── ArtistRepository.java
│   │   │   │   └── exception/        # Excepciones de dominio
│   │   │   │       └── DomainException.java
│   │   │   ├── infrastructure/       # Adaptadores
│   │   │   │   ├── persistence/      # Adaptadores de persistencia
│   │   │   │   │   ├── JdbcCompactDiscRepository.java
│   │   │   │   │   ├── JdbcCollectionRepository.java
│   │   │   │   │   └── JdbcArtistRepository.java
│   │   │   │   └── config/           # Configuraciones
│   │   │   │       └── DatabaseConfig.java
│   │   │   └── MusicStoreApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── schema.sql            # DDL para PostgreSQL
│   │       └── data.sql              # Datos iniciales
│   └── test/                         # Pruebas
│       ├── java/com/musicstore/
│       │   ├── application/
│       │   │   ├── service/
│       │   │   │   ├── CompactDiscServiceTest.java
│       │   │   │   └── CollectionServiceTest.java
│       │   │   └── web/
│       │   │       ├── CompactDiscControllerTest.java
│       │   │       └── CollectionControllerTest.java
│       │   └── infrastructure/
│       │       └── persistence/
│       │           ├── JdbcCompactDiscRepositoryTest.java
│       │           └── JdbcCollectionRepositoryTest.java
│       └── resources/
│           └── test-data.sql         # Datos para pruebas
├── pom.xml
```


## Troubleshooting
### Problemas comunes y soluciones:
  #### 1. Error de conexión a PostgreSQL:
      Verificar credenciales en application.properties
      Asegurar que el servicio de PostgreSQL esté corriendo

  #### 2. Tablas no se crean:
      Verificar que schema.sql esté en src/main/resources
      Ejecutar manualmente: psql -U user_music_store -d music_store_db -f src/main/resources/schema.sql

  #### 3. Error en deserialización JSON:
      Asegurar que el formato del JSON coincida con los ejemplos
      Verificar que todos los campos requeridos estén presentes

  #### 4. Problemas con UUIDs:
      Usar el formato correcto: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
      No incluir comillas en los valores UUID en los JSONs

Para más información, consultar los logs de la aplicación o abrir un issue en el repositorio del proyecto.



## Documentación de Pruebas Unitarias
### Estructura de Pruebas
El proyecto incluye pruebas unitarias para los repositorios JDBC que validan la interacción con la base de datos PostgreSQL usando Testcontainers. Las pruebas se organizan en tres clases principales:

1. JdbcArtistRepositoryTest
Pruebas para el repositorio de artistas musicales.

Casos de prueba:
    - save_shouldInsertNewArtist: Verifica la creación correcta de un nuevo artista
    - findById_shouldReturnArtist: Valida la búsqueda por ID
    - findByName_shouldReturnArtist: Prueba la búsqueda por nombre exacto
    - save_shouldUpdateExistingArtist: Comprueba la actualización de artistas existentes

Ejemplo de uso:
```java
@Test
void save_shouldInsertNewArtist() {
    Artist artist = new Artist();
    artist.setName("Unique Artist");
    artist.setCountry("UK");
    artist.setGenre(MusicGenre.POP);
    artist.setMembers(List.of("Member1"));
    
    Artist saved = artistRepository.save(artist);
    assertNotNull(saved.getId());
}
```

2. JdbcCompactDiscRepositoryTest

Pruebas para el repositorio de discos compactos.
Casos de prueba:
    save_shouldInsertNewDisc: Valida la creación de CDs con sus pistas
    findById_shouldReturnDiscWithTracks: Verifica la recuperación completa con relaciones
    findByGenre_shouldFilterCorrectly: Prueba búsqueda por género musical
    updateStock_shouldModifyQuantity: Valida actualización de inventario
    existsByStoreCode_shouldFindExisting: Verifica códigos de tienda únicos

** Ejemplo de uso: **
```java
@Test
void save_shouldInsertNewDisc() {
    CompactDisc disc = new CompactDisc();
    disc.setStoreCode("CD-123");
    disc.setTitle("Album Test");
    disc.setTracks(List.of(
        new Track(1, "Song 1", new Duration(3, 30))
    );
    
    CompactDisc saved = discRepository.save(disc);
    assertEquals(1, saved.getTracks().size());
}
```

3. JdbcRecordCompanyRepositoryTest
   
Pruebas para el repositorio de compañías discográficas.
Casos de prueba:
    save_shouldInsertNewCompany: Valida creación de compañías
    findByName_shouldReturnCompany: Prueba búsqueda por nombre
    save_shouldUpdateExisting: Verifica actualización de datos
    shouldHandleSpecialCharacters: Valida manejo de caracteres especiales

** Ejemplo de uso: **
```java
@Test
void findByName_shouldReturnCompany() {
    RecordCompany company = new RecordCompany(
        "Sony Music",
        new Address("Main St", "100", "NY", "US"),
        "Japan"
    );
    repository.save(company);
    
    Optional<RecordCompany> found = repository.findByName("Sony Music");
    assertTrue(found.isPresent());
}
```

### Configuración Requerida
    Docker: Debe estar instalado y corriendo
    Perfil de prueba: Usa application-test.properties
    Contenedores:
        PostgreSQL 13
        Se inician automáticamente con Testcontainers

### Ejecución
Para ejecutar todas las pruebas:
```bash
mvn test
```

Para ejecutar pruebas específicas:
```bash
mvn test -Dtest=JdbcArtistRepositoryTest
```

### Estructura de archivos


```text
src/test/
├── java/
│   └── com/acerap/musicstore/infrastructure/persistence/
│       ├── JdbcArtistRepositoryTest.java
│       ├── JdbcCompactDiscRepositoryTest.java
│       └── JdbcRecordCompanyRepositoryTest.java
└── resources/
    ├── application-test.properties
    └── test-data.sql (opcional)
```

### Cobertura de Pruebas
Las pruebas validan:
    Operaciones CRUD básicas
    Relaciones entre entidades
    Restricciones de base de datos
    Consultas personalizadas
    Manejo de transacciones

Mejoras Futuras
    Añadir pruebas para condiciones de error
    Implementar pruebas de rendimiento
    Añadir validación de restricciones únicas
    Cubrir edge cases con datos boundary
