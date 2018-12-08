package br.com.meacodeapp.meacodemobile.ui.fragment.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.activity.IntroActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepSixFragment extends Fragment {

    @BindView(R.id.step_six_image)
    ImageView imageView;

    float[] touchCoordinates = new float[2];

    public StepSixFragment() {
        // Required empty public constructor
    }

    public static StepSixFragment newInstance() {
        return new StepSixFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_six, container, false);
        ButterKnife.bind(this, view);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    touchCoordinates[0] = event.getX();
                    touchCoordinates[1] = event.getY();
                }

                return false;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IntroActivity)getActivity()).nextStep();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
