package br.com.meacodeapp.meacodemobile.ui.fragment.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.meacodeapp.meacodemobile.R;
import butterknife.ButterKnife;

public class StepSixCFragment extends Fragment {

    public StepSixCFragment() {
        // Required empty public constructor
    }

    public static StepSixCFragment newInstance() {
        return new StepSixCFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_six_c, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        return view;
    }
}
