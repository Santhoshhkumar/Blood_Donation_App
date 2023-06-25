package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText regusername,regemailid,regpassword,regmobileno,regdob,regno,regdepartment,regbloodgroup,reggender;
    TextView regeligibility;
    Button register,dob,gotolog;
    ProgressBar prgbar;
    Spinner spin,spin1;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userid;
    String TAG;
    private int curr_prgbar=0;
    public int date,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        regeligibility=(TextView)findViewById(R.id.regeligibilityidtext);
        reggender=(EditText)findViewById(R.id.genderedittext);
        spin1=(Spinner)findViewById(R.id.spinner1);
        regusername=(EditText) findViewById(R.id.regusernameedittxt);
        regemailid=(EditText) findViewById(R.id.regemailidtext);
        regpassword=(EditText) findViewById(R.id.regpasswordidtext);
        regmobileno=(EditText)findViewById(R.id.regphonenoidtext);
        regbloodgroup=(EditText)findViewById(R.id.bgsdittext);
        regdepartment=(EditText)findViewById(R.id.regdepartmentidtext);
        //For calender
        regdob=(EditText)findViewById(R.id.dobtxt);
        dob=(Button)findViewById(R.id.calbtn);
        //Going to login page
        gotolog=(Button)findViewById(R.id.gotologin);
        regno=(EditText)findViewById(R.id.regnoidtext);

        spin=(Spinner) findViewById(R.id.spinner);
        register=(Button)findViewById(R.id.regbtn);
        prgbar=(ProgressBar) findViewById(R.id.regprogressid);
        fAuth=FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Userprofile.class));
            finish();
        }
        fstore=FirebaseFirestore.getInstance();
        final String spinarray[]={"A+","B+","AB+","O+","O-","AB-","B-","A-"};
        final String gender[]={"Male","Female","Others"};
        ArrayAdapter arrayAdapter2=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,gender);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,spinarray);
        spin.setAdapter(arrayAdapter);
        spin1.setAdapter(arrayAdapter2);
        /*if(fAuth.getCurrentUser()!=null)
        {
            Toast.makeText(getApplicationContext(),"You logged in",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Registration.this,Successful.class));
            finish();
        }*/
        dob.setOnClickListener(view1 -> {
            final Calendar cal = Calendar.getInstance();
            date=cal.get(Calendar.DATE);
            month=cal.get(Calendar.MONTH);
            year=cal.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(Registration.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mdate) {
                    mmonth=mmonth+1;
                    regdob.setText(mdate+"-"+mmonth+"-"+myear);
                    try
                    {
                        if(year-myear>=18)
                        {

                        }
                    }
                    catch (Exception e)
                    {
                        regdob.setError("Your age less than 18");
                    }
                }
            },year,month,date);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        });
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Registration.this,adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                regbloodgroup.setText(adapterView.getSelectedItem().toString().trim());
                ((TextView)view).setText(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Registration.this,adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                reggender.setText(adapterView.getSelectedItem().toString().trim());
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gotolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,MainActivity.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=regusername.getText().toString().trim();
                String emailid=regemailid.getText().toString().trim();
                String password=regpassword.getText().toString().trim();
                String mobileno=regmobileno.getText().toString().trim();
                String registerno=regno.getText().toString().trim();
                String dateofbirth=regdob.getText().toString().trim();
                String department=regdepartment.getText().toString().trim();
                String bloodgroup=regbloodgroup.getText().toString().trim();
                String gender=reggender.getText().toString().trim();
                String eligibility=regeligibility.getText().toString();
                if(TextUtils.isEmpty(gender))
                {
                    reggender.setError("Gender Required");
                    return;
                }
                if(TextUtils.isEmpty(username))
                {
                    regusername.setError("UserName Required");
                    return;
                }
                if(TextUtils.isEmpty(emailid))
                {
                    regemailid.setError("Email Id Required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    regpassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 8)
                {
                    regpassword.setError("Password must be greater than 8");
                    return;
                }
                if(TextUtils.isEmpty(mobileno))
                {
                    regmobileno.setError("Mobile No required");
                    return;
                }
                if(mobileno.length() >=10)
                {
                    regmobileno.setError("Mobile No greater than 10");
                    return;
                }
                if(TextUtils.isEmpty(registerno))
                {
                    regno.setError("Register No Required");
                    return;
                }
                if(TextUtils.isEmpty(dateofbirth))
                {
                    regdob.setError("Date of birth Required");
                }
                if(TextUtils.isEmpty(department))
                {
                    regdepartment.setError("Department Required");
                }
                fAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                            prgbar.setVisibility(view.VISIBLE);
                            //
                            userid = fAuth.getCurrentUser().getUid().toString();
                            DocumentReference documentReference = fstore.collection("student").document(userid);
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
                                    CountDownTimer countDownTimer = new CountDownTimer(5 * 1000, 1000) {
                                        @Override
                                        public void onTick(long l) {
                                            curr_prgbar = curr_prgbar + 20;
                                            prgbar.setProgress(curr_prgbar);
                                            prgbar.setMax(100);
                                        }

                                        @Override
                                        public void onFinish() {
                                            //.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "OnSuccess User Registered Succesfully for UserId" + userid);
                                            startActivity(new Intent(Registration.this, MainActivity.class));
                                            prgbar.setVisibility(View.GONE);
                                        }
                                    };
                                    countDownTimer.start();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure" + e.toString());
                                }
                            });
                            //
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        DocumentReference dcstuid = fstore.collection("student_uid").document(registerno);
                        Map<String,Object> std = new HashMap<>();
                        {
                            std.put("uid",userid);
                        }
                        dcstuid.set(std).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }



                });
            }
        });
    }
    }
