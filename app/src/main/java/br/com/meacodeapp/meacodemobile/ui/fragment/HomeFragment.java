package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.w3c.dom.Text;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.activity.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.search_card_text)
    TextView search_card;

    @BindView(R.id.my_courses_card_text)
    TextView my_courses_card;

    @BindView(R.id.my_profile_card_text)
    TextView my_profile_card;

    @BindView(R.id.settings_card_text)
    TextView settings_card;

    @BindView(R.id.new_content_card_text)
    TextView suggestions_card;

    @BindView(R.id.logout_card_text)
    TextView logout_card;

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
        search_card.setTextSize(21);
        my_courses_card.setTextSize(21);
        my_profile_card.setTextSize(21);
        settings_card.setTextSize(21);
        suggestions_card.setTextSize(21);
        logout_card.setTextSize(21);

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
        final MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                .title("Atenção")
                .content("Tem certeza que deseja sair de sua conta do aplicativo?")
                .positiveText("SIM")
                .negativeText("NÃO")
                .negativeColor(getResources().getColor(R.color.colorPrimaryDark))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MainActivity activity = (MainActivity) getActivity();
                        dialog.dismiss();
                        activity.logout();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.new_content_card)
    public void newContentClick(){
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(NewSuggestionFragment.newInstance());
    }
}