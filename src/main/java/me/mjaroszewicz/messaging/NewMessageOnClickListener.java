package me.mjaroszewicz.messaging;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by admin on 1/11/18.
 */

public class NewMessageOnClickListener implements View.OnClickListener {

    private MessagingService messagingService;

    private MainActivity mainActivity;

    NewMessageOnClickListener(MessagingService messagingService, MainActivity mainActivity){
        super();
        this.messagingService = messagingService;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {

        LayoutInflater li = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = li.inflate(R.layout.new_message_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

        Button pickContactBtn = v.findViewById(R.id.newmsg_select_contact);

        EditText numberEditText = v.findViewById(R.id.newmsg_addr);

        pickContactBtn.setOnClickListener(v1 -> mainActivity.pickContact(numberEditText));

        View finalV = v;
        alertDialogBuilder.
                setTitle("New message")
                .setCancelable(true)
                .setView(v)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .setPositiveButton("Send", (dialog, which) -> {
                    if(which == DialogInterface.BUTTON_POSITIVE){

                        EditText addrField = finalV.findViewById(R.id.newmsg_addr);
                        String addr = addrField.getText().toString();

                        EditText msgField = finalV.findViewById(R.id.newmsg_edittext);
                        String msg = msgField.getText().toString();

                        if(msg.isEmpty() || addr.isEmpty()){
                            Toast.makeText(finalV.getContext(), "Fields can't be empty.", Toast.LENGTH_LONG);
                            return;
                        }

                        messagingService.sendMessage(addr, msg);

                    }
                })
                .show();

    }

}
