package br.com.meacodeapp.meacodemobile.app;

import android.app.Application;

import br.com.meacodeapp.meacodemobile.service.AuthService;
import br.com.meacodeapp.meacodemobile.service.RestService;
import br.com.meacodeapp.meacodemobile.service.UserService;

/**
 * Created by usuario on 10/08/2018.
 */

public class MeAcodeMobileApplication extends Application {
    private static final String URL = "https://5b6df97bd8f3430014e79762.mockapi.io/api/";

    private static MeAcodeMobileApplication instance;

    private UserService userService;
    private AuthService authService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createServices();
    }

    private void createServices(){
        userService = (new RestService(URL).getService(UserService.class));
        authService = (new RestService(URL).getService(AuthService.class));
    }

    public static MeAcodeMobileApplication getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public AuthService getAuthService() {
        return authService;
    }
}