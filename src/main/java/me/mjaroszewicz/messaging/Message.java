package me.mjaroszewicz.messaging;

import android.support.annotation.NonNull;

public class Message implements Comparable<Message>{

    private String address;

    private String body;

    private Long date;

    private boolean seen;

    private boolean incoming;

    public Message(String address, Long date, String body) {
        this.address = address;
        this.body = body;
        this.date = date;
        this.incoming = false;
    }

    public Message(String address, Long date, String body, boolean incoming){
        this.address = address;
        this.body = body;
        this.date = date;
        this.incoming = incoming;
    }

    public Message(String address, String body, Long date, boolean seen, boolean incoming) {
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
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

    @Override
    public String toString() {
        return "Message{" +
                "address='" + address + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                ", seen=" + seen +
                ", incoming=" + incoming +
                '}';
    }


    @Override
    public int compareTo(@NonNull Message o) {

        return (int) (date - o.getDate());
    }
}
