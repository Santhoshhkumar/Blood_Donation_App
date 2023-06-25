package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Successful extends AppCompatActivity {

    Button slogout;
    ProgressBar sprgbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);
        slogout=(Button)findViewById(R.id.logoutbtn);
        sprgbar=(ProgressBar)findViewById(R.id.successbar);

        slogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                sprgbar.setVisibility(view.VISIBLE);
                startActivity(new Intent(Successful.this,MainActivity.class));
                finish();
            }
        });
    }
}