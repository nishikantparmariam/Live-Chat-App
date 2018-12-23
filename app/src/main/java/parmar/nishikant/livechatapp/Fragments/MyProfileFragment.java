package parmar.nishikant.livechatapp.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;

import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;

import static android.app.Activity.RESULT_OK;

public class MyProfileFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference reference,reference2;
    TextView ppoints, pfullname;
    ImageView pimgdp;
    StorageReference storageReference;
    private static final int DPR=1;
    private Uri uri;
    private StorageTask storageTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);
        pimgdp = view.findViewById(R.id.pimgdp);
        pfullname = view.findViewById(R.id.pfullname);
        ppoints = view.findViewById(R.id.ppoints);
        storageReference = FirebaseStorage.getInstance().getReference("img/profilepic");
        FirebaseUser puser= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user_info/"+puser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                pfullname.setText(user.getFullname());
                ppoints.setText(user.getPoints()+" Points");
                if(user.getDpURL().equals("default")){
                    pimgdp.setImageResource(R.drawable.userdp);
                }
                else {
                    try{
                        Glide.with(getActivity()).load(user.getDpURL()).into(pimgdp);
                    }finally {
                        int n=5;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pimgdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgshow();
            }
        });
        return view;
    }
    private void imgshow(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, DPR);
    }
    private String fileExget(Uri uri){
        ContentResolver contentResolver= getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void upimg(String user_id){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading...");
        pd.show();
        if(uri!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+user_id+"."+fileExget(uri));
            storageTask = fileReference.putFile(uri);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if(!task.isSuccessful()){
                       throw task.getException();
                   }
                   return fileReference.getDownloadUrl();
                };
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloaduri = task.getResult();
                        String mUri = downloaduri.toString();
                        reference2 = FirebaseDatabase.getInstance().getReference("user_info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dpURL");
                        reference2.setValue(mUri);
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Could not upload.", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==DPR&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uri = data.getData();
            if(storageTask!=null&&storageTask.isInProgress()){
                Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();
            } else {

                upimg(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        }
    }

}
