package com.desmond.rippledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.desmond.ripple.RippleCompat;
import com.desmond.ripple.RippleCompatDrawable;
import com.desmond.ripple.RippleConfig;

/**
 * Created by Jiayi Yao on 2015/11/3.
 */
public class ScaleTypeActivity extends AppCompatActivity{
    private static final String TAG = "ScaleTypeActivity";
    ImageView.ScaleType[] types = new ImageView.ScaleType[]{
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE,
            ImageView.ScaleType.MATRIX
    };

    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RippleCompat.init(this);
        setContentView(R.layout.scaletype_layout);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        iv = (ImageView) findViewById(R.id.image_view);
        RippleConfig config = new RippleConfig();
        config.setIsFull(true);
        config.setType(RippleCompatDrawable.Type.HEART);
        RippleCompat.apply(iv, config);
        String[] strings = getResources().getStringArray(R.array.scale_types);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, strings);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RippleCompat.setScaleType(iv, types[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
