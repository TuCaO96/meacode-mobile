package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

    private AccessToken mAccessToken;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mAccessToken = loginResult.getAccessToken();
                getUserProfile(mAccessToken);
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        /*try {
                            Gson gson = new Gson();
                            object.getJSONObject(“picture”).
                            getJSONObject("data").getString("url");
                            object.getString(“name”);
                            object.getString(“email”));
                            object.getString(“id”));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode,  data);
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