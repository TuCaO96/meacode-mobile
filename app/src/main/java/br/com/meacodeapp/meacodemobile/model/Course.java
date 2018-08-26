package br.com.meacodeapp.meacodemobile.model;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    private int id;
    private String name;
    private Attach image;
    private List<Content> contents;
    private String created_at;
    private String updated_at;

    public String getName() {
        return name;
    }

    public Attach getImage() {
        return image;
    }

    public List<Content> getContents() {
        return contents;
    }
}
