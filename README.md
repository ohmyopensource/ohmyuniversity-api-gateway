# OhMyUniversity! - API Gateway

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=flat&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.1-6DB33F?style=flat&logo=spring&logoColor=white)
![License](https://img.shields.io/badge/license-Apache%202.0-blue?style=flat)

API Gateway for the **OhMyUniversity!** platform - part of the [OhMyOpenSource!](https://github.com/ohmyopensource) organization.

---

## What is this?

This is the single entry point for all client traffic in the OhMyUniversity! platform. It handles routing, JWT validation, rate limiting, circuit breaking, and load balancing across all internal microservices. No client ever communicates directly with a microservice - everything goes through this gateway.

Built with Spring Cloud Gateway Server WebFlux (reactive stack) on top of Spring Boot 4.

---

## Part of OhMyUniversity!

OhMyUniversity! is an open source university platform designed to simplify academic life for students, professors, and administrative staff. It provides real-time chat, institutional data integration, course and canteen management, transport schedules, room booking, and more.

### Public repositories

| Repository                                                                        | Description                                                              |
|-----------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| [ohmyuniversity-gateway](https://github.com/ohmyopensource/ohmyuniversity-gateway) | This repo - API Gateway                                                  |
| [ohmyuniversity-core](https://github.com/ohmyopensource/ohmyuniversity-core)      | Core API - institutional data, courses, canteen, transport               |
| [ohmyuniversity-chat](https://github.com/ohmyopensource/ohmyuniversity-chat)      | Chat microservice - real-time WebSocket messaging                        |
| [ohmyuniversity-fetcher](https://github.com/ohmyopensource/ohmyuniversity-fetcher) | Fetcher - scheduled sync from MUR, InPA, EPSO and professional registers |
| [ohmyuniversity-web](https://github.com/ohmyopensource/ohmyuniversity-web)        | Web frontend - Angular                                                   |
| [ohmyuniversity-mobile](https://github.com/ohmyopensource/ohmyuniversity-mobile)  | Mobile app - Flutter                                                     |
| [ohmyuniversity-desktop](https://github.com/ohmyopensource/ohmyuniversity-desktop) | Desktop app - Tauri                                                      |

---

## Documentation & guidelines

- **Full platform documentation:** [ohmyuniversity-docs](https://github.com/ohmyopensource/ohmyuniversity-docs)
- **Organization guidelines:** [ohmyopensource-guidelines](https://github.com/ohmyopensource/ohmyopensource-guidelines)

---

## Tech stack

| Layer | Technology                                                |
|---|-----------------------------------------------------------|
| Language | Java 21                                                   |
| Framework | Spring Boot 4.0.6                                         |
| Gateway | Spring Cloud Gateway Server WebFlux 2025.1.1              |
| Auth | Spring Security 7 - OAuth2 Resource Server (JWT / Nimbus) |
| Rate limiting | Spring Cloud Gateway `RequestRateLimiter` + Redis         |
| Circuit breaker | Resilience4J (reactive)                                   |
| Metrics | Micrometer + Prometheus                                   |
| Tracing | Micrometer Tracing + OpenTelemetry                        |
| Code style | Google Java Style Guide (Checkstyle)                      |

---

## Getting started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker


### Run locally

This repository includes a `Dockerfile`. To run the full platform locally (including all required infrastructure), refer to the infrastructure setup described in the platform documentation:

📚 [ohmyuniversity-docs](https://github.com/ohmyopensource/ohmyuniversity-docs)

### Build

```bash
./mvnw clean install
```

Checkstyle runs automatically during the `validate` phase. The build fails if the code does not comply with Google Java Style Guidelines.

### Build Docker image

```bash
docker build -t ohmyuniversity-gateway .
```
 
---

## License

This project is licensed under the AGPL-3.0 - see the [LICENSE](LICENSE) file for details.