package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.meacodeapp.meacodemobile.R;

public class SettingskFragment extends Fragment {

    public SettingskFragment() {
        // Required empty public constructor
    }

    public static SettingskFragment newInstance() {
        SettingskFragment fragment = new SettingskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settingsk, container, false);
    }
}
