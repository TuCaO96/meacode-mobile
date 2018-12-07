package br.com.meacodeapp.meacodemobile.ui.fragment.intro;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.ui.activity.IntroActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepThreeFragment extends Fragment {

    @BindView(R.id.step_three_image)
    ImageView imageView;

    float[] touchCoordinates = new float[2];

    public StepThreeFragment() {
        // Required empty public constructor
    }

    public static StepThreeFragment newInstance() {
        return new StepThreeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_three, container, false);
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

                int width = getContext().getResources().getDisplayMetrics().widthPixels;
                int height = getContext().getResources().getDisplayMetrics().heightPixels;
                float density = getContext().getResources().getDisplayMetrics().density;
                int p_x = (int)(409 * density);
                int p_y = (int)(699 * density);

                float f_x = (409 * density);
                float f_y = (699 * density);

                // use the coordinates for whatever
                Log.i("TAG", "onLongClick: x = " + x + ", y = " + y);
                Log.i("TAG", "screenSizeWidth: x = " + getContext().getResources().getDisplayMetrics().widthPixels);
                Log.i("TAG", "screenSizeHeight: x = " + getContext().getResources().getDisplayMetrics().heightPixels);
                Log.i("TAG", "scaledDensity: x = " + getContext().getResources().getDisplayMetrics().scaledDensity);

                if(x >= (272 * density) && y >= (412 * density)){
                    ((IntroActivity)getActivity()).nextStep();
                }


            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
