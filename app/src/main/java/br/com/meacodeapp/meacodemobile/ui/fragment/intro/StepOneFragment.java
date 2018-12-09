package br.com.meacodeapp.meacodemobile.ui.fragment.intro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.activity.IntroActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepOneFragment extends Fragment {

    @OnClick(R.id.step1_text)
    public void textClick(){
        ((IntroActivity)getActivity()).nextStep();
    }

    @OnClick(R.id.step1_title)
    public void titleClick(){
        ((IntroActivity)getActivity()).nextStep();
    }

    @OnClick(R.id.step1_rlt_layout)
    public void rltLayoutClick(){
        ((IntroActivity)getActivity()).nextStep();
    }

    @OnClick(R.id.step1_frm_layout)
    public void frmLayoutClick(){
        ((IntroActivity)getActivity()).nextStep();
    }

    public StepOneFragment() {
        // Required empty public constructor
    }

    public static StepOneFragment newInstance() {
        return new StepOneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_one, container, false);
        ButterKnife.bind(this, view);
        // Inflate the layout for this fragment
        return view;
    }
}
