```markdown
# Movie Database API

## Project Overview

This project is a RESTful API built using Spring Boot and JPA that manages a movie database for a local film society. The system helps store and manage movies, genres, and actors efficiently. Users can add new movies, update existing ones, and retrieve information about specific films or collections of films based on genres, release year, or actors. The API allows managing genres and actors separately and supports CRUD operations for all entities. The system also handles complex relationships between movies, genres, and actors.

### Main Features:
- **Movies**: Create, update, retrieve, and delete movies. Filter movies by genre, release year, and actors.
- **Genres**: Create, update, retrieve, and delete genres. View movies associated with specific genres.
- **Actors**: Create, update, retrieve, and delete actors. View movies that specific actors starred in.
- **Relationships**: Manage relationships between movies, genres, and actors with Many-to-Many mappings.
- **Error Handling**: Implements basic error handling with custom exceptions, validation, and global exception handling.
- **Pagination**: Supports pagination for listing multiple movies, genres, and actors.
- **Search**: Supports searching movies by partial title, with case-insensitive matching.

---

## Setup and Installation Instructions

### Prerequisites:
1. **Java 17 or later** - Make sure Java is installed on your machine.
2. **Maven** - Maven is used to manage dependencies and build the project.
3. **SQLite** - The database for this project is SQLite. Make sure SQLite is installed on your machine.

### Steps:

1. **Clone the Project:**
   Clone this repository to your local machine using:
   ```bash
   git clone https://github.com/your-repo/movies-api.git
```

2.  **Set Up the Database:**
    
    *   SQLite will be used for the database. Ensure you have SQLite installed.
    *   The database configuration is handled in the `application.properties` file.
    *   The SQLite JDBC dependency is included in `pom.xml`.
3.  **Build the Project:** Navigate to the project directory and run:
    
    ```bash
    mvn clean install
    ```
    
4.  **Run the Application:** After the build is successful, start the Spring Boot application with:
    
    ```bash
    mvn spring-boot:run
    ```
    
5.  **Accessing the API:** The API will be available at:
    
    ```bash
    http://localhost:8080/api/
    ```
    

### Database Configuration (application.properties)

```properties
spring.datasource.url=jdbc:sqlite:movie-db.sqlite
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.hibernate.ddl-auto=update
```

* * *

Usage Guide
-----------

### API Endpoints:

#### **Movies:**

*   **Create a Movie:**
    
    *   `POST /api/movies`
    *   Sample Request Body:
        
        ```json
        {
          "title": "Inception",
          "releaseYear": "2010",
          "duration": "148",
          "genreIds": [1, 2],
          "actorIds": [1, 3]
        }
        ```
        
*   **Retrieve All Movies (with Pagination):**
    
    *   `GET /api/movies?page=0&size=10`
*   **Retrieve a Movie by ID:**
    
    *   `GET /api/movies/{id}`
*   **Search Movies by Title (Case-insensitive, partial match):**
    
    *   `GET /api/movies/search?title=matrix`
*   **Retrieve Movies by Genre:**
    
    *   `GET /api/movies?genre={genreId}`
*   **Retrieve Movies by Release Year:**
    
    *   `GET /api/movies?year={releaseYear}`
*   **Retrieve Actors in a Movie:**
    
    *   `GET /api/movies/{movieId}/actors`
*   **Update a Movie:**
    
    *   `PATCH /api/movies/{id}`
    *   Sample Request Body (partial update):
        
        ```json
        {
          "title": "Inception Updated"
        }
        ```
        
*   **Delete a Movie (with optional force delete):**
    
    *   `DELETE /api/movies/{id}?force=true`

#### **Genres:**

*   **Create a Genre:**
    
    *   `POST /api/genres`
    *   Sample Request Body:
        
        ```json
        {
          "name": "Action"
        }
        ```
        
*   **Retrieve All Genres (with Pagination):**
    
    *   `GET /api/genres?page=0&size=10`
*   **Retrieve a Genre by ID:**
    
    *   `GET /api/genres/{id}`
*   **Update a Genre:**
    
    *   `PATCH /api/genres/{id}`
*   **Delete a Genre (with optional force delete):**
    
    *   `DELETE /api/genres/{id}?force=true`

#### **Actors:**

*   **Create an Actor:**
    
    *   `POST /api/actors`
    *   Sample Request Body:
        
        ```json
        {
          "name": "Tom Hanks",
          "birthDate": "1956-07-09",
          "movieIds": [1, 5]
        }
        ```
        
*   **Retrieve All Actors (with Pagination):**
    
    *   `GET /api/actors?page=0&size=10`
*   **Retrieve an Actor by ID:**
    
    *   `GET /api/actors/{id}`
*   **Search Actors by Name:**
    
    *   `GET /api/actors?name=Tom`
*   **Update an Actor:**
    
    *   `PATCH /api/actors/{id}`
*   **Delete an Actor (with optional force delete):**
    
    *   `DELETE /api/actors/{id}?force=true`

* * *

Additional Features and Bonus Functionality
-------------------------------------------

1.  **Force Delete**:
    
    *   Force delete allows you to delete entities (like movies, genres, or actors) that have existing relationships. When `force=true` is passed as a query parameter in the delete request, the system will remove relationships before deleting the entity.
2.  **Pagination**:
    
    *   All `GET` requests that return a list of movies, genres, or actors support pagination using the `page` and `size` parameters.
3.  **Global Exception Handling**:
    
    *   The system has a global exception handler that catches common exceptions like `ResourceNotFoundException` and validation errors, returning user-friendly error messages.
4.  **Input Validation**:
    
    *   Entities are validated with Bean Validation annotations (`@NotNull`, `@Size`, `@Pattern`, etc.) to ensure valid data is submitted to the API.

* * *

Sample Data
-----------

To test the API, you can use the following sample data for movies, genres, and actors:

### Genres:

```json
[
  { "name": "Action" },
  { "name": "Drama" },
  { "name": "Comedy" },
  { "name": "Thriller" },
  { "name": "Sci-Fi" }
]
```

### Movies:

```json
[
  { "title": "Inception", "releaseYear": "2010", "duration": "148", "genreIds": [1, 5], "actorIds": [1, 2] },
  { "title": "The Matrix", "releaseYear": "1999", "duration": "136", "genreIds": [1, 5], "actorIds": [3] },
  ...
]
```

### Actors:

```json
[
  { "name": "Leonardo DiCaprio", "birthDate": "1974-11-11" },
  { "name": "Ellen Page", "birthDate": "1987-02-21" },
  { "name": "Keanu Reeves", "birthDate": "1964-09-02" },
  ...
]
```

* * *