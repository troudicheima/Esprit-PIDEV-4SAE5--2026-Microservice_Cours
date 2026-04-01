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
