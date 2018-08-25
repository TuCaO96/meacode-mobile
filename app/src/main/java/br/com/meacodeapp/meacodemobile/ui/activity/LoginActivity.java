package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.zip.Inflater;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.User;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tlbr_login_title)
    TextView loginTitle;

    @BindView(R.id.facebook_sign_in_button)
    LoginButton loginButton;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;

    @BindView(R.id.google_sign_in_button)
    SignInButton googleSignInButton;

    @BindView(R.id.email_sign_up_button)
    Button emailSignUpButton;

    @BindView(R.id.email_forgot_button)
    Button forgotButton;

    private final Context context = this;

    private GoogleSignInClient mGoogleSignInClient;
    private AccessToken mAccessToken;
    private Toolbar toolbar;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private ActionBar ab;
    private TextView tv;
    private int actionBarTextSize = 24;
    private int textViewTextSize = 21;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tlbr_zoom_in:
                actionBarTextSize++;
                textViewTextSize++;
                setActionBarTextSizeSp(actionBarTextSize);
                setTextViewTextSize(textViewTextSize);
                return true;
            case R.id.tlbr_zoom_out:
                actionBarTextSize--;
                textViewTextSize--;
                setActionBarTextSizeSp(actionBarTextSize);
                setTextViewTextSize(textViewTextSize);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setActionBarTextSizeSp(int size){
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.activity_login, null);

        //if you need to customize anything else about the text, do it here.
        //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
        ((TextView) v.findViewById(R.id.tlbr_login_title)).setText(this.getTitle());
        ((TextView) v.findViewById(R.id.tlbr_login_title)).setTextSize(size);


        //assign the view to the actionbar
        this.getSupportActionBar().setCustomView(v);
    }
    protected void setTextViewTextSize(int size){
        email.setTextSize(size);
        password.setTextSize(size);
        loginButton.setTextSize(size);
        emailSignInButton.setTextSize(size);
        emailSignUpButton.setTextSize(size);
        forgotButton.setTextSize(size);
//        googleSignInButton.setSize(size);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.tlbr_login);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Test");

        setActionBarTextSizeSp(actionBarTextSize);
        setTextViewTextSize(textViewTextSize);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @OnClick(R.id.email_forgot_button)
    public void forgotPassword(){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("email", email.getText().toString());

        MeAcodeMobileApplication.getInstance().getAuthService().postForgotPassword(parameters)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code() == 201){
                            new MaterialDialog.Builder(context)
                                    .title("Um email foi enviado à você")
                                    .content("Abra o link do email para redefinir sua senha.").show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao criar conta. Por favor, verifique" +
                                        "se o email e senha são válidos.").show();
                    }
                });
    }

    @OnClick(R.id.email_sign_up_button)
    public void signUp(){
        RestParameters parameters = new RestParameters();
        final Context context = getBaseContext();
        parameters.setProperty("email", email.getText().toString());
        parameters.setProperty("password", password.getText().toString());
        MeAcodeMobileApplication.getInstance().getAuthService().postSignUp(parameters)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 201){

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao criar conta. Por favor, verifique" +
                                        "se o email e senha são válidos.").show();
                    }
                });
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        final Context context = this;
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{
                            String facebook_id = object.getString("id");
                            new MaterialDialog.Builder(context).title("Result")
                                    .content(facebook_id).show();
                        }
                        catch (JSONException e){
                            new MaterialDialog.Builder(context).title("Error")
                                    .content(e.getMessage()).show();
                        }
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

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if(requestCode == 9001){
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @OnClick(R.id.google_sign_in_button)
    public void googleLogin(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 9001);
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
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<RestParameters> call, Throwable t) {
                    new MaterialDialog.Builder(context)
                            .title("Erro")
                            .content("Ocorreu um erro ao autenticar sua conta. Por favor, verifique" +
                                    "se o email e senha são válidos.").show();
                }
            });
    }

    @OnClick(R.id.facebook_sign_in_button)
    public void facebookLogin(){

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            new MaterialDialog.Builder(this).title("Result").content(account.getEmail()).show();
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            updateUI(null);
        }
    }
}