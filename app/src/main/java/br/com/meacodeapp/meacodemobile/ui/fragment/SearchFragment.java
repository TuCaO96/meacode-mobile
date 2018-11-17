package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Content;
import br.com.meacodeapp.meacodemobile.model.Course;
import br.com.meacodeapp.meacodemobile.model.SearchResult;
import br.com.meacodeapp.meacodemobile.model.User;
import br.com.meacodeapp.meacodemobile.service.RestService;
import br.com.meacodeapp.meacodemobile.ui.activity.MainActivity;
import br.com.meacodeapp.meacodemobile.ui.adapter.CourseAdapter;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import br.com.meacodeapp.meacodemobile.util.SimpleDividerItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    @BindView(R.id.rcv_search)
    RecyclerView coursesRecyclerView;

    @BindView(R.id.sv_search)
    SearchView searchView;

    List<Course> courses;

    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        ((MainActivity)getActivity()).getSupportActionBar().hide();

        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(preferences.getInt("font_size", 18));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SharedPreferences sharedPreferences = MeAcodeMobileApplication.getInstance()
                        .getSharedPreferences("session", Context.MODE_PRIVATE);

                if(sharedPreferences.contains("user") && sharedPreferences.getString("user", null) != null){
                    User user = JsonConverter.fromJson(sharedPreferences.getString("user", null), User.class);
                    search(user.getId(), query);
                }
                else{
                    search(-1, query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.show();

        final Context context = getContext();

        MeAcodeMobileApplication.getInstance().getCourseService().getCourses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                dialog.dismiss();
                courses = response.body();
                CourseAdapter adapter = new CourseAdapter(getContext(), courses);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                coursesRecyclerView.setLayoutManager(layoutManager);
                coursesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
                coursesRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                dialog.dismiss();

                MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(context)
                        .title("Erro")
                        .content("Ocorreu um erro ao buscar cursos. Por favor, tente" +
                                " novamente mais tarde")
                        .positiveText("OK");

                final MaterialDialog dialog = dialog1.build();
                dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                dialog.show();

            }
        });

        return view;
    }

    public void search(int user_id, String query){

        final Context context = getContext();

        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Carregando")
                .content("Aguarde mais alguns instantes...")
                .progress(true,0,false);

        final MaterialDialog dialog = materialDialog.build();
        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
        dialog.show();

        RestParameters parameters = new RestParameters();
        parameters.setProperty("query", query);

        MeAcodeMobileApplication.getInstance().getUserSearchService().postUserSearch(parameters)
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        dialog.dismiss();

                        if(response.code() == 200){
                            courses = response.body().getCourses();
                            CourseAdapter adapter = new CourseAdapter(context, courses);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            coursesRecyclerView.setLayoutManager(layoutManager);
                            coursesRecyclerView.setAdapter(adapter);
                            coursesRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        dialog.dismiss();

                        MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao buscar cursos. Por favor, tente" +
                                        " novamente mais tarde")
                                .positiveText("OK");

                        final MaterialDialog dialog = dialog1.build();
                        dialog.getTitleView().setTextSize(preferences.getInt("title_size", 21));
                        dialog.getContentView().setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(preferences.getInt("font_size", 18));
                        dialog.show();
                    }
                });
    }
}
