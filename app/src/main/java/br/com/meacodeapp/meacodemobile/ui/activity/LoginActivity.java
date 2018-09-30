package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    @BindView(R.id.email_sign_in_button)
    Button loginButton;

    @BindView(R.id.facebook_sign_in_button)
    LoginButton facebookLoginButton;

    @BindView(R.id.google_sign_in_button)
    SignInButton googleSignInButton;

    private final Context context = this;

    private GoogleSignInClient mGoogleSignInClient;
    private AccessToken mAccessToken;
    private Toolbar toolbar;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private ActionBar ab;
    private TextView tv;
    private int actionBarTextSize = 24;
    private int currentIncrease = 0;
    private int maxIncrease = 2;
    private int minIncrease = 0;
    private int textViewTextSize = 21;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tlbr_zoom_in:
                if(currentIncrease == maxIncrease){
                    new MaterialDialog.Builder(context)
                            .title("Atenção")
                            .content("Tamanho máximo para textos atingido :(")
                            .positiveText(R.string.action_ok).show();
                    return true;
                }

                currentIncrease++;
                actionBarTextSize++;
                textViewTextSize++;
                setActionBarTextSizeSp(actionBarTextSize);
                setTextViewTextSize(textViewTextSize);
                return true;
            case R.id.tlbr_zoom_out:
                if(currentIncrease == minIncrease){
                    new MaterialDialog.Builder(context)
                            .title("Atenção")
                            .content("Tamanho mínimo para textos atingido :(")
                            .positiveText(R.string.action_ok).show();
                    return true;
                }

                currentIncrease--;
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
        facebookLoginButton.setTextSize(size);
        loginButton.setTextSize(size);
//        googleSignInButton.setSize(size);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("token") && sharedPreferences.contains("user")){
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.tlbr_login);
        setSupportActionBar(toolbar);

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

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

    private void getUserProfile(AccessToken currentAccessToken) {
        final Context context = this;
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{
                            socialLogin(object.getString("id"), object.getString("id"));
                        }
                        catch (JSONException e){
                            new MaterialDialog.Builder(context).title("Erro ao buscar informações" +
                                    "do Facebook. Por favor, tente novamente mais tarde")
                                    .content(e.getMessage()).show();
                        }
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
        Intent emailSignInIntent = new Intent(this, LoginEmailActivity.class);
        startActivity(emailSignInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            socialLogin(account.getEmail(), account.getId());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            new MaterialDialog.Builder(context)
                    .title("Erro")
                    .content("Ocorreu um erro ao buscar informações de sua conta." +
                            "Por favor, tente novamente mais tarde.")
                    .positiveText(R.string.action_ok).show();
        }
    }

    private void socialLogin(String user_id, String token){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("user_id", user_id);
        parameters.setProperty("token", token);
        final Context context = this;

        final MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false)
                .show();

        MeAcodeMobileApplication.getInstance().getAuthService().postSocialSignIn(parameters)
                .enqueue(new Callback<RestParameters>() {
                    @Override
                    public void onResponse(Call<RestParameters> call, Response<RestParameters> response) {
                        materialDialog.dismiss();
                        if(response.code() == 200){
                            final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                                    .getInstance()
                                    .getSharedPreferences("session", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor edit = sharedPreferences.edit();

                            edit.remove("token");
                            edit.remove("user");
                            edit.putString("token",response.body().getProperty("token"));
                            edit.putString("user",response.body().getProperty("user"));
                            edit.apply();


                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<RestParameters> call, Throwable t) {
                        materialDialog.dismiss();

                        new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao autenticar sua conta. Por favor, tente" +
                                        "novamente mais tarde.").positiveText(R.string.action_ok).show();
                    }
                });
    }
}