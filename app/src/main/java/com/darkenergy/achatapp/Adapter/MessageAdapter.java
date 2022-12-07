package com.darkenergy.achatapp.Adapter;

import static com.darkenergy.achatapp.Activity.ChatActivity.rImage;
import static com.darkenergy.achatapp.Activity.ChatActivity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darkenergy.achatapp.Model.Message;
import com.darkenergy.achatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    Context context;
    ArrayList<Message> messageArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View viewS = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(viewS);
        } else if (viewType == ITEM_RECEIVE) {
            View viewR = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new receiverViewHolder(viewR);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message messages=messageArrayList.get(position);
        if (holder.getClass()==senderViewHolder.class)
        {
            senderViewHolder viewHolder=(senderViewHolder)holder;
            viewHolder.textView.setText(messages.getMeassage());

            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        }
        else
        {
            receiverViewHolder viewHolder=(receiverViewHolder) holder;
            viewHolder.textView.setText(messages.getMeassage());

            Picasso.get().load(rImage).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message messages=messageArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND=1;
        }
        else
        {
            return ITEM_RECEIVE=2;
        }

    }

    class  senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView;
        ImageView imageView;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.SImage);
            circleImageView=itemView.findViewById(R.id.user_Image);
            textView=itemView.findViewById(R.id.txtMesssage);

        }
    }
    class  receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textView;
        ImageView imageView;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.RImage);
            circleImageView=itemView.findViewById(R.id.user_Image);
            textView=itemView.findViewById(R.id.txtMesssage);


        }
    }
}
