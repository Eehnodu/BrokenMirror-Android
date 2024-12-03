package com.example.brokenmirror.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.FriendRoomDto;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface FriendRoomDao {

    // insertAll (친구 정보 삽입)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<FriendRoomDto> roomList);

    // getAll (친구 정보 조회)
    @Query("SELECT * FROM FriendRoom")
    List<FriendRoomDto> getAll();

//    @Delete
//    void deleteAll(String id);
}