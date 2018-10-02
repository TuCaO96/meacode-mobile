package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
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

    @BindView(R.id.email_sign_in)
    Button emailSignInButton;

    @BindView(R.id.email_sign_up_button)
    Button emailSignUpButton;

    @BindView(R.id.email_forgot_button)
    Button forgotButton;

    @BindView(R.id.tlbr_login_title)
    TextView toolbar_title;

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
        toolbar_title.setTextSize(24);
        email.setTextSize(21);
        password.setTextSize(21);
        emailSignInButton.setTextSize(21);
        forgotButton.setTextSize(21);
        emailSignUpButton.setTextSize(21);
        toolbar_title.setText("Entrar com email e senha");
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
                        if(response.code() == 200){
                            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                                    .title("Um email foi enviado à você")
                                    .content("Abra o link do email para redefinir sua senha.")
                                    .positiveText("OK");

                            final MaterialDialog dialog = materialDialog.build();
                            dialog.getTitleView().setTextSize(24);
                            dialog.getContentView().setTextSize(21);
                            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao criar conta. Por favor, verifique" +
                                        "se o email e senha são válidos.")
                                .positiveText("OK");

                        final MaterialDialog dialog = materialDialog.build();
                        dialog.getTitleView().setTextSize(24);
                        dialog.getContentView().setTextSize(21);
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                        dialog.show();
                    }
                });
    }

    @OnClick(R.id.email_sign_up_button)
    public void signUp(){
        RestParameters parameters = new RestParameters();
        final Context context = this;
        parameters.setProperty("email", email.getText().toString());
        parameters.setProperty("password", password.getText().toString());

        final MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false)
                .show();

        MeAcodeMobileApplication.getInstance().getAuthService().postSignUp(parameters)
                .enqueue(new Callback<RestParameters>() {
                    @Override
                    public void onResponse(Call<RestParameters> call, Response<RestParameters> response) {
                        materialDialog.dismiss();

                        if (response.code() == 200){
                            final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                                    .getInstance()
                                    .getSharedPreferences("session", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor edit = sharedPreferences.edit();

                            edit.remove("token");
                            edit.putString("token",response.body().getProperty("token"));
                            edit.apply();

                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<RestParameters> call, Throwable t) {
                        materialDialog.dismiss();

                        MaterialDialog.Builder materialDialog1 = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao criar conta. Por favor, verifique" +
                                        "se o email e senha são válidos.")
                                .positiveText("OK");

                        final MaterialDialog dialog = materialDialog1.build();
                        dialog.getTitleView().setTextSize(24);
                        dialog.getContentView().setTextSize(21);
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                        dialog.show();
                    }
                });
    }

    @OnClick(R.id.email_sign_in)
    public void emailLogin(){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("email", email.getText().toString());
        parameters.setProperty("password", password.getText().toString());
        final Context context = this;

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(24);
        dialog.getContentView().setTextSize(21);
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
        dialog.show();

        MeAcodeMobileApplication.getInstance().getAuthService().postSignIn(parameters)
                .enqueue(new Callback<RestParameters>() {
                    @Override
                    public void onResponse(Call<RestParameters> call, Response<RestParameters> response) {
                        dialog.dismiss();

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
                        dialog.dismiss();

                        MaterialDialog.Builder materialDialog1 = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao autenticar sua conta. Por favor, verifique" +
                                        "se o email e senha são válidos.")
                                .positiveText("OK");

                        final MaterialDialog dialog = materialDialog1.build();
                        dialog.getTitleView().setTextSize(24);
                        dialog.getContentView().setTextSize(21);
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                        dialog.show();
                    }
                });
    }
}
