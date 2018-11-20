package br.com.meacodeapp.meacodemobile.app;

import android.app.Application;

import br.com.meacodeapp.meacodemobile.service.AuthService;
import br.com.meacodeapp.meacodemobile.service.ContentService;
import br.com.meacodeapp.meacodemobile.service.CourseService;
import br.com.meacodeapp.meacodemobile.service.RestService;
import br.com.meacodeapp.meacodemobile.service.SuggestionService;
import br.com.meacodeapp.meacodemobile.service.UserSearchService;
import br.com.meacodeapp.meacodemobile.service.UserService;

/**
 * Created by usuario on 10/08/2018.
 */

public class MeAcodeMobileApplication extends Application {
//    private static final String URL = "http://172.16.212.70/meacode-yii/api/web/";
//    private static final String URL = "http://192.168.137.1/meacode-yii/api/web/";
//    private static final String URL = "http://192.168.0.19/meacode-yii/api/web/";
//    private static final String URL = "http://192.168.0.24/meacode-yii/api/web/";
    private static final String URL = "http://meacodeapp.com.br/api/web/";
//    private static final String URL = "http://10.0.2.2/meacode-yii/api/web/";

    private static MeAcodeMobileApplication instance;

    private UserService userService;
    private AuthService authService;
    private CourseService courseService;
    private ContentService contentService;
    private SuggestionService suggestionService;
    private UserSearchService userSearchService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createServices();
    }

    private void createServices(){
        userService = (new RestService(URL).getService(UserService.class));
        authService = (new RestService(URL).getService(AuthService.class));
        courseService = (new RestService(URL).getService(CourseService.class));
        contentService = (new RestService(URL).getService(ContentService.class));
        suggestionService = (new RestService(URL).getService(SuggestionService.class));
        userSearchService = (new RestService(URL).getService(UserSearchService.class));
    }

    public static MeAcodeMobileApplication getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public static String getURL() {
        return URL;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public CourseService getCourseService() { return courseService; }

    public ContentService getContentService() { return contentService; }

    public UserSearchService getUserSearchService() {
        return userSearchService;
    }
    public SuggestionService getSuggestionService() { return suggestionService; }
}