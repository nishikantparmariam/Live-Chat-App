package parmar.nishikant.livechatapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import parmar.nishikant.livechatapp.Adapter.UserAdapterCreateNewGroup;
import parmar.nishikant.livechatapp.Model.User;

public class CreateNewGroup extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    List<User> mUsers;
    TextView added_users;
    static List<String> addedUserIds;
    UserAdapterCreateNewGroup userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUsers = new ArrayList<>();
        recyclerView.setVisibility(View.VISIBLE);
        listenForChanges();
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
        userAdapter = new UserAdapterCreateNewGroup(getApplicationContext(),mUsers);
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
    public static List<String> addUserId(String userid){
        addedUserIds.add(userid);
        return addedUserIds;
    }
}
