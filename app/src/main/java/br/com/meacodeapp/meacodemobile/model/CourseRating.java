package br.com.meacodeapp.meacodemobile.model;

public class CourseRating {
    private int id;
    private int user_id;
    private int course_id;
    private String comments;
    private int score;
    private String created_at;
    private String updated_at;

    public int getUserId() {
        return user_id;
    }

    public int getCourseId() {
        return course_id;
    }

    public String getComments() {
        return comments;
    }

    public int getScore() {
        return score;
    }
}
