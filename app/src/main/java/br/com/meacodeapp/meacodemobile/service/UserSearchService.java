package br.com.meacodeapp.meacodemobile.service;

import java.util.List;

import br.com.meacodeapp.meacodemobile.model.SearchResult;
import br.com.meacodeapp.meacodemobile.model.User;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by usuario on 10/08/2018.
 */

public interface UserSearchService {
    @POST("users-search/query")
    Call<SearchResult> postUserSearch(@Body RestParameters parameters);
}
