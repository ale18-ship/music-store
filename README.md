music-store
Documentación del Sistema de Gestión de Tienda de Música
Tabla de Contenidos
1. Requisitos

2. Configuración Inicial

3. Ejecución del Proyecto

4. API Endpoints

5. Ejemplo de Uso

6. Estructura del Proyecto

7. Troubleshooting
Requisitos
Java 17

PostgreSQL 13+

Maven 3.8+

4GB RAM mínimo

2GB de espacio en disco
--Configuración Inicial

Instalación de PostgreSQL
# Instalar PostgreSQL en macOS (usando Homebrew)
brew install postgresql@15

--Iniciar el servicio
brew services start postgresql@15

--Crear usuario y base de datos
psql -U postgres -c "CREATE DATABASE music_store_db;"
psql -U postgres -c "CREATE USER user_music_store WITH PASSWORD 'password_music_store';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE music_store_db TO user_music_store;"
Configuración de la Base de Datos
