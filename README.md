# AInalyse

A minimal Spring Boot application skeleton for `AInalyse`.

## Requirements
- Java 17+ (JDK)
- Maven

## Build and run

From project root:

```cmd
mvn clean package
mvn spring-boot:run
```

Or run the built jar:

```cmd
java -jar target\AInalyse-0.0.1-SNAPSHOT.jar
```

The app will start on port 8080 by default. Open `http://localhost:8080/` to see a health response.

Swagger UI and OpenAPI
----------------------

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

After starting the app (see build/run above), open the Swagger URL to explore the APIs.


Gemini 2.5 Prompt API
---------------------

POST `/api/gemini`

Request JSON:
```json
{
	"prompt": "Your question or instruction here"
}
```

Response JSON:
```json
{
	"response": "Gemini's reply text"
}
```

Setup:
- Add your Gemini API key to `src/main/resources/application.properties` as `gemini.api.key=YOUR_GEMINI_API_KEY`
