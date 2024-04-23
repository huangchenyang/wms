package com.feiruirobots.jabil.p1.common;

import android.speech.tts.TextToSpeech;

import com.feiruirobots.jabil.p1.App;

import java.util.Locale;

public class TTSUtil {
    private static TextToSpeech textToSpeech;
    private static float speechRate = 1.0f;  // 默认语速
    private static float pitch = 1.0f;       // 默认音调
    private static Locale locale = Locale.ENGLISH; // 默认语言
    private static String utteranceId = "jabil"; // 唯一标识符

    public static void speak(String text) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(App.getInstance(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(locale);
                    textToSpeech.setSpeechRate(speechRate);
                    textToSpeech.setPitch(pitch);
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    return;
                }
            });
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
