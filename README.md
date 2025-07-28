# Social Campaign Manager (Java)

This is a full-stack desktop application built with Java, demonstrating a modern client-server architecture. It allows users to create and view social awareness campaigns.

## Architecture
The project is composed of two main parts:
- **/campaign-manager-api**: A backend REST API built with Spring Boot that handles all business logic and database interactions.
- **/campaign-manager-ui**: A frontend desktop client built with JavaFX that provides the user interface.

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Data JPA, H2 Database
- **Frontend:** JavaFX
- **Build Tool:** Maven

## How to Run

### Prerequisites
- Java 17 or later
- Maven
- An IDE like Eclipse or IntelliJ

### 1. Backend Setup
1. Navigate to the `campaign-manager-api` directory.
2. **IMPORTANT:** Create a file named `application.properties` inside `src/main/resources/`.
3. Add your Google Gemini API key to this file:
   ```properties
   google.api.key=YOUR_OWN_API_KEY