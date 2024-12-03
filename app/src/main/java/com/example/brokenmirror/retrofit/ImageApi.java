package com.example.brokenmirror.retrofit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageApi {

    @Multipart
    @POST("users/profile")
    Call<String> saveImage(@Part MultipartBody.Part imageUrl);
}
