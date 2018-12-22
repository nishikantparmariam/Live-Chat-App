package parmar.nishikant.livechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    MaterialEditText email, password;
    Button buttonlogin;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView register_ = findViewById(R.id.sub);
        TextView fp_ = findViewById(R.id.fpb);
        register_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        fp_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPass.class));
                finish();
            }
        });
        if (firebaseUser!=null)
        {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        auth = FirebaseAuth.getInstance();
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        buttonlogin = findViewById(R.id.buttonlogin);
        ((View) buttonlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EL = email.getText().toString();
                String PW = password.getText().toString();
                if (TextUtils.isEmpty(EL)||TextUtils.isEmpty(PW)){
                    Toast.makeText(LoginActivity.this, "Please fill all details.", Toast.LENGTH_SHORT).show();

                }
                else
                    {
                        auth.signInWithEmailAndPassword(EL,PW)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task){
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Toast.makeText(LoginActivity.this, "Logged in!!", Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                            finish();
                                        }else
                                            {
                                                Toast.makeText(LoginActivity.this, "Invalid User Details", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                } );
                    }
            }
        });
    }
}
