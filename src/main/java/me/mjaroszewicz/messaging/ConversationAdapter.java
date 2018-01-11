package me.mjaroszewicz.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class ConversationAdapter extends BaseAdapter {

    private Context context;

    private List<Message> data;

    private static LayoutInflater layoutInflater = null;

    public ConversationAdapter(Context context, List<Message> data){

        this.context = context;
        this.data = data;

        Collections.reverse(this.data);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message msg = data.get(position);
        View row = convertView;

        if(msg.isIncoming())
            row = layoutInflater.inflate(R.layout.left, parent, false);
        else
            row = layoutInflater.inflate(R.layout.right, parent, false);

        TextView tv = row.findViewById(R.id.msgr);
        tv.setText(msg.getBody());


        return row;
    }
}
