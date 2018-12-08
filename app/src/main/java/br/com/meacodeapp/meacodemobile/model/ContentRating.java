package br.com.meacodeapp.meacodemobile.model;

public class ContentRating {
    private int id;
    private int user_id;
    private int content_id;
    private int score;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getContentId() {
        return content_id;
    }

    public int getScore() {
        return score;
    }
}
