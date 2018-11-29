package br.com.meacodeapp.meacodemobile.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import br.com.meacodeapp.meacodemobile.R;
import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.model.Suggestion;
import br.com.meacodeapp.meacodemobile.model.User;
import br.com.meacodeapp.meacodemobile.service.RestService;
import br.com.meacodeapp.meacodemobile.ui.activity.MainActivity;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSuggestionFragment extends Fragment {

    @BindView(R.id.suggestion_title)
    TextView title;

    @BindView(R.id.suggestion_text)
    TextView text;

    @BindView(R.id.suggestion_send)
    Button send_button;

    SharedPreferences preferences = MeAcodeMobileApplication.getInstance().getSharedPreferences("session", Context.MODE_PRIVATE);

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

        ((MainActivity)getActivity()).getSupportActionBar().show();

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(((MainActivity)getActivity()));
        View v = inflator.inflate(R.layout.title_textview, null);

        ((TextView) v.findViewById(R.id.title_textview)).setText(R.string.title_new_suggestion);
        ((TextView) v.findViewById(R.id.title_textview)).setTextSize(preferences.getInt("title_size",  21));
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(v);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_suggestion, container, false);
        ButterKnife.bind(this, view);
        send_button.setTextSize(preferences.getInt("font_size", 18));
        title.setTextSize(preferences.getInt("font_size", 18));
        text.setTextSize(preferences.getInt("font_size", 18));
        return view;
    }

    @OnClick(R.id.suggestion_send)
    public void sendSuggestionClick(){
        final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE);

        User user = JsonConverter.fromJson(sharedPreferences.getString("user", null), User.class);

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

        RestParameters parameters = new RestParameters();
        parameters.setProperty("title", title.getText().toString());
        parameters.setProperty("text", text.getText().toString());
//        parameters.setProperty("user_id", Integer.toString(user.getId()));

        final Context context = getContext();

        MeAcodeMobileApplication.getInstance().getSuggestionService().postSuggestion(parameters)
                .enqueue(new Callback<Suggestion>() {
                    @Override
                    public void onResponse(Call<Suggestion> call, Response<Suggestion> response) {
                        dialog.dismiss();
                        if(response.code() == 201){
                            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                                    .title("Sucesso")
                                    .content("Sua sugestão foi enviada aos devidos responsáveis." +
                                            " Caso seja aprovada, o curso será disponibilizado em " +
                                            "nossa plataforma! :)")
                                    .positiveText("OK");

                            final MaterialDialog dialog = materialDialog.build();
                            dialog.getTitleView().setTextSize(24);
                            dialog.getContentView().setTextSize(21);
                            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                            dialog.show();
                        }
                        else{
                            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                                    .title("Erro")
                                    .content("Ocorreu um erro ao enviar sua sugestão. Por favor, tente" +
                                            " novamente mais tarde.")
                                    .positiveText("OK");

                            final MaterialDialog dialog = materialDialog.build();
                            dialog.getTitleView().setTextSize(24);
                            dialog.getContentView().setTextSize(21);
                            dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                            dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Suggestion> call, Throwable t) {
                        dialog.dismiss();
                        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                                .title("Erro")
                                .content("Ocorreu um erro ao enviar sua sugestão. Por favor, tente" +
                                        " novamente mais tarde.");

                        final MaterialDialog dialog = materialDialog.build();
                        dialog.getTitleView().setTextSize(24);
                        dialog.getContentView().setTextSize(21);
                        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(21);
                        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(21);
                        dialog.show();
                    }
                });
    }
}
