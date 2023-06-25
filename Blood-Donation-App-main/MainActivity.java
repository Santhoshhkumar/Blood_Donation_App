package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText emailidedtxt;
    EditText passwordedtxt;
    ImageView logout;
    Button login;
    TextView lforgetpassword;
    TextView newuser;
    ProgressBar lprgbar;
    FirebaseAuth fauth;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    Boolean passwordvisible=false;
     String DonatedOrNot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newuser=(TextView)findViewById(R.id.newusertxtview);
        emailidedtxt=(EditText)findViewById(R.id.emailidtext);
        passwordedtxt=(EditText)findViewById(R.id.passwordidtext);
        lforgetpassword=(TextView)findViewById(R.id.fgrpassword);
        login=(Button)findViewById(R.id.loginbtn);
        lprgbar=(ProgressBar)findViewById(R.id.lprgbar);
        fauth=FirebaseAuth.getInstance();
        //firestore=FirebaseFirestore.getInstance();
        //firebaseUser=fauth.getCurrentUser();
        //String uid=firebaseUser.getUid();
        /*DocumentReference documentReference = firestore.collection("DonatedList").document("7doDZcW2UtVBzeD1MJoG6fJNPv33");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    Toast.makeText(getApplicationContext(),"Ready for donation",Toast.LENGTH_SHORT).show();
                     DonatedOrNot =task.getResult().getString("regeligibility");
                    if(DonatedOrNot.equals("Yes"))
                    {
                        DocumentReference documentReference1=firestore.collection("student").document("7doDZcW2UtVBzeD1MJoG6fJNPv33");
                        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists())
                                {
                                    String username = task.getResult().getString("regusername");
                                    String emailid = task.getResult().getString("regemailid");
                                    String mobileno = task.getResult().getString("regmobileno");
                                    String registerno = task.getResult().getString("regno");
                                    String dateofbirth = task.getResult().getString("regdob");
                                    String department = task.getResult().getString("regdepartment");
                                    String bloodgroup = task.getResult().getString("regbloodgroup");
                                    String eligibility = task.getResult().getString("regeligibility");
                                    String gender=task.getResult().getString("reggender");
                                    if(eligibility.equals("No"))
                                    {
                                        eligibility="Yes";
                                    }
                                    Map<String, Object> hmp = new HashMap<>();
                                    {

                                        hmp.put("regusername", username);
                                        hmp.put("regemailid", emailid);
                                        hmp.put("regmobileno", mobileno);
                                        hmp.put("regno", registerno);
                                        hmp.put("regdob", dateofbirth);
                                        hmp.put("regdepartment", department);
                                        hmp.put("regbloodgroup", bloodgroup);
                                        hmp.put("reggender", gender);
                                        hmp.put("regeligibility", eligibility);
                                    }
                                    documentReference.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            DonatedOrNot="";
                                        }
                                    });
                                }
                            }
                        });
                    }

                }
            }
        });*/

        passwordedtxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int right=2;
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    if(motionEvent.getRawX()>=passwordedtxt.getRight()-passwordedtxt.getCompoundDrawables()[right].getBounds().width()){
                        int selection = passwordedtxt.getSelectionEnd();
                        if(passwordvisible)
                        {
                            passwordedtxt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            passwordedtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            passwordvisible=false;
                        }
                        else
                        {
                            passwordedtxt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            passwordedtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                            passwordvisible=true;
                        }
                        passwordedtxt.setSelection(selection);
                        return  true;
                    }
                }
                return  false;
            }

        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailid=emailidedtxt.getText().toString().trim();
                String password=passwordedtxt.getText().toString().trim();
                if(TextUtils.isEmpty(emailid))
                {
                    emailidedtxt.setError("Email Id Required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    passwordedtxt.setError("Password is Required");
                    return;
                }
                if(password.length() < 8)
                {
                    passwordedtxt.setError("Password length less than 8");
                    return;
                }
                lprgbar.setVisibility(View.VISIBLE);

                fauth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"You Signedin",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),cardview.class));
                            lprgbar.setVisibility(View.GONE);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Error:"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            lprgbar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        lforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetmail = new EditText(view.getContext());
                final AlertDialog.Builder passwrdalert = new AlertDialog.Builder(view.getContext());
                passwrdalert.setTitle("Reset Password");
                passwrdalert.setMessage("Enter your EmailId to get reset link");
                passwrdalert.setView(resetmail);
                passwrdalert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = resetmail.getText().toString();
                        if(email.isEmpty())
                        {
                            resetmail.setError("Email id required");
                        }
                        fauth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this,"Reset link sended",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error! Reset link not send"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwrdalert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwrdalert.create().show();
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Registration.class));
                finish();
            }
        });
    }
}