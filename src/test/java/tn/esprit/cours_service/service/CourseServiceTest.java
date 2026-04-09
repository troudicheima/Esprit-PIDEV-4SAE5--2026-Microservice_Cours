package tn.esprit.cours_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.cours_service.Repository.CourseRepository;
import tn.esprit.cours_service.Service.CourseService;
import tn.esprit.cours_service.entities.Course;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1);
        course.setTitle("Spring Boot");
        course.setLevel("Intermediate");
        course.setDuration(10);
        course.setDescription("Learn Spring Boot");
    }

    @Test
    void createCourse_shouldReturnSavedCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.createCourse(course);

        assertNotNull(result);
        assertEquals("Spring Boot", result.getTitle());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void getCourseById_whenExists_shouldReturnCourse() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Spring Boot", result.getTitle());
    }

    @Test
    void getCourseById_whenNotExists_shouldReturnNull() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        Course result = courseService.getCourseById(99);

        assertNull(result);
    }

    @Test
    void getAllCourses_shouldReturnList() {
        Course course2 = new Course();
        course2.setId(2);
        course2.setTitle("Microservices");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course, course2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void updateCourse_whenExists_shouldReturnUpdatedCourse() {
        Course updatedDetails = new Course();
        updatedDetails.setTitle("Advanced Spring Boot");
        updatedDetails.setLevel("Advanced");
        updatedDetails.setDuration(20);
        updatedDetails.setDescription("Deep dive into Spring Boot");

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.updateCourse(1, updatedDetails);

        assertNotNull(result);
        assertEquals("Advanced Spring Boot", result.getTitle());
        assertEquals("Advanced", result.getLevel());
        assertEquals(20, result.getDuration());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void updateCourse_whenNotExists_shouldReturnNull() {
        when(courseRepository.findById(99)).thenReturn(Optional.empty());

        Course result = courseService.updateCourse(99, new Course());

        assertNull(result);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourse_shouldCallDeleteById() {
        doNothing().when(courseRepository).deleteById(1);

        courseService.deleteCourse(1);

        verify(courseRepository, times(1)).deleteById(1);
    }
}
