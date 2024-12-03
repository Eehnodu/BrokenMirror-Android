package com.example.brokenmirror.retrofit;

import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.FriendRoomDto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FriendApi {

    // getFriendsByUserId (친구목록 가져오기)
    @GET("/api/friend/user/{userId}")
    Call<List<FriendDto>> getFriendsByUserId(@Path("userId") String userId);
}