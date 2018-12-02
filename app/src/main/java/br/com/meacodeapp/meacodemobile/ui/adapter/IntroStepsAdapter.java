package br.com.meacodeapp.meacodemobile.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepEightFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepElevenFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepFifteenFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepFiveFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepFourBFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepFourFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepFourteenFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepNineFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepOneFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepSevenFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepSixBFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepSixCFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepSixFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepTenFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepThirteenFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepThreeFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepTwelveFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.intro.StepTwoFragment;
import butterknife.BindView;

public class IntroStepsAdapter extends FragmentStatePagerAdapter {

    public IntroStepsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return StepOneFragment.newInstance();
            case 1:
                return StepTwoFragment.newInstance();
            case 2:
                return StepThreeFragment.newInstance();
            case 3:
                return StepFourFragment.newInstance();
            case 4:
                return StepFiveFragment.newInstance();
            case 5:
                return StepSixFragment.newInstance();
            case 6:
                return StepSixBFragment.newInstance();
            case 7:
                return StepSixCFragment.newInstance();
            case 8:
                return StepSevenFragment.newInstance();
            case 9:
                return StepEightFragment.newInstance();
            case 10:
                return StepNineFragment.newInstance();
            case 11:
                return StepTenFragment.newInstance();
            case 12:
                return StepElevenFragment.newInstance();
            case 13:
                return StepTwelveFragment.newInstance();
            case 14:
                return StepThirteenFragment.newInstance();
            case 15:
                return StepFourteenFragment.newInstance();
            case 16:
                return StepFifteenFragment.newInstance();
            default:
                return StepFifteenFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 17;
    }
}
