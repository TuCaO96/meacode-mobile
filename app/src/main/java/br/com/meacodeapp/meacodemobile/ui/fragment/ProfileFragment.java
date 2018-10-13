package br.com.meacodeapp.meacodemobile.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.User;
import br.com.meacodeapp.meacodemobile.ui.activity.MainActivity;
import br.com.meacodeapp.meacodemobile.util.FontSizeConfigurator;
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

    private GoogleSignInClient mGoogleSignInClient;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        getUserProfile();

        return view;
    }

    public void goHome(){
        MainActivity activity = (MainActivity) getActivity();
        activity.setFragment(HomeFragment.newInstance());
    }

    public void googleAuth(){
        MainActivity activity = (MainActivity) getActivity();
        activity.googleAuth();
    }

    public void getUserProfile(){
        String token = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE)
                .getString("token", null);

        token = token.replace("\"", "");

        if(token == null || token.length() < 1){
            MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(getContext())
                    .title("Ops...")
                    .content("Para acessar essa opção, você precisa de criar uma conta." +
                            "Deseja entrar com sua conta do Google?")
                    .positiveText(R.string.action_ok)
                    .negativeText(R.string.action_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            googleAuth();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            goHome();
                        }
                    })
                    .positiveColor(getResources().getColor(R.color.colorAccent))
                    .negativeColor(getResources().getColor(R.color.colorPrimaryDark));

            final MaterialDialog dialog = dialog1.build();
            dialog.getTitleView().setTextSize(24);
            dialog.getContentView().setTextSize(21);
            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
            dialog.show();

            return;
        }

        MeAcodeMobileApplication.getInstance().getUserService().getUserByToken(token)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code() == 200){
                            nome.setText(response.body().getFullName());
                            email.setText(response.body().getEmail());
                            if(response.body().getImage_url() == null || response.body().getImage_url().length() > 0){
                                Glide.with(getContext()).load(response.body().getImage_url()).into(imagem);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        MaterialDialog.Builder dialog1 = new MaterialDialog.Builder(getContext())
                                .title("Erro")
                                .content("Ocorreu um erro ao buscar suas informações de perfil. Por favor, tente" +
                                        " novamente mais tarde")
                                .positiveText(R.string.action_ok);

                        final MaterialDialog dialog = dialog1.build();
                        dialog.getTitleView().setTextSize(FontSizeConfigurator.getTitleTextSize());
                        dialog.getContentView().setTextSize(FontSizeConfigurator.getBodyTextSize());
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(FontSizeConfigurator.getBodyTextSize());
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(FontSizeConfigurator.getBodyTextSize());
                        dialog.show();
                    }
                });
    }
}
