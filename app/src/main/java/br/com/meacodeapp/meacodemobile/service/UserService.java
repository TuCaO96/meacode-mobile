package br.com.meacodeapp.meacodemobile.service;

import java.util.List;

import br.com.meacodeapp.meacodemobile.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by usuario on 10/08/2018.
 */

public interface UserService {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/token/{token}")
    Call<User> getUserByToken(@Path("token") String token);

}
