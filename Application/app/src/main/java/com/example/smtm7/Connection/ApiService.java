package com.example.smtm7.Connection;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    //회원 가입 요청 (가입정보 전송) POST
    @FormUrlEncoded
    @POST("/accounts/register/") //api path
    Call<ResponseSignin> signup(@Field("nickname") String nickname, @Field("username") String username, @Field("password1") String password1,
                                @Field("password2") String password2, @Field("email") String email);

    //로그인 요청 (아이디/비밀번호 전송) POST
    @FormUrlEncoded
    @POST("/api/token/") //api path
    Call<ResponseToken> login(@Field("username") String username, @Field("password") String password);

    //로그인 이후 닉네임 정보 받아오기
    @FormUrlEncoded
    @POST("/accounts/login/") //api path
    Call<ResponseLogin> getnickname(@Header("Authorization") String token, @Field("username") String username, @Field("password") String password);

    //이메일 연동 여부
    @FormUrlEncoded
    @POST("/mailcheck/")
    Call<ResponseSignin> getemailinterlock(@Header("Authorization") String token, @Field("useremail") String email,@Field("userpassword") String password);

    //거래내역 업데이트
    @FormUrlEncoded
    @POST("/maildata/")
    Call<ResponseUpdate> updatelist(@Header("Authorization") String token, @Field("username") String username, @Field("useremail") String email, @Field("userpassword") String password, @Field("index") int index);

    //거래내역 확인
    @FormUrlEncoded
    @POST("/accounts/check/")
    Call<ResponseCheck> checkTransaction(@Header("Authorization") String token, @Field("username") String username, @Field("myIndex") int index);

    //거래내역 받아오기
    @FormUrlEncoded
    @POST("/accounts/datalist/")
    Call<List<ResponseTransaction>> getTransaction(@Header("Authorization") String token, @Field("username") String username, @Field("myIndex") int index);

    //OCR
    @Multipart
    @POST("/upload/")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    //OCR
    @Multipart
    @POST("/ocr/")
    Call<ResponseOCR> getOCR(@Part MultipartBody.Part file);

//    //암복호화
//    @FormUrlEncoded
//    @POST("/aestest/")
//    Call<ResponseCipher> getCipher(@Header("Authorization") String token, @Field("userpassword") String password, @Field("key") String key);
}
