package com.example.brokenmirror.ui.setting;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brokenmirror.R;
import com.example.brokenmirror.ui.friend.KoreanExtraction;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

public class setting_friend_block extends AppCompatActivity {

    private final ArrayList<String> userNames = new ArrayList<>(Arrays.asList(        // test data
            "house", "jellyfish", "wizard", "xylophone", "*개발", "!기획관리", "~솔루션", "#시스템", "@R&D", "세종", "baloon", "cake", "dog", "egg", "flamingoflamingoflamingo", "1grape", "house", "icecream", "kite", "2lemon", "nose", "mouse", "pig", "orange", "rabbit", "queen", "zebra", "yo-wassup","violin", "umbrella", "tomato", "sun"
    ));
    private final ArrayList<String> userNumbers = new ArrayList<>();
    private ArrayList<Pair<String, String>> combinedList = new ArrayList<>();
    PairComparator pairComparator = new PairComparator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_friend_block);

        RecyclerView block_recyclerView = findViewById(R.id.setting_friend_block_recyclerView);
        block_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button back_button = findViewById(R.id.setting_friend_block_back_button);

        NestedScrollView nestedScrollView = findViewById(R.id.setting_friend_block_nestedScrollView);


        // generate phoneNum
        for (int i = 0; i < userNames.size(); i++) {
            String userNumber = String.format("010%04d%04d", generateRandomNumber(), generateRandomNumber());
            userNumbers.add(userNumber);
            Pair<String, String> item = new Pair<>(userNames.get(i), userNumbers.get(i));
            combinedList.add(item);
        }

        // sorted by userName
        Collections.sort(combinedList, pairComparator);

        // set block_recyclerView adapter
        setting_friend_block_adapter adapter = new setting_friend_block_adapter(this, combinedList);
        block_recyclerView.setAdapter(adapter);

        // Button
        // back_button : back
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Log.d("tlqkf", "combinedList" + combinedList);
            }
        });

    }       // onCreate

    // generate userNumber data
    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(10000);
    }

}

// sorting userName data
class PairComparator implements Comparator<Pair<String, String>> {
    private Collator collator = Collator.getInstance(Locale.KOREAN);

    @Override
    public int compare(Pair<String, String> o1, Pair<String, String> o2) {
        int category1 = getCategory(o1.first);
        int category2 = getCategory(o2.first);

        if (category1 != category2) {
            return category1 - category2;
        }

        return collator.compare(o1.first, o2.first);
    }

    // Ordering character
    private int getCategory(String str) {
        char firstChar = KoreanExtraction.extractInitial(str.charAt(0));

        if (Character.getType(firstChar) == Character.OTHER_LETTER) {       // sequence sorting : kor -> eng -> num -> etc
            return 0; // etc (special character)
        } else if (Character.getType(firstChar) == Character.LOWERCASE_LETTER) {
            return 1; // eng
        } else if (Character.isDigit(firstChar)) {
            return 2; // num
        } else {
            return 3; // kor
        }
    }
}