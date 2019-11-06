package com.example.smtm7.Connection;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("/token/")
    Call<ResponseSignin> getToken();

    //회원 가입 요청 (가입정보 전송) POST
    @FormUrlEncoded
    @POST("/accounts/register/") //api path
    Call<ResponseSignin> signup(@Field("nickname") String nickname, @Field("username") String username, @Field("password1") String password1,
                                @Field("password2") String password2, @Field("email") String email, @Field("email_1") String email_1, @Field("pw_1") String pw_1);

    //로그인 요청 (아이디/비밀번호 전송) POST
    @FormUrlEncoded
    @POST("/accounts/login/") //api path
    Call<ResponseLogin> login(@Field("username") String username, @Field("password") String password);

    //거래내역 받아오기
    @FormUrlEncoded
    @POST("/accounts/datalist/")
    Call<List<ResponseTransaction>> getTransaction(@Field("username") String username);
}
