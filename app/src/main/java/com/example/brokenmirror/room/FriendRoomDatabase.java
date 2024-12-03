package com.example.brokenmirror.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.brokenmirror.dao.FriendRoomDao;
import com.example.brokenmirror.data.FriendRoomDto;

@Database(entities = {FriendRoomDto.class}, version = 2)
public abstract class FriendRoomDatabase extends RoomDatabase {

    // 데이터베이스 인스턴스
    private static FriendRoomDatabase instance;

    public abstract FriendRoomDao friendRoomDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `friendroom` ADD COLUMN 'key' int NOT NULL");
        }
    };

    // 싱글톤 패턴으로 데이터베이스 인스턴스 생성
    public static FriendRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (FriendRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    FriendRoomDatabase.class, "friendroom.db")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return instance;
    }
}
