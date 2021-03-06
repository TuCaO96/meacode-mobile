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
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.ContentRating;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.model.CourseRating;
import br.com.meacodeapp.meacodemobile.model.Rating;
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

    ContentRating contentRating = null;

    boolean isLoading = false;

    MaterialDialog.Builder screenDialog = null;

    Context context = this;

    MaterialDialog screenBuiltDialog = null;

    CourseRating courseRating = null;

    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    @BindView(R.id.useful_content)
    TextView usefulContentTextView;

    @BindView(R.id.content_webview)
    WebView content_webview;

    @BindView(R.id.previous_content)
    AppCompatButton previous_button;

    @BindView(R.id.next_content)
    AppCompatButton next_button;

    @BindView(R.id.go_home)
    AppCompatButton home_button;

    @BindView(R.id.like_content)
    ImageButton likeContent;

    @BindView(R.id.dislike_content)
    ImageButton dislikeContent;

    RestParameters courseParameters = new RestParameters();

    @OnClick(R.id.previous_content)
    public void goBack(){
        if (!isLoading) {
            isLoading = true;
            finish();
            ContentAdapter.previousContent(this);
        }
    }

    @OnClick(R.id.adjust_font)
    public void openAdjustFontSize(){
        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_size_option)
                .customView(R.layout.dialog_single_choice_view, false)
                .positiveColor(getResources().getColor(R.color.colorSecondaryDark))
                .positiveText(R.string.action_apply)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        recreate();
                    }
                });

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.show();

        RadioGroup dialog_selected = dialog.getCustomView().findViewById(R.id.option_size_group);
        RadioButton big = dialog.getCustomView().findViewById(R.id.option_size_big);
        RadioButton normal = dialog.getCustomView().findViewById(R.id.option_size_normal);
        big.setTextSize(preferences.getInt("font_size", 18));
        normal.setTextSize(preferences.getInt("font_size", 18));
        int font_size  = preferences.getInt("title_size", 21);
        dialog_selected.check(font_size == 21 ? R.id.option_size_normal : R.id.option_size_big);
        dialog_selected.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor edit = preferences.edit();

                switch (checkedId){
                    case R.id.option_size_normal:
                        edit.remove("size_selected_index");
                        edit.remove("title_size");
                        edit.remove("font_size");
                        edit.putInt("size_selected_index", 0);
                        edit.putInt("title_size", 21);
                        edit.putInt("font_size", 18);
                        break;
                    case R.id.option_size_big:
                        edit.remove("size_selected_index");
                        edit.remove("title_size");
                        edit.remove("font_size");
                        edit.putInt("size_selected_index", 1);
                        edit.putInt("title_size", 24);
                        edit.putInt("font_size", 21);
                        break;
                }

                edit.apply();
            }
        });
    }

    @OnClick(R.id.next_content)
    public void goForward(){
        if(!isLoading){
            isLoading = true;
            finish();
            ContentAdapter.nextContent(this);
        }
    }

    @OnClick(R.id.dislike_content)
    public void dislikeContent(){
        if (contentRating == null || contentRating.getScore() < 5) {
            dislikeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
            likeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
            RestParameters restParameters = new RestParameters();
            restParameters.setProperty("content_id", Integer.toString(content.getId()));
            restParameters.setProperty("user_id", preferences.getString("user_id", null));
            restParameters.setProperty("liked", "0");

            final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                    .title(R.string.title_loading)
                    .content(R.string.message_loading)
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
    }

    @OnClick(R.id.like_content)
    public void likeContent(){
        if(contentRating == null || contentRating.getScore() > 4){
            likeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
            dislikeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
            RestParameters restParameters = new RestParameters();
            final Context context = this;
            restParameters.setProperty("content_id", Integer.toString(content.getId()));
            restParameters.setProperty("user_id", preferences.getString("user_id", null));
            restParameters.setProperty("liked", "5");

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
    }

    @OnClick(R.id.go_home)
    public void goHome(){
        int course = preferences.getInt("current_course", 0);
        String user_id = preferences.getString("user_id", null);
        if((courseRating == null || courseRating.getCourseId() == 0) && (course != 0 && user_id != null)){
            final Context context = this;
            courseParameters.setProperty("course_id", Integer.toString(preferences.getInt("current_course", 1)));
            courseParameters.setProperty("user_id", preferences.getString("user_id", null));

            final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(this)
                    .title(R.string.title_finish_course)
                    .customView(R.layout.dialog_rate_course_view, true)
                    .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                                    .title(R.string.title_loading)
                                    .content(R.string.message_loading)
                                    .progress(true,0,false);

                            final MaterialDialog dlg = materialDialog.build();
                            dlg.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                            dlg.getContentView().setTextSize(preferences.getInt("font_size", 18));
                            dlg.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                            dlg.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                            dlg.show();

                            MeAcodeMobileApplication.getInstance().getCourseService().postRateCourse(courseParameters)
                                    .enqueue(new Callback<Course>() {
                                        @Override
                                        public void onResponse(Call<Course> call, Response<Course> response) {
                                            dlg.dismiss();
                                            final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(context)
                                                    .title(R.string.rate_sent_title)
                                                    .content(R.string.rate_sent_message)
                                                    .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                                    .positiveText(R.string.action_ok)
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            startHomeActivity();
                                                        }
                                                    });

                                            final MaterialDialog dialog = errorMessageBuilder.build();
                                            dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                                            dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                                            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                                            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                                            dialog.show();
                                        }

                                        @Override
                                        public void onFailure(Call<Course> call, Throwable t) {
                                            dlg.dismiss();
                                            final MaterialDialog.Builder errorMessageBuilder = new MaterialDialog.Builder(context)
                                                    .title(R.string.title_error)
                                                    .content(R.string.rate_sent_message_error)
                                                    .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            startHomeActivity();
                                                        }
                                                    })
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
                    })
                    .positiveText(R.string.action_send);

            final MaterialDialog errorDialog = errorMessageBuilder.build();
            errorDialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
            errorDialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
            final ImageButton like = errorDialog.getCustomView().findViewById(R.id.like_course);
            final ImageButton dislike = errorDialog.getCustomView().findViewById(R.id.dislike_course);
            final AutoCompleteTextView courseComments = errorDialog.getCustomView().findViewById(R.id.course_rate_comments);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislike.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    like.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    courseParameters.setProperty("liked", "5");
                    courseParameters.setProperty("comments", courseComments.getText().toString());
                }
            });
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    dislike.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    courseParameters.setProperty("liked", "0");
                    courseParameters.setProperty("comments", courseComments.getText().toString());
                }
            });
            errorDialog.show();
        }
        else{
            startHomeActivity();
        }

    }

    public void startHomeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void getCourseRating(){
        
        if(preferences.getInt("current_course", 0) != 0){
            MeAcodeMobileApplication.getInstance()
                    .getCourseService()
                    .getCourseUserRating(preferences.getInt("current_course", 0), Integer.parseInt(preferences.getString("user_id", "-1")))
                    .enqueue(new Callback<CourseRating>() {
                        @Override
                        public void onResponse(Call<CourseRating> call, Response<CourseRating> response) {
                            if(response.code() == 200){
                                courseRating = response.body();
                            }
                            screenBuiltDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<CourseRating> call, Throwable t) {
                            screenBuiltDialog.dismiss();
                        }
                    });
        }
    }
    
    public void getContentRating(){
        MeAcodeMobileApplication.getInstance()
                .getContentService()
                .getContentUserRating(content.getId(), Integer.parseInt(preferences.getString("user_id", "-1")))
                .enqueue(new Callback<Rating>() {
                    @Override
                    public void onResponse(Call<Rating> call, Response<Rating> response) {
                        if(response.code() == 200){
                            contentRating = response.body().getContentRating();
                            courseRating = response.body().getCourseRating();
                            if(contentRating != null){
                                if(contentRating.getScore() == 5){
                                    likeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                                    dislikeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                                }
                                else{
                                    likeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryLight));
                                    dislikeContent.setBackgroundColor(getResources().getColor(R.color.colorSecondaryDark));
                                }

                                if(courseRating == null){
                                    getCourseRating();
                                }
                                else{
                                    screenBuiltDialog.dismiss();
                                }
                            }
                            else{
                                getCourseRating();
                            }
                        }
                        else{
                            getCourseRating();
                        }
                    }

                    @Override
                    public void onFailure(Call<Rating> call, Throwable t) {
                        getCourseRating();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);

        screenDialog = new MaterialDialog.Builder(this)
                .title(R.string.title_loading)
                .content(R.string.message_loading)
                .progress(true,0,false);

        screenBuiltDialog = screenDialog.build();
        screenBuiltDialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        screenBuiltDialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
        screenBuiltDialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        screenBuiltDialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        screenBuiltDialog.show();

        Bundle bundle = getIntent().getExtras();
        content = JsonConverter.fromJson(bundle.getString("content"), Content.class);

        getContentRating();

        previous_button.setTextSize(preferences.getInt("font_size", 18));
        next_button.setTextSize(preferences.getInt("font_size", 18));
        home_button.setTextSize(preferences.getInt("font_size", 18));
        usefulContentTextView.setTextSize(preferences.getInt("font_size", 18));

        if(preferences.getInt("current_content", 0) == 0){
            previous_button.setVisibility(View.INVISIBLE);
        }

        if(preferences.getInt("current_content", 0) >= preferences.getInt("last_content", 0)){
            next_button.setVisibility(View.GONE);
            home_button.setVisibility(View.VISIBLE);
        }

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
        webSettings.setUseWideViewPort(true);
        content_webview.setBackgroundColor(Color.TRANSPARENT);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head><body>";
        String closedTag = "</body></html>";
        String changeFontHtml = head + content.getText() + closedTag;
        content_webview.loadDataWithBaseURL(MeAcodeMobileApplication.getURL(), changeFontHtml, "text/html", "utf8", null);
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
