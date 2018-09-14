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
}
