package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class cardview extends AppCompatActivity {

    CardView user,update,request,tips,logout,donate,bloodtypes,moreinfo;
    boolean agr;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private String EVENT_DATE_TIME ="2023-3-14 20:06:00";
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;
    String TAG;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    boolean donated;
    //String uid;
    DocumentReference documentReference3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        user=(CardView) findViewById(R.id.User);
        update=(CardView) findViewById(R.id.Update);
        request=(CardView) findViewById(R.id.Request);
        tips=(CardView) findViewById(R.id.Tips);
        donate=(CardView) findViewById(R.id.Donate);
        logout=(CardView) findViewById(R.id.Logout);
        bloodtypes=(CardView)findViewById(R.id.bloodgroups);
        moreinfo=(CardView)findViewById(R.id.AboutUs);
        String uid = firebaseUser.getUid();
        initUI();
        DocumentReference documentReference2=firestore.collection("student").document(uid);
        DocumentReference documentReference1=firestore.collection("donatedlist").document(uid);
        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if(task.getResult().exists())
              {
                  String es = task.getResult().getString("regeligibility");
                  Log.d(TAG, "OnSuccess User eligibility for UserId" + es);
                  //by default eligibility is no and donatedlist is no / yes
                  if(es.equals("No"))
                  {
                      documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists())
                            {
                                String h =task.getResult().getString("eligible");
                                if(h.equals("No"))
                                {
                                    Toast.makeText(getApplicationContext(),"Entered",Toast.LENGTH_SHORT).show();
                                   checkdonatedlist();
                                }
                            }
                          }
                      });//checking donatedlist once he needs to wait for  3 months and the timer should run at backfround.
                  }
                  else if(es.equals("Yes"))
                  {
                      documentReference3=firestore.collection("donatedlist").document(uid);
                      //Toast.makeText(getApplicationContext(),"Tsdlcd",Toast.LENGTH_SHORT).show();
                      checkingstdeligibility();//This is for to  check whether the student is eligible for donating the blood after donating the blood or not
                      /*documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                              if(task.getResult().exists())
                              {
                                  Toast.makeText(getApplicationContext(),"Hodmck",Toast.LENGTH_LONG).show();
                                  String DonatedOrNot=task.getResult().getString("regeligibility");
                                  if(DonatedOrNot.equals("No"))
                                  {
                                      documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                                  String gender = task.getResult().getString("reggender");
                                                  if(eligibility.equals("Yes"))
                                                  {
                                                      eligibility="No";
                                                      agr=false;
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
                                                  documentReference2.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void unused) {

                                                         countDownStart();
                                                      }
                                                  });
                                              }
                                          }
                                      });
                                  }
                                  else if(DonatedOrNot.equals("Yes"))
                                  {
                                      documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                                  String gender = task.getResult().getString("reggender");
                                                  if(eligibility.equals("No"))
                                                  {
                                                      eligibility="Yes";
                                                      agr=true;
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
                                                  documentReference2.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void unused) {
                                                          notficationhandler(agr, username);
                                                          handler.removeCallbacks(runnable);
                                                      }
                                                  });
                                              }
                                          }
                                      });
                                  }//else if
                              }//if of get result
                          }//oncomplete
                      });*///documentreference3

                  }
                  // eligibility is yes and donatedlist is no
                  /*else if(es.equals("Yes"))
                  {
                      documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists())
                                {
                                    String DonatedOrNot = task.getResult().getString("regeligibility");
                                    if(DonatedOrNot.equals("No"))
                                    {
                                      agr=true;
                                    }
                                }
                          }
                      });
                      if(agr==true)
                      {
                          String username = task.getResult().getString("regusername");
                          String emailid = task.getResult().getString("regemailid");
                          String mobileno = task.getResult().getString("regmobileno");
                          String registerno = task.getResult().getString("regno");
                          String dateofbirth = task.getResult().getString("regdob");
                          String department = task.getResult().getString("regdepartment");
                          String bloodgroup = task.getResult().getString("regbloodgroup");
                          //String eligibilitye = task.getResult().getString("regeligibility");
                          String gender = task.getResult().getString("reggender");
                          es="No";
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
                              hmp.put("regeligibility", es);
                          }
                          documentReference2.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  notficationhandler(agr, username);
                                  handler.removeCallbacks(runnable);
                              }
                          });
                      }
                  }*/
                  // eligibility is no and donatedlist is yes
                  /*else if(es.equals("No"))
                  {
                      documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                              if(task.getResult().exists())
                              {
                                  String DonatedOrNot = task.getResult().getString("regeligibility");
                                  if(DonatedOrNot.equals("Yes"))
                                  {
                                      agr=true;
                                  }
                              }
                          }
                      });
                      if(agr==true)
                      {
                          String username = task.getResult().getString("regusername");
                          String emailid = task.getResult().getString("regemailid");
                          String mobileno = task.getResult().getString("regmobileno");
                          String registerno = task.getResult().getString("regno");
                          String dateofbirth = task.getResult().getString("regdob");
                          String department = task.getResult().getString("regdepartment");
                          String bloodgroup = task.getResult().getString("regbloodgroup");
                          //String eligibilitye = task.getResult().getString("regeligibility");
                          String gender = task.getResult().getString("reggender");
                          es="Yes";
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
                              hmp.put("regeligibility", es);
                          }
                          documentReference2.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  notficationhandler(agr, username);
                                  handler.removeCallbacks(runnable);
                              }
                          });
                      }
                  }*/

              }
            }
        });
        //countDownStart();
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Userprofile.class));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Editprofile.class));
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Request.class));
            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Donate.class));
            }
        });

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Tips.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Successful.class));
            }
        });
        bloodtypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cardview.this,Bloodtypes.class));
            }
        });

    }
    public void checkingstdeligibility()//1) eligibility yes is changed to no by the admin and 2)after time up eligibility no is changed to yes
    {

        String uidd = firebaseUser.getUid();
        //Toast.makeText(getApplicationContext(),"Hjsbhjdnc",Toast.LENGTH_SHORT).show();
        DocumentReference documentReference3 = firestore.collection("donatedlist").document(uidd);
        DocumentReference documentReference4 = firestore.collection("student").document(uidd);
        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    String h = task.getResult().getString("eligible");
                    //Toast.makeText(getApplicationContext(),h,Toast.LENGTH_SHORT).show();
                    if(h.equals("Yes"))
                    {
                        documentReference4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists())
                                {
                                    String e =task.getResult().getString("regeligibility");
                                    if(e.equals("Yes"))
                                    {
                                        int y=2;//this to stop the notification once they have received the notification already
                                        changedonatedlist(y);
                                    }
                                }
                            }
                        });
                    }
                    if(h.equals("No"))//if the admin changes the eligibility then the studnet needs to wait for 3 months.
                    {
                        documentReference4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if(task.isSuccessful())
                               {
                                   String username = task.getResult().getString("regusername");
                                   String emailid = task.getResult().getString("regemailid");
                                   String mobileno = task.getResult().getString("regmobileno");
                                   String registerno = task.getResult().getString("regno");
                                   String dateofbirth = task.getResult().getString("regdob");
                                   String department = task.getResult().getString("regdepartment");
                                   String bloodgroup = task.getResult().getString("regbloodgroup");
                                   String eligibility = task.getResult().getString("regeligibility");
                                   String gender = task.getResult().getString("reggender");
                                   eligibility=h;
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
                                   } documentReference4.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                     Toast.makeText(getApplicationContext(),"eligibility changed",Toast.LENGTH_SHORT).show();
                                     countDownStart();
                                   }
                               });
                               }
                            }
                        });
                    }
                }
            }
        });
    }
    public void checkdonatedlist()//logging to check whether the donar can donate or not
    {
        Date current_date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date event_date = dateFormat.parse(EVENT_DATE_TIME);
            if (!current_date.after(event_date))
            {
                countDownStart();
            }
        }
        catch (Exception e)
        {

        }
    }
    public void changedonatedlist(int notificationreceivedonce)
    {
       if(notificationreceivedonce>1)
       {
          return;
       }
    }
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
    private void initUI() {
        linear_layout_1 = findViewById(R.id.linear_layout_1);
        linear_layout_2 = findViewById(R.id.linear_layout_2);
        tv_days = findViewById(R.id.tv_days);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);
    }
    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Calendar calendar = Calendar.getInstance();
                    handler.postDelayed(this, 1000);
                    //Toast.makeText(getApplicationContext(),"jjhs",Toast.LENGTH_SHORT).show();
                    eventdatetime(EVENT_DATE_TIME);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    //String datetime=dateFormat.format(calendar.getTime());
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;

                        //
                        tv_days.setText(String.format("%02d :", Days));
                        tv_hour.setText(String.format("%02d :", Hours));
                        tv_minute.setText(String.format("%02d :", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
                         String uid = firebaseUser.getUid();
                            linear_layout_1.setVisibility(View.VISIBLE);
                            linear_layout_2.setVisibility(View.GONE);
                            DocumentReference documentReference = firestore.collection("student").document(uid);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                        String eligibility = task.getResult().getString("regeligibility");
                                        String gender = task.getResult().getString("reggender");
                                        if (eligibility.equals("No")) {
                                            eligibility = "Yes";
                                            agr = true;
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
                                                    notficationhandler(agr, username);
                                                    handler.removeCallbacks(runnable);
                                                }
                                            });
                                        }
                                        else
                                        {
                                            agr=false;
                                        }
                                    }
                                }
                            });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }
    public void notficationhandler(boolean agr,String username)
    {
        if(agr==true) {
            String uid=firebaseUser.getUid();
            String donateddate = getdateofdonation();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(cardview.this, "My Notification");
            builder.setContentTitle("My Title");
            builder.setContentText("Hello donar"+username+"Now you are eligible for donating blood");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(cardview.this);
            notificationManagerCompat.notify(1, builder.build());
            agr=false;
            DocumentReference documentReference =firestore.collection("donatedlist").document(uid);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if(task.getResult().exists())
                      {
                          String e = task.getResult().getString("eligible");
                          if(e.equals("No"))
                          {
                              e="Yes";
                              Map<String,Object>hmp=new HashMap<>();
                              {
                                  hmp.put("eligible", e);
                              }
                              documentReference.set(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void unused) {
                                      int notificationreceivedonetime=1;
                                      changedonatedlist(notificationreceivedonetime);
                                  }
                              });
                          }
                      }
                }
            });

        }
    }
    public String getdateofdonation()
    {
        Calendar calendar = Calendar.getInstance();
        String date= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now().toString();
        }
        return date;
    }
    /*public String startingdate()
    {
        Calendar calendar = Calendar.getInstance();
        Date today =new Date();
        String realdate="";
        LocalDateTime now=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
            //now.plusDays(90);
           // LocalDate.parse(now.toString());
            now.plusDays(90);
            //LocalDate date =LocalDate.now();
           // date.plusMonths(3);
            //realdate=dtf.format(date);
            //Toast.makeText(getApplicationContext(),date.toString(),Toast.LENGTH_SHORT);
            //LocalTime.now();
            //realdate=dateLocalTime.now();
            //Toast.makeText(getApplicationContext(),now.toString(),Toast.LENGTH_LONG).show();
            //LocalDateTime l =LocalDateTime.parse(now.toString(),dtf);
           // String h=l.toString().replace('T',' ');
            //Toast.makeText(getApplicationContext(),h,Toast.LENGTH_LONG).show();
            //realdate=h;

        }
        return realdate;
    }*/
    public void eventdatetime(String edt)
    {
        String tm=edt;
    }
}
