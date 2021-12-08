package com.gurjeet.codeitcapstone1project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gurjeet.codeitcapstone1project.adapter.UserChatAdapter;
import com.gurjeet.codeitcapstone1project.model.UserChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

//https://codingshiksha.com/how-to-integrate-emojis-keyboard-in-your-android-app



public class Message extends AppCompatActivity {
    View v;//RelativeLayout activity_message;//View V
   EmojiconEditText emojiconEditText;

  //  EditText emojiconEditText;
    private String rec_name;
    private String sen_name;
    TextView contactName;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    RecyclerView listOfMessage;
    private LinearLayoutManager layoutManager;
    private List<UserChatModel> messagesFromDB = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mAuth=FirebaseAuth.getInstance();
        toolbar();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        sen_name = mAuth.getCurrentUser().getUid();
        rec_name =intent.getExtras().getString("id");
        String title =intent.getExtras().getString("title");
       // ((AppCompatActivity)Message.this).getSupportActionBar().setTitle(title);//need to work for this

        v = findViewById(R.id.message_activity);//(RelativeLayout)findViewById(R.id.message_activity);
        contactName = findViewById(R.id.contactName);
        contactName.setText(title);
        listOfMessage = (RecyclerView) findViewById(R.id.list_of_message);
        layoutManager = new LinearLayoutManager(this);
        listOfMessage.setLayoutManager(layoutManager);
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
       // emojiconEditText = findViewById(R.id.emojicon_edit_text);
       emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),v,emojiconEditText,emojiButton);
       // EmojIconActions emojIcon = new EmojIconActions(getApplicationContext(),v,emojiconEditText,emojiButton);
        emojIconActions.ShowEmojIcon();//
        //emojIconActions.ShowEmojicon();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });


    }

    private void toolbar() {
    }

    private void sendmessage() {
        String tex = emojiconEditText.getText().toString();


        if (TextUtils.isEmpty(tex))
        {
            Toast.makeText(this, "first write your message", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Message/" + sen_name + "/" + rec_name;
            String messageReceiverRef = "Message/" + rec_name + "/" + sen_name;
            //generate message id for sender and receiver
            DatabaseReference userMessageKeyRef = mDatabase.child("Chat")
                    .child(sen_name).child(rec_name).push();

            String messagePushID = userMessageKeyRef.getKey();
            Map messageTextBody = new HashMap();
            messageTextBody.put("message",tex);
            messageTextBody.put("type","text");
            messageTextBody.put("from",sen_name);
            messageTextBody.put("to",rec_name);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID,messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID,messageTextBody);

            mDatabase.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {

                        //Toast.makeText(Message.this, "message send successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Message.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    emojiconEditText.setText("");
                }
            });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("Message").child(sen_name).child(rec_name)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        UserChatModel messages= snapshot.getValue(UserChatModel.class);
                        messagesFromDB.add(messages);
                        Log.d("Receiver gk", "rec: "+messages.getTo());
                        Log.d("Sender gk", "sender: "+messages.getFrom());
                        UserChatAdapter adapter = new UserChatAdapter(messagesFromDB, getApplicationContext());
                        listOfMessage.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //  listOfMessage.smoothScrollToPosition(listOfMessage.getAdapter().getItemCount());

                    }


                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void showallmessages() {
        // loggedInUserName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userEmail=mAuth.getCurrentUser().getEmail();
        // Log.d("Main", "user id: " + loggedInUserName);
        //userSpecificAdapter = new userSpecificAdapter(this, ChatMessage.class, R.layout.item_in_message,
        //      FirebaseDatabase.getInstance().getReference());
        //  userSpecificAdapter adapter=new userSpecificAdapter(messagesFromDB,userEmail,Message.this);
    }
}