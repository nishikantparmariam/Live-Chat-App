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
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import parmar.nishikant.livechatapp.Adapter.MessageAdapter;
import parmar.nishikant.livechatapp.Adapter.UserAdapter;
import parmar.nishikant.livechatapp.Model.Message;
import parmar.nishikant.livechatapp.Model.User;

public class MessageActivity extends AppCompatActivity {
    CircleImageView xdp;
    TextView xfullname, xpoints, xstatus;
    EditText  msgsend;
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference2,reference3;
    Intent intent;
    Toolbar toolbar;
    ImageButton btnsend;
    MessageAdapter messageAdapter;
    List<Message> mMessages;
    RecyclerView recyclerView;

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
                startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        xpoints = findViewById(R.id.xpoints);
        xstatus = findViewById(R.id.xstatus);
        msgsend = findViewById(R.id.text_message);
        btnsend = findViewById(R.id.btn_send);


        intent = getIntent();
        final String xuseruid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgsend.getText().toString().trim();
                if(message.equals("")){
                    Toast.makeText(MessageActivity.this, "Please enter some message", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendMessage(firebaseUser.getUid(),xuseruid,message);
                }
                msgsend.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("user_info").child(xuseruid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User xuser = dataSnapshot.getValue(User.class);
                xfullname.setText(xuser.getFullname());
                xpoints.setText("("+xuser.getPoints()+" Points)");
                if(xuser.getStatus().equals("online")){
                    xstatus.setText(xuser.getStatus());
                }
                else {
                    xstatus.setText("");
                }

                if(xuser.getDpURL().equals("default")){
                    xdp.setImageResource(R.drawable.userdp);
                }
                else {
                    Glide.with(MessageActivity.this).load(xuser.getDpURL()).into(xdp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listenForChanges();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("offline");
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
    private void sendMessage(String sender_id, String receiver_id, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender_id);
        hashMap.put("receiver",receiver_id);
        hashMap.put("message",message);
        reference.child("messages").push().setValue(hashMap);
    }
    private void listenForChanges(){
        ChildEventListener childEventListener = new ChildEventListener() {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String xuseruid = intent.getStringExtra("userid");
            @Override

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Message newMessage = dataSnapshot.getValue(Message.class);
                if(newMessage.getSender().equals(firebaseUser.getUid())&&newMessage.getReceiver().equals(xuseruid)||newMessage.getReceiver().equals(firebaseUser.getUid())&&newMessage.getSender().equals(xuseruid)){
                    addSingleItem(newMessage);
                }
                recyclerView.scrollToPosition(mMessages.size() - 1);
                //Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
                //addSingleItem(newUser);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



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
        messageAdapter = new MessageAdapter(MessageActivity.this,mMessages);
        recyclerView.setAdapter(messageAdapter);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("messages");
        reference2.addChildEventListener(childEventListener);
    }

    private void addSingleItem(Message newmessage){
        int size = messageAdapter.getItemCount();
        //Toast.makeText(MessageActivity.this,Integer.toString(size)+" go", Toast.LENGTH_SHORT).show();
        mMessages.add(size,newmessage);//add user at lasts
        messageAdapter.notifyItemInserted(size);
    }

}
