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
import com.bumptech.glide.Glide;

import java.util.List;

import parmar.nishikant.livechatapp.MessageActivity;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    public UserAdapter(Context mContext, List<User> mUsers){
        this.mUsers = mUsers;
        this.mContext= mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.oneuser ,parent,false);
        return new UserAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        final User user = mUsers.get(position);
        holder.oneuserfn.setText(user.getFullname());
        holder.oneuserpoints.setText(user.getPoints()+" Points");
        if(user.getStatus().equals("online")){
            holder.oneuserstatus.setText("online");
        }
        else {
            holder.oneuserstatus.setText(" ");
        }
        if (user.getDpURL().equals("default")){
            holder.oneuserpp.setImageResource(R.drawable.userdp);
        } else {
            Glide.with(mContext).load(user.getDpURL()).into(holder.oneuserpp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });
    }
    public int getUserAndPositionFromKey(String userKey){
        int i=0;
        int positiontoreturn = 0; //random position

        for (User tempuser:mUsers
             ) {
            if(tempuser.getId().equals(userKey)){
                //usertoreturn = tempuser;
                positiontoreturn = i;
            }
            i++;
        }

        return positiontoreturn;
    };
    @Override
    public int getItemCount() {
        return mUsers.size();
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
