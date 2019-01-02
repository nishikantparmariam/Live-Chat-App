package parmar.nishikant.livechatapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Map;

import parmar.nishikant.livechatapp.Adapter.ChatAdapter;
import parmar.nishikant.livechatapp.Adapter.UserAdapter;
import parmar.nishikant.livechatapp.Adapter.searchUserAdapter;
import parmar.nishikant.livechatapp.MessageActivity;
import parmar.nishikant.livechatapp.Model.Chat;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> mChats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //all code below this
        mChats = new ArrayList<>();
        listenforchanges();
        return view;
    }
    public void listenforchanges(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
        final String louid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String chat_key = dataSnapshot.getKey();
                List<String> members = new ArrayList<>();
                for(DataSnapshot member:dataSnapshot.child("members").getChildren()){
                    members.add(member.getValue().toString());
                }
                if(members.contains(louid)){
                    String last_message1 = dataSnapshot.child("last_message").child("message").getValue().toString();
                    String lmtime = " ";
                    if(dataSnapshot.child("last_message").child("time").exists()){
                        lmtime = dataSnapshot.child("last_message").child("time").getValue().toString();
                    }
                    if(dataSnapshot.child("last_message").child("sender").exists()){
                        if(dataSnapshot.child("last_message").child("sender").getValue().toString().equals(louid)){
                            last_message1="You : "+last_message1;
                        }
                    }
                    final String last_message= last_message1;
                    String type_of_chat = dataSnapshot.child("typeofchat").getValue().toString();
                    int no_of_unseen_messages = 0;
                    for (DataSnapshot message : dataSnapshot.child("messages").getChildren()) {
                        if (!message.child("sender").getValue().toString().equals(louid)) {
                            int unseen = 0;
                            List<String> isseen_list = new ArrayList<>();
                            long no_of_loos = message.child("isseen").getChildrenCount();
                            int no_of_loops = (int) no_of_loos;
                            int loop_count=0;
                            for (DataSnapshot isseen:message.child("isseen").getChildren()) {
                                isseen_list.add(isseen.getKey().toString());

                                loop_count++;
                                if(no_of_loops==loop_count){
                                    if(!isseen_list.contains(louid)){

                                        no_of_unseen_messages++;
                                    }
                                }
                            }
                        }

                    }

                    List<String> members_list = members;
                    if (type_of_chat.equals("oneToOne")) {
                        members_list.remove(louid);
                        final String xuserid = members_list.get(0);
                        final Chat newChat = new Chat();
                        newChat.setChatName(dataSnapshot.child("chatName").child(louid).getValue().toString());
                        newChat.setDpURL(dataSnapshot.child("dpURL").child(louid).getValue().toString());;
                        newChat.setXuserid(xuserid);
                        newChat.setLmtime(lmtime);
                        newChat.setLastmessage(last_message);
                        newChat.setNoofunseenmessages(no_of_unseen_messages);
                        newChat.setChat_key(chat_key);
                        addSingleChat(newChat);

                    } else {
                        //group chat
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String chat_key = dataSnapshot.getKey();
                List<String> members = new ArrayList<>();
                for(DataSnapshot member:dataSnapshot.child("members").getChildren()){
                    members.add(member.getValue().toString());
                }
                if(members.contains(louid)){
                    String last_message1 = dataSnapshot.child("last_message").child("message").getValue().toString();
                    String lmtime = " ";
                    if(dataSnapshot.child("last_message").child("time").exists()){
                        lmtime = dataSnapshot.child("last_message").child("time").getValue().toString();
                    }
                    if(dataSnapshot.child("last_message").child("sender").exists()){
                        if(dataSnapshot.child("last_message").child("sender").getValue().toString().equals(louid)){
                            last_message1="You : "+last_message1;
                        }
                    }
                    final String last_message= last_message1;
                    String type_of_chat = dataSnapshot.child("typeofchat").getValue().toString();
                    int no_of_unseen_messages = 0;
                    for (DataSnapshot message : dataSnapshot.child("messages").getChildren()) {
                        if (!message.child("sender").getValue().toString().equals(louid)) {
                            int unseen = 0;
                            List<String> isseen_list = new ArrayList<>();
                            long no_of_loos = message.child("isseen").getChildrenCount();
                            int no_of_loops = (int) no_of_loos;
                            int loop_count=0;
                            for (DataSnapshot isseen:message.child("isseen").getChildren()) {
                                isseen_list.add(isseen.getKey().toString());

                                loop_count++;
                                if(no_of_loops==loop_count){
                                    if(!isseen_list.contains(louid)){

                                        no_of_unseen_messages++;
                                    }
                                }
                            }
                        }

                    }

                    List<String> members_list = members;
                    if (type_of_chat.equals("oneToOne")) {
                        members_list.remove(louid);
                        final String xuserid = members_list.get(0);
                        final Chat newChat = new Chat();
                        newChat.setChatName(dataSnapshot.child("chatName").child(louid).getValue().toString());
                        newChat.setDpURL(dataSnapshot.child("dpURL").child(louid).getValue().toString());;
                        newChat.setXuserid(xuserid);
                        newChat.setLmtime(lmtime);
                        newChat.setLastmessage(last_message);
                        newChat.setNoofunseenmessages(no_of_unseen_messages);
                        newChat.setChat_key(chat_key);
                        updateSingleChat(newChat);

                    } else {
                        //group chat
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        chatAdapter = new ChatAdapter(getContext(),mChats);
        recyclerView.setAdapter(chatAdapter);
    }
    public  void addSingleChat(Chat newchat){
        //bug fixed by add
        boolean add = true;
        for(Chat tempChat:mChats){
            if(tempChat.getChat_key().equals(newchat.getChat_key())){
                add = false;
            }
        }
        if(add){
            mChats.add(mChats.size(), newchat);
            chatAdapter.notifyItemInserted(mChats.size());
        }

    }
    public void updateSingleChat(Chat chat){
        int co =0;
        int res = getres(chat);
        mChats.set(res, chat);
        chatAdapter.notifyItemChanged(res);
        mChats.remove(res);
        mChats.add(0,chat);
        chatAdapter.notifyItemMoved(res,0);
    }
    public int getres(Chat chat){
        int co=0;
        int res = 0;
        for(Chat tempChat:mChats){
            if(tempChat.getChat_key().equals(chat.getChat_key())){
                res = co;

                break;
            }
            co++;
        }
        return res;
    }
}
