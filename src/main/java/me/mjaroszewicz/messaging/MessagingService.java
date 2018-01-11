package me.mjaroszewicz.messaging;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingService {

    private final Context context;

    private static Map<String, List<Message>> msgMap;

    private static List<Conversation> conversations;



    MessagingService(Context context) {
        this.context = context;

        init();

        getMessages();
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

        messages.addAll(getMessages());

        HashMap<String, List<Message>> map = new HashMap<>();

        for(Message m: messages){

            String addr = m.getAddress();
            List<Message> list = map.computeIfAbsent(addr, k -> new ArrayList<>());

            list.add(m);
        }

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

    public void sendMessage(String addr, String message){

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(addr, null, message, null, null);
            Toast.makeText(context, "Sent.", Toast.LENGTH_SHORT);
        }catch(Throwable t){
            Toast.makeText(context, "Could not send message.", Toast.LENGTH_LONG);

        }



    }


}


