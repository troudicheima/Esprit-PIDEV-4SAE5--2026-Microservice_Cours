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
