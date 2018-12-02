package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.ui.adapter.ContentAdapter;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import br.com.meacodeapp.meacodemobile.util.SimpleDividerItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseActivity extends AppCompatActivity {

    Course course;

    @OnClick(R.id.related_courses)
    public void relatedCourses(){
        Intent intent = new Intent(this, RelatedCoursesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("category_id", Integer.toString(course.getCategory().getId()));
        bundle.putString("course_id", Integer.toString(course.getId()));
        bundle.putString("course_name", course.getName());
        bundle.putString("course_image", course.getImageUrl());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    @BindView(R.id.single_course_contents)
    RecyclerView contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Bundle bundle = getIntent().getExtras();
        course = JsonConverter.fromJson(bundle.getString("course"), Course.class);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_textview, null);

        ((TextView) v.findViewById(R.id.title_textview)).setText(course.getName());
        ((TextView) v.findViewById(R.id.title_textview)).setTextSize(preferences.getInt("title_size",  21));
        this.getSupportActionBar().setCustomView(v);

        ContentAdapter contentAdapter = new ContentAdapter(course.getContents());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contents.setLayoutManager(layoutManager);
        contents.setAdapter(contentAdapter);

        preferences.edit().putInt("current_content", 0).apply();
        preferences.edit().putInt("current_course", course.getId()).apply();
        preferences.edit().putInt("last_content", course.getContents().size() -1).apply();
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