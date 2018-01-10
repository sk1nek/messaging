package me.mjaroszewicz.messaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.Toolbar;

public class ThreadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getIntent().getExtras();

//        Toast.makeText(this, savedInstanceState.getString("addr"), 5000);

        setContentView(R.layout.activity_thread);

        getSupportActionBar().setTitle(b.getString("addr"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
