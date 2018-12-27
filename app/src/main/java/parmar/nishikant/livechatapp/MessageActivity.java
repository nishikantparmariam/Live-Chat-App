package parmar.nishikant.livechatapp;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import parmar.nishikant.livechatapp.Adapter.MessageAdapter;
import parmar.nishikant.livechatapp.Adapter.UserAdapter;
import parmar.nishikant.livechatapp.Model.Message;
import parmar.nishikant.livechatapp.Model.Typstatus;
import parmar.nishikant.livechatapp.Model.User;

public class MessageActivity extends AppCompatActivity {
    CircleImageView xdp;
    TextView xfullname, xpoints, xstatus;
    EditText  msgsend;
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference2,reference3,reference4;
    Intent intent;
    ValueEventListener seenlistener;
    Toolbar toolbar;
    ImageButton btnsend;
    MessageAdapter messageAdapter;
    List<Message> mMessages;
    RecyclerView recyclerView;

    public static class MyGlobals {
        public static String chat_key_final;
        public static String typeofchat;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mMessages = new ArrayList<>();
        xdp = findViewById(R.id.xprofileimage);
        xfullname = findViewById(R.id.xfullname);
        xstatus = findViewById(R.id.xstatus);
        msgsend = findViewById(R.id.text_message);
        btnsend = findViewById(R.id.btn_send);
        intent = getIntent();
        final String xuseruid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //get the chat key of these two users ----
        reference2 = FirebaseDatabase.getInstance().getReference("chats");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String chat_key = "not_found";
                int no_of_members = 0;
                for(DataSnapshot sp:dataSnapshot.getChildren()){

                    List<String> members_ = new ArrayList<>();
                    for(DataSnapshot sp2:sp.child("members").getChildren()){
                        members_.add(sp2.getValue().toString());
                        no_of_members++;
                    }
                    if(no_of_members==2){

                        if((members_.get(0).equals(xuseruid)&&members_.get(1).equals(firebaseUser.getUid()))||(members_.get(1).equals(xuseruid)&&members_.get(0).equals(firebaseUser.getUid()))){
                            chat_key = sp.getKey();
                            //Toast.makeText(MessageActivity.this, "Found chat.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            chat_key = "not_found";
                        }
                    }

                }

