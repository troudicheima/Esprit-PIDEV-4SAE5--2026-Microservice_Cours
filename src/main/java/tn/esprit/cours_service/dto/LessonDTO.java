package tn.esprit.cours_service.dto;

public class LessonDTO {

    private int id;
    private String lessontitle;
    private String content;
    private int lessonorder;
    private int courseId;

    public LessonDTO() {}

    public LessonDTO(int id, String lessontitle, String content, int lessonorder, int courseId) {
        this.id = id;
        this.lessontitle = lessontitle;
        this.content = content;
        this.lessonorder = lessonorder;
        this.courseId = courseId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLessontitle() { return lessontitle; }
    public void setLessontitle(String lessontitle) { this.lessontitle = lessontitle; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLessonorder() { return lessonorder; }
    public void setLessonorder(int lessonorder) { this.lessonorder = lessonorder; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
}
