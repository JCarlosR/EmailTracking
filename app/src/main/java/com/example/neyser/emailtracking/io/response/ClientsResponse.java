package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.Client;

import java.util.ArrayList;

public class ClientsResponse {

    private ArrayList<Client> clients;

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
}
