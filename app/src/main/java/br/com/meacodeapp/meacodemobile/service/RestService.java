package br.com.meacodeapp.meacodemobile.service;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import br.com.meacodeapp.meacodemobile.app.MeAcodeMobileApplication;
import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestService {

    private Retrofit adapter;

    public RestService(String endPoint){

        JsonSerializer<RestParameters> serializer = new JsonSerializer<RestParameters>() {
            @Override
            public JsonElement serialize(RestParameters parameters, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject json = new JsonObject();

                for (Map.Entry<String, String> entry : parameters.getProperties().entrySet()){
                    if(JsonConverter.isJSONValid(entry.getValue())){
                        json.add(entry.getKey(), new JsonParser().parse(entry.getValue()));
                    }
                    else{
                        json.addProperty(entry.getKey(), entry.getValue());
                    }
                }

                return json;
            }
        };

        JsonDeserializer<RestParameters> deserializer = new JsonDeserializer<RestParameters>() {
            @Override
            public RestParameters deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject rootObject = json.getAsJsonObject();

                RestParameters parameters = new RestParameters();

                for(Map.Entry<String, JsonElement> property : rootObject.entrySet()){
                    parameters.setProperty(property.getKey(), property.getValue().toString());
                }

                return parameters;
            }
        };

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(RestParameters.class, serializer)
                .registerTypeAdapter(RestParameters.class, deserializer)
                .create();

        OkHttpClient.Builder okHttpClient = interceptorHttp();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.addInterceptor(logging);

        adapter = new Retrofit.Builder().
                baseUrl(endPoint).
                addConverterFactory(GsonConverterFactory.create(gson)).
                addConverterFactory(ScalarsConverterFactory.create()).client(okHttpClient.build()).
                build();

    }

    private OkHttpClient.Builder interceptorHttp() {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        SharedPreferences sharedPreferences = MeAcodeMobileApplication.getInstance()
                .getSharedPreferences("session", MeAcodeMobileApplication.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token","");
        if(!token.isEmpty()) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    String authorization = "Bearer " + token.toString();
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", authorization.replace("\"", ""))
                            .method(original.method(), original.body()).build();
                    return chain.proceed(request);
                }
            });
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        return httpClient;
    }

    public <T> T getService(Class<T> c){
        return adapter.create(c);
    }

}
