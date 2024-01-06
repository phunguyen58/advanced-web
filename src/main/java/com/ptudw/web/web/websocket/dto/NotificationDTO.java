package com.ptudw.web.web.websocket.dto;

import java.util.ArrayList;
import java.util.List;

public class NotificationDTO {

    private String topic;

    private String message;

    private String userSenderLogin;

    private String userReceiverLogin;

    private String gradeReviewId;

    private String type;

    private List<String> receivers = new ArrayList<>();

    public NotificationDTO() {
        // Default constructor for Jackson
    }

    public NotificationDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserSenderLogin() {
        return this.userSenderLogin;
    }

    public void setUserSenderLogin(String userSenderLogin) {
        this.userSenderLogin = userSenderLogin;
    }

    public String getUserReceiverLogin() {
        return this.userReceiverLogin;
    }

    public void setUserReceiverLogin(String userReceiverLogin) {
        this.userReceiverLogin = userReceiverLogin;
    }

    public String getGradeReviewId() {
        return this.gradeReviewId;
    }

    public void setGradeReviewId(String gradeReviewId) {
        this.gradeReviewId = gradeReviewId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getReceivers() {
        return this.receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }
}
