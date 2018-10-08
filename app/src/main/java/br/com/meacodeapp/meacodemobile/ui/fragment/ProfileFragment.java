package br.com.meacodeapp.meacodemobile.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_name)
    TextView nome;

    @BindView(R.id.profile_email)
    TextView email;

    @BindView(R.id.profile_image)
    ImageView imagem;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void getUserProfile(){
        String token = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE)
                .getString("token", null);

        MeAcodeMobileApplication.getInstance().getUserService().getUserByToken(token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        nome.setText(response.body().getUsername());
                        email.setText(response.body().getEmail());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(getContext())
                                .title("Erro")
                                .content("Ocorreu um erro ao buscar suas informações de perfil. Por favor, tente" +
                                        " novamente mais tarde")
                                .positiveText(R.string.action_ok);

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
