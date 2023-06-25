package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    EditText regusername,regemailid,regpassword,regmobileno,regdob,regno,regdepartment,regbloodgroup,reggender;
    TextView regeligibility;
    Button update,dob,gotolog;
    ProgressBar prgbar;
    Spinner spin,spin1;
    String userid;
    String TAG;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    private int  curr_prgbar=0;
    public int date,month,year;
    final String spinarray[]={"A+","B+","AB+","O+","O-","AB-","B-","A-"};
    DocumentReference reference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        fstore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        reggender=(EditText) findViewById(R.id.genderedittext);
        regeligibility=(TextView)findViewById(R.id.eligibility);
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
        update=(Button)findViewById(R.id.upbtn);
        prgbar=(ProgressBar) findViewById(R.id.upprogressid);
        /*if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),Userprofile.class));
            finish();
        }*/
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(Editprofile.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
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
                Toast.makeText(Editprofile.this,adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Editprofile.this,adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(Editprofile.this,cardview.class));
                finish();
            }
        });
        firebaseUser = fAuth.getCurrentUser();
        userid=firebaseUser.getUid();
        reference = fstore.collection("student").document(userid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    String username=task.getResult().getString("regusername");
                    String emailid=task.getResult().getString("regemailid");
                    String mobileno=task.getResult().getString("regmobileno");
                    String registerno=task.getResult().getString("regno");
                    String dateofbirth=task.getResult().getString("regdob");
                    String department=task.getResult().getString("regdepartment");
                    String bloodgroup=task.getResult().getString("regbloodgroup");
                    String gender=task.getResult().getString("reggender");
                    String eligibility=task.getResult().getString("regeligibility");
                    regusername.setText(username);
                    regemailid.setText(emailid);
                    regmobileno.setText(mobileno);
                    regno.setText(registerno);
                    regdob.setText(dateofbirth);
                    regdepartment.setText(department);
                    regbloodgroup.setText(bloodgroup);
                    reggender.setText(gender);
                    regeligibility.setText(eligibility);
                    userid=fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("student").document(userid);
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String uname = regusername.getText().toString().trim();
                            String eid = regemailid.getText().toString().trim();
                            String mno = regmobileno.getText().toString().trim();
                            String rno = regno.getText().toString().trim();
                            String dateob = regdob.getText().toString().trim();
                            String depart= regdepartment.getText().toString().trim();
                            String blood = regbloodgroup.getText().toString().trim();
                            String gender = reggender.getText().toString().trim();
                            String eligibility = regeligibility.getText().toString().trim();
                            Map<String,Object> hmp=new HashMap<>();
                            {
                                hmp.put("regusername", uname);
                                hmp.put("regemailid", eid);
                                hmp.put("regmobileno", mno);
                                hmp.put("regno", rno);
                                hmp.put("regdob", dateob);
                                hmp.put("regdepartment", depart);
                                hmp.put("regbloodgroup", blood);
                                hmp.put("reggender",gender);
                                hmp.put("regeligibility",eligibility);
                                prgbar.setVisibility(View.VISIBLE);
                            }
                            documentReference.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    CountDownTimer countDownTimer =new CountDownTimer(5*1000,1000)
                                    {
                                        @Override
                                        public void onTick(long l) {
                                            curr_prgbar = curr_prgbar +20;
                                            prgbar.setProgress(curr_prgbar);
                                            prgbar.setMax(100);
                                        }
                                        @Override
                                        public void onFinish() {
                                            Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG,"OnSuccess User Updated Succesfully for UserId"+userid);
                                            startActivity(new Intent(Editprofile.this,cardview.class));
                                            prgbar.setVisibility(View.GONE);
                                        }
                                    };
                                    countDownTimer.start();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"OnFailure"+e.toString());
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}