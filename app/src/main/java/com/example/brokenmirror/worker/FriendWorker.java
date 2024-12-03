package com.example.brokenmirror.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.FriendRoomDto;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.FriendApi;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.room.FriendRoomDatabase;
import com.example.brokenmirror.ui.friend.friendList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendWorker extends Worker {
    FriendApi friendApi;
    FriendRoomDatabase db;

    List<FriendDto> friendInfoList;

    public FriendWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        friendApi = RetrofitService.getFriendApi();
        db = FriendRoomDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        String id = getInputData().getString("ID"); // 사용자 ID 전달 필요
        try {
            Log.d("TAG", "doWork: 작업 실행");
            getFriendsByUserId(id);
        } catch (Exception e) {
            Log.e("TAG", "doWork : 작업 실패: " + e.getMessage());
            return Result.failure();
        }
        return Result.success();
    }

    // getFriendsByUserId (친구 유저 정보 Room에 저장)
    public void getFriendsByUserId(String id) {
        FriendApi friendApi = RetrofitService.getFriendApi();
        friendApi.getFriendsByUserId(id).enqueue(new Callback<List<FriendDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<FriendDto>> call, @NonNull Response<List<FriendDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendInfoList = response.body();
                    getFriendInfo(id, friendInfoList, new friendList.OnFriendInfoLoaded() {
                        @Override
                        public void onFriendInfoLoaded(List<FriendRoomDto> roomList) {
                            insertFriend(roomList);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FriendDto>> call, @NonNull Throwable t) {
                Log.e("FriendList", "Error: " + t.getMessage());
            }
        });
    }

    // getFriendInfo (친구 유저 정보 조회)
    public void getFriendInfo(String id, List<FriendDto> friendinfolist, friendList.OnFriendInfoLoaded callback) {
        UserApi userApi = RetrofitService.getUserApi();
        userApi.getFriendInfo(id, friendinfolist).enqueue(new Callback<List<UserDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserDto>> call, @NonNull Response<List<UserDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserDto> userResult = response.body();
                    List<FriendRoomDto> roomResult = new ArrayList<>();
                    for (int i = 0; i < userResult.size(); i++) {
                        UserDto dto = userResult.get(i);
                        FriendDto friendDto = friendinfolist.get(i);
                        FriendRoomDto friendRoomDto = new FriendRoomDto(id, dto.getId(), dto.getUserName(), dto.getBirth(), dto.getPhoneNum(), dto.getProfileImg(), friendDto.getCertifiedKey());
                        roomResult.add(friendRoomDto);
                    }
                    callback.onFriendInfoLoaded(roomResult);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserDto>> call, @NonNull Throwable t) {
                Log.e("FriendList", "Error: " + t.getMessage());
            }
        });
    }

    // Room에다가 데이터 넣기
    public void insertFriend(List<FriendRoomDto> roomList) {
        FriendRoomDatabase db = FriendRoomDatabase.getInstance(FriendWorker.this.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 삽입 후 반환값으로 성공 여부 확인
                    // insertAll (친구 정보 삽입)
                    db.friendRoomDao().insertAll(roomList);
                } catch (Exception e) {
                    Log.e("TAG", "Login Insert failed: " + e.getMessage());
                }
            }
        }).start();
    }
}
