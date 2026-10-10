# Social Campaign Manager

[![CI](https://github.com/Subroto761/Social-Campaign-Manager-Java/actions/workflows/ci.yml/badge.svg)](https://github.com/Subroto761/Social-Campaign-Manager-Java/actions/workflows/ci.yml)

A client–server Java application for creating and managing social-awareness campaigns. It has a **Spring Boot REST API** backend and a **JavaFX desktop client**, and it can draft campaign descriptions with **Google Gemini**.

**Stack:** Java 21 · Spring Boot 3 · Spring Data JPA · H2 · JavaFX · Gemini API · Maven · GitHub Actions

## Architecture

```
┌────────────────────────┐   HTTP / JSON   ┌──────────────────────────────┐        ┌──────────────┐
│  campaign-manager-ui   │ ──────────────▶ │     campaign-manager-api     │ ─────▶ │  H2 database │
│  (JavaFX desktop app)  │ ◀────────────── │  (Spring Boot, port 8080)    │        └──────────────┘
└────────────────────────┘                 │                              │        ┌──────────────┐
                                           │  /api/ai/generate-description│ ─────▶ │  Gemini API  │
                                           └──────────────────────────────┘        └──────────────┘
```

- **`campaign-manager-api`** is a REST API with Controller → Repository (Spring Data JPA) layering. The AI call sits in its own `AIService`, and the API key is read from an environment variable, so no secrets live in the repo.
- **`campaign-manager-ui`** is the JavaFX client. All network calls run on background threads, and the UI is updated through `Platform.runLater` so the window never freezes.

## Features

- Create, list, edit and delete campaigns (full CRUD)
- "Generate with AI" button that writes a campaign description from its title
- Confirmation dialog before deleting

## REST API

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/campaigns` | List all campaigns |
| `POST` | `/api/campaigns` | Create a campaign (`{"title": "...", "description": "..."}`) |
| `PUT` | `/api/campaigns/{id}` | Update a campaign (`404` if it doesn't exist) |
| `DELETE` | `/api/campaigns/{id}` | Delete a campaign (`204`, or `404`) |
| `POST` | `/api/ai/generate-description` | Plain-text campaign idea in, generated description out |

## Run it locally

**Prerequisites:** Java 21 and Maven. A free [Gemini API key](https://aistudio.google.com/app/apikey) is optional and only needed for the AI button.

**1. Start the backend**

```bash
cd campaign-manager-api
export GEMINI_API_KEY=your_key_here     # Windows PowerShell: $env:GEMINI_API_KEY="your_key_here"
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. The H2 console is available at `/h2-console` (JDBC URL `jdbc:h2:mem:campaigns`).

**2. Start the desktop client** (in a second terminal)

```bash
cd campaign-manager-ui
mvn javafx:run
```

**Try the API without the UI**

```bash
curl -X POST localhost:8080/api/campaigns -H "Content-Type: application/json" \
     -d '{"title":"Clean Rivers","description":"Weekend river clean-up drive"}'
curl localhost:8080/api/campaigns
```

## Configuration

| Variable | Default | Purpose |
|---|---|---|
| `GEMINI_API_KEY` | *(empty)* | Turns on AI description generation |
| `GEMINI_MODEL` | `gemini-2.5-flash` | Which Gemini model to call |

## Next steps

- Swap H2 for PostgreSQL and containerise the API with Docker Compose
- Add input validation (`@Valid`) and controller tests with MockMvc
- Return proper HTTP error codes from the AI endpoint instead of an error string

## Author

**Subroto Mondal** · [GitHub](https://github.com/Subroto761) · [LinkedIn](https://www.linkedin.com/in/cnctsubroto)
