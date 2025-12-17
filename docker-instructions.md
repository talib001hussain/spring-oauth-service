# Running the Authentication Service with Docker

This guide provides step-by-step instructions for running the Authentication Service using Docker.

## Prerequisites

Before you begin, make sure you have the following installed on your system:

- Docker: [Install Docker](https://docs.docker.com/get-docker/)
- Docker Compose: [Install Docker Compose](https://docs.docker.com/compose/install/)

## Steps to Run the Application

### 1. Clone the Repository (if you haven't already)

```bash
git clone https://github.com/talib001hussain/spring-oauth-service
cd spring-oauth-service
```

### 2. Build the Docker Image

Build the Docker image for the Authentication Service:

```bash
docker build -t auth-service .
```

This command builds a Docker image named `auth-service` using the Dockerfile in the current directory.

> **Note for Windows Users**: The Dockerfile includes a step to set the executable permission on the Maven wrapper script (`chmod +x mvnw`), which is necessary when building on Windows systems. This ensures the build process works correctly across different operating systems.

### 3. Run with Docker Compose

Start the application and the PostgreSQL database using Docker Compose:

```bash
docker-compose up -d
```

The `-d` flag runs the containers in detached mode (in the background).

This command:
- Starts a PostgreSQL container
- Initializes the database using the script at `src/main/resources/db/docker_init.sql`
- Builds and starts the Authentication Service container
- Links the two containers together

### 4. Verify the Application is Running

Check that both containers are running:

```bash
docker ps
```

You should see two containers running: `postgres` and `auth-service`.

### 5. Access the Application

The application is now available at:

```
http://localhost:8080
```

You can test the API endpoints using cURL or a tool like Postman:

- Sign Up: `POST http://localhost:8080/api/auth/signup`
- Sign In: `POST http://localhost:8080/api/auth/signin`
- Get User Profile: `GET http://localhost:8080/api/user/profile`

See the [curl-commands.md](curl-commands.md) file for detailed examples.

### 6. Stop the Application

To stop the running containers:

```bash
docker-compose down
```

To stop the containers and remove the volumes (this will delete the database data):

```bash
docker-compose down -v
```

## Troubleshooting

### Windows-Specific Issues

When running Docker on Windows, you might encounter some specific issues:

1. **Line Ending Issues**: If you get errors related to line endings (CR/LF vs LF), you can configure Git to handle line endings correctly:
   ```bash
   git config --global core.autocrlf input
   ```
   Then clone the repository again.

2. **Permission Denied Errors**: If you still get "permission denied" errors despite the `chmod +x mvnw` command in the Dockerfile, you can try running the following command in PowerShell before building:
   ```powershell
   git update-index --chmod=+x mvnw
   ```

3. **Docker Desktop Settings**: Make sure Docker Desktop has enough resources allocated (CPU, memory) in the settings.

### Database Connection Issues

If the application cannot connect to the database, check that:

1. The PostgreSQL container is running: `docker ps`
2. The database initialization script was executed correctly: `docker logs postgres`
3. The application container can reach the database container: `docker logs auth-service`

### Port Conflicts

If you see an error like "port is already allocated", you may have another service using port 8080 or 5432. You can modify the port mappings in the `docker-compose.yml` file:

```yaml
ports:
  - "8081:8080"  # Map container port 8080 to host port 8081
```

### Container Logs

To view the logs of a container:

```bash
docker logs auth-service  # View application logs
docker logs postgres      # View database logs
```

Add the `-f` flag to follow the logs in real-time:

```bash
docker logs -f auth-service
```
