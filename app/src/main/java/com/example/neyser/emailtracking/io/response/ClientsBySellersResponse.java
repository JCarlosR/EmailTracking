package com.example.neyser.emailtracking.io.response;

import java.util.ArrayList;

public class ClientsBySellersResponse {

    private ArrayList<ClientCounter> sellers;

    public ArrayList<ClientCounter> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<ClientCounter> sellers) {
        this.sellers = sellers;
    }

    public class ClientCounter {
        private int id;
        private int quantity;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

}
