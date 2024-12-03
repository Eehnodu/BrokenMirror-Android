package com.example.brokenmirror.ui.friend;

// extraction of korean initial sound
// 적용 클래스 : friendList.java & chat_add.java 등 (↙ usages에서 확인)
public class KoreanExtraction {
    private static final char HANGUL_BEGIN_UNICODE = 44032;
    private static final char HANGUL_BASE_UNIT = 588;
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',
            'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    private static boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }

    private static char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    private static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= (HANGUL_BEGIN_UNICODE + 11171);
    }

    public static char extractInitial(char c) {
        if (isHangul(c)) {
            return getInitialSound(c);
        } else {
            return c;
        }
    }
}
