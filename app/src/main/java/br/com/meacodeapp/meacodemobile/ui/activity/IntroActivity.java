package br.com.meacodeapp.meacodemobile.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.adapter.IntroStepsAdapter;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepOneFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepTwoFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.fragment_view_pager)
    ViewPager viewPager;

    @BindView(R.id.button_jump)
    Button button_skip;

    @BindView(R.id.button_finish)
    ImageButton button_finish;

    IntroStepsAdapter pagerAdapter;

    @OnClick(R.id.button_finish)
    public void onFinish(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_jump)
    public void onSkip(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        pagerAdapter = new IntroStepsAdapter(fragmentManager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 7){
                    button_finish.setVisibility(View.VISIBLE);
                    button_skip.setVisibility(View.INVISIBLE);
                }
                else{
                    button_finish.setVisibility(View.INVISIBLE);
                    button_skip.setVisibility(View.VISIBLE);
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

    public void nextStep(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public IntroStepsAdapter getPagerAdapter() {
        return pagerAdapter;
    }
}
