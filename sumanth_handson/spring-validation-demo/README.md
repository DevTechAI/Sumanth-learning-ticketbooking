# Spring Validation Demo

A basic Spring Boot project showing request validation using Jakarta Bean Validation.

## Concepts demonstrated

- `@Valid`
- `@NotNull`
- `@NotBlank`
- `@Size`
- `@Email`
- `@Min`
- `@Max`
- `@Pattern`
- `MethodArgumentNotValidException`
- `@RestControllerAdvice`
- `@ExceptionHandler`

## Requirements

- Java 17+
- Maven 3.9+
- IntelliJ IDEA or another Java IDE
- Postman (optional)

## Run

From the project folder:

```bash
mvn spring-boot:run
```

Or open `ValidationApplication.java` in IntelliJ and run it.

## API

### POST

`http://localhost:8080/api/students`

### Valid request

```json
{
  "name": "Sumanth",
  "email": "sumanth@gmail.com",
  "age": 25,
  "phone": "2145551234"
}
```

Expected response:

```text
Student Sumanth created successfully
```

### Invalid request

```json
{
  "name": "",
  "email": "hello",
  "age": 15,
  "phone": "123"
}
```

Example response:

```json
{
  "name": "Name is required",
  "email": "Email format is invalid",
  "age": "Age must be at least 18",
  "phone": "Phone number must contain exactly 10 digits"
}
```

## Request flow

```text
Postman / Client
      |
      v
StudentController
      |
      | @Valid
      v
StudentRequest
      |
      +-- valid --> StudentService --> 200 OK
      |
      +-- invalid --> MethodArgumentNotValidException
                        |
                        v
               GlobalExceptionHandler
                        |
                        v
                   400 Bad Request
```
