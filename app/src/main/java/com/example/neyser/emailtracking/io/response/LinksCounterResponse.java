package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.LinkCounter;

import java.util.ArrayList;

public class LinksCounterResponse {

    private ArrayList<LinkCounter> links;

    public ArrayList<LinkCounter> getLinkCounters() {
        return links;
    }

    public void setLinkCounters(ArrayList<LinkCounter> links) {
        this.links = links;
    }

}
