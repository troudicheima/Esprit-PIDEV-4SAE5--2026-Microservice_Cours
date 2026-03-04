# Cours Service – Course Management Microservice

## Overview

This project was developed as part of the PIDEV – 4rd Year Engineering Program at **Esprit School of Engineering** (Academic Year 2025–2026).

Cours Service is a Spring Boot microservice that provides a comprehensive REST API for managing educational courses and their associated lessons. It enables educational platforms to create, organize, and manage course content with a hierarchical structure of courses containing multiple lessons.

## Features

- **Course Management**: Create, read, update, and delete courses with attributes like title, level, duration, and description
- **Lesson Management**: Manage lessons within courses with ordering and content support
- **Hierarchical Structure**: One-to-many relationship between courses and lessons
- **RESTful API**: Full CRUD operations via REST endpoints
- **Service Discovery**: Integrated with Netflix Eureka for microservice registration
- **MySQL Database**: Persistent storage using MySQL connector
- **Spring Boot**: Modern Java-based enterprise application framework

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Spring Cloud Netflix Eureka Client**
- **MySQL**
- **Maven**

## Architecture

The project follows a standard Spring Boot layered architecture:

```
src/main/java/tn/esprit/cours_service/
├── CoursServiceApplication.java       # Main Spring Boot application
├── Controller/
│   ├── CourseController.java         # REST endpoints for courses
│   └── LessonController.java         # REST endpoints for lessons
├── Service/
│   ├── CourseService.java            # Business logic for courses
│   └── LessonService.java            # Business logic for lessons
├── Repository/
│   ├── CourseRepository.java         # Data access for courses
│   └── LessonRepository.java         # Data access for lessons
└── entities/
    ├── Course.java                   # Course entity model
    └── Lesson.java                   # Lesson entity model
```

### API Endpoints

**Courses:**
- `POST /api/courses` - Create a new course
- `GET /api/courses` - Get all courses
- `GET /api/courses/{id}` - Get course by ID
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

**Lessons:**
- `POST /api/courses/{courseId}/lessons` - Create a lesson under a course
- `GET /api/lessons` - Get all lessons
- `GET /api/lessons/{id}` - Get lesson by ID
- `GET /api/courses/{courseId}/lessons` - Get all lessons for a course
- `PUT /api/lessons/{id}` - Update lesson
- `DELETE /api/lessons/{id}` - Delete lesson

## Contributors

- PIDEV 3A Students – **Esprit School of Engineering**

## Academic Context

Developed at **Esprit School of Engineering – Tunisia**

PIDEV – 3A | 2025–2026

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL Server

### Configuration

Configure the database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cours_service
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Build and Run

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The service will start on `http://localhost:8080`

### Running Tests

```bash
./mvnw test
```

## Acknowledgments

- **Esprit School of Engineering** for providing the academic framework
- Spring Boot and Spring Cloud communities for the excellent documentation
