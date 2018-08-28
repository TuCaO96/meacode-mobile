package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

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

public class LoginEmailActivity extends AppCompatActivity {

    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;

    @BindView(R.id.email_sign_up_button)
    Button emailSignUpButton;

    @BindView(R.id.email_forgot_button)
    Button forgotButton;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    private ActionBar ab;
    private TextView tv;
    private int actionBarTextSize = 24;
    private int textViewTextSize = 21;

    protected void setTextViewTextSize(int size){
        email.setTextSize(size);
        password.setTextSize(size);
        emailSignInButton.setTextSize(size);
        emailSignUpButton.setTextSize(size);
        forgotButton.setTextSize(size);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.email_forgot_button)
    public void forgotPassword(){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("email", email.getText().toString());
        final Context context = this;

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
        final Context context = this;
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

    @OnClick(R.id.email_sign_in_button)
    public void emailLogin(){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("email", email.getText().toString());
        parameters.setProperty("password", password.getText().toString());
        final Context context = this;

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
}
