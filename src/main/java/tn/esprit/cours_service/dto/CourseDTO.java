package tn.esprit.cours_service.dto;

public class CourseDTO {

    private int id;
    private String title;
    private String level;
    private int duration;
    private String description;

    public CourseDTO() {}

    public CourseDTO(int id, String title, String level, int duration, String description) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.duration = duration;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
