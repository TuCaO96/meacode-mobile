package br.com.meacodeapp.meacodemobile.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.ui.adapter.ContentAdapter;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseActivity extends AppCompatActivity {

    Course course;

    @BindView(R.id.single_course_contents)
    RecyclerView contents;

    @BindView(R.id.single_course_image)
    ImageView image;

    @BindView(R.id.single_course_name)
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Bundle bundle = getIntent().getExtras();
        course = JsonConverter.fromJson(bundle.getString("course"), Course.class);
        ButterKnife.bind(this);
        name.setText(course.getName());

        ContentAdapter contentAdapter = new ContentAdapter(course.getContents());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        contents.setLayoutManager(layoutManager);
        contents.setAdapter(contentAdapter);
    }
}
