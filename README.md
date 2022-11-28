# Tennis club reservation system

## How to build and run

Download maven and run `mvn clean install spring-boot:run`

which runs the whole application and the web server is accessible on http://localhost:8080

## Documentation

API documentation is available at `http://localhost:8080/api/v1/docs`
with a swagger UI client at `http://localhost:8080/api/v1/swagger-ui`

## How to populate app with data
To trigger data population you must change the app configuration in /src/main/resources/application-prod.properties;
set the application.data-population.enabled to true. You might also set the spring.jpa.hibernate.ddl-auto to create in order to delete all data before starting the
application and populate the app with new data.

## Development
Before beginning development setup git hooks by running

```console
git config core.hooksPath .githooks
```

### Run mutation tests

```console
mvn -Ptest test-compile org.pitest:pitest-maven:mutationCoverage
```