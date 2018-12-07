package br.com.meacodeapp.meacodemobile.service;

import java.util.List;

import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.ContentRating;
import br.com.meacodeapp.meacodemobile.model.Suggestion;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by usuario on 10/08/2018.
 */

public interface ContentService {
    @GET("contents")
    Call<List<Content>> getContents();

    @POST("contents/rate")
    Call<Content> postRateContent(@Body RestParameters parameters);

    @GET("contents/{content}/rating/{user}")
    Call<ContentRating> getContentUserRating(@Path("content") int id, @Path("user") int user_id);

    @GET("contents/{content}")
    Call<Content> getContent(@Path("content") int id);
}
