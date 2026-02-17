# Prompt to generate a React frontend consuming the Cours_Service API

Copy and paste the following prompt into Kilo Code:

---

Create a React frontend application (using Vite + React + Axios) that consumes a Spring Boot microservice REST API called **Cours_Service**. The backend runs on `http://localhost:5059/Cours_Service`.

## API Endpoints

### Course API (`/api/courses`)
Base URL: `http://localhost:5059/Cours_Service/api/courses`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/courses` | Get all courses | - |
| GET | `/api/courses/{id}` | Get course by ID | - |
| POST | `/api/courses` | Create a new course | `{ "title": string, "level": string, "duration": number, "description": string }` |
| PUT | `/api/courses/{id}` | Update a course | `{ "title": string, "level": string, "duration": number, "description": string }` |
| DELETE | `/api/courses/{id}` | Delete a course | - |

### Lesson API (`/api`)
Base URL: `http://localhost:5059/Cours_Service/api`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/lessons` | Get all lessons | - |
| GET | `/api/lessons/{id}` | Get lesson by ID | - |
| GET | `/api/courses/{courseId}/lessons` | Get lessons by course | - |
| POST | `/api/courses/{courseId}/lessons` | Create lesson under a course | `{ "lessontitle": string, "content": string, "lessonorder": number }` |
| PUT | `/api/lessons/{id}` | Update a lesson | `{ "lessontitle": string, "content": string, "lessonorder": number }` |
| DELETE | `/api/lessons/{id}` | Delete a lesson | - |

## Data Models

### Course
```json
{
  "id": int,
  "title": "string",
  "level": "string",
  "duration": int,
  "description": "string"
}
```

### Lesson
```json
{
  "id": int,
  "lessontitle": "string",
  "content": "string",
  "lessonorder": int,
  "course": { "id": int, "title": "string", ... }
}
```

## Requirements

1. **Project setup**: Create a Vite + React project with Axios for HTTP calls. Use React Router for navigation.

2. **API Service Layer**: Create an `api.js` (or `api.ts`) service file using Axios with base URL `http://localhost:5059/Cours_Service` containing all API call functions for both Course and Lesson endpoints.

3. **Pages to create**:
   - **Course List Page** (`/courses`): Display all courses in a table or card layout. Each course should have Edit, Delete, and "View Lessons" buttons.
   - **Course Form Page** (`/courses/new` and `/courses/edit/:id`): A form to create or edit a course with fields: title, level, duration, description.
   - **Lesson List Page** (`/courses/:courseId/lessons`): Display all lessons for a specific course. Each lesson should have Edit and Delete buttons.
   - **Lesson Form Page** (`/courses/:courseId/lessons/new` and `/lessons/edit/:id`): A form to create or edit a lesson with fields: lessontitle, content, lessonorder.

4. **Features**:
   - Full CRUD operations for Courses and Lessons
   - Navigation between courses and their lessons
   - Success/error notifications on API calls
   - Loading states while fetching data
   - Confirmation dialog before deleting

5. **Styling**: Use a simple CSS framework like Bootstrap or Tailwind CSS for clean UI.

6. **CORS**: The backend may need CORS configuration. Add a note about adding `@CrossOrigin` on the Spring Boot controllers if needed.

Make sure all API calls use the correct base URL `http://localhost:5059/Cours_Service` and handle errors gracefully.

---
