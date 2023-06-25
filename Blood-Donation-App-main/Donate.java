package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Donate extends AppCompatActivity {

    Button donate;
    CheckBox willing;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    DocumentReference documentReference1,documentReference2,documentReference3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        donate=(Button)findViewById(R.id.donatebtn);
        willing=(CheckBox) findViewById(R.id.checkBox);
        firebaseAuth=FirebaseAuth.getInstance();
        firestore= FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        String uid=firebaseUser.getUid();
        documentReference1=firestore.collection("willingness").document(uid);
        documentReference2=firestore.collection("student").document(uid);
        documentReference3=firestore.collection("donatedlist").document(uid);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(willing.isChecked())
                {
                    final AlertDialog.Builder will = new AlertDialog.Builder(view.getContext());
                    will.setTitle("Your Confirmation");
                    will.setMessage("Are you really want to donate your blood ?");
                    will.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.getResult().exists()) {
                                        String username = task.getResult().getString("regusername");
                                        String emailid = task.getResult().getString("regemailid");
                                        String mobileno = task.getResult().getString("regmobileno");
                                        String registerno = task.getResult().getString("regno");
                                        String dateofbirth = task.getResult().getString("regdob");
                                        String department = task.getResult().getString("regdepartment");
                                        String bloodgroup = task.getResult().getString("regbloodgroup");
                                        String gender =task.getResult().getString("reggender");
                                        String eligibility = task.getResult().getString("regeligibility");
                                        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                Map<String,Object> hmp=new HashMap<>();
                                                {
                                                    hmp.put("regusername",username);
                                                    hmp.put("regemailid",emailid);
                                                    hmp.put("regmobileno",mobileno);
                                                    hmp.put("regno",registerno);
                                                    hmp.put("regdob",dateofbirth);
                                                    hmp.put("regdepartment",department);
                                                    hmp.put("regbloodgroup",bloodgroup);
                                                    hmp.put("reggender",gender);
                                                    hmp.put("regeligibility",eligibility);
                                                }
                                                documentReference1.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(),"Thanks for your willingness",Toast.LENGTH_LONG).show();
                                                        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                             if(task.isSuccessful())
                                                             {
                                                                 Map<String,Object>hmp=new HashMap<>();
                                                                 {
                                                                     hmp.put("eligible", eligibility);
                                                                 }
                                                                 documentReference3.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                     @Override
                                                                     public void onSuccess(Void unused) {
                                                                         Toast.makeText(getApplicationContext(),"Confirmed Successfully see request",Toast.LENGTH_LONG).show();
                                                                     }
                                                                 });
                                                             }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }

                                }
                            });
                        }
                    });
                    will.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    will.create().show();
                }
            }
        });
    }
}