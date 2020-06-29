package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;


public class ChatActivity extends AppCompatActivity {

    String activeUser;
    EditText chatEditText;
    ArrayList<String> messages = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ParseLiveQueryClient parseLiveQueryClient = null;
    String messageContent;
    Handler handler = new Handler();
    public void getChat(){

                final ListView chatHistoryListView = (ListView)findViewById(R.id.chatHistoryListView);
                arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,messages);
                chatHistoryListView.setAdapter(arrayAdapter);

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Chats");
                query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
                query1.whereEqualTo("recepient", activeUser);

                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Chats");
                query2.whereEqualTo("sender", activeUser);
                query2.whereEqualTo("recepient", ParseUser.getCurrentUser().getUsername());

                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                queries.add(query1);
                queries.add(query2);

                ParseQuery<ParseObject> query =  ParseQuery.or(queries);
                query.orderByAscending("createdAt");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null){
                            if(objects.size()>0){
                                for(ParseObject message:objects){
                                    String sender = message.getString("sender");
                                    if(sender.equals(ParseUser.getCurrentUser().getUsername())){
                                        messages.add("Me: "+message.getString("message"));
                                    }
                                    else{
                                        messages.add(activeUser+": "+message.getString("message"));
                                    }
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                            else if(objects.size()==0){
                                Toast.makeText(ChatActivity.this, "You don't yet have any chats with "+ activeUser, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            e.printStackTrace();
                        }
                    }
                });

    }
    public void sendChat(View view){

        ParseObject chats = new ParseObject("Chats");
        messageContent = chatEditText.getText().toString();
        chats.put("sender", ParseUser.getCurrentUser().getUsername());
        chats.put("recepient", activeUser);
        chats.put("message", messageContent);

        chatEditText.setText("");
        chats.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    messages.add("Me: "+messageContent);
                    arrayAdapter.notifyDataSetChanged();
                    
                }
                else{
                    Toast.makeText(ChatActivity.this, "Server error! Message could not be sent", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        activeUser = intent.getStringExtra("userTapped");
        setTitle("Your Chats with "+ activeUser);
        chatEditText = (EditText)findViewById(R.id.chatEditText);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getChat();
            }
        },3000);


        /*try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("https://whatsapptrishit.back4app.io/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (parseLiveQueryClient != null) {

            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(query3);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                @Override
                public void onEvent(final ParseQuery<ParseObject> query, final ParseObject object) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            query3.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e == null){
                                        if(objects.size()>0){
                                            for(ParseObject message:objects){
                                                String sender = message.getString("sender");
                                                if(sender.equals(ParseUser.getCurrentUser().getUsername())){
                                                    messages.add("Me: "+message.getString("message"));
                                                }
                                                else{
                                                    messages.add(activeUser+": "+message.getString("message"));
                                                }
                                            }
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                        else if(objects.size()==0){
                                            Toast.makeText(ChatActivity.this, "You don't yet have any chats with "+ activeUser, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    });
                }
            });
        } */
    }
}