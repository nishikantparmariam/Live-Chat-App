package parmar.nishikant.livechatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import parmar.nishikant.livechatapp.CreateNewGroup;
import parmar.nishikant.livechatapp.MessageActivity;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;

public class UserAdapterCreateNewGroup extends RecyclerView.Adapter<UserAdapterCreateNewGroup.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private List<String> addedUserIds = new ArrayList<>();
    public UserAdapterCreateNewGroup(Context mContext, List<User> mUsers){
        this.mUsers = mUsers;
        this.mContext= mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.oneuser ,parent,false);
        return new UserAdapterCreateNewGroup.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        final User user = mUsers.get(position);
        holder.oneuserfn.setText(user.getFullname());
        holder.oneuserpoints.setText(user.getPoints()+" Points");

        holder.oneuserstatus.setText("Add");
        holder.oneuserstatus.setTextSize(15);
        holder.oneuserstatus.setPadding(30,10,30,10);
        if (user.getDpURL().equals("default")){
            holder.oneuserpp.setImageResource(R.drawable.userdp);
        } else {
            Glide.with(mContext).load(user.getDpURL()).into(holder.oneuserpp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);*/
                holder.oneuserstatus.setText("Added");
                addedUserIds.add(user.getId());
                Toast.makeText(mContext, addedUserIds.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public List<String> getaddUserIds(){
        return addedUserIds;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView oneuserfn, oneuserpoints,oneuserstatus;
        public ImageView oneuserpp;
        public ViewHolder(View itemView){
            super(itemView);
            oneuserpp  = itemView.findViewById(R.id.oneuserpp);
            oneuserfn = itemView.findViewById(R.id.oneuserfn);
            oneuserpoints = itemView.findViewById(R.id.oneuserpoints);
            oneuserstatus = itemView.findViewById(R.id.oneuserstatus);
        }
    }
}
