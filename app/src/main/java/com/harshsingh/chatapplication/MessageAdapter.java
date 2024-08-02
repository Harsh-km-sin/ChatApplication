package com.harshsingh.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Message> messageList;
    int ITEM_SENT = 2;
    int ITEM_RECEIVE = 1;

    public MessageAdapter(Context context, ArrayList<Message> messageList){
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.receive, parent, false);
            return new ReceiveViewHolder(view);
        }else {

            View view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false);
            return new SentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message currentMessage = messageList.get(position);
        if(holder instanceof SentViewHolder){
            SentViewHolder viewHolder = (SentViewHolder) holder;


            viewHolder.sentMessage.setText(currentMessage.message);


        }else if (holder instanceof ReceiveViewHolder){
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.receiveMessage.setText(currentMessage.message);


        }
    }
    @Override
    public int getItemViewType(int position) {
        Message currentMessage = messageList.get(position);
        if(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(currentMessage.senderId)){
            return ITEM_SENT;
        }else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    public static class SentViewHolder extends RecyclerView.ViewHolder{
        TextView sentMessage = itemView.findViewById(R.id.txt_sent_message);
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public static class ReceiveViewHolder extends RecyclerView.ViewHolder{
        TextView receiveMessage = itemView.findViewById(R.id.txt_receive_message);

        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
