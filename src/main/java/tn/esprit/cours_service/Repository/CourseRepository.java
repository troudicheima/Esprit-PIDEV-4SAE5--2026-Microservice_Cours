package tn.esprit.cours_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.cours_service.entities.Course;

public interface CourseRepository  extends JpaRepository<Course, Integer> {

}
