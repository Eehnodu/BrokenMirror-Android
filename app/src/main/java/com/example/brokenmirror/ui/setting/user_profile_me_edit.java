package com.example.brokenmirror.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.brokenmirror.R;
import com.example.brokenmirror.bitmap.BitmapConverter;
import com.example.brokenmirror.data.ImageDto;
import com.example.brokenmirror.data.UserDto;
import com.example.brokenmirror.retrofit.ImageApi;
import com.example.brokenmirror.retrofit.RetrofitService;
import com.example.brokenmirror.retrofit.UserApi;
import com.example.brokenmirror.sharedpref.UserSharedPref;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_profile_me_edit extends AppCompatActivity {
    private ImageView editMyProfileImg;
    private TextView editMyNameText;
    private TextView editMyPhoneText;
    private String changedName;
    private UserDto user_info;
    // UserApi
    private final UserApi userApi = RetrofitService.getUserApi();
    private final ImageApi imageApi = RetrofitService.getImageApi();
    // UserSharedPreference
    private UserSharedPref user_pref;
    private MultipartBody.Part body;

    boolean imageChanged = false;

    //    private static final String IMAGE_BASE_URL = "http://localhost:8081/profile/"; // 서버의 이미지 URL 기본 주소
    private static final String IMAGE_BASE_URL = "http://10.0.2.2:8081/profile/"; // 서버의 이미지 URL 기본 주소(에뮬레이터)

    // 권한 허용
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) 이상
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_VIDEO)) {
                    // 권한 요청 이유를 설명하는 화면을 사용자에게 표시
                    Toast.makeText(this, "미디어 파일 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO},
                            1);
                } else {
                    // 권한이 거부되어 다시 요청할 수 없을 경우, 설정 화면으로 유도
                    mediaAlertBox();
                }
            } else {
                moveToPhoto();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11~12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 권한 요청 이유를 설명하는 화면을 사용자에게 표시
                    Toast.makeText(this, "저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    // 권한이 거부되어 다시 요청할 수 없을 경우, 설정 화면으로 유도
                    mediaAlertBox();
                }
            } else {
                moveToPhoto();
            }
        } else {
            // Android 10 이하
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 권한 요청 이유를 설명하는 화면을 사용자에게 표시
                    Toast.makeText(this, "저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                    // 권한 요청
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else {
                    // 권한이 거부되어 다시 요청할 수 없을 경우, 설정 화면으로 유도
                    mediaAlertBox();
                }
            } else {
                moveToPhoto();
            }
        }
    }

    // 사진 및 동영상 권한 알림 메세지 박스
    public void mediaAlertBox() {
        new AlertDialog.Builder(user_profile_me_edit.this)
                .setTitle(getResources().getString(R.string.join_agree_setting_media_title))
                .setMessage(getResources().getString(R.string.join_agree_setting_media_message))
                .setPositiveButton(getResources().getString(R.string.join_agree_setting_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.join_agree_setting_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()
                .show();
    }

    // 앨범으로 이동
    public void moveToPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/jpeg"); // JPEG 형식만 선택 가능
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"}); // JPEG 및 PNG 형식만 선택 가능
        imagelauncher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_me_edit);

        // userSharedPref
        user_pref = new UserSharedPref(this);
        user_info = user_pref.getUser();

        Button closeMyProfileBtn = findViewById(R.id.close_edit_myProfile_btn);

        editMyProfileImg = findViewById(R.id.edit_my_profile_img);
        ImageButton editMyProfileBtn = findViewById(R.id.edit_my_profile_img_btn);
        editMyNameText = findViewById(R.id.edit_my_name_text);
        editMyPhoneText = findViewById(R.id.my_phoneNumber_text);

        Button closeBtn = findViewById(R.id.cancel_btn);
        Button doneBtn = findViewById(R.id.done_btn);

        editMyNameText.setText(user_info.getUserName());
        transNumber(user_info.getPhoneNum());

        if (user_info != null) {
            loadGlideImage(user_info.getProfileImg(), editMyProfileImg);
        }

        closeMyProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 2. 프로필 이미지 설정
        editMyProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        editMyProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        // 3. 이름 설정
        editMyNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile_me_edit.this, user_profile_me_change_name.class);
                intent.putExtra("userName", editMyNameText.getText().toString());
                launcher.launch(intent);
            }
        });

        // 4. 하단 버튼 [취소][확인]
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedName = editMyNameText.getText().toString().trim();
                Intent profile = new Intent();
                boolean nameChanged = !changedName.equals(user_info.getUserName());
                if (nameChanged) {
                    user_info.setUserName(changedName);
                    user_pref.putUser(user_info);
                    newUserName(user_info.getId(), changedName);
                }
                if (imageChanged) {
                    saveImage(body);
                }
                Toast.makeText(getApplicationContext(), "프로필이 변경되었습니다.", Toast.LENGTH_LONG).show();
                // 0.5초 딜레이 추가 후 화면 종료
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK, profile);
                        finish();
                    }
                }, 500); // 500 밀리초 = 0.5초
            }
        });
    } //onCreate

    // registerForActivityResult
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String changeName = result.getData().getStringExtra("userName");
                    editMyNameText.setText(changeName);
                }
            }
        }
    });

    // imageActivityResult
    ActivityResultLauncher<Intent> imagelauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData();

                    // 선택한 이미지를 ImageView에 로드
                    Glide.with(user_profile_me_edit.this)
                            .load(imageUri)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(editMyProfileImg);

                    File imageFile = new File(getRealPathFromURI(imageUri)); // Uri를 파일 경로로 변환
                    // RequestBody 생성
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
                    // MultipartBody.Part 생성
                    body = MultipartBody.Part.createFormData("imageFile", imageFile.getName(), requestFile);
                    imageChanged = true;
                }
            }
        }
    });

    // Uri를 파일 경로로 변환하는 함수
    public String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    // newUserName (이름 변경)
    public void newUserName(String id, String userName) {
        userApi.newUserName(id, userName).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

    // newImage (이미지 변경)
    public void newImage(String id, String profileImg) {
        userApi.newImage(id, profileImg).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
            }

            @Override
            public void onFailure(@NonNull Call<UserDto> call, @NonNull Throwable t) {
                Log.e("TAG", "Error: " + t.getMessage());
            }
        });
    }

    // saveImage
    public void saveImage(MultipartBody.Part body) {
        imageApi.saveImage(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = IMAGE_BASE_URL + response.body();
                    newImage(user_info.getId(), imageUrl);
                    user_info.setProfileImg(imageUrl); // user_info 업데이트
                    user_pref.putUser(user_info); // SharedPref에 저장
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d("TAG", "Login t : " + t.getMessage());
            }
        });
    }

    // Glide (이미지 로드)
    public void loadGlideImage(String imageUrl, ImageView profileImage) {
        Glide.with(user_profile_me_edit.this)
                .load(imageUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(profileImage);
    }

    public void transNumber(String userNumber) {
        if (userNumber != null) {
            if (userNumber.length() == 11) {
                String formattedNumber = userNumber.substring(0, 3) + "-" + userNumber.substring(3, 7) + "-" + userNumber.substring(7);
                editMyPhoneText.setText(formattedNumber);
            } else {
                editMyPhoneText.setText(userNumber);
            }
        }
    }
}