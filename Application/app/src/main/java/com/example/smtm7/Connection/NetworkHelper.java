package com.example.smtm7.Connection;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private Retrofit retrofit;
    private ApiService apiService;
    private static final String url = "http://13.125.88.89:8000"; //서버 URL

    public NetworkHelper(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url) //api의 baseURL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //데이터 자동으로 컨버팅
                .build();

        apiService = retrofit.create(ApiService.class); //실제 api Method들이 선언된 interface 객체 선언
    }

    //API Interface 객체 얻기
    public ApiService getApiService(){
        return apiService;
    }
}
