package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.Email;

import java.util.ArrayList;

public class OpenedEmailsResponse {

    private ArrayList<Email> emails;

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<Email> emails) {
        this.emails = emails;
    }
}
