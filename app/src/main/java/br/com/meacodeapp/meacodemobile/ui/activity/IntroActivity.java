package br.com.meacodeapp.meacodemobile.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.adapter.IntroStepsAdapter;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepOneFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepTwoFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.intro_view_pager)
    ViewPager viewPager;

    IntroStepsAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment[] steps = new Fragment[2];

        steps[0] = StepOneFragment.newInstance();
        steps[1] = StepTwoFragment.newInstance();

        pagerAdapter = new IntroStepsAdapter(fragmentManager, steps);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() > 0){
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public IntroStepsAdapter getPagerAdapter() {
        return pagerAdapter;
    }
}
