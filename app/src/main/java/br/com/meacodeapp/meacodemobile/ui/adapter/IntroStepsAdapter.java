package br.com.meacodeapp.meacodemobile.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class IntroStepsAdapter extends FragmentPagerAdapter {

    private Fragment[] steps;
    private int currentStep = 0;

    public IntroStepsAdapter(FragmentManager fm, Fragment[] steps) {
        super(fm);
        this.steps = steps;
    }

    public Fragment getNextStep() {
        return currentStep + 1 > steps.length ? null : steps[++currentStep];
    }

    public Fragment getLastStep() {
        return currentStep - 1 > steps.length ? null : steps[--currentStep];
    }

    @Override
    public Fragment getItem(int position) {
        return steps[currentStep];
    }

    @Override
    public int getCount() {
        return 0;
    }
}
