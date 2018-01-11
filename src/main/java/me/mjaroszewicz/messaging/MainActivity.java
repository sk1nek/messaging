package me.mjaroszewicz.messaging;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MessagingService msgService;

    private static Context ctxt;

    private ContactsService cs;

    private static EditText sendSelector;

    private static final int RESULT_PICK_CONTACT = 54321;

    public static Context getContextInstance(){
        return ctxt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctxt = this;

        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);

        tb.setTitle("Inbox");
        tb.setTitleMarginStart(5);
        tb.setTitleMarginTop(5);

        setSupportActionBar(tb);

        msgService = new MessagingService(getApplicationContext());


        ListView lv = findViewById(R.id.threadlist);

        ArrayList<String> addressList = new ArrayList<>();
        msgService.getThreadList().forEach(k -> addressList.add(k.getAddress()));

        ThreadRowAdapter tra = new ThreadRowAdapter(this, msgService.getThreadList());

        lv.setAdapter(tra);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(ctxt, ThreadActivity.class);
            Bundle b = new Bundle();
            b.putString("addr", addressList.get(position));
            i.putExtras(b);
            ctxt.startActivity(i);
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new NewMessageOnClickListener(msgService, this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    MessagingService getMessagingServiceInstance(){

        if(msgService == null)
            msgService = new MessagingService(this);

        return this.msgService;
    }

    public void pickContact(EditText et) {

        Intent pickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(pickerIntent, RESULT_PICK_CONTACT);

        sendSelector = et;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            switch (requestCode){

                case RESULT_PICK_CONTACT:
                    contactPick(data);
                    break;

            }
        }
    }

    private void contactPick(Intent data){

        try{
            Uri uri = data.getData();

            Cursor c = getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();

            String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.w("PICKER", number);

            sendSelector.setText(number);
        }catch (Throwable t){
            Log.e("PCKR", t.getLocalizedMessage());
        }

    }
}
