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

public class StepTenFragment extends Fragment {
    @BindView(R.id.step_ten_image)
    ImageView imageView;

    float[] touchCoordinates = new float[2];


    public StepTenFragment() {
        // Required empty public constructor
    }

    public static StepTenFragment newInstance() {
        return new StepTenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_ten, container, false);
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
                // retrieve the stored coordinates
                float x = touchCoordinates[0];
                float y = touchCoordinates[1];
                float density = getContext().getResources().getDisplayMetrics().density;

                if((x >= (245 * density) && y >= (533 * density)) && (x <= (466 * density) && y <= (586 * density))){
                    ((IntroActivity)getActivity()).nextStep();
                }

                // use the coordinates for whatever
                Log.i("TAG", "onLongClick: x = " + x + ", y = " + y);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
