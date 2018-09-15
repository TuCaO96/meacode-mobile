package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.meacodeapp.meacodemobile.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSuggestionFragment extends Fragment {

    public NewSuggestionFragment() {
        // Required empty public constructor
    }

    public static NewSuggestionFragment newInstance() {
        return new NewSuggestionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_suggestion, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.suggestion_send)
    public void sendSuggestionClick(){

    }
}
