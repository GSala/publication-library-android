package edu.upc.mcia.publications.data.remote

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object ApiManager {

    @JvmStatic
    private val API_BASE_URL = "http://registros.mcia.upc.edu/api/"

    @JvmStatic
    val IMAGE_BASE_URL = "http://registros.mcia.upc.edu%s"

    private val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ -> Date(json.asJsonPrimitive.asLong) })
            .create()

    private val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BASIC }

    private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    val remoteApi: RemoteApi = retrofit.create(RemoteApi::class.java)
}
