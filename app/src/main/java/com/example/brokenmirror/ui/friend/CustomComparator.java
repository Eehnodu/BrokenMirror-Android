package com.example.brokenmirror.ui.friend;

import com.example.brokenmirror.data.FriendDto;
import com.example.brokenmirror.data.FriendRoomDto;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomComparator implements Comparator<FriendRoomDto> {
    private Collator collator = Collator.getInstance(Locale.KOREAN);
    private HashMap<String, Integer> sectionIndexes = new HashMap<>();

//    public HashMap<String, Integer> getSectionIndexes(List<FriendRoomDto> data) {
//        generateSectionIndexes(data);
//        return sectionIndexes;
//    }

    @Override
    public int compare(FriendRoomDto o1, FriendRoomDto o2) {
        int category1 = getCategory(o1.getUserName());
        int category2 = getCategory(o2.getUserName());
        if (category1 != category2) {
            return category1 - category2; // 카테고리로 정렬
        }
        if (o1.getUserName().equals(o2.getUserName())) {
            int id1 = Integer.parseInt(o1.getUserId2());
            int id2 = Integer.parseInt(o2.getUserId2());
            return Integer.compare(id1, id2);
        }
        return compareKorean(o1.getUserName(), o2.getUserName()); // 한국어 정렬을 적용
    }

    // 글자별 분류
    private int getCategory(String str) {
        if (str.isEmpty()) {
            return 3; // 빈 문자열은 특수문자로 분류
        }
        char firstChar = str.charAt(0);
        if (isKorean(firstChar)) {
            return 0; // 한글
        } else if (Character.isLetter(firstChar)) {
            return 1; // 영어
        } else if (Character.isDigit(firstChar)) {
            return 2; // 숫자
        } else {
            return 3; // 특수문자 및 기타
        }
    }

    private boolean isKorean(char c) {
        return (c >= 0xAC00 && c <= 0xD7A3); // 한글 음절 영역
    }

    private int compareKorean(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int minLength = Math.min(len1, len2);
        for (int i = 0; i < minLength; i++) {
            char char1 = s1.charAt(i);
            char char2 = s2.charAt(i);
            // 한글 분리
            int result = compareHangul(char1, char2);
            if (result != 0) {
                return result; // 차이가 나면 반환
            }
        }
        return Integer.compare(len1, len2); // 길이가 다르면 짧은 것이 앞
    }

    // 모음 비교
    private int compareHangul(char char1, char char2) {
        if (isKorean(char1) && isKorean(char2)) {
            // 한글 음절을 초성, 중성으로 분리
            int index1 = char1 - 0xAC00; // 한글 시작 유니코드
            int index2 = char2 - 0xAC00;
            int cho1 = index1 / (21 * 28); // 초성
            int jung1 = (index1 % (21 * 28)) / 28; // 중성
            int jong1 = index1 % 28; // 종성
            int cho2 = index2 / (21 * 28);
            int jung2 = (index2 % (21 * 28)) / 28;
            int jong2 = index2 % 28;
            // 초성 비교
            if (cho1 != cho2) {
                return cho1 - cho2;
            }
            // 중성 비교
            if (jung1 != jung2) {
                return jung1 - jung2;
            }
            // 종성 비교
            return jong1 - jong2;
        }
        return collator.compare(String.valueOf(char1), String.valueOf(char2)); // 비한글 문자 비교
    }

    public void generateSectionIndexes(List<FriendRoomDto> data) {
        String currentSection = "";
        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i).getUserName();
            String firstChar = String.valueOf(KoreanExtraction.extractInitial(item.charAt(0))).toUpperCase(Locale.getDefault());

            if (!currentSection.equals(firstChar)) {
                sectionIndexes.put(firstChar, i);
                currentSection = firstChar;
            }
        }
    }

    public Map<String, Integer> getSectionIndexes(List<FriendRoomDto> data) {
        Map<String, Integer> sectionIndexes = new HashMap<>();
        String currentSection = "";

        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i).getUserName();
            String firstChar = String.valueOf(KoreanExtraction.extractInitial(item.charAt(0))).toUpperCase(Locale.getDefault());

            if (!currentSection.equals(firstChar)) {
                sectionIndexes.put(firstChar, i);
                currentSection = firstChar;
            }
        }
        return sectionIndexes;
    }
}

