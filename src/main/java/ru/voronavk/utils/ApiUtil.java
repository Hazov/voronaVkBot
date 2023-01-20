package ru.voronavk.utils;

import api.longpoll.bots.model.events.messages.MessageEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ApiUtil {
    public static String getUserAnswer(MessageEvent messageEvent) {
        String answer = null;
        JsonObject payload = messageEvent.getPayload().getAsJsonObject();
        JsonElement answerJson = payload.get("answer");
        if (answerJson != null) {
            answer = answerJson.toString();
        }
        if (answer != null) {
            return answer.substring(1, payload.get("answer").toString().length() - 1);
        } else {
            return "";
        }

    }
}
