package br.com.meacodeapp.meacodemobile.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.ui.adapter.IntroStepsAdapter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity {

    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    @BindView(R.id.fragment_view_pager)
    ViewPager viewPager;

    @BindView(R.id.button_jump)
    Button button_skip;

    @BindView(R.id.button_jump2)
    Button button_skip2;

    @BindView(R.id.button_finish)
    ImageButton button_finish;

    @BindView(R.id.button_restart)
    Button button_restart;

    IntroStepsAdapter pagerAdapter;

    @OnClick(R.id.button_finish)
    public void onFinish(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_jump)
    public void skip(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_jump2)
    public void skip2(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_restart)
    public void restart(){
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        authenticate();

        FragmentManager fragmentManager = getSupportFragmentManager();

        pagerAdapter = new IntroStepsAdapter(fragmentManager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 16){
                    button_finish.setVisibility(View.VISIBLE);
                    button_restart.setVisibility(View.VISIBLE);
                    button_skip.setVisibility(View.INVISIBLE);
                }
                else{
                    if(position > 3 && position < 7){
                        button_skip.setVisibility(View.INVISIBLE);
                        button_skip2.setVisibility(View.VISIBLE);
                    }
                    else{
                        button_skip.setVisibility(View.VISIBLE);
                        button_skip2.setVisibility(View.INVISIBLE);
                    }

                    button_finish.setVisibility(View.INVISIBLE);
                    button_restart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() > 0){
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void authenticate(){

        if(preferences.getString("user_id", null) == null){
            RestParameters parameters = new RestParameters();
            final Context context = this;
            parameters.setProperty("sn", Long.toString(System.currentTimeMillis()));

            final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                    .title(R.string.title_loading)
                    .content(R.string.message_loading)
                    .progress(true,0,false);

            final MaterialDialog dialog = materialDialog.build();
            dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
            dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
            dialog.show();

            MeAcodeMobileApplication.getInstance().getAuthService().postAuthenticateMobile(parameters)
                    .enqueue(new Callback<RestParameters>() {
                        @Override
                        public void onResponse(Call<RestParameters> call, Response<RestParameters> response) {
                            dialog.dismiss();

                            if (response.code() == 200){
                                final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                                        .getInstance()
                                        .getSharedPreferences("session", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor edit = sharedPreferences.edit();

                                edit.remove("token");
                                edit.remove("user_id");
                                edit.putString("token",response.body().getProperty("token"));
                                edit.putString("user_id",response.body().getProperty("user_id"));
                                edit.apply();
                            }
                        }

                        @Override
                        public void onFailure(Call<RestParameters> call, Throwable t) {
                            dialog.dismiss();

                            MaterialDialog.Builder materialDialog1 = new MaterialDialog.Builder(context)
                                    .title("Erro")
                                    .content("Ocorreu um erro ao conectar com nossos serviores. Por favor, verifique sua conexão com a internet")
                                    .positiveText(R.string.action_ok);

                            final MaterialDialog dialog = materialDialog1.build();
                            dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                            dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                            dialog.show();
                        }
                    });
        }
    }

    public void nextStep(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void goRelatedCourses(){
        viewPager.setCurrentItem(17);
    }

    public void goCourseDetails(){
        viewPager.setCurrentItem(4);
    }

    public void goMenuOptions(){
        viewPager.setCurrentItem(8);
    }

    public IntroStepsAdapter getPagerAdapter() {
        return pagerAdapter;
    }
}
