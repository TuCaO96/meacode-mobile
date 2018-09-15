package br.com.meacodeapp.meacodemobile.service;

import java.util.List;

import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.Course;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by usuario on 10/08/2018.
 */

public interface CourseService {
    @GET("courses")
    Call<List<Course>> getCourses();

    @GET("courses/{course}")
    Call<Course> getCourse(@Path("course") int id);
}
