package br.com.meacodeapp.meacodemobile.model;

import java.util.List;

/**
 * Created by usuario on 10/08/2018.
 */

public class Content {
    private int id;
    private String title;
    private String text;
    private User user;
    private List<Attach> attaches;
    private List<ContentRating> ratings;
    private Course course;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public List<Attach> getAttaches() {
        return attaches;
    }

    public List<ContentRating> getRatings() {
        return ratings;
    }

    public Course getCourse() {
        return course;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
