package tn.esprit.cours_service.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.cours_service.Repository.CourseRepository;
import tn.esprit.cours_service.entities.Course;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course getCourseById(int id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(int id, Course courseDetails) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course != null) {
            course.setTitle(courseDetails.getTitle());
            course.setLevel(courseDetails.getLevel());
            course.setDuration(courseDetails.getDuration());
            course.setDescription(courseDetails.getDescription());
            return courseRepository.save(course);
        }
        return null;
    }

    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }
}
