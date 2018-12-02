package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.util.List;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.model.SearchResult;
import br.com.meacodeapp.meacodemobile.ui.adapter.ContentAdapter;
import br.com.meacodeapp.meacodemobile.ui.adapter.CourseAdapter;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedCoursesActivity extends AppCompatActivity {
    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    @BindView(R.id.courses_related)
    RecyclerView courses_rcv;

    @BindView(R.id.related_course_title)
    TextView courseName;

    @BindView(R.id.image_related_course)
    AppCompatImageView courseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_courses);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String category_id = bundle.getString("category_id");
        String course_id = bundle.getString("course_id");
        String course_name = bundle.getString("course_name");
        String course_image = bundle.getString("course_image");
        courseName.setText("Relacionado a: " + course_name);
        courseName.setTextSize(preferences.getInt("font_size",  18));
        courseImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        if(course_image != null){
            Glide.with(this).load(MeAcodeMobileApplication.getFrontendUrl() + course_image).into(courseImage);
        }

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_textview, null);

        ((TextView) v.findViewById(R.id.title_textview)).setText("Cursos Relacionados");
        ((TextView) v.findViewById(R.id.title_textview)).setTextSize(preferences.getInt("title_size",  21));
        this.getSupportActionBar().setCustomView(v);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = this;

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final MaterialDialog dlg = materialDialog.build();
        dlg.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        dlg.getContentView().setTextSize(preferences.getInt("font_size", 18));
        dlg.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        dlg.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        dlg.show();

        MeAcodeMobileApplication.getInstance().getCourseService().getRelatedCourses(course_id, category_id)
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        dlg.dismiss();
                        CourseAdapter contentAdapter = new CourseAdapter(context, response.body().getCourses());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

                        if(response.body().getCourses().size() == 0){
                            MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(context)
                                    .title(R.string.title_error)
                                    .content(R.string.error_404_courses)
                                    .positiveText(R.string.action_ok);

                            final MaterialDialog dialog = dialog1.build();
                            dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                            dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                            dialog.show();
                        }

                        courses_rcv.setLayoutManager(layoutManager);
                        courses_rcv.setAdapter(contentAdapter);
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        dlg.dismiss();

                        MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao buscar cursos. Por favor, tente" +
                                        " novamente mais tarde")
                                .positiveText(R.string.action_ok);

                        final MaterialDialog dialog = dialog1.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }
                });
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