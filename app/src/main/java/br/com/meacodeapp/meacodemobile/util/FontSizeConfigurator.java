package br.com.meacodeapp.meacodemobile.util;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;

public class FontSizeConfigurator {
    public static int getTitleTextSize(){
        final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE);

        return sharedPreferences.getInt("title_text_size", 24);
    }

    public static int getBodyTextSize(){
        final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE);

        return sharedPreferences.getInt("body_text_size", 21);
    }

    public static void setTitleTextSize(int size){
        final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE);

        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("title_text_size", size);
        edit.apply();
    }

    public static void setBodyTextSize(int size){
        final SharedPreferences sharedPreferences = MeAcodeMobileApplication
                .getInstance()
                .getSharedPreferences("session", Context.MODE_PRIVATE);

        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("body_text_size", size);
        edit.apply();
    }
}
