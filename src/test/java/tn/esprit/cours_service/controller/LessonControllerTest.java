package tn.esprit.cours_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.cours_service.Controller.LessonController;
import tn.esprit.cours_service.Service.LessonService;
import tn.esprit.cours_service.entities.Course;
import tn.esprit.cours_service.entities.Lesson;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    private Lesson lesson;
    private Course course;

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
    void createLesson_whenCourseExists_shouldReturn201() throws Exception {
        when(lessonService.createLesson(eq(1), any(Lesson.class))).thenReturn(lesson);

        mockMvc.perform(post("/api/courses/1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lessontitle").value("Introduction"));
    }

    @Test
    void createLesson_whenCourseNotExists_shouldReturn404() throws Exception {
        when(lessonService.createLesson(eq(99), any(Lesson.class))).thenReturn(null);

        mockMvc.perform(post("/api/courses/99/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLessonById_whenExists_shouldReturn200() throws Exception {
        when(lessonService.getLessonById(1)).thenReturn(lesson);

        mockMvc.perform(get("/api/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lessontitle").value("Introduction"));
    }

    @Test
    void getLessonById_whenNotExists_shouldReturn404() throws Exception {
        when(lessonService.getLessonById(99)).thenReturn(null);

        mockMvc.perform(get("/api/lessons/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllLessons_shouldReturn200WithList() throws Exception {
        Lesson lesson2 = new Lesson();
        lesson2.setId(2);
        lesson2.setLessontitle("Dependency Injection");

        when(lessonService.getAllLessons()).thenReturn(Arrays.asList(lesson, lesson2));

        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].lessontitle").value("Introduction"))
                .andExpect(jsonPath("$[1].lessontitle").value("Dependency Injection"));
    }

    @Test
    void getLessonsByCourse_shouldReturn200WithList() throws Exception {
        when(lessonService.getLessonsByCourse(1)).thenReturn(Arrays.asList(lesson));

        mockMvc.perform(get("/api/courses/1/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].lessontitle").value("Introduction"));
    }

    @Test
    void updateLesson_whenExists_shouldReturn200() throws Exception {
        Lesson updated = new Lesson();
        updated.setId(1);
        updated.setLessontitle("Advanced DI");
        updated.setContent("Deep dive into DI");
        updated.setLessonorder(2);

        when(lessonService.updateLesson(eq(1), any(Lesson.class))).thenReturn(updated);

        mockMvc.perform(put("/api/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessontitle").value("Advanced DI"))
                .andExpect(jsonPath("$.lessonorder").value(2));
    }

    @Test
    void updateLesson_whenNotExists_shouldReturn404() throws Exception {
        when(lessonService.updateLesson(eq(99), any(Lesson.class))).thenReturn(null);

        mockMvc.perform(put("/api/lessons/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLesson_shouldReturn204() throws Exception {
        doNothing().when(lessonService).deleteLesson(1);

        mockMvc.perform(delete("/api/lessons/1"))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).deleteLesson(1);
    }
}
