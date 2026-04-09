package tn.esprit.cours_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.cours_service.Controller.CourseController;
import tn.esprit.cours_service.Service.CourseService;
import tn.esprit.cours_service.Service.NotificationService;
import tn.esprit.cours_service.entities.Course;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void createCourse_shouldReturn201WithCourse() throws Exception {
        when(courseService.createCourse(any(Course.class))).thenReturn(course);
        doNothing().when(notificationService).notifyCourseCreated(any(Course.class));

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot"))
                .andExpect(jsonPath("$.level").value("Intermediate"));

        verify(notificationService, times(1)).notifyCourseCreated(any(Course.class));
    }

    @Test
    void getCourseById_whenExists_shouldReturn200() throws Exception {
        when(courseService.getCourseById(1)).thenReturn(course);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot"));
    }

    @Test
    void getCourseById_whenNotExists_shouldReturn404() throws Exception {
        when(courseService.getCourseById(99)).thenReturn(null);

        mockMvc.perform(get("/api/courses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCourses_shouldReturn200WithList() throws Exception {
        Course course2 = new Course();
        course2.setId(2);
        course2.setTitle("Microservices");

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course, course2));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Spring Boot"))
                .andExpect(jsonPath("$[1].title").value("Microservices"));
    }

    @Test
    void updateCourse_whenExists_shouldReturn200() throws Exception {
        Course updated = new Course();
        updated.setId(1);
        updated.setTitle("Advanced Spring Boot");
        updated.setLevel("Advanced");
        updated.setDuration(20);
        updated.setDescription("Deep dive");

        when(courseService.updateCourse(eq(1), any(Course.class))).thenReturn(updated);

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Advanced Spring Boot"))
                .andExpect(jsonPath("$.level").value("Advanced"));
    }

    @Test
    void updateCourse_whenNotExists_shouldReturn404() throws Exception {
        when(courseService.updateCourse(eq(99), any(Course.class))).thenReturn(null);

        mockMvc.perform(put("/api/courses/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCourse_shouldReturn204() throws Exception {
        doNothing().when(courseService).deleteCourse(1);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourse(1);
    }
}
