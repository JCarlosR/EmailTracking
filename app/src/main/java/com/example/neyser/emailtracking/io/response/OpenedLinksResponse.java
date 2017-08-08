package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.OpenedLink;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OpenedLinksResponse {

    @SerializedName("opened_links")
    private ArrayList<OpenedLink> openedLinks;

    public ArrayList<OpenedLink> getOpenedLinks() {
        return openedLinks;
    }

    public void setOpenedLinks(ArrayList<OpenedLink> openedLinks) {
        this.openedLinks = openedLinks;
    }
}
