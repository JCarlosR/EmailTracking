package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.LinkType;

import java.util.ArrayList;

public class LinkTypesResponse {

    private ArrayList<LinkType> link_types;

    public ArrayList<LinkType> getLinkTypes() {
        return link_types;
    }

    public void setLinkTypes(ArrayList<LinkType> link_types) {
        this.link_types = link_types;
    }

}
