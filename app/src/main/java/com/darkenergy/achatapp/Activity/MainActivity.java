
package com.darkenergy.achatapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.darkenergy.achatapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 1500);
    }
}

