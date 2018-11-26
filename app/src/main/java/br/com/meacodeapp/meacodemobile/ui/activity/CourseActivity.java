package br.com.meacodeapp.meacodemobile.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

        String url = course.getImageUrl();
        if(url != null && !url.isEmpty()){
            image.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            Glide.with(this).load(MeAcodeMobileApplication.getFrontendUrl() + url).into(image);
        }

        getSupportActionBar().setTitle(course.getName());
        ContentAdapter contentAdapter = new ContentAdapter(course.getContents());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contents.setLayoutManager(layoutManager);
        contents.addItemDecoration(new SimpleDividerItemDecoration(this));
        contents.setAdapter(contentAdapter);
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
