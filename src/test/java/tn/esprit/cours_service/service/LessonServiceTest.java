package tn.esprit.cours_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.cours_service.Repository.CourseRepository;
import tn.esprit.cours_service.Repository.LessonRepository;
import tn.esprit.cours_service.Service.LessonService;
import tn.esprit.cours_service.entities.Course;
import tn.esprit.cours_service.entities.Lesson;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonService lessonService;

    private Course course;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1);
        course.setTitle("Spring Boot");

        lesson = new Lesson();
        lesson.setId(1);
        lesson.setLessontitle("Introduction");
        lesson.setContent("Welcome to Spring Boot");
        lesson.setLessonorder(1);
        lesson.setCourse(course);
    }

    @Test
    void createLesson_whenCourseExists_shouldReturnLesson() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = lessonService.createLesson(1, lesson);

        assertNotNull(result);
        assertEquals("Introduction", result.getLessontitle());
        assertEquals(course, result.getCourse());
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void createLesson_whenCourseNotExists_shouldReturnNull() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        Lesson result = lessonService.createLesson(99, lesson);

        assertNull(result);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void getLessonById_whenExists_shouldReturnLesson() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));

        Lesson result = lessonService.getLessonById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Introduction", result.getLessontitle());
    }

    @Test
    void getLessonById_whenNotExists_shouldReturnNull() {
        when(lessonRepository.findById(99)).thenReturn(Optional.empty());

        Lesson result = lessonService.getLessonById(99);

        assertNull(result);
    }

    @Test
    void getAllLessons_shouldReturnList() {
        Lesson lesson2 = new Lesson();
        lesson2.setId(2);
        lesson2.setLessontitle("Dependency Injection");

        when(lessonRepository.findAll()).thenReturn(Arrays.asList(lesson, lesson2));

        List<Lesson> result = lessonService.getAllLessons();

        assertEquals(2, result.size());
        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    void getLessonsByCourse_shouldReturnLessonsForCourse() {
        when(lessonRepository.findByCourseId(1)).thenReturn(Arrays.asList(lesson));

        List<Lesson> result = lessonService.getLessonsByCourse(1);

        assertEquals(1, result.size());
        assertEquals("Introduction", result.get(0).getLessontitle());
        verify(lessonRepository, times(1)).findByCourseId(1);
    }

    @Test
    void updateLesson_whenExists_shouldReturnUpdatedLesson() {
        Lesson updatedDetails = new Lesson();
        updatedDetails.setLessontitle("Advanced DI");
        updatedDetails.setContent("Deep dive into DI");
        updatedDetails.setLessonorder(2);

        when(lessonRepository.findById(1)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = lessonService.updateLesson(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Advanced DI", result.getLessontitle());
        assertEquals("Deep dive into DI", result.getContent());
        assertEquals(2, result.getLessonorder());
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    void updateLesson_whenNotExists_shouldReturnNull() {
        when(lessonRepository.findById(99)).thenReturn(Optional.empty());

        Lesson result = lessonService.updateLesson(99, new Lesson());

        assertNull(result);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void deleteLesson_shouldCallDeleteById() {
        doNothing().when(lessonRepository).deleteById(1);

        lessonService.deleteLesson(1);

        verify(lessonRepository, times(1)).deleteById(1);
    }
}
