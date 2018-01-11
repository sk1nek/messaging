package me.mjaroszewicz.messaging;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MessagingService {

    //TODO - get uri from base class

    private final Context context;

    private final static Uri smsInboxUri = Telephony.Sms.Inbox.CONTENT_URI;

    private final static Uri smsSentUri = Telephony.Sms.Sent.CONTENT_URI;

    private final static Uri conversationsUri = Telephony.Sms.Conversations.CONTENT_URI;

    private static Map<String, List<Message>> msgMap;

    private static List<Conversation> conversations;



    MessagingService(Context context) {
        this.context = context;

        init();

        getMessages();

        //
        Uri u = Telephony.Sms.CONTENT_URI;

        Cursor c = context.getContentResolver().query(u, new String[]{"*"}, null, null, null);

        for(String s: c.getColumnNames()){
            Log.w("XD", s + "");
        }




    }

    List<Message> getInbox() {

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(smsInboxUri, new String[]{"*"}, null, null, null);

        ArrayList<Message> ret = new ArrayList<>();

        int addressC = c.getColumnIndex(Telephony.Sms.ADDRESS);
        int dateC = c.getColumnIndex(Telephony.Sms.DATE);
        int bodyC = c.getColumnIndex(Telephony.Sms.BODY);
        int seenC = c.getColumnIndex(Telephony.Sms.SEEN);

        while(c.getCount() > 0 && c.moveToNext()){


            String addr = parseAddress(c.getString(addressC));
            Long date = Long.parseLong(c.getString(dateC));
            String body = c.getString(bodyC);
            boolean seen = Boolean.parseBoolean(c.getString(seenC));

            Message msg = new Message(addr, body, date, seen, true);
            ret.add(msg);
        }
        c.close();

        return ret;
    }

    List<Message> getSent(){

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(smsSentUri, new String[]{Telephony.Sms.Sent.ADDRESS, Telephony.Sms.Sent.BODY, Telephony.Sms.Sent.DATE}, null, null, null);

        int addrC = c.getColumnIndex(Telephony.Sms.Sent.ADDRESS);
        int bodyC = c.getColumnIndex(Telephony.Sms.Sent.BODY);
        int dateC = c.getColumnIndex(Telephony.Sms.Sent.DATE);

        ArrayList<Message> ret = new ArrayList<>();

        while(c.getCount() > 0 && c.moveToNext()){

            String addr = parseAddress(c.getString(addrC));
            String body = c.getString(bodyC);
            Long date = Long.parseLong(c.getString(dateC));

            Message msg = new Message(addr, date, body);
            ret.add(msg);
        }

        return ret;
    }

    List<Message> getMessages(){

        ContentResolver cr = context.getContentResolver();

        Uri uri = Telephony.Sms.CONTENT_URI;

        Cursor c = cr.query(uri, new String[]{Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE, Telephony.Sms.TYPE}, null, null, null);

        int typeC = c.getColumnIndex(Telephony.Sms.TYPE);
        int addrC = c.getColumnIndex(Telephony.Sms.ADDRESS);
        int dateC = c.getColumnIndex(Telephony.Sms.DATE);
        int bodyC = c.getColumnIndex(Telephony.Sms.BODY);

        List<Message> ret = new ArrayList<>();

        while(c.getCount() > 0 && c.moveToNext()){

            String addr = parseAddress(c.getString(addrC));
            Long date = Long.parseLong(c.getString(dateC));
            String body = c.getString(bodyC);
            int type = Integer.parseInt(c.getString(typeC));

            boolean incoming = !(type == Telephony.Sms.MESSAGE_TYPE_SENT);

            Message msg = new Message(addr, date, body, incoming);
            ret.add(msg);
        }

        return ret;
    }

    private void init(){

        ArrayList<Message> messages = new ArrayList<>();

//        messages.addAll(getInbox());
//        messages.addAll(getSent());

        messages.addAll(getMessages());

        HashMap<String, List<Message>> map = new HashMap<>();

        for(Message m: messages){

            String addr = m.getAddress();
            List<Message> list = map.computeIfAbsent(addr, k -> new ArrayList<>());

            list.add(m);
        }



//        map.values().forEach(p -> p.sort(Comparator.naturalOrder()));

        conversations = new ArrayList<>();

        map.forEach((k, v) -> conversations.add(new Conversation(k, v)));
        conversations.sort(Comparator.reverseOrder());

        msgMap = map;

    }

    List<Message> getSingleThread(String addr){
        return msgMap.get(addr);
    }

    List<Conversation> getThreadList(){
        return conversations;
    }

    /**
     * Removes unneccessary characters from address, such as +48 (number for Poland)
     * and spaces.
     *
     * @param addr Unparsed address
     * @return Clear address
     */
    private String parseAddress(String addr){
        addr = addr.replaceAll("\\s", "");
        addr = addr.replaceFirst("\\+48", "");

        return addr;
    }



}


