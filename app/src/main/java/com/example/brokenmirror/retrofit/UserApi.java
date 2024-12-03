package com.example.brokenmirror.retrofit;

import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    // userJoin (회원가입)
    @POST("users")
    Call<UserDto> userJoin(@Body UserDto user);

    // userLogin (로그인)
    @POST("users/login")
    Call<UserDto> userLogin(@Body UserDto user);

    // currentPw (현재 비밀번호 확인)
    @POST("users/{id}/password/check")
    Call<UserDto> currentPw(@Path("id") String id);

    // newPw (비밀번호 변경)
    @FormUrlEncoded // Field 사용하기 위해..
    @PUT("users/{id}/password")
    Call<UserDto> newPw(@Path("id") String id, @Field("pw") String pw);

    // currentPhoneNum (현재 전화번호 확인)
    @POST("users/{id}/phone/check")
    Call<UserDto> currentPhoneNum(@Path("id") String id);

    // newPhoneNum (전화번호 변경)
    @FormUrlEncoded
    @PUT("users/{id}/phone")
    Call<UserDto> newPhoneNum(@Path("id") String id, @Field("phoneNum") String pw);

    // userDelete (회원 탈퇴)
    @DELETE("users/{id}")
    Call<Void> userDelete(@Path("id") String id);

    // newUserName (이름 변경)
    @FormUrlEncoded
    @PUT("users/{id}/name")
    Call<UserDto> newUserName(@Path("id") String id, @Field("userName") String userName);

    // findId (아이디 찾기)
    @POST("users/findid")
    Call<UserDto> findId(@Body UserDto user);

    // findPw (비밀번호 찾기)
    @FormUrlEncoded
    @POST("users/findpw")
    Call<UserDto> findPw(@Field("id") String id, @Field("userName") String userName,
                         @Field("birth") String birth, @Field("phoneNum") String phoneNum,
                         @Field("address") String address);

    // newImage (이미지 변경)
    @FormUrlEncoded
    @PUT("users/{id}/image")
    Call<UserDto> newImage(@Path("id") String id, @Field("profileImg") String profileImg);

    // certifyMail (인증번호 메일 발송)
    @POST("users/{id}/certify")
    Call<String> certifyMail(@Path("id") String id);

    // getFriendInfo (친구 유저 정보 조회)
    @POST("users/{id}/friend")
    Call<List<UserDto>> getFriendInfo(@Path("id") String id, @Body List<FriendDto> friendlist);

}