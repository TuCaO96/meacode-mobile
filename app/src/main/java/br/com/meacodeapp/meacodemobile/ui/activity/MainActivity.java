package br.com.meacodeapp.meacodemobile.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.fragment.HomeFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.MyCoursesFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.ProfileFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.SearchFragment;
import br.com.meacodeapp.meacodemobile.ui.fragment.SettingsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_navigation)
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_search:
                    setFragment(SearchFragment.newInstance());
                    return true;
                case R.id.navigation_my_courses:
                    setFragment(MyCoursesFragment.newInstance());
                    return true;
                case R.id.navigation_my_profile:
                    setFragment(ProfileFragment.newInstance());
                    return true;
                case R.id.navigation_settings:
                    setFragment(SettingsFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(HomeFragment.newInstance());
        ButterKnife.bind(this);
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
