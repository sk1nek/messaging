package me.mjaroszewicz.messaging;

public class Message {

    private String address;

    private String body;

    private String date;

    private boolean seen;

    private boolean incoming = true;

    public Message(String address, String date, String body) {
        this.address = address;
        this.body = body;
        this.date = date;
    }

    public Message(String address, String date, String body, boolean incoming){
        this.address = address;
        this.body = body;
        this.date = date;
        this.incoming = incoming;
    }

    public Message(String address, String body, String date, boolean seen, boolean incoming) {
        this.address = address;
        this.body = body;
        this.date = date;
        this.seen = seen;
        this.incoming = incoming;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }
}
