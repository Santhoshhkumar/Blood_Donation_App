package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class Tips extends AppCompatActivity {

        ViewPager viewPager;
        int currentpage=0;
        int images[]={R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4};
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tips);
            viewPager=(ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(new SliderAdapter(images,Tips.this));

            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if(currentpage==images.length)
                    {
                        currentpage=0;
                    }
                    viewPager.setCurrentItem(currentpage++,true);
                }
            };
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },4500,3000);
        }
    }
