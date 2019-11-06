package com.example.smtm7.Connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private Retrofit retrofit;
    private ApiService apiService;
    private static final String url = "http://13.125.88.89:8000"; //서버 URL

    public NetworkHelper(){

        retrofit = new Retrofit.Builder()
                .baseUrl(url) //api의 baseURL
               // .client(client)
                .addConverterFactory(GsonConverterFactory.create()) //데이터 자동으로 컨버팅
                .build();

        apiService = retrofit.create(ApiService.class); //실제 api Method들이 선언된 interface 객체 선언
    }

    //API Interface 객체 얻기
    public ApiService getApiService(){
        return apiService;
    }
}
