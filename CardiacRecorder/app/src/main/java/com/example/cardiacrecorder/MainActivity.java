package com.example.cardiacrecorder;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cardiacrecorder.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText signupe1, signupe2, signupe3;
    private TextView t1;
    private Button b1;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        signupe1 = (EditText) findViewById(R.id.s1);
        signupe2 = (EditText) findViewById(R.id.s2);
        signupe3 = (EditText) findViewById(R.id.s3);
        b1 = (Button) findViewById(R.id.s4);
        t1 = (TextView) findViewById(R.id.s5);
        progressbar = findViewById(R.id.s6);
        b1.setOnClickListener(this);
        t1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s4:
                userRegister();
                break;
            case R.id.s5:
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegister() {
        String name = signupe1.getText().toString().trim();
        String email = signupe2.getText().toString().trim();
        String password = signupe3.getText().toString().trim();
        if (email.isEmpty()) {
            signupe2.setError("Enter an email address");
            signupe2.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            signupe1.setError("Please enter your name");
            signupe1.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupe2.setError("Enter a valid email address");
            signupe2.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            signupe3.setError("Enter a password");
            signupe3.requestFocus();
            return;
        }
        if (password.length() < 6) {
            signupe3.setError("Minimum length of a password should be 6");
            signupe3.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("Name", name);
                    hashMap.put("Email", email);
                    hashMap.put("Password", password);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "User created...!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(getApplicationContext(), "User is already registred", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}