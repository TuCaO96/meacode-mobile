package br.com.meacodeapp.meacodemobile.service;

import br.com.meacodeapp.meacodemobile.model.User;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<RestParameters> postSignIn(@Body RestParameters parameters);

    @POST("auth/signup")
    Call<User> postSignUp(@Body RestParameters parameters);

    @POST("auth/forgot")
    Call<User> postForgotPassword(@Body RestParameters parameters);
}
