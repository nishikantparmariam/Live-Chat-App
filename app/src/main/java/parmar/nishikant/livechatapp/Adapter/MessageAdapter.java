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
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import parmar.nishikant.livechatapp.MessageActivity;
import parmar.nishikant.livechatapp.Model.Message;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> mMessages;
    private int no_of_members;
    public static  final int tmsgleft = 0;
    public static  final int tmsglright = 1;
    FirebaseUser firebaseUser;
    public MessageAdapter(Context mContext, List<Message> mMessages,int no_of_members){
        this.mMessages = mMessages;
        this.mContext= mContext;
        this.no_of_members = no_of_members;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        if(viewType == tmsgleft){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chatmsgleft,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chatmsgright,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        final Message message = mMessages.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.msg_textview.setText(message.getMessage());
        holder.time_.setText(message.getTime());
        if(true){
            if((message.getIsseen().size()==(no_of_members))&&message.getSender().equals(firebaseUser.getUid())){ //not (no_of_members-1) because it was never null
                holder.seen_.setVisibility(View.VISIBLE);
                holder.seen_.setBackgroundResource(R.mipmap.dbtc);
            }
            else if(message.getIsseen().size()==1&&message.getSender().equals(firebaseUser.getUid())) {
                holder.seen_.setVisibility(View.VISIBLE);
                holder.seen_.setBackgroundResource(R.mipmap.dbtcnd);
            }
            else {

                holder.seen_.setVisibility(View.GONE);
            }
        }else {
            holder.seen_.setVisibility(View.GONE);
        }
    }
    public int getpositionfromkey(String key){
        int r = 0;
        int res= 0;
        for (Message tempmessage:mMessages){
            if(tempmessage.getKey().equals(key)){
                res = r;
                break;
            }
            r++;
        }
        return res;
    }
    @Override
    public int getItemCount() {
        return mMessages.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView msg_textview;
        public TextView seen_;
        public TextView time_;
        public ViewHolder(View itemView){
            super(itemView);
            msg_textview  = itemView.findViewById(R.id.msg_textview);
            seen_  = itemView.findViewById(R.id.seen_);
            time_  = itemView.findViewById(R.id.time_);
        }
    }
    @Override
    public  int getItemViewType(int position){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();
        if(mMessages.get(position).getSender().equals(firebaseUser.getUid())){
            return tmsglright;
        }else {
            return tmsgleft;
        }
    };
}
