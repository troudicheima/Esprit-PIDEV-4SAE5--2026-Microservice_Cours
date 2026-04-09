package tn.esprit.cours_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LessonDTOTest {

    @Test
    void defaultConstructor_shouldCreateEmptyDTO() {
        LessonDTO dto = new LessonDTO();
        assertEquals(0, dto.getId());
        assertNull(dto.getLessontitle());
        assertNull(dto.getContent());
        assertEquals(0, dto.getLessonorder());
        assertEquals(0, dto.getCourseId());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LessonDTO dto = new LessonDTO(1, "Introduction", "Welcome content", 1, 10);

        assertEquals(1, dto.getId());
        assertEquals("Introduction", dto.getLessontitle());
        assertEquals("Welcome content", dto.getContent());
        assertEquals(1, dto.getLessonorder());
        assertEquals(10, dto.getCourseId());
    }

    @Test
    void setters_shouldUpdateFields() {
        LessonDTO dto = new LessonDTO();
        dto.setId(3);
        dto.setLessontitle("Advanced Topics");
        dto.setContent("Deep dive content");
        dto.setLessonorder(5);
        dto.setCourseId(2);

        assertEquals(3, dto.getId());
        assertEquals("Advanced Topics", dto.getLessontitle());
        assertEquals("Deep dive content", dto.getContent());
        assertEquals(5, dto.getLessonorder());
        assertEquals(2, dto.getCourseId());
    }

    @Test
    void setContent_withNull_shouldAllowNull() {
        LessonDTO dto = new LessonDTO();
        dto.setContent(null);
        assertNull(dto.getContent());
    }
}
