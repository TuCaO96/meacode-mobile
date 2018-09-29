package br.com.meacodeapp.meacodemobile.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import butterknife.ButterKnife;

public class ContentActivity extends AppCompatActivity {

    Content content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        content = JsonConverter.fromJson(bundle.getString("content"), Content.class);
    }
}
