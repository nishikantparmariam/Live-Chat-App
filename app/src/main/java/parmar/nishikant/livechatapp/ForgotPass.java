package parmar.nishikant.livechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class ForgotPass extends AppCompatActivity {
    Button buttonlogin;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    MaterialEditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null)
        {
            Intent intent = new Intent(ForgotPass.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView login_ = findViewById(R.id.sibfp);
        login_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPass.this,LoginActivity.class));
                finish();
            }
        });
        TextView sub_ = findViewById(R.id.subfp);
        sub_.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPass.this,RegisterActivity.class));
                finish();
            }
        });
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);

        Button buttonlogin = findViewById(R.id.buttonfp);
        ((View) buttonlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String femail = email.getText().toString();
                if (TextUtils.isEmpty(femail)){
                    Toast.makeText(ForgotPass.this, "Please provide an email.", Toast.LENGTH_SHORT).show();

                }
                else {

                    auth.sendPasswordResetEmail(femail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPass.this, "Check your email and then Sign In.", 15000).show();
                                        startActivity(new Intent(ForgotPass.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(ForgotPass.this, "Please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

        });
    }
}