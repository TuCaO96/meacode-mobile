package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.activity.MainActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.search_card)
    public void searchCoursesClick(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(SearchFragment.newInstance());
    }

    @OnClick(R.id.my_courses_card)
    public void myCoursesClick(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(MyCoursesFragment.newInstance());
    }

    @OnClick(R.id.my_profile_card)
    public void myProfileClick(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(ProfileFragment.newInstance());
    }

    @OnClick(R.id.settings_card)
    public void settingsClick(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(SettingsFragment.newInstance());
    }

    @OnClick(R.id.logout_card)
    public void logoutClick(){
        //TODO: Logout user
    }

    @OnClick(R.id.new_content_card)
    public void newContentClick(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(NewSuggestionFragment.newInstance());
    }
}