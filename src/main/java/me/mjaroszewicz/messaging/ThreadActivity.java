package me.mjaroszewicz.messaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class ThreadActivity extends AppCompatActivity {

    private MessagingService ms;

    private List<Message> data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        ms = ((MainActivity) MainActivity.getContextInstance()).getMessagingServiceInstance();

        Bundle b = this.getIntent().getExtras();

        String addr = b.getString("addr");

        getSupportActionBar().setTitle(addr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = ms.getSingleThread(addr);

        ListView lv = findViewById(R.id.messages_view);
        ConversationAdapter adapter = new ConversationAdapter(this, data);
        lv.setAdapter(adapter);



    }

}
