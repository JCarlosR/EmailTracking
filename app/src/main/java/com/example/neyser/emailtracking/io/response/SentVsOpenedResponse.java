package com.example.neyser.emailtracking.io.response;

public class SentVsOpenedResponse {

    private int sent;
    private int opened;

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }
}
