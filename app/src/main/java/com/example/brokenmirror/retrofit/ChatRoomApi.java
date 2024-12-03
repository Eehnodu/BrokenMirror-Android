package com.example.brokenmirror.retrofit;

import com.example.brokenmirror.data.ChatRoomDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatRoomApi {

    @GET("/api/chatroom/{userId}")
    Call<List<ChatRoomDto>> getChatRoom(@Path("userId") String userId);

}
