package com.darkenergy.achatapp.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darkenergy.achatapp.Adapter.MessageAdapter;
import com.darkenergy.achatapp.Model.Message;
import com.darkenergy.achatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReceiverImage, ReceiverName, ReceiverUid, SenderUid;
    CircleImageView imageProfile;
    TextView receiverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage, rImage;


    RecyclerView messageRecycler;
    MessageAdapter Adapter;
    ArrayList<Message> messageArrayList;

    EditText typeYourMessage;
    String senderRoom, receiverRoom;
    CardView sendButton;
    ImageView sendImage;
    FirebaseStorage storage;

    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        ReceiverName = getIntent().getStringExtra("username");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverUid = getIntent().getStringExtra("uid");
        receiverName = findViewById(R.id.receiver_Name);
        imageProfile = findViewById(R.id.image_profile);

        sendButton = findViewById(R.id.sendbtn);
        sendImage = findViewById(R.id.sendImageBtn);
        typeYourMessage = findViewById(R.id.typeYourMessage);

        messageArrayList = new ArrayList<>();

        messageRecycler = findViewById(R.id.messageRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.scrollToPosition(0);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
        messageRecycler.setLayoutManager(linearLayoutManager);
        Adapter = new MessageAdapter(ChatActivity.this, messageArrayList);
        messageRecycler.setAdapter(Adapter);


        Picasso.get().load(ReceiverImage).into(imageProfile);
        receiverName.setText("" + ReceiverName);

        SenderUid = firebaseAuth.getUid();
        senderRoom = SenderUid + ReceiverUid;
        receiverRoom = ReceiverUid + SenderUid;


        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message messages = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(messages);
                }

                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference reference = database.getReference().child("users").child(firebaseAuth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sImage = (String) snapshot.child("imageUri").getValue();
                rImage = ReceiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = typeYourMessage.getText().toString();

                    if (message.isEmpty()) {
                        Toast.makeText(ChatActivity.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    typeYourMessage.setText("");
                    Date date = new Date();
                    Message messages = new Message(message, SenderUid, date.getTime());

                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                }

            });
        }
    }