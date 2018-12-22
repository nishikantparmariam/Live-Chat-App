package parmar.nishikant.livechatapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText username, email, password;
    Button buttonregister;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView login_ = findViewById(R.id.sib);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null)
        {
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        login_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        buttonregister = findViewById(R.id.buttonregister);
        auth = FirebaseAuth.getInstance();
        buttonregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                 String UN = username.getText().toString();
                 String EL = email.getText().toString();
                 String PW = password.getText().toString();
                 if (TextUtils.isEmpty(UN)||TextUtils.isEmpty(EL)||TextUtils.isEmpty(PW))
                 {
                     Toast.makeText(RegisterActivity.this, "Please provide all details.", Toast.LENGTH_SHORT).show();
                 } else if(PW.length()<6)
                 {
                     Toast.makeText(RegisterActivity.this, "Password must be atleast 6 characers long.", Toast.LENGTH_SHORT).show();
                 }
                 else if (UN.length()>=27) {
                     Toast.makeText(RegisterActivity.this, "Full name must not be more than 26 characters.", Toast.LENGTH_SHORT).show();
                 }
                 else
                     {
                         register(UN, EL, PW);
                     }
            }
        });
    }
    private void register(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser FirebaseUser = auth.getCurrentUser();
                            assert FirebaseUser != null;
                            String userid = FirebaseUser.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            /*
                            reference = FirebaseDatabase.getInstance().getReference("user_info").child(userid);
                             */
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("fullname",username);
                            hashMap.put("dpURL","default");
                            hashMap.put("points","0");
                            hashMap.put("status","online");
                            mDatabase.child("user_info").child(userid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                            {
                                Toast.makeText(RegisterActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}
