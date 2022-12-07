package com.darkenergy.achatapp.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.darkenergy.achatapp.R;
import com.darkenergy.achatapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

public class RegistrationActivity extends AppCompatActivity {

    TextInputEditText username, email, password, confirmPassword;
    Button signUp;
    TextView alreadyAccount;
    CircularImageView profileView;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        profileView = findViewById(R.id.profile);
        signUp = findViewById(R.id.SignUpButton);
        username = findViewById(R.id.regUsername);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        confirmPassword = findViewById(R.id.regConfirmPass);
        alreadyAccount = findViewById(R.id.alreadtAcountBtn);


        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();
                String Status="Hey, I am using AChatApp";
                if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Email) ||
                        TextUtils.isEmpty(Password) || TextUtils.isEmpty(ConfirmPassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
                else if (!Email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    email.setError("Enter Correct Email");
                    Toast.makeText(RegistrationActivity.this, "Enter Correct Email", Toast.LENGTH_SHORT).show();
                }
                else if (!Password.equals(ConfirmPassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "password doen not match", Toast.LENGTH_SHORT).show();
                }
                else if (Password.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "password should be 6 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
                                if (imageUri!=null)
                                {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                     imageURI=uri.toString();
                                                     Users users= new Users(auth.getUid(),Username,Email,imageURI,Status);
                                                     reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful())
                                                             {
                                                                 progressDialog.dismiss();
                                                                 startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                             }
                                                             else
                                                             {
                                                                 Toast.makeText(RegistrationActivity.this, "Error in Creating a new User", Toast.LENGTH_SHORT).show();
                                                             }
                                                         }
                                                     });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    String Status="Hey, I am using AChatApp";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/achatapp-81af7.appspot.com/o/user.png?alt=media&token=778b33c5-cc63-44f0-9781-d01910a6cbdf";
                                    Users users= new Users(auth.getUid(),Username,Email,imageURI,Status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                            }
                                            else
                                            {
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating a new User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

            }
        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }
    // set profile picture Uri for result
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null) {
                imageUri=result;
                profileView.setImageURI(imageUri);
            }
        }
    });
}
