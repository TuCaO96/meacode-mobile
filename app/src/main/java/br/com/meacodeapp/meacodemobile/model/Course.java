package br.com.meacodeapp.meacodemobile.model;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    private int id;
    private String name;
    private Category category;
    private List<Content> contents;
    private String image_url;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public List<Content> getContents() {
        return contents;
    }
}
