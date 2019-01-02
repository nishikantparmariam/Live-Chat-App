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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import parmar.nishikant.livechatapp.MessageActivity;
import parmar.nishikant.livechatapp.Model.Chat;
import parmar.nishikant.livechatapp.Model.Message;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private List<Chat> mChats;
    public ChatAdapter(Context mContext, List<Chat> mChats){
        this.mChats = mChats;
        this.mContext= mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.onechat ,parent,false);
        return new ChatAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,int position){
        final Chat chat = mChats.get(position);
        holder.onechatn.setText(chat.getChatName());
        holder.onechatlastmessage.setText(chat.getLastmessage());
        holder.onechattime.setVisibility(View.VISIBLE);
        holder.onechattime.setText(chat.getLmtime().toLowerCase());
        //Toast.makeText(mContext, chat.getChatName(), Toast.LENGTH_SHORT).show();
        if(chat.getNoofunseenmessages()==0){
            holder.onechatunseen.setVisibility(View.GONE);
        }
        else{
            holder.onechatunseen.setVisibility(View.VISIBLE);
            holder.onechatunseen.setText(Integer.toString(chat.getNoofunseenmessages()));
        }

        //Toast.makeText(mContext,chat.getLastmessage(),Toast.LENGTH_SHORT).show();
        if (chat.getDpURL()==null){
            holder.onechatpp.setImageResource(R.drawable.userdp);
        } else if(!chat.getDpURL().equals("default")) {
            Glide.with(mContext).load(chat.getDpURL()).into(holder.onechatpp);
        } else {
            holder.onechatpp.setImageResource(R.drawable.userdp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",chat.getXuserid());
                mContext.startActivity(intent);
            }
        });
    }
    public int getChatFromKey(String key){
        int position_tr=0;
        int cc=0;
        for(Chat tempChats:mChats){
            if(tempChats.getChat_key().equals(key)){
                position_tr=cc;
                break;
            }
            cc++;
        }
        return position_tr;
    };
    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView onechatn, onechatlastmessage,onechatunseen,onechattime;
        public ImageView onechatpp;
        TextView useronline;
        public ViewHolder(View itemView){
            super(itemView);
            onechatlastmessage  = itemView.findViewById(R.id.onechatlastmessage);
            onechatn = itemView.findViewById(R.id.onechatn);
            onechatunseen = itemView.findViewById(R.id.onechatunseen);
            onechatpp = itemView.findViewById(R.id.onechatpp);
            useronline = itemView.findViewById(R.id.useronline);
            onechattime = itemView.findViewById(R.id.onechattime);
        }
    }
}
