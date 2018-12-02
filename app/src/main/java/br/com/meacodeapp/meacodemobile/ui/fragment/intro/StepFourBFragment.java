package br.com.meacodeapp.meacodemobile.ui.fragment.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.meacodeapp.meacodemobile.R;
import butterknife.ButterKnife;

public class StepFourBFragment extends Fragment {

    public StepFourBFragment() {
        // Required empty public constructor
    }

    public static StepFourBFragment newInstance() {
        return new StepFourBFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_four_b, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        return view;
    }
}
