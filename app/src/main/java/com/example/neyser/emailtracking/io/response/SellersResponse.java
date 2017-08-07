package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.Seller;

import java.util.ArrayList;

public class SellersResponse {

    private ArrayList<Seller> sellers;

    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Seller> sellers) {
        this.sellers = sellers;
    }
}
