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

public class BottomSheetNationDialog extends BottomSheetDialogFragment {

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
        View view = inflater.inflate(R.layout.bottomsheet_nation, container, false);

        int colorEmph = ContextCompat.getColor(getContext(), R.color.white);

        Button nat = view.findViewById(R.id.bottom_sheet_name_nat_btn);
        Button fore = view.findViewById(R.id.bottom_sheet_name_fore_btn);

        // Display the chosen Button
        Bundle bundle = getArguments();
        if (bundle != null) {
            String buttonText = bundle.getString("buttonText");

            if (buttonText.equals(nat.getText().toString())) {
                nat.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                nat.setTextColor(colorEmph);
            }
            if (buttonText.equals(fore.getText().toString())) {
                fore.setBackgroundResource(R.drawable.btn_bottomsheet_emph);
                fore.setTextColor(colorEmph);
            }
        }

        // Button
        // bottom_sheet_name_close_btn
        Button closeButton = view.findViewById(R.id.bottom_sheet_name_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // bottom_sheet_name_nat_btn
        nat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(nat);
            }
        });

        // bottom_sheet_name_fore_btn
        fore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonClick(fore);
            }
        });

        return view;
    }

    public interface TextButtonClickListener {
        void onTextButtonClick(String buttonText);
    }
    private BottomSheetNationDialog.TextButtonClickListener listener;

    public void setTextButtonClickListener(BottomSheetNationDialog.TextButtonClickListener listener) {
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

        if(listener != null) {
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

        Button nat = getView().findViewById(R.id.bottom_sheet_name_nat_btn);
        Button fore = getView().findViewById(R.id.bottom_sheet_name_fore_btn);

        nat.setBackgroundResource(R.drawable.btn_bottomsheet);
        nat.setTextColor(colorDefault);

        fore.setBackgroundResource(R.drawable.btn_bottomsheet);
        fore.setTextColor(colorDefault);
    }
}
