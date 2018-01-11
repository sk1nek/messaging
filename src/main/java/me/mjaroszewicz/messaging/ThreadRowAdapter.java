package me.mjaroszewicz.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

public class ThreadRowAdapter extends BaseAdapter {

    private Context context;

    private List<Conversation> data;

    private ContactsService cs;

    private static LayoutInflater layoutInflater = null;

    public ThreadRowAdapter(Context context, List<Conversation> data) {

        this.context = context;
        this.data = data;
        this.cs = new ContactsService();

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

        View v = convertView;

        if(v == null)
            v = layoutInflater.inflate(R.layout.row, null);

        TextView header = v.findViewById(R.id.thread_row_address);
        TextView sub = v.findViewById(R.id.thread_row_content);

        Conversation c = data.get(position);

        header.setText(cs.getContactName(c.getAddress()));

//        ArrayList<Message> convo = data.get(tli.getAddr());

        sub.setText(c.getLastMessageSnippet());

        return v;
    }
}
