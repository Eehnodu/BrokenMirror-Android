/*
__author__ = 'Song Chae Young'
__date__ = 'Jan.24, 2024'
__email__ = '0.0yeriel@gmail.com'
__fileName__ = 'BottomSheetNationDialog.java'
__github__ = 'SongChaeYoung98'
__status__ = 'Development'
*/

package com.example.brokenmirror.ui.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.brokenmirror.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetEmailDialog extends BottomSheetDialogFragment {

    private String emailName;

    public void onStart() {     // round corner, background == transparent
        super.onStart();
        View view = getView();
        if (view != null) {
            ViewGroup parentView = (ViewGroup) view.getParent();
            parentView.setBackgroundColor(Color.TRANSPARENT);

            // Set BottomSheetDialog fully expanded
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            bottomSheetBehavior.setSkipCollapsed(true);     // If the functionality does not work, activate the corresponding code
        }
    }
    private Button lastClickedButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_email, container, false);

        int colorEmph = ContextCompat.getColor(getContext(), R.color.white);

        Button list_0 = view.findViewById(R.id.bottom_sheet_email_list_0_btn);
        Button list_1 = view.findViewById(R.id.bottom_sheet_email_list_1_btn);
        Button list_2 = view.findViewById(R.id.bottom_sheet_email_list_2_btn);
        Button list_3 = view.findViewById(R.id.bottom_sheet_email_list_3_btn);
        Button list_4 = view.findViewById(R.id.bottom_sheet_email_list_4_btn);

        list_1.setText("@" + getString(R.string.find_id_email_list_1));
        list_2.setText("@" + getString(R.string.find_id_email_list_2));
        list_3.setText("@" + getString(R.string.find_id_email_list_3));
        list_4.setText("@" + getString(R.string.find_id_email_list_4));

        // Display the chosen Button
        Bundle bundle = getArguments();
        if (bundle != null) {
            String buttonText = bundle.getString("buttonText");
//            Log.v(buttonText, buttonText);

            if (buttonText.equals(list_0.getText().toString())) {
                list_0.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                list_0.setTextColor(colorEmph);
            }
            if (buttonText.equals(list_1.getText().toString())) {
                list_1.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                list_1.setTextColor(colorEmph);
            }
            if (buttonText.equals(list_2.getText().toString())) {
                list_2.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                list_2.setTextColor(colorEmph);
            }
            if (buttonText.equals(list_3.getText().toString())) {
                list_3.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                list_3.setTextColor(colorEmph);
            }
            if (buttonText.equals(list_4.getText().toString())) {
                list_4.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                list_4.setTextColor(colorEmph);
            }
        }

        // Button
        // bottom_sheet_email_close_btn
        Button closeButton = view.findViewById(R.id.bottom_sheet_email_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // bottom_sheet_email_list_0
        list_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(list_0);
            }
        });

        // bottom_sheet_email_list_1_btn
        list_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(list_1);
            }
        });

        // bottom_sheet_email_list_2_btn
        list_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(list_2);
            }
        });

        // bottom_sheet_email_list_3_btn
        list_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(list_3);
            }
        });

        // bottom_sheet_email_list_4_btn
        list_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(list_4);
            }
        });

        return view;
    }

    public interface TextButtonClickListener {
        void onTextButtonClick(String buttonText);
    }
    private TextButtonClickListener listener;

    public void setTextButtonClickListener(TextButtonClickListener listener) {
        this.listener = listener;
    }

    private void handleButtonClick(Button button) {
        int colorEmph = ContextCompat.getColor(getContext(), R.color.white);


        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundResource(R.drawable.btn_bottomsheet);
        }

        initializeButtons();

        button.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
        button.setTextColor(colorEmph);

        lastClickedButton = button;

        if (listener != null) {
            listener.onTextButtonClick(button.getText().toString());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 290);
    }

    private void initializeButtons() {
        int colorDefault = ContextCompat.getColor(getContext(), R.color.text_default_2);

        Button list_0 = getView().findViewById(R.id.bottom_sheet_email_list_0_btn);
        Button list_1 = getView().findViewById(R.id.bottom_sheet_email_list_1_btn);
        Button list_2 = getView().findViewById(R.id.bottom_sheet_email_list_2_btn);
        Button list_3 = getView().findViewById(R.id.bottom_sheet_email_list_3_btn);
        Button list_4 = getView().findViewById(R.id.bottom_sheet_email_list_4_btn);

        list_0.setBackgroundResource(R.drawable.btn_bottomsheet);
        list_0.setTextColor(colorDefault);

        list_1.setBackgroundResource(R.drawable.btn_bottomsheet);
        list_1.setTextColor(colorDefault);

        list_2.setBackgroundResource(R.drawable.btn_bottomsheet);
        list_2.setTextColor(colorDefault);

        list_3.setBackgroundResource(R.drawable.btn_bottomsheet);
        list_3.setTextColor(colorDefault);

        list_4.setBackgroundResource(R.drawable.btn_bottomsheet);
        list_4.setTextColor(colorDefault);
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }


}
