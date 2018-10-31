package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.CallbackManager;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.ui.fragment.HomeFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.MyCoursesFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.NewSuggestionFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.ProfileFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.SearchFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.SettingsFragment;
import br.com.meacodeapp.meacodemobile.util.BottomNavigationViewHelper;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @BindView(R.id.main_navigation)
    FloatingActionMenu floatingActionMenu;

    @BindView(R.id.search_option)
    com.github.clans.fab.FloatingActionButton searchMenu;
    @BindView(R.id.suggestion_option)
    com.github.clans.fab.FloatingActionButton suggestionMenu;
    @BindView(R.id.settings_option)
    com.github.clans.fab.FloatingActionButton optionsMenu;

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("size_selected_index", 0);
        edit.putInt("title_size", 24);
        edit.putInt("font_size", 21);
        edit.apply();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        setContentView(R.layout.activity_main);
        setFragment(SearchFragment.newInstance());
        ButterKnife.bind(this);

        final Context context = this;

        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                        .title(R.string.title_size_option)
                        .items(R.array.options_font_size)
                        .itemsCallbackSingleChoice(preferences.getInt("size_selected_index", 0), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                SharedPreferences.Editor edit = preferences.edit();

                                switch (which){
                                    case 0:
                                        edit.remove("size_selected_index");
                                        edit.remove("title_size");
                                        edit.remove("font_size");
                                        edit.putInt("size_selected_index", 0);
                                        edit.putInt("title_size", 21);
                                        edit.putInt("font_size", 18);
                                        break;
                                    case 1:
                                        edit.remove("size_selected_index");
                                        edit.remove("title_size");
                                        edit.remove("font_size");
                                        edit.putInt("size_selected_index", 1);
                                        edit.putInt("title_size", 24);
                                        edit.putInt("font_size", 21);
                                        break;
                                }

                                edit.apply();

                                return false;
                            }
                        })
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .positiveText("APLICAR");

                final MaterialDialog dialog = materialDialog.build();
                dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                dialog.show();

                floatingActionMenu.close(true);
            }
        });

        searchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(SearchFragment.newInstance());
                floatingActionMenu.close(true);
            }
        });

        suggestionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(NewSuggestionFragment.newInstance());
                floatingActionMenu.close(true);
            }
        });
    }

    public void logout(){
        SharedPreferences sharedPreferences = MeAcodeMobileApplication.getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public void googleAuth(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 9001);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Context context = this;
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            socialLogin(account.getEmail(), account.getId(),
                    account.getPhotoUrl().toString(), account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                    .title("Erro")
                    .content("Ocorreu um erro ao buscar informações de sua conta." +
                            "Por favor, tente novamente mais tarde.")
                    .positiveText(R.string.action_ok);

            final MaterialDialog dialog = materialDialog.build();
            dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
            dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
            dialog.show();
        }
    }

    private void socialLogin(String user_id, String token, String image_url, String name){
        RestParameters parameters = new RestParameters();
        parameters.setProperty("user_id", user_id);
        parameters.setProperty("image_url", image_url);
        parameters.setProperty("name", name);
        parameters.setProperty("token", token);

        final Context context = this;

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.show();

        MeAcodeMobileApplication.getInstance().getAuthService().postSocialSignIn(parameters)
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
                        }
                    }

                    @Override
                    public void onFailure(Call<RestParameters> call, Throwable t) {
                        dialog.dismiss();

                        MaterialDialog.Builder materialDialog1 = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao autenticar sua conta. Por favor, tente" +
                                        "novamente mais tarde.").positiveText(R.string.action_ok);

                        final MaterialDialog dialog = materialDialog1.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }
                });
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .addToBackStack(Long.toString(System.currentTimeMillis()));
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
