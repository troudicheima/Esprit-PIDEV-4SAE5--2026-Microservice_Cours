# WebSocket Real-Time Notification - Complete Implementation

## 1. Backend - WebSocket Configuration

**File:** `src/main/java/tn/esprit/cours_service/Config/WebSocketConfig.java`

```java
package tn.esprit.cours_service.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple in-memory message broker
        config.enableSimpleBroker("/topic", "/queue");
        // Prefix for messages from clients
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register WebSocket endpoint with SockJS fallback
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
```

---

## 2. Backend - Notification Service

**File:** `src/main/java/tn/esprit/cours_service/Service/NotificationService.java`

```java
package tn.esprit.cours_service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.cours_service.entities.Course;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Send a real-time notification when a new course is created
     * This broadcasts to /topic/courses for all subscribed clients
     * @param course The newly created course
     */
    public void notifyCourseCreated(Course course) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "COURSE_CREATED");
        notification.put("message", "A new course has been created: " + course.getTitle());
        notification.put("courseId", course.getId());
        notification.put("courseTitle", course.getTitle());
        notification.put("timestamp", System.currentTimeMillis());

        // Broadcast to all subscribers of /topic/courses
        messagingTemplate.convertAndSend("/topic/courses", notification);
    }
}
```

---

## 3. Backend - Course Controller (Modified)

**File:** `src/main/java/tn/esprit/cours_service/Controller/CourseController.java`

```java
package tn.esprit.cours_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cours_service.Service.CourseService;
import tn.esprit.cours_service.Service.NotificationService;
import tn.esprit.cours_service.entities.Course;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course created = courseService.createCourse(course);
        
        // Send real-time notification via WebSocket
        notificationService.notifyCourseCreated(created);
        
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        Course updated = courseService.updateCourse(id, course);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 4. Frontend - Notification Display (HTML + JavaScript)

**File:** `src/main/resources/static/notifications.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Notifications</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
        }
        h1 { color: #333; }
        #notification-container { margin-top: 20px; }
        .notification {
            background-color: #f0f0f0;
            border-left: 4px solid #4CAF50;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 4px;
            animation: slideIn 0.3s ease-out;
        }
        .notification h3 { margin: 0 0 10px 0; color: #333; }
        .notification p { margin: 0; color: #666; }
        .notification .timestamp { font-size: 12px; color: #999; margin-top: 5px; }
        .notification .course-id { font-size: 12px; color: #999; }
        @keyframes slideIn {
            from { transform: translateX(-100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        #connection-status {
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        #connection-status.connected { background-color: #d4edda; color: #155724; }
        #connection-status.disconnected { background-color: #f8d7da; color: #721c24; }
    </style>
</head>
<body>
    <h1>📚 Course Notifications</h1>
    <div id="connection-status" class="disconnected">Connecting to WebSocket...</div>
    <div id="notification-container">
        <h2>Recent Notifications</h2>
        <div id="notifications-list"></div>
    </div>

    <!-- SockJS Client -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"></script>
    <!-- STOMP Client -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    
    <script>
        const wsUrl = '/ws';
        const topicUrl = '/topic/courses';
        let stompClient = null;
        
        function connect() {
            const socket = new SockJS(wsUrl);
            stompClient = Stomp.over(socket);
            
            stompClient.connect({}, function(frame) {
                console.log('Connected: ' + frame);
                setConnectionStatus(true);
                
                // Subscribe to course notifications
                stompClient.subscribe(topicUrl, function(message) {
                    const notification = JSON.parse(message.body);
                    displayNotification(notification);
                });
            }, function(error) {
                console.log('Error: ' + error);
                setConnectionStatus(false);
                setTimeout(connect, 5000);
            });
        }
        
        function displayNotification(notification) {
            const notificationsList = document.getElementById('notifications-list');
            const notificationDiv = document.createElement('div');
            notificationDiv.className = 'notification';
            
            const timestamp = new Date(notification.timestamp).toLocaleString();
            
            notificationDiv.innerHTML = `
                <h3>${notification.message}</h3>
                <p class="course-id">Course ID: ${notification.courseId}</p>
                <p class="timestamp">${timestamp}</p>
            `;
            
            notificationsList.insertBefore(notificationDiv, notificationsList.firstChild);
        }
        
        function setConnectionStatus(connected) {
            const statusDiv = document.getElementById('connection-status');
            if (connected) {
                statusDiv.className = 'connected';
                statusDiv.textContent = '✅ Connected - Receiving notifications';
            } else {
                statusDiv.className = 'disconnected';
                statusDiv.textContent = '❌ Disconnected - Attempting to reconnect...';
            }
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            connect();
        });
    </script>
</body>
</html>
```

---

## 5. How It Works

### Flow:
1. User fills the form and clicks "Create Course"
2. POST request to `/api/courses` with course data
3. Course is saved in database
4. `NotificationService.notifyCourseCreated()` is called
5. Notification is broadcast via WebSocket to `/topic/courses`
6. Frontend clients subscribed to `/topic/courses` receive the notification in real-time

### WebSocket Endpoint:
- **URL:** `http://localhost:8080/ws`
- **Topic:** `/topic/courses`

### Notification Data Structure:
```json
{
    "type": "COURSE_CREATED",
    "message": "A new course has been created: [course title]",
    "courseId": 1,
    "courseTitle": "Course Title",
    "timestamp": 1711986400000
}
```

---

## 6. Required Dependency (Already in pom.xml)

```xml
<!-- WebSocket with STOMP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```
