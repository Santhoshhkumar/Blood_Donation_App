package com.example.blooddonation;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    int [] images;
    LayoutInflater layerinfaltor;
    Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public SliderAdapter(LayoutInflater layerinfaltor) {
        this.layerinfaltor = layerinfaltor;
    }

    public SliderAdapter(int [] images,Context context) {
        this.images = images;
        this.layerinfaltor=LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        View myimagelayout=layerinfaltor.inflate(R.layout.slider_layout, (ViewGroup) container, false);
        ImageView imageView = myimagelayout.findViewById(R.id.pic1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            imageView.setImageDrawable(context.getDrawable(images[position]));
        }
        else
        {
            imageView.setImageDrawable(context.getResources().getDrawable(images[position]));
        }
        ((ViewGroup) container).addView(myimagelayout);
        return myimagelayout;
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}


