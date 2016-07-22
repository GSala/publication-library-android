package edu.upc.mcia.publications.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.ryanharter.auto.value.gson.AutoValueGsonTypeAdapterFactory;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    public static final String API_BASE_URL = "http://registros.mcia.upc.edu/api/";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new AutoValueGsonTypeAdapterFactory())
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) ->
                    new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) ->
                    src == null ? null : new JsonPrimitive(src.getTime()))
            .create();

    private static final Retrofit RETROFIT =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .build();

    private static final MciaService MCIA_SERVICE = RETROFIT.create(MciaService.class);

    public static MciaService getMciaService() {
        return MCIA_SERVICE;
    }
}
