package com.example.brokenmirror.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Retrofit 인스턴스 생성
public class RetrofitService {
    //    private static String BASE_URL = "http://10.0.2.2:8081";
    private static String BASE_URL = "http://172.22.7.37:8081";

    private static Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    // FriendListApi
    public static FriendApi getFriendApi() {
        return getInstance().create(FriendApi.class);
    }

    // ChatRoomApi
    public static ChatRoomApi getChatRoomApi() {
        return getInstance().create(ChatRoomApi.class);
    }

    // UserApi
    public static UserApi getUserApi() {
        return getInstance().create(UserApi.class);
    }

    // LoginInfoApi
    public static LoginInfoApi getLoginInfoApi() {
        return getInstance().create(LoginInfoApi.class);
    }

    // ImageApi
    public static ImageApi getImageApi(){
        return getInstance().create(ImageApi.class);
    }

    // 날짜 형식 파싱
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // 서버에서 넘어오는 날짜 형식 설정
            .create();
}
