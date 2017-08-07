package com.example.neyser.emailtracking.io.response;

import java.util.ArrayList;

public class ClientsBySourceResponse {

    private ArrayList<ClientCounter> sources;

    public ArrayList<ClientCounter> getSources() {
        return sources;
    }

    public void setSources(ArrayList<ClientCounter> sources) {
        this.sources = sources;
    }

    public class ClientCounter {
        private String source;
        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

}
