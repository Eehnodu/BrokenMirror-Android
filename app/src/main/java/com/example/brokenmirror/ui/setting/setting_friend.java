/*
__author__ = 'Song Chae Young'
__date__ = 'Mar.15, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'setting_friend.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brokenmirror.R;
import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.FriendRoomDto;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.FriendApi;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.room.FriendRoomDatabase;
import com.example.brokenmirror.sharedpref.UserSharedPref;
import com.example.brokenmirror.ui.friend.friendList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class setting_friend extends AppCompatActivity {

    List<FriendDto> friendInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_friend);

        UserSharedPref user_pref = new UserSharedPref(this);
        UserDto user_info = user_pref.getUser();

        Button back_button = findViewById(R.id.setting_friend_back_button);

        TextView block_layout = findViewById(R.id.setting_friend_block_layout);
        TextView sync_layout = findViewById(R.id.setting_friend_sync_layout);
        TextView update_layout = findViewById(R.id.setting_friend_update_layout);

        View loading = findViewById(R.id.loading);

        Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.test_need_server), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);

        // Button
        // back_button : OnClickListener
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // LinearLayout
        // block_layout : OnClickListener
//        block_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(setting_friend.this, setting_friend_block.class);
//                startActivity(intent);
//                onPause();
//            }
//        });

        // sync_layout : OnClickListener
        sync_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                    }
                }, 3000);
                toast.show();
            }
        });

        // update_layout : OnClickListener
        update_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFriendsByUserId(user_info.getId());
                Toast.makeText(setting_friend.this, "데이터 업데이트 성공", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Room에다가 데이터 넣기
    public void insertFriend(List<FriendRoomDto> roomList) {
        FriendRoomDatabase db = FriendRoomDatabase.getInstance(this);
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

    // getFriendsByUserId (친구목록 가져오기)
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
                } else {
                    Toast.makeText(setting_friend.this, "서버에서 데이터 못 가져옴1", Toast.LENGTH_SHORT).show();
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
                    callback.onFriendInfoLoaded(roomResult);  // 데이터가 준비되면 콜백 호출
                } else {
                    Toast.makeText(setting_friend.this, "서버에서 데이터 못 가져옴2", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserDto>> call, @NonNull Throwable t) {
                Log.e("FriendList", "Error: " + t.getMessage());
            }
        });
    }
}
