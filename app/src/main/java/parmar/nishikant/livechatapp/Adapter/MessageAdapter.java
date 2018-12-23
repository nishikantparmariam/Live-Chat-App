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

import java.util.List;
import parmar.nishikant.livechatapp.MessageActivity;
import parmar.nishikant.livechatapp.Model.Message;
import parmar.nishikant.livechatapp.Model.User;
import parmar.nishikant.livechatapp.R;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> mMessages;
    public static  final int tmsgleft = 0;
    public static  final int tmsglright = 1;
    FirebaseUser firebaseUser;
    public MessageAdapter(Context mContext, List<Message> mMessages){
        this.mMessages = mMessages;
        this.mContext= mContext;
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
        holder.msg_textview.setText(message.getMessage());
    }
    @Override
    public int getItemCount() {
        return mMessages.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView msg_textview;
        public ViewHolder(View itemView){
            super(itemView);
            msg_textview  = itemView.findViewById(R.id.msg_textview);
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
