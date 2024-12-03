package com.example.brokenmirror.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.brokenmirror.R;

import java.io.ByteArrayOutputStream;

public class BitmapConverter {

    private Context context;
//    Bitmap ->  String
    public String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

//    //Bitmap ->  String
//    public String BitmapToString(Bitmap bitmap) {
//
//        ByteArrayOutputStream baos;
//        String temp;
//        int quality = 80;
//
//        do {
//            // 비트맵 리사이즈
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * 0.75), (int)(bitmap.getHeight() * 0.75), true);
//
//            // 비트맵 압축
//            baos = new ByteArrayOutputStream();
//            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            byte[] bytes = baos.toByteArray();
//            temp = Base64.encodeToString(bytes, Base64.DEFAULT);
//
//            // 최종 파일 크기 로그 출력
//            Log.d("비트맵 컨버터", "리사이즈된 비트맵 크기: " + baos.size() + " bytes");
//
//            // 비트맵 메모리 해제
//            resizedBitmap.recycle();
//
//            // 품질 감소
//            quality -= 10;
//        } while (baos.size() > 500 * 1024 && quality > 0); // 1MB 이하가 될 때까지 반복
//
//        return temp;
//    }

    //  String ->  BitMap
    public Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}