                //make new chat if not found
                if(chat_key.equals("not_found")){
                    final String chat_key_final = reference2.push().getKey();
                    MyGlobals.chat_key_final=chat_key_final;
                    HashMap<String,Object> value_to_intialise_chat = new HashMap<>();
                    value_to_intialise_chat.put("chat_key",chat_key_final);
                    HashMap<String,Object> members = new HashMap<>();
                    members.put(xuseruid,xuseruid);
                    members.put(firebaseUser.getUid(),firebaseUser.getUid());
                    value_to_intialise_chat.put("members",members);
                    value_to_intialise_chat.put("typeofchat","oneToOne");
                    reference2.child(chat_key_final).setValue(value_to_intialise_chat);
                    Toast.makeText(MessageActivity.this, "New chat established.", Toast.LENGTH_SHORT).show();
                    listenForChanges(MyGlobals.chat_key_final,no_of_members);
                    seenMessage(MyGlobals.chat_key_final);
                }
                else {
                    MyGlobals.chat_key_final=chat_key;
                    listenForChanges(MyGlobals.chat_key_final,no_of_members);
                    seenMessage(MyGlobals.chat_key_final);
                }
                btnsend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = msgsend.getText().toString().trim();
                        if(message.equals("")){
                            Toast.makeText(MessageActivity.this, "Please enter some message", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sendMessage(MyGlobals.chat_key_final ,firebaseUser.getUid(),xuseruid,message);
                        }
                        msgsend.setText("");

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("user_info").child(xuseruid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User xuser = dataSnapshot.getValue(User.class);
                xfullname.setText(xuser.getFullname()+"\n"+xuser.getPoints()+" Points");
                if(xuser.getStatus().equals("online")){
                    xstatus.setVisibility(View.VISIBLE);
                    xstatus.setText(xuser.getStatus());
                }
                else {
                    xstatus.setText("");
                    xstatus.setVisibility(View.GONE);
                }

                if(xuser.getDpURL().equals("default")){
                    xdp.setImageResource(R.drawable.userdp);
                }
                else {
                    Glide.with(getApplicationContext()).load(xuser.getDpURL()).into(xdp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference reference22 = FirebaseDatabase.getInstance().getReference("typing_status");
        msgsend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().equals("")){
                    reference22.child(firebaseUser.getUid()).setValue(MyGlobals.chat_key_final);
                }
                else {
                    reference22.child(firebaseUser.getUid()).setValue(" ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final TextView typingstatus = findViewById(R.id.typingstatus);
        typingstatus.setVisibility(View.GONE);
        reference22.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                typingstatus.setVisibility(View.GONE);
                for(DataSnapshot sp:dataSnapshot.getChildren()){
                    String userkey = sp.getKey();
                    String typstatus = sp.getValue().toString();
                    if(userkey.equals(xuseruid)&&typstatus.equals(MyGlobals.chat_key_final)){
                        typingstatus.setVisibility(View.VISIBLE);
                        String[] xfn = xfullname.getText().toString().split("[\\r\\n]+");
                        typingstatus.setText("Typing..");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        reference4.removeEventListener(seenlistener);
        setStatus("offline");
        MyGlobals.chat_key_final=" ";
    }
    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }
    private void setStatus(String s){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user_info/"+firebaseUser.getUid()).child("status");
        reference.setValue(s);

    }
    private void seenMessage(String chat_key){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference4 = FirebaseDatabase.getInstance().getReference("chats/"+chat_key+"/messages");
        seenlistener = reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sp:dataSnapshot.getChildren()){
                    Message tempmessage = sp.getValue(Message.class);
                    if(!tempmessage.getSender().equals(firebaseUser.getUid())){
                        sp.getRef().child("isseen").child(firebaseUser.getUid()).setValue("YES");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String chat_key, String sender_id, String receiver_id, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender_id);
        hashMap.put("message",message);
        SimpleDateFormat gmtDateFormat = new SimpleDateFormat("K:mm a");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        SimpleDateFormat gmtDateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        gmtDateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time = gmtDateFormat.format(new Date());
        String date = gmtDateFormat2.format(new Date());
        hashMap.put("time",time);
        hashMap.put("date",date);
        HashMap<String,Object> h2 = new HashMap<>();
        //can't keep isseen null
        h2.put("testuser","YES");
        hashMap.put("isseen",h2);
        String pushkey = reference.child("messages").push().getKey();
        hashMap.put("key",pushkey);
        reference.child("chats/"+chat_key+"/messages").child(pushkey).setValue(hashMap);
    }
    private void listenForChanges(String chat_key,int no_of_members){
        ChildEventListener childEventListener = new ChildEventListener() {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String xuseruid = intent.getStringExtra("userid");
            @Override

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Message newMessage = dataSnapshot.getValue(Message.class);
                addSingleItem(newMessage);
                recyclerView.scrollToPosition(mMessages.size() - 1);
                //Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
                //addSingleItem(newUser);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Message edittedMessage = dataSnapshot.getValue(Message.class);
                updateMessage(key,edittedMessage);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                /*String key = dataSnapshot.getKey();
                detmsgfromkey(key);*/

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        messageAdapter = new MessageAdapter(MessageActivity.this,mMessages,no_of_members);
        recyclerView.setAdapter(messageAdapter);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("chats/"+chat_key+"/messages");
        reference2.addChildEventListener(childEventListener);
    }

    private void addSingleItem(Message newmessage){
        int size = messageAdapter.getItemCount();
        //Toast.makeText(MessageActivity.this,Integer.toString(size)+" go", Toast.LENGTH_SHORT).show();
        mMessages.add(size,newmessage);//add user at lasts
        messageAdapter.notifyItemInserted(size);
    }
    private void updateMessage(String key, Message editedMessage){
        int pwc = messageAdapter.getpositionfromkey(editedMessage.getKey());
        mMessages.set(pwc, editedMessage);
        messageAdapter.notifyItemChanged(pwc);
    }

}
