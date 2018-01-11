package me.mjaroszewicz.messaging;

import android.support.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class Conversation implements Comparable<Conversation>{

    private String contactName;

    private List<Message> messages;

    public Conversation(String contactName, List<Message> messages) {
        this.contactName = contactName;
        this.messages = messages;
    }

    public String getAddress() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getLastMessageSnippet(){

        String msg = messages.get(0).getBody();

        if(msg.length() >= 50)
            return msg.substring(0, 49);

        return msg;
    }

    public int getMessageCount(){
        return messages.size();
    }

    public int getUnreadCount(){
        int ret = 0;

//        messages.forEa;
        return ret;
    }

    @Override
    public int compareTo(@NonNull Conversation o) {

        Long d1 = messages.get(0).getDate();
        Date dateThis = new Date(d1);
        Date dateThat = new Date(o.getMessages().get(0).getDate());

        return dateThis.compareTo(dateThat);
    }
}
