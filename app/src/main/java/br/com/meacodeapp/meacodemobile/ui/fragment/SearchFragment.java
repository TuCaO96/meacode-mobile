package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.ui.activity.MainActivity;
import br.com.meacodeapp.meacodemobile.ui.adapter.CourseAdapter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    @BindView(R.id.rcv_search)
    RecyclerView coursesRecyclerView;

    List<Course> courses;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        searchCourses();

        return view;
    }

    public void searchCourses(){

        final Context context = getContext();

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(24);
        dialog.getContentView().setTextSize(21);
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
        dialog.show();

        MeAcodeMobileApplication.getInstance().getCourseService().getCourses()
                .enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        dialog.dismiss();

                        if(response.code() == 200){
                            courses = response.body();
                            CourseAdapter adapter = new CourseAdapter(getContext(), courses);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            coursesRecyclerView.setLayoutManager(layoutManager);
                            coursesRecyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        dialog.dismiss();

                        MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao buscar cursos. Por favor, tente" +
                                        " novamente mais tarde");

                        final MaterialDialog dialog = dialog1.build();
                        dialog.getTitleView().setTextSize(24);
                        dialog.getContentView().setTextSize(21);
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                        dialog.show();
                    }
                });
    }
}
