package tn.esprit.cours_service.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cours_service.Repository.CourseRepository;
import tn.esprit.cours_service.Repository.LessonRepository;
import tn.esprit.cours_service.entities.Course;
import tn.esprit.cours_service.entities.Lesson;

import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;


    @Autowired
    private CourseRepository courseRepository;


    public Lesson createLesson(int courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            lesson.setCourse(course);
            return lessonRepository.save(lesson);
        }
        return null;
    }


    public Lesson getLessonById(int id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> getLessonsByCourse(int courseId) {
        return lessonRepository.findByCourseId(courseId);
    }



    public Lesson updateLesson(int id, Lesson lessonDetails) {
        Lesson lesson = lessonRepository.findById(id).orElse(null);
        if (lesson != null) {
            lesson.setLessontitle(lessonDetails.getLessontitle());
            lesson.setContent(lessonDetails.getContent());
            lesson.setLessonorder(lessonDetails.getLessonorder());
            // change course if needed
            if (lessonDetails.getCourse() != null) {
                lesson.setCourse(lessonDetails.getCourse());
            }
            return lessonRepository.save(lesson);
        }
        return null;
    }





    public void deleteLesson(int id) {
        lessonRepository.deleteById(id);
    }



}

