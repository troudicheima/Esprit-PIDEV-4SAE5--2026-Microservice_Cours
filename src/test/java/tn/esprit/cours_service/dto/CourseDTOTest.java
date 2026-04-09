package tn.esprit.cours_service.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseDTOTest {

    @Test
    void defaultConstructor_shouldCreateEmptyDTO() {
        CourseDTO dto = new CourseDTO();
        assertEquals(0, dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getLevel());
        assertEquals(0, dto.getDuration());
        assertNull(dto.getDescription());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        CourseDTO dto = new CourseDTO(1, "Spring Boot", "Intermediate", 10, "Learn Spring Boot");

        assertEquals(1, dto.getId());
        assertEquals("Spring Boot", dto.getTitle());
        assertEquals("Intermediate", dto.getLevel());
        assertEquals(10, dto.getDuration());
        assertEquals("Learn Spring Boot", dto.getDescription());
    }

    @Test
    void setters_shouldUpdateFields() {
        CourseDTO dto = new CourseDTO();
        dto.setId(2);
        dto.setTitle("Microservices");
        dto.setLevel("Advanced");
        dto.setDuration(20);
        dto.setDescription("Build microservices");

        assertEquals(2, dto.getId());
        assertEquals("Microservices", dto.getTitle());
        assertEquals("Advanced", dto.getLevel());
        assertEquals(20, dto.getDuration());
        assertEquals("Build microservices", dto.getDescription());
    }

    @Test
    void setTitle_withNull_shouldAllowNull() {
        CourseDTO dto = new CourseDTO();
        dto.setTitle(null);
        assertNull(dto.getTitle());
    }
}
