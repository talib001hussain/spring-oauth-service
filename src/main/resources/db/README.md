# Database Scripts

This directory contains SQL scripts for initializing the database for the Authentication Service.

## Scripts

1. **init.sql**
   - A general initialization script that creates the database, tables, and inserts test data.
   - Contains `CREATE DATABASE` and `\c` commands which are PostgreSQL-specific.
   - Should be executed manually using a database client or command line.

2. **schema.sql**
   - Contains only the table creation statements.
   - Uses `IF NOT EXISTS` clauses to ensure idempotence.
   - Automatically executed by Spring Boot during startup if configured in `application.yaml`.

3. **data.sql**
   - Contains only the data insertion statements.
   - Uses `ON CONFLICT DO NOTHING` clauses to ensure idempotence.
   - Automatically executed by Spring Boot during startup if configured in `application.yaml`.

4. **postgres_init.sql**
   - A PostgreSQL-specific initialization script.
   - Contains `CREATE DATABASE` and `\c` commands.
   - Should be executed manually using the psql command line tool.

5. **docker_init.sql**
   - A Docker-specific initialization script.
   - Uses `IF NOT EXISTS` and `ON CONFLICT DO NOTHING` clauses to ensure idempotence.
   - Automatically executed when the PostgreSQL container starts in Docker Compose.

## Usage

### Local Development

For local development, you can use the following approaches:

1. **Manual Execution**:
   ```bash
   psql -U postgres -f postgres_init.sql
   ```

2. **Spring Boot Auto-Execution**:
   - Create the database manually:
     ```sql
     CREATE DATABASE authdb;
     ```
   - Let Spring Boot execute schema.sql and data.sql during startup.

### Docker

When using Docker Compose, the docker_init.sql script is automatically executed when the PostgreSQL container starts.

## Database Schema

The scripts create the following tables:

1. **users**
   - `id` (UUID): Primary key
   - `email` (VARCHAR): Unique email address
   - `password` (VARCHAR): Encrypted password

2. **user_profiles**
   - `user_id` (UUID): Primary key, foreign key to users.id
   - `full_name` (VARCHAR): User's full name
   - `height` (DOUBLE PRECISION): User's height in cm
   - `bmi` (DOUBLE PRECISION): User's BMI