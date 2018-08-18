package br.com.meacodeapp.meacodemobile.service;

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

import java.lang.reflect.Type;
import java.util.Map;

import br.com.meacodeapp.meacodemobile.util.JsonConverter;
import br.com.meacodeapp.meacodemobile.util.RestParameters;
import okhttp3.OkHttpClient;
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

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.addInterceptor(logging);

        adapter = new Retrofit.Builder().
                baseUrl(endPoint).
                addConverterFactory(GsonConverterFactory.create(gson)).
                addConverterFactory(ScalarsConverterFactory.create()).client(okHttpClient.build()).
                build();

    }

    public <T> T getService(Class<T> c){
        return adapter.create(c);
    }

}
