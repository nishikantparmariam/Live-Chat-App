package parmar.nishikant.livechatapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import parmar.nishikant.livechatapp.Adapter.UserAdapter;
import parmar.nishikant.livechatapp.Adapter.searchUserAdapter;
import parmar.nishikant.livechatapp.MainActivity;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;

import static android.support.v4.content.ContextCompat.getSystemService;

public class UserFragment extends Fragment {
    private RecyclerView recyclerView,recyclerViewSearch2;
    private UserAdapter userAdapter;
    private searchUserAdapter searchUserAdapter;
    private List<User> mUsers;
    private List<User> onlineUsers;
    private List<User> offlineUsers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch2 = view.findViewById(R.id.recycler_viewSearch);
        recyclerViewSearch2.setHasFixedSize(true);
        recyclerViewSearch2.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        offlineUsers = new ArrayList<>();
        onlineUsers = new ArrayList<>();
        final List<User> res_users =  new ArrayList<>();
        final EditText search_text = view.findViewById(R.id.search_text);
        final TextView close_btn = view.findViewById(R.id.close_btn);
        final TextView srchre  = view.findViewById(R.id.srchre);
        srchre.setText("");
        srchre.setVisibility(View.GONE);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_btn.setBackgroundResource(0);
                search_text.setText("");
                res_users.clear();
                srchre.setText("");
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewSearch2.setVisibility(View.GONE);
                srchre.setVisibility(View.GONE);
            }
        });

        //        search_text.findFocus();
        recyclerView.setVisibility(View.VISIBLE);
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_ = s.toString().trim().toLowerCase();
                if(!search_.equals("")){
                    recyclerView.setVisibility(View.GONE);
                    close_btn.setBackgroundResource(R.mipmap.close_2);
                    int result_count=0;
                    res_users.clear();
                    if(search_.startsWith("@")&&search_.contains("@on")){
                        for(User tempuser:mUsers){

                            if(tempuser.getStatus().equals("online")) {
                                result_count++;
                                res_users.add(tempuser);
                            }
                        }
                    }
                    else {
                        for(User tempuser:mUsers){

                            if(tempuser.getFullname().toLowerCase().contains(search_)||tempuser.getPoints().toLowerCase().contains(search_)||tempuser.getFullname().toLowerCase().indexOf(search_)!=(-1)||tempuser.getPoints().toLowerCase().indexOf(search_)!=(-1)) {
                                result_count++;
                                res_users.add(tempuser);
                            }
                        }
                    }

                    srchre.setVisibility(View.VISIBLE);
                    if(result_count==0){
                        srchre.setText("No results found.");
                    } else if (result_count==1){
                        srchre.setText("1 Result found");
                    }else {
                        srchre.setText(Integer.toString(result_count)+" Results found");
                    }
                    searchUserAdapter = new searchUserAdapter(getContext(),res_users);
                    recyclerViewSearch2.setAdapter(searchUserAdapter);
                    recyclerViewSearch2.setVisibility(View.VISIBLE);
                }
                else {
                    close_btn.setBackgroundResource(0);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewSearch2.setVisibility(View.GONE);
                    srchre.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //readUsers();
        //Below method add/delete/update users
        listenForChanges();

        if(userAdapter.getItemCount()==0){
            TextView text_if_no_user = view.findViewById(R.id.text_if_no_user);
            text_if_no_user.setText("");//set_no_because search problem.
        }
        return view;
    }
    private void listenForChanges(){
        ChildEventListener childEventListener = new ChildEventListener() {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            @Override

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                User newUser = dataSnapshot.getValue(User.class);
                assert firebaseUser != null;
                if(!firebaseUser.getUid().equals(key)){
                    addSingleItem(newUser);
                }

                //Toast.makeText(getContext(), key, Toast.LENGTH_SHORT).show();
                //addSingleItem(newUser);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
                User userwherechange = dataSnapshot.getValue(User.class);
                if(!key.equals(firebaseUser.getUid())){
                    updateSingleItem(key,userwherechange);
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

        };
        userAdapter = new UserAdapter(getContext(),mUsers);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("user_info");
        reference2.addChildEventListener(childEventListener);
    }
    private void addSingleItem(User newuser){
        int size = userAdapter.getItemCount();
        //Toast.makeText(getContext(),Integer.toString(size), Toast.LENGTH_SHORT).show();
        mUsers.add(size,newuser);//add user at lasts
        userAdapter.notifyItemInserted(size);
    }
    private void updateSingleItem(String userKey,User changeduser) {
        int positionFromKey = userAdapter.getUserAndPositionFromKey(userKey);
        mUsers.set(positionFromKey, changeduser);
        userAdapter.notifyItemChanged(positionFromKey);
    }
    /*
    private void readUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_info");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                onlineUsers.clear();
                offlineUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                        /*if(user.getStatus().equals("online")) {
                            onlineUsers.add(user);
                        }
                        else {
                            offlineUsers.add(user);
                        }

                    }
                }
                /*int i=0;
                while(i<onlineUsers.size()){
                    mUsers.add(onlineUsers.get(i));
                    i++;
                }
                int j=0;
                while(j<offlineUsers.size()){
                    mUsers.add(offlineUsers.get(j));
                    j++;
                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

}
