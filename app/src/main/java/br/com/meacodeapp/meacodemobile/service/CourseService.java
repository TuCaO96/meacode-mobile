package br.com.meacodeapp.meacodemobile.service;

import java.util.List;

import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.model.SearchResult;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by usuario on 10/08/2018.
 */

public interface CourseService {
    @GET("courses")
    Call<List<Course>> getCourses();

    @GET("courses/{course}/categories/{category}")
    Call<SearchResult> getRelatedCourses(@Path("course")String course_id, @Path("category") String category_id);

    @POST("courses/rate")
    Call<Course> postRateCourse(@Body RestParameters parameters);

    @GET("courses/{course}")
    Call<Course> getCourse(@Path("course") int id);
}
