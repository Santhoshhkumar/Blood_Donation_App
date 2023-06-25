package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Collections;

public class Request extends AppCompatActivity {

    TextView name,hname,haddress,eligibility,hbg;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DocumentReference documentReference;
    boolean agr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        name=(TextView) findViewById(R.id.username);
        hbg=(TextView)findViewById(R.id.bloodgroup);
        hname=(TextView) findViewById(R.id.hospitalname);
        haddress=(TextView) findViewById(R.id.hospitaladdress);
        eligibility=(TextView) findViewById(R.id.eligibility);
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        String uid=firebaseUser.getUid();
        documentReference= firestore.collection("willingness").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if(task.getResult().exists())
             {
                 String e = task.getResult().getString("regeligibility");
                 String bg = task.getResult().getString("regbloodgroup");
                 if(e.equals("Yes"))
                 {
                     String n = task.getResult().getString("regusername");
                     DocumentReference documentReference1 = firestore.collection("request").document("28-11-2022");
                     documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           String hn = task.getResult().getString("hospitalName");
                           String had = task.getResult().getString("hosadd");
                           name.setText(n);
                           hbg.setText(bg);
                           hname.setText(hn);
                           haddress.setText(had);
                           eligibility.setText(e);
                         }
                     });
                 }
             }
            }
        });
    }
}