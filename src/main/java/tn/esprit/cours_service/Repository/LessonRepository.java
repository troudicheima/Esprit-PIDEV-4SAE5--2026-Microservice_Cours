package tn.esprit.cours_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.cours_service.entities.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findByCourseId(int courseId);

}
