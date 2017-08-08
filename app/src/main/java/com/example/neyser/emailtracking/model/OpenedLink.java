package com.example.neyser.emailtracking.model;

public class OpenedLink {
/*
{"link":"La Republica",
"category":"Software",
"title":"Aplicacion Escritorio",
"client":"Maria Auxiliadora",
"SO":null,
"browser":null,
"IP":null,
"date":"Jun 21 2017 12:00AM"}
*/
    private String link;
    private String category;
    private String title;
    private String client;
    private String SO;
    private String browser;
    private String IP;
    private String date;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSO() {
        return SO;
    }

    public void setSO(String SO) {
        this.SO = SO;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
