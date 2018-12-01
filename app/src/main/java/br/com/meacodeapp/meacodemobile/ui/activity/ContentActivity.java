package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.ui.adapter.ContentAdapter;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentActivity extends AppCompatActivity {

    Content content;

    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    @BindView(R.id.content_webview)
    WebView content_webview;

    @BindView(R.id.previous_content)
    AppCompatButton previous_button;

    @BindView(R.id.next_content)
    AppCompatButton next_button;

    @BindView(R.id.go_home)
    AppCompatButton home_button;

    @OnClick(R.id.previous_content)
    public void goBack(){
        finish();
        ContentAdapter.previousContent(this);
    }

    @OnClick(R.id.next_content)
    public void goForward(){
        finish();
        ContentAdapter.nextContent(this);
    }

    @OnClick(R.id.dislike_content)
    public void dislikeContent(){
        RestParameters restParameters = new RestParameters();
        restParameters.setProperty("content_id", Integer.toString(content.getId()));
        restParameters.setProperty("user_id", preferences.getString("user_id", null));
        restParameters.setProperty("score", "1");

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final Context context = this;

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.show();

        MeAcodeMobileApplication.getInstance().getContentService().postRateContent(restParameters)
                .enqueue(new Callback<Content>() {
                    @Override
                    public void onResponse(Call<Content> call, Response<Content> response) {
                        dialog.dismiss();
                        final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(context)
                                .title(R.string.rate_sent_title)
                                .content(R.string.rate_sent_message)
                                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                .positiveText(R.string.action_ok);

                        final MaterialDialog dialog = errorMessageBuilder.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<Content> call, Throwable t) {
                        dialog.dismiss();

                        final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(context)
                                .title(R.string.title_error)
                                .content(R.string.rate_sent_message_error)
                                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                .positiveText(R.string.action_ok);

                        final MaterialDialog dialog = errorMessageBuilder.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }
                });
    }

    @OnClick(R.id.like_content)
    public void likeContent(){
        RestParameters restParameters = new RestParameters();
        final Context context = this;
        restParameters.setProperty("content_id", Integer.toString(content.getId()));
        restParameters.setProperty("user_id", preferences.getString("user_id", null));
        restParameters.setProperty("score", "5");

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

        MeAcodeMobileApplication.getInstance().getContentService().postRateContent(restParameters)
                .enqueue(new Callback<Content>() {
                            @Override
                            public void onResponse(Call<Content> call, Response<Content> response) {
                                dialog.dismiss();
                        final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(context)
                                .title(R.string.rate_sent_title)
                                .content(R.string.rate_sent_message)
                                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                .positiveText(R.string.action_ok);

                        final MaterialDialog dialog = errorMessageBuilder.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<Content> call, Throwable t) {
                        dialog.dismiss();

                        final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(context)
                                .title(R.string.title_error)
                                .content(R.string.rate_sent_message_error)
                                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                .positiveText(R.string.action_ok);

                        final MaterialDialog dialog = errorMessageBuilder.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }
                });
    }

    @OnClick(R.id.go_home)
    public void goHome(){

        final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(this)
                .title(R.string.title_finish_course)
                .content(R.string.message_finish_course)
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startHomeActivity();
                    }
                })
                .positiveText(R.string.action_ok);

        final MaterialDialog errorDialog = errorMessageBuilder.build();
        errorDialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        errorDialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
        errorDialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        errorDialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        errorDialog.show();
    }

    public void startHomeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);

        previous_button.setTextSize(preferences.getInt("font_size", 18));
        next_button.setTextSize(preferences.getInt("font_size", 18));
        home_button.setTextSize(preferences.getInt("font_size", 18));

        if(preferences.getInt("current_content", 0) == 0){
            previous_button.setVisibility(View.INVISIBLE);
        }

        if(preferences.getInt("current_content", 0) >= preferences.getInt("last_content", 0)){
            next_button.setVisibility(View.GONE);
            home_button.setVisibility(View.VISIBLE);
        }

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


        Bundle bundle = getIntent().getExtras();
        content = JsonConverter.fromJson(bundle.getString("content"), Content.class);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_textview, null);

        ((TextView) v.findViewById(R.id.title_textview)).setText(content.getTitle());
        ((TextView) v.findViewById(R.id.title_textview)).setTextSize(preferences.getInt("title_size",  21));
        this.getSupportActionBar().setCustomView(v);

        getSupportActionBar().setTitle(content.getTitle());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set the font size for the webview
        final WebSettings webSettings = content_webview.getSettings();
        webSettings.setDefaultFontSize(preferences.getInt("font_size", 18));
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setNeedInitialFocus(false);
        content_webview.setBackgroundColor(Color.TRANSPARENT);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        content_webview.loadDataWithBaseURL(MeAcodeMobileApplication.getURL(), content.getText(), "text/html", "utf8", null);
        dialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
