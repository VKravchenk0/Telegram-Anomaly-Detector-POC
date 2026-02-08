# Lab Monitoring System - Setup Guide

## Prerequisites

- Docker and Docker Compose
- Java 25 (for local development)
- Maven (for local development)
- Node.js 18+ (for local frontend development)

## Quick Start with Docker

1. **Clone/Navigate to the project directory**
   ```bash
   cd lab-monitoring-system
   ```

2. **Start all services**
   ```bash
   docker-compose up --build
   ```

   This will:
   - Build all 5 services (4 backend + 1 frontend)
   - Start PostgreSQL with PostGIS
   - Start RabbitMQ
   - Initialize databases
   - Connect all services

3. **Access the application**
   - **Frontend UI**: http://localhost:3000
   - **Group Management API**: http://localhost:8081
   - **Notification Service**: http://localhost:8082
   - **Telegram Ingestion**: http://localhost:8083
   - **Anomaly Detection**: http://localhost:8084
   - **RabbitMQ Management**: http://localhost:15672 (user/pass: monitoring/monitoring123)
   - **PostgreSQL**: localhost:5432 (user/pass: monitoring/monitoring123)

## System Flow

1. **Telegram Ingestion Service** generates mock Telegram messages from Ukrainian locations
2. Messages are sent to RabbitMQ queue `telegram-events`
3. **Anomaly Detection Service** reads from `telegram-events`, detects keywords (warning, alert, urgent, crisis, emergency)
4. Anomalies are sent to RabbitMQ queue `anomaly-notifications`
5. **Notification Service** reads from `anomaly-notifications`, saves to DB, and broadcasts via SSE
6. **Frontend** receives SSE events and displays on map with blinking animation

## Testing the System

### 1. Add Groups
- Go to http://localhost:3000/groups
- Add some Telegram groups with Ukrainian coordinates
- Example: Kyiv (50.4501, 30.5234)

### 2. Set Zone of Interest
- Go to http://localhost:3000/pipeline
- Draw a rectangle on the map covering Ukraine
- Click "Save Zone"

### 3. Watch the Dashboard
- Go to http://localhost:3000/
- You should see notifications appearing every 5 seconds (mock data)
- Markers will blink when first appearing
- Table shows recent notifications with keyword highlighting

## Local Development

### Backend Services

Each service can be run locally:

```bash
# Group Management Service
cd group-management-service
mvn spring-boot:run

# Notification Service
cd notification-service
mvn spring-boot:run

# Telegram Ingestion Service
cd telegram-ingestion-service
mvn spring-boot:run

# Anomaly Detection Service
cd anomaly-detection-service
mvn spring-boot:run
```

Make sure PostgreSQL and RabbitMQ are running (use docker-compose or local instances).

### Frontend

```bash
cd frontend
npm install
npm start
```

Access at http://localhost:3000

## Project Structure

```
lab-monitoring-system/
├── docker-compose.yml
├── docker/
│   └── init-db.sh
├── group-management-service/        # Spring MVC, JPA, PostGIS
│   ├── src/main/java/.../groupmanagement/
│   │   ├── controller/              # REST controllers
│   │   ├── service/                 # Business logic (functional)
│   │   ├── repository/              # JPA repositories
│   │   └── model/                   # Entities and DTOs
│   ├── pom.xml
│   └── Dockerfile
├── notification-service/             # Spring WebFlux, R2DBC
│   ├── src/main/java/.../notification/
│   │   ├── controller/              # SSE endpoint
│   │   ├── service/                 # RabbitMQ listener (functional)
│   │   ├── repository/              # R2DBC repository
│   │   └── model/                   # Entities and records
│   ├── pom.xml
│   └── Dockerfile
├── telegram-ingestion-service/       # Spring WebFlux, reactive
│   ├── src/main/java/.../ingestion/
│   │   ├── service/                 # Mock data generator (functional)
│   │   └── model/                   # Records
│   ├── pom.xml
│   └── Dockerfile
├── anomaly-detection-service/        # Spring Boot with RabbitMQ
│   ├── src/main/java/.../anomaly/
│   │   ├── service/                 # Keyword detection (functional)
│   │   └── model/                   # Records
│   ├── pom.xml
│   └── Dockerfile
└── frontend/                         # React, Leaflet, SSE
    ├── src/
    │   ├── components/              # Navigation
    │   ├── pages/                   # Dashboard, Groups, Pipeline
    │   ├── services/                # API client
    │   └── styles/                  # Dark theme CSS
    ├── package.json
    ├── nginx.conf
    └── Dockerfile

```

## Functional Programming Features

This project demonstrates functional programming concepts:

### 1. **Immutability**
- Records for DTOs (NotificationEvent, TelegramMessage, etc.)
- Immutable transformations

### 2. **Higher-Order Functions**
- `Function<TelegramGroupRequest, TelegramGroup>` for transformations
- `Predicate<TelegramEvent>` for filtering
- Stream operations (map, filter, flatMap)

### 3. **Function Composition**
- Chaining transformations in services
- Optional pipelines for error handling

### 4. **Reactive Streams**
- Flux and Mono for reactive data processing
- SSE for event streaming
- RabbitMQ reactive consumption

### 5. **Pure Functions**
- Side-effect-free transformations
- Closures for configuration

## Stopping the System

```bash
docker-compose down
```

To remove volumes:
```bash
docker-compose down -v
```

## Troubleshooting

### Services won't start
- Check if ports 3000, 5432, 5672, 8081-8084, 15672 are available
- Check Docker logs: `docker-compose logs <service-name>`

### No notifications appearing
- Check RabbitMQ queues at http://localhost:15672
- Verify telegram-ingestion-service logs: `docker-compose logs telegram-ingestion-service`
- Ensure anomaly detection is running: `docker-compose logs anomaly-detection-service`

### Frontend can't connect to backend
- Check nginx proxy configuration in `frontend/nginx.conf`
- Verify services are running: `docker-compose ps`

## Future Enhancements

- [ ] Replace mock data with real Telegram API (using TDLight Java)
- [ ] Add authentication
- [ ] Implement zone-based filtering in ingestion service
- [ ] Add more sophisticated anomaly detection (ML-based)
- [ ] Add notification preferences
- [ ] Implement historical data analysis
