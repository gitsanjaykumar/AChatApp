package com.darkenergy.achatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.darkenergy.achatapp.R;
import com.darkenergy.achatapp.Adapter.UserAdapter;
import com.darkenergy.achatapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView MainUserRecycler;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logoutBtn=findViewById(R.id.imgLogout);
        usersArrayList=new ArrayList<>();

        database=FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        DatabaseReference reference=database.getReference().child("user");

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                Toast.makeText(HomeActivity.this,"Log Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MainUserRecycler=findViewById(R.id.mainUserRecycler);
        MainUserRecycler.setLayoutManager(new LinearLayoutManager(this));
        MainUserRecycler.setHasFixedSize(true);
        adapter=new UserAdapter(HomeActivity.this, usersArrayList);
        MainUserRecycler.setAdapter(adapter);


        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this,RegistrationActivity.class));
        }
    }
}