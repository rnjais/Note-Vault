# NoteVault 📝

NoteVault is a backend REST API built using **Java and Spring Boot** for managing personal notes.

The project started as a way to practice Spring Boot and gradually evolved into a more complete backend application with authentication, security, database operations, validation, searching, pagination, and testing.

## Features

* User registration and login
* JWT-based authentication
* BCrypt password encryption
* User-specific note access
* Create, read, update, and delete notes
* Search notes by title or content
* Search notes by category
* Pagination and sorting
* Request validation
* Global exception handling
* Custom exceptions
* DTO and Mapper pattern
* Swagger/OpenAPI documentation
* JUnit and Mockito tests
* MySQL database integration

## Tech Stack

* **Java 22**
* **Spring Boot 4.1.1**
* **Spring Security**
* **JWT**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **JUnit 5**
* **Mockito**
* **Swagger / OpenAPI**
* **Postman**
* **IntelliJ IDEA**

## How It Works

The application follows a simple layered architecture:

```text
Client / Postman
       ↓
Controller
       ↓
Service
       ↓
Repository
       ↓
MySQL
```

The Controller handles API requests, the Service contains the main business logic, and the Repository communicates with the database.

DTOs are used to control the data sent through the API, while Mapper classes handle conversion between entities and DTOs.

## Authentication

NoteVault uses JWT for authentication.

When a user registers, their password is encrypted using BCrypt before being stored in the database.

During login, the credentials are verified and a JWT token is generated.

The token must then be sent with requests to protected endpoints.

```text
Register
   ↓
Password encrypted
   ↓
User saved in MySQL

Login
   ↓
Credentials verified
   ↓
JWT generated
   ↓
JWT used for protected APIs
```

## API Endpoints

### Authentication

| Method | Endpoint             | Description           |
| ------ | -------------------- | --------------------- |
| POST   | `/api/auth/register` | Register a new user   |
| POST   | `/api/auth/login`    | Login and receive JWT |

### Notes

| Method | Endpoint          | Description      |
| ------ | ----------------- | ---------------- |
| POST   | `/api/notes`      | Create a note    |
| GET    | `/api/notes`      | Get user's notes |
| GET    | `/api/notes/{id}` | Get a note by ID |
| PUT    | `/api/notes/{id}` | Update a note    |
| DELETE | `/api/notes/{id}` | Delete a note    |

### Search

```text
GET /api/notes/search?keyword=java
```

Searches notes by title or content.

```text
GET /api/notes/category?category=Programming
```

Searches notes by category.

### Pagination and Sorting

```text
GET /api/notes?page=0&size=5&sortBy=createdAt&direction=desc
```

## Example Request

### Register

```json
{
  "username": "aryan",
  "email": "aryan@example.com",
  "password": "password123"
}
```

### Login

```json
{
  "username": "aryan",
  "password": "password123"
}
```

### Create Note

```json
{
  "title": "Spring Boot",
  "content": "Learning Spring Boot REST APIs",
  "category": "Backend"
}
```

## Response Format

The API uses a common response structure for operations that return `ApiResponse`.

Example:

```json
{
  "success": true,
  "message": "Note created successfully",
  "data": {
    "id": 1,
    "title": "Spring Boot",
    "content": "Learning Spring Boot REST APIs",
    "category": "Backend"
  }
}
```

## Exception Handling

Errors are handled centrally using `GlobalExceptionHandler`.

Some examples:

| Situation               | Status |
| ----------------------- | -----: |
| Validation failed       |    400 |
| Invalid credentials     |    401 |
| Username already exists |    409 |
| Email already exists    |    409 |
| Note not found          |    404 |
| User not found          |    404 |

Example:

```json
{
  "success": false,
  "message": "Email already exists",
  "data": null
}
```

## Validation

The application uses Jakarta Validation to validate incoming requests.

For example:

* Username cannot be empty
* Email must have a valid format
* Password must be at least 6 characters
* Note title cannot be empty
* Note content cannot be empty

## Testing

I used **JUnit 5 and Mockito** to test the service layer.

The tests cover:

### AuthService

* Successful registration
* Duplicate username
* Duplicate email
* Invalid password
* Successful login

### NoteService

* Creating a note
* Getting a note
* Note not found
* Unauthorized note access
* Updating a note
* Deleting a note
* Searching notes
* Category search
* Pagination

Current test result:

```text
Tests run: 14
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Swagger

Swagger/OpenAPI is included for API documentation and testing.

After starting the application, open:

```text
http://localhost:8080/swagger-ui/index.html
```

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/your-username/note-vault.git
```

### 2. Create the database

Create a MySQL database:

```sql
CREATE DATABASE notevault;
```

### 3. Configure MySQL

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/notevault
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Add your JWT configuration as required by the project.

### 4. Run the application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will run on:

```text
http://localhost:8080
```

### 5. Run tests

```powershell
.\mvnw.cmd test
```

## Project Structure

```text
src
├── main
│   └── java
│       └── com.Note_Vault
│           ├── config
│           ├── controller
│           ├── dto
│           ├── entity
│           ├── exception
│           ├── mapper
│           ├── repository
│           ├── response
│           ├── security
│           └── service
│
└── test
    └── java
        └── com.Note_Vault
            ├── AuthServiceTest.java
            └── NoteServiceTest.java
```

## What I Practiced

While building NoteVault, I worked with:

* Spring Boot REST APIs
* Java
* Spring Security
* JWT authentication
* BCrypt
* Spring Data JPA
* MySQL
* DTOs and Mappers
* Exception handling
* Validation
* Pagination and sorting
* Search functionality
* JUnit and Mockito
* Swagger/OpenAPI
* Postman
* Git and GitHub

## Future Improvements

Some features I may add in the future:

* Refresh tokens
* Note sharing
* Tags
* Soft delete
* Redis caching
* Docker
* CI/CD
* Cloud deployment

## Author

**Aryan Jaiswal**

BE Information Technology
Java | Spring Boot | Backend Development | DSA

---

### Project Goal

NoteVault was built to get practical experience with Spring Boot and understand how different backend components work together in a real REST API.

The main focus of the project is **clean backend architecture, security, database interaction, validation, exception handling, and testing**.
