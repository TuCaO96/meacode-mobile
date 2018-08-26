package br.com.meacodeapp.meacodemobile.model;

/**
 * Created by usuario on 10/08/2018.
 */

public class Attach {
    private int id;
    private String path;
    private String url;
    private String name;
    private String mime_type;
    private String created_at;
    private String updated_at;

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getMime_type() {
        return mime_type;
    }
}
