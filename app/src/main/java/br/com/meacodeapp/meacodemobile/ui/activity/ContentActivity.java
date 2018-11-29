package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentActivity extends AppCompatActivity {

    Content content;

    @BindView(R.id.content_webview)
    WebView content_webview;
    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
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
