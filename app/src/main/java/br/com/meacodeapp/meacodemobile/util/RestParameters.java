package br.com.meacodeapp.meacodemobile.util;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class RestParameters implements Serializable {

    public RestParameters(String[] parameters){
        for(String parameter:parameters){
            this.setProperty(parameter,"");
        }
    }

    public RestParameters(){

    }
    private Map<String, String> properties = new HashMap<String, String>();
    private Map<String, Callable<Object>> callables = new HashMap<>();

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public Object call(String key) {
        Callable<Object> callable = callables.get(key);

        if (callable != null) {
            try{
                return callable.call();
            }
            catch (Exception e){
                return null;
            }

        }
        return null;
    }

    public void define(String key, Callable<Object> callable) {
        callables.put(key, callable);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(getProperties());
    }
}
