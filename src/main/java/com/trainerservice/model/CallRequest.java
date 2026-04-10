package com.trainerservice.model;

import java.util.List;
import java.util.Map;

public class CallRequest {

    private String phoneNumber;
    private String task;
    private String voice;
    private List<Map<String, String>> pathways;
    private String firstSentence;
    private boolean waitForGreeting;

    public CallRequest() {
    }

    public CallRequest(String phoneNumber, String task) {
        this.phoneNumber = phoneNumber;
        this.task = task;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public List<Map<String, String>> getPathways() {
        return pathways;
    }

    public void setPathways(List<Map<String, String>> pathways) {
        this.pathways = pathways;
    }

    public String getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(String firstSentence) {
        this.firstSentence = firstSentence;
    }

    public boolean isWaitForGreeting() {
        return waitForGreeting;
    }

    public void setWaitForGreeting(boolean waitForGreeting) {
        this.waitForGreeting = waitForGreeting;
    }
}
