package com.example.brokenmirror.ui.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.setting.setting_key_purchase;
import com.example.brokenmirror.ui.setting.user_profile_other;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class chat_room_adapter extends RecyclerView.Adapter<chat_room_adapter.ViewHolder> {
    final private int TYPE_DATE = 0;
    final private int TYPE_EXPIRED = 1;
    final private int TYPE_ME_KEY_COMPLETE = 2;
    final private int TYPE_OTHER_KEY_COMPLETE = 3;
    final private int TYPE_OTHER_KEY_REQ = 4;
    final private int TYPE_ME_KEY_EXPIRED = 5;
    final private int TYPE_OTHER_KEY_EXPIRED = 6;
    final private int TYPE_ME_NOTICE = 7;
    final private int TYPE_OTHER_NOTICE = 8;
    final private int TYPE_ME_GNRL = 9;
    final private int TYPE_OTHER_GNRL = 10;

    private final ArrayList<Integer> types;
    private final ArrayList<String> messages;
    private final ArrayList<Integer> keys;
    private final ArrayList<String> names;
    private final Context context;

    //private List<String> messageList; // 메시지 리스트를 저장할 List 객체

    // 생성자
    public chat_room_adapter(Context context) {
        this.types = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.names = new ArrayList<>();
        this.context = context;

        //messageList = new ArrayList<>(); // 메시지 리스트를 초기화합니다.

    }

    // RecyclerView에 새로운 메시지를 추가하는 메서드
//    public void addMessage(String text) {
//        messageList.add(text); // 받은 메시지를 메시지 리스트에 추가합니다.
//        notifyDataSetChanged(); // RecyclerView를 갱신하여 새로운 메시지를 화면에 표시합니다.
//    }


    public void addMessage(String message) {
        // 메시지 리스트에 상대방 메시지 추가
        addItem(TYPE_OTHER_GNRL, message, 2, "");
        notifyDataSetChanged(); // RecyclerView 갱신
    }

    public void addMyMessage(String text) {
        // 메시지 리스트에 내 메시지 추가
        addItem(TYPE_ME_GNRL, text, 3, "");
        notifyDataSetChanged();
    }


    // ViewHolder 클래스 정의
    public class ViewHolder extends RecyclerView.ViewHolder {
        // TYPE_DATE
        private final TextView date_textView;

        // TYPE_EXPIRED
        private final TextView expired_textView;

        // TYPE_ME_KEY_COMPLETE
        private final TextView me_key_complete_time_textView;

        // TYPE_OTHER_KEY_COMPLETE
        private TextView other_key_complete_time_textView;
        private TextView other_key_complete_name_textView;
        private View other_key_complete_round;
        private View other_key_complete_icon;

        // TYPE_OTHER_KEY_REQ
        private TextView other_key_req_time_textView;
        private TextView other_key_req_name_textView;
        private View other_key_req_round;
        private View other_key_req_icon;
        private ImageView other_key_req_img;

        // TYPE_ME_KEY_EXPIRED
        private TextView me_key_expired_time_textView;
        private ImageView me_key_expired_img;


        // TYPE_OTHER_KEY_EXPIRED
        private TextView other_key_expired_time_textView;
        private TextView other_key_expired_name_textView;
        private View other_key_expired_round;
        private View other_key_expired_icon;
        private ImageView other_key_expired_img;

        // TYPE_ME_NOTICE

        // TYPE_OTHER_NOTICE
        private TextView other_notice_name_textView;
        private View other_notice_round;
        private View other_notice_icon;
        private View other_notice_confirm_button;

        // TYPE_ME_GNRL
        private TextView me_gnrl_message_textView;
        private TextView me_gnrl_time_textView;

        // TYPE_OTHER_GNRL
        private TextView other_gnrl_message_textView;
        private TextView other_gnrl_time_textView;
        private TextView other_gnrl_name_textView;
        private View other_gnrl_round;
        private View other_gnrl_icon;


        private FrameLayout profile_frameLayout;


        public ViewHolder (View view) {
            super(view);
            // TYPE_DATE
            date_textView = view.findViewById(R.id.chatroomDate);

            // TYPE_EXPIRED
            expired_textView = view.findViewById(R.id.expired_date);

            // TYPE_ME_KEY_COMPLETE
            me_key_complete_time_textView = view.findViewById(R.id.me_key_complete_time_textView);

            // TYPE_OTHER_KEY_COMPLETE
            other_key_complete_time_textView = view.findViewById(R.id.other_key_complete_time_textView);
            other_key_complete_name_textView = view.findViewById(R.id.other_key_complete_name_textView);
            other_key_complete_round = view.findViewById(R.id.other_key_complete_round);
            other_key_complete_icon = view.findViewById(R.id.other_key_complete_icon);

            // TYPE_OTHER_KEY_REQ
            other_key_req_time_textView = view.findViewById(R.id.other_key_req_time_textView);
            other_key_req_name_textView = view.findViewById(R.id.other_key_req_name_textView);
            other_key_req_round = view.findViewById(R.id.other_key_req_round);
            other_key_req_icon = view.findViewById(R.id.other_key_req_icon);
            other_key_req_img = view.findViewById(R.id.img_other_key_complete);

            // TYPE_ME_KEY_EXPIRED
            me_key_expired_time_textView = view.findViewById(R.id.me_key_expired_time_textView);
            me_key_expired_img = view.findViewById(R.id.img_me_key_expired);

            // TYPE_OTHER_KEY_EXPIRED
            other_key_expired_time_textView = view.findViewById(R.id.other_key_expired_time_textView);
            other_key_expired_name_textView = view.findViewById(R.id.other_key_expired_name_textView);
            other_key_expired_round = view.findViewById(R.id.other_key_expired_round);
            other_key_expired_icon = view.findViewById(R.id.other_key_expired_icon);
            other_key_expired_img = view.findViewById(R.id.img_other_key_expired);


            // TYPE_OTHER_NOTICE
            other_notice_name_textView = view.findViewById(R.id.other_notice_name_textView);
            other_notice_round = view.findViewById(R.id.other_notice_round);
            other_notice_icon = view.findViewById(R.id.other_notice_icon);
            other_notice_confirm_button = view.findViewById(R.id.other_key_req_confirm_button);


            // TYPE_ME_GNRL
            me_gnrl_time_textView = view.findViewById(R.id.me_gnrl_time_textView);
            me_gnrl_message_textView = view.findViewById(R.id.me_gnrl_message_textView);

            // TYPE_OTHER_GNRL
            other_gnrl_message_textView = view.findViewById(R.id.other_gnrl_message_textView);
            other_gnrl_time_textView = view.findViewById(R.id.other_gnrl_time_textView);
            other_gnrl_name_textView = view.findViewById(R.id.other_gnrl_name_textView);
            other_gnrl_round = view.findViewById(R.id.other_gnrl_round);
            other_gnrl_icon = view.findViewById(R.id.other_gnrl_icon);

            profile_frameLayout = view.findViewById(R.id.profile_frame);

        }
    }


    // onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_DATE:
                view = inflater.inflate(R.layout.chat_room_adapter_date, parent, false);
                return new ViewHolder(view);

            case TYPE_EXPIRED:
                view = inflater.inflate(R.layout.chat_room_adapter_expired, parent, false);
                return new ViewHolder(view);

            case TYPE_ME_KEY_COMPLETE:
                view = inflater.inflate(R.layout.chat_room_adapter_me_key_complete, parent, false);
                return new ViewHolder(view);

            case TYPE_OTHER_KEY_COMPLETE:
                view = inflater.inflate(R.layout.chat_room_adapter_other_key_complete, parent, false);
                return new ViewHolder(view);

            case TYPE_OTHER_KEY_REQ:
                view = inflater.inflate(R.layout.chat_room_adapter_other_key_req, parent, false);
                return new ViewHolder(view);

            case TYPE_ME_KEY_EXPIRED:
                view = inflater.inflate(R.layout.chat_room_adapter_me_key_expired, parent, false);
                return new ViewHolder(view);

            case TYPE_OTHER_KEY_EXPIRED:
                view = inflater.inflate(R.layout.chat_room_adapter_other_key_expired, parent, false);
                return new ViewHolder(view);

            case TYPE_ME_NOTICE:
                view = inflater.inflate(R.layout.chat_room_adapter_me_notice, parent, false);
                return new ViewHolder(view);

            case TYPE_OTHER_NOTICE:
                view = inflater.inflate(R.layout.chat_room_adapter_other_notice, parent, false);
                return new ViewHolder(view);

            case TYPE_ME_GNRL:
                view = inflater.inflate(R.layout.chat_room_adapter_me_gnrl, parent, false);
                return new ViewHolder(view);

            case TYPE_OTHER_GNRL:
                view = inflater.inflate(R.layout.chat_room_adapter_other_gnrl, parent, false);
                return new ViewHolder(view);

            default:
                Log.e("ghkrdls", "Invalid view type: " + viewType);
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        int viewType = getItemViewType(adapterPosition);
        int currentKey = keys.get(adapterPosition);

        // 이전 아이템의 날짜와 비교하여 날짜 뷰를 표시할지 여부 결정
//        if (adapterPosition == 0 || !curDate.equals(messages.get(adapterPosition - 1).getDate())) {
//            holder.date.setVisibility(View.VISIBLE);
//            setCurrentDate(holder.date); // 현재 날짜 설정
//        } else {
//            holder.date.setVisibility(View.GONE);
//        }

        switch (viewType) {

            case TYPE_DATE:
                setCurrentDate(holder.date_textView);
                break;

            case TYPE_EXPIRED:
                setExpiredDate(holder.expired_textView);
                break;

            case TYPE_ME_KEY_COMPLETE:
                setCurrentTime(holder.me_key_complete_time_textView);
                break;

            case TYPE_OTHER_KEY_COMPLETE:
                setCurrentTime(holder.other_key_complete_time_textView);
                holder.other_key_complete_name_textView.setText(names.get(adapterPosition));
                setKeyValueView(currentKey, holder.other_key_complete_round, holder.other_key_complete_icon);
                holder.profile_frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfile(names.get(adapterPosition), currentKey);
                    }
                });

                break;

            case TYPE_OTHER_KEY_REQ:
                setCurrentTime(holder.other_key_req_time_textView);
                holder.other_key_req_name_textView.setText(names.get(adapterPosition));
                setKeyValueView(currentKey, holder.other_key_req_round, holder.other_key_req_icon);
                applyBlur(holder.other_key_req_img);
                holder.profile_frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfile(names.get(adapterPosition), currentKey);
                    }
                });
                break;

            case TYPE_ME_KEY_EXPIRED:
                setCurrentTime(holder.me_key_expired_time_textView);
                applyBlur(holder.me_key_expired_img);
                break;

            case TYPE_OTHER_KEY_EXPIRED:
                setCurrentTime(holder.other_key_expired_time_textView);
                holder.other_key_expired_name_textView.setText(names.get(adapterPosition));
                setKeyValueView(currentKey, holder.other_key_expired_round, holder.other_key_expired_icon);
                applyBlur(holder.other_key_expired_img);
                holder.profile_frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfile(names.get(adapterPosition), currentKey);
                    }
                });
                break;

            case TYPE_OTHER_NOTICE:
                holder.other_notice_name_textView.setText(names.get(adapterPosition));
                setKeyValueView(currentKey, holder.other_notice_round, holder.other_notice_icon);
                holder.other_notice_confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupDialogLack();
                    }
                });
                holder.profile_frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfile(names.get(adapterPosition), currentKey);
                    }
                });
                break;

            case TYPE_ME_GNRL:
                holder.me_gnrl_message_textView.setText(messages.get(adapterPosition));
                setCurrentTime(holder.me_gnrl_time_textView);
                break;

            case TYPE_OTHER_GNRL:
                holder.other_gnrl_message_textView.setText(messages.get(adapterPosition));
                setCurrentTime(holder.other_gnrl_time_textView);
                holder.other_gnrl_name_textView.setText(names.get(adapterPosition));
                setKeyValueView(currentKey, holder.other_gnrl_round, holder.other_gnrl_icon);
                holder.profile_frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfile(names.get(adapterPosition), currentKey);
                    }
                });
                break;

            default:
                break;

        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    // Set current time
    private void setCurrentTime(TextView textView) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        textView.setText(currentTime);
    }

    private void setCurrentDate(TextView textView) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd (EEE)", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String currentDate = sdf.format(new Date());
        textView.setText(currentDate);
    }

    // Set Expired Time
    private void setExpiredDate(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(currentTime);
        textView.setText(formattedDate);
    }

    public void addItem(int type, String message, int key, String name) {
        types.add(type);
        messages.add(message);
        keys.add(key);

        names.add(name);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;

        // GNRL
        switch (types.get(position)) {
            case 0:
                viewType = TYPE_DATE;
                Log.d("ghkrdls", "TYPE_DATE");
                break;

            case 1:
                viewType = TYPE_EXPIRED;
                break;

            case 2:
                viewType = TYPE_ME_KEY_COMPLETE;
                break;

            case 3:
                viewType = TYPE_OTHER_KEY_COMPLETE;
                break;

            case 4:
                viewType = TYPE_OTHER_KEY_REQ;
                break;

            case 5:
                viewType = TYPE_ME_KEY_EXPIRED;
                break;

            case 6:
                viewType = TYPE_OTHER_KEY_EXPIRED;
                break;

            case 7:
                viewType = TYPE_ME_NOTICE;
                break;

            case 8:
                viewType = TYPE_OTHER_NOTICE;
                break;

            case 9:
                viewType = TYPE_ME_GNRL;
                break;

            case 10:
                viewType = TYPE_OTHER_GNRL;
                break;

            default:
                Log.e("ghkrdls", "Invalid view type at position: " + position);
                viewType = -1;
                break;
        }

        return viewType;
    }

    private void setKeyValueView(int currentKey, View roundView, View iconView) {
        switch (currentKey) {
            case 3:
                roundView.setVisibility(View.INVISIBLE);
                iconView.setVisibility(View.GONE);
                break;

            case 2:
                roundView.setVisibility(View.VISIBLE);
                roundView.setBackgroundResource(R.drawable.bg_round_key_2);
                iconView.setVisibility(View.VISIBLE);
                iconView.setBackgroundResource(R.drawable.icon_key_ver_2);
                break;

            case 1:
                roundView.setVisibility(View.VISIBLE);
                roundView.setBackgroundResource(R.drawable.bg_round_key_1);
                iconView.setVisibility(View.VISIBLE);
                iconView.setBackgroundResource(R.drawable.icon_key_ver_1);
                break;

            case 0:
                roundView.setVisibility(View.VISIBLE);
                roundView.setBackgroundResource(R.drawable.bg_round_key_0);
                iconView.setVisibility(View.VISIBLE);
                iconView.setBackgroundResource(R.drawable.icon_key_ver_0);
                break;

            default:
                break;
        }
    }

    private void showUserProfile(String userName, int keyVal) {
        Intent intent = new Intent(context, user_profile_other.class);
        intent.putExtra("userName", userName);
        intent.putExtra("keyVal", keyVal);
        context.startActivity(intent);
    }

    // Blur
    private void applyBlur(ImageView imageView) {

        Context context = imageView.getContext();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.key_use_img);

        Bitmap blurredBitmap = blurBitmap(context, originalBitmap, 25);

        int cornerRadius = dpToPx(context, 16);
        blurredBitmap = addRoundedCorners(blurredBitmap, cornerRadius);

        imageView.setImageBitmap(blurredBitmap);
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private Bitmap addRoundedCorners(Bitmap bitmap, int cornerRadius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final float roundPx = cornerRadius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rect, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }

    private Bitmap blurBitmap(Context context, Bitmap bitmap, float blurRadius) {
        // RenderScript 생성
        RenderScript rs = RenderScript.create(context);

        // 입력 및 출력 Allocation 생성
        Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // ScriptIntrinsicBlur 생성
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(blurRadius); // 블러 반경 설정
        script.setInput(input);

        // 블러 처리 실행
        script.forEach(output);

        // 결과 비트맵 생성
        Bitmap blurredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        output.copyTo(blurredBitmap);

        // 리소스 해제
        rs.destroy();

        return blurredBitmap;
    }

    private void showPopupDialogLack() {
        // create dialog object
        Dialog popupDialog = new Dialog(context);

        // connect layout xml
        popupDialog.setContentView(R.layout.popup_dialog_two_button);

        // background color of the dialog to transparent
        Window window = popupDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView textView = popupDialog.findViewById(R.id.textView);
        Button cancel_button = popupDialog.findViewById(R.id.cancel_button);
        Button confirm_button = popupDialog.findViewById(R.id.confirm_button);

        textView.setText(R.string.popup_key_lack);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
                Intent intent = new Intent(context, setting_key_purchase.class);
                context.startActivity(intent);
            }
        });

        popupDialog.show();
    }

}