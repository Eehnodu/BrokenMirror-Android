package com.example.brokenmirror.retrofit;

import com.example.brokenmirror.data.LoginInfoDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginInfoApi {

    // putLoginInfo (로그인 내역 등록)
    @POST("users/putlogininfo")
    Call<Void> putLoginInfo(@Body LoginInfoDto loginInfo);

    // getLoginInfo (로그인 내역 조회)
    @GET("users/{id}/getlogininfo")
    Call<List<LoginInfoDto>> getLoginInfo(@Path("id") String id);
}
