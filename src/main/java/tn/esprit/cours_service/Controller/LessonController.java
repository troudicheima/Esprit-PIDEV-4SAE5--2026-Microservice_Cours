package tn.esprit.cours_service.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cours_service.Service.LessonService;
import tn.esprit.cours_service.entities.Lesson;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LessonController {

    @Autowired
    private LessonService lessonService;


    // Create a lesson under a specific course
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Lesson> createLesson(@PathVariable int courseId, @RequestBody Lesson lesson) {
        Lesson created = lessonService.createLesson(courseId, lesson);
        return created != null ? new ResponseEntity<>(created, HttpStatus.CREATED)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable int id) {
        Lesson lesson = lessonService.getLessonById(id);
        return lesson != null ? ResponseEntity.ok(lesson) : ResponseEntity.notFound().build();
    }

    @GetMapping("/lessons")
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }


    @GetMapping("/courses/{courseId}/lessons")
    public List<Lesson> getLessonsByCourse(@PathVariable int courseId) {
        return lessonService.getLessonsByCourse(courseId);
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable int id, @RequestBody Lesson lesson) {
        Lesson updated = lessonService.updateLesson(id, lesson);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable int id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
