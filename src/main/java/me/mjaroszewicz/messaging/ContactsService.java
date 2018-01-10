package me.mjaroszewicz.messaging;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ContactsService {

    private final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    private static HashMap<String, String> addrToNameMap = new HashMap<>();

    private ContentResolver cr;

    public ContactsService(){
        init();
    }

    private void init(){

        cr = MainActivity.getContextInstance().getContentResolver();

        Cursor c = cr.query(uri, null, null, null, null);

        int numberC = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int displayNameC = c.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME);


        if(c.getCount() > 0 ) {
            while (c.moveToNext()) {
                if(numberC != -1) {
                    addrToNameMap.put(c.getString(numberC), c.getString(displayNameC));
                    Log.w("NUM", c.getString(numberC));
                }

            }
        }

        Log.w("ADDRMAPSIZE", addrToNameMap.size() + "");

        for (Map.Entry<String,String> e: addrToNameMap.entrySet()){
            Log.w("MAP" ,e.getKey() + "  " + e.getValue());
        }


    }

    public String getContactName(String addr){
        String ret = addrToNameMap.get(addr);

        if(ret == null)
            return addr;

        return ret;
    }

}
