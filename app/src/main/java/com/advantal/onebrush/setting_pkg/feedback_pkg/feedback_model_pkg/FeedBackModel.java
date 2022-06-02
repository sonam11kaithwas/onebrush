package com.advantal.onebrush.setting_pkg.feedback_pkg.feedback_model_pkg;

/**
 * Created by Surbhi joshi on 09-03-2022 11:47.
 */
public class FeedBackModel {
    private final String uuId;
    private final String firstName;
    private final String feedback;
//{"feedback": "I am a jane", "firstName": "jane", "uuId": "235ea6c5-cd8a-47b2-b508-9720061f22ec"}

    public FeedBackModel(String uuid, String firstName, String feedback) {
        this.uuId = uuid;
        this.firstName = firstName;
        this.feedback = feedback;
    }


    public String getUuId() {
        return uuId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFeedback() {
        return feedback;
    }
}
