package parmar.nishikant.livechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import parmar.nishikant.livechatapp.Model.User;

public class MessageActivity extends AppCompatActivity {
    CircleImageView xdp;
    TextView xfullname, xpoints, xstatus;
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference2;
    Intent intent;
    Toolbar toolbar;
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
        xdp = findViewById(R.id.xprofileimage);
        xfullname = findViewById(R.id.xfullname);
        xpoints = findViewById(R.id.xpoints);
        xstatus = findViewById(R.id.xstatus);
        intent = getIntent();
        String xuseruid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user_info").child(xuseruid);
        reference2 = FirebaseDatabase.getInstance().getReference("user_info").child(firebaseUser.getUid()).child("status");
        reference2.setValue("online");
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
    }


}
