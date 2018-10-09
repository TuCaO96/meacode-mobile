package br.com.meacodeapp.meacodemobile.model;

/**
 * Created by usuario on 10/08/2018.
 */

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String username;
    private String image_url;
    private String email;
    private String type;
    private String status;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFullName(){
        return first_name + " " + last_name;
    }
}
