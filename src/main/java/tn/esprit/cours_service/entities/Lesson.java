package tn.esprit.cours_service.entities;


import jakarta.persistence.*;

@Entity
public class Lesson {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String lessontitle;
    @Column(columnDefinition = "TEXT")
    private String content;
    private int lessonorder;


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public String getLessontitle() {
        return lessontitle;
    }

    public void setLessontitle(String lessontitle) {
        this.lessontitle = lessontitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLessonorder() {
        return lessonorder;
    }

    public void setLessonorder(int lessonorder) {
        this.lessonorder = lessonorder;
    }
}
