package me.mjaroszewicz.messaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MessagingService msgService;

    private static Context ctxt;

    private ContactsService cs;

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
            Intent i = new Intent(view.getContext(), ThreadActivity.class);
            ctxt.startActivity(i);
        });


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
}
