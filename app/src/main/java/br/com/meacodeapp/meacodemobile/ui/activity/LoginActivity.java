package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.List;
import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.facebook_sign_in_button)
    LoginButton loginButton;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @OnClick(R.id.email_sign_in_button)
    public void emailLogin(){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("email", email.getText().toString());
        parameters.setProperty("password", password.getText().toString());

        MeAcodeMobileApplication.getInstance().getAuthService().postSignIn(parameters)
            .enqueue(new Callback<RestParameters>() {
                @Override
                public void onResponse(Call<RestParameters> call, Response<RestParameters> response) {
                    if(response.code() == 201){

                    }
                }

                @Override
                public void onFailure(Call<RestParameters> call, Throwable t) {

                }
            });
    }

    @OnClick(R.id.facebook_sign_in_button)
    public void facebookLogin(){

    }
}