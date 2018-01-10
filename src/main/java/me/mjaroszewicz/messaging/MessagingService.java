package me.mjaroszewicz.messaging;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import java.util.*;

public class MessagingService {

    private final Context context;

    private final static Uri inboxUri = Telephony.Sms.Inbox.CONTENT_URI;

    private final static Uri conversationsUri = Telephony.Sms.Conversations.CONTENT_URI;

    private static HashMap<String, ArrayList<Message>> data;

    private static List<Message> sent;

    private static ArrayList<Conversation> conversations;

    private static List<Message> inbox;

    MessagingService(Context context) {
        this.context = context;

        init();
    }

    private void fetchSent(){

        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Sent.CONTENT_URI, new String[]{
                Telephony.Sms.Sent.ADDRESS,
                Telephony.Sms.Sent.BODY,
                Telephony.Sms.Sent.DATE_SENT},
                null, null, null);

        int addrIndex = c.getColumnIndex(Telephony.Sms.Sent.ADDRESS);
        int bodyIndex = c.getColumnIndex(Telephony.Sms.Sent.BODY);
        int dateIndex = c.getColumnIndex(Telephony.Sms.Sent.DATE_SENT);

        ArrayList<Message> buffer = new ArrayList<>();

        while(c.moveToNext() && c.getCount() > 0){

            Message m = new Message(c.getString(addrIndex), c.getString(dateIndex), c.getString(bodyIndex), false);
            buffer.add(m);

        }

        sent = buffer;
    }


    private void fetchInbox(){

        ArrayList<Message> ret = new ArrayList<>();


        Cursor c = context.getContentResolver().query(inboxUri, new String[]{
                Telephony.Sms.Inbox._ID,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.DATE,
                Telephony.Sms.Inbox.BODY,
                Telephony.Sms.Inbox.SEEN},
                null, null, null);
        c.moveToFirst();

        for(String s: c.getColumnNames())
            Log.w("COLUMNSNAMES", s +  " ");

        while(c.moveToNext()){

            String address = c.getString(1);
            String date = c.getString(2);
            String body = c.getString(3);

            Message m = new Message(address, date, body);
            ret.add(m);
        }

        inbox = ret;
    }

    private void init(){
        fetchSent();
        fetchInbox();

        List<Message> messages = new ArrayList<>();
        messages.addAll(inbox);
        messages.addAll(sent);

        HashMap<String, ArrayList<Message>> map = new HashMap<>();

        for(Message m: messages){
            String addr = m.getAddress();

            ArrayList<Message> al = map.get(addr);

            if( al == null)
                al = new ArrayList<>();

            al.add(m);

            map.put(addr, al);
        }

        map.forEach((k, v) -> v.sort((o1, o2) -> (int) (Long.parseLong(o1.getDate()) - Long.parseLong(o2.getDate()))));


        data = map;
    }

    public List<Conversation> getThreadList(){

        ArrayList<Conversation> ret = new ArrayList<>();

        for(Map.Entry<String, ArrayList<Message>> e: data.entrySet()){
            ret.add(new Conversation(e.getKey(), e.getValue()));
        }

        return ret;
    }

    public List<Message> getSingleThread(String addr){

        List<Message> ret = data.get(addr);

        if(ret == null)
            return Collections.emptyList();

        return ret;
    }

}


