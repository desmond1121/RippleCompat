package com.desmond.rippledemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.desmond.ripple.RippleCompat;
import com.desmond.ripple.RippleConfig;

/**
 * Created by Jiayi Yao on 2015/11/5.
 */
public class NormalActivity extends Activity{
    private int[] color = new int[]{
            0x44ff0000,
            0x4400ff00,
            0x440000ff,
            0x4400ffff
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_widget);
        RippleCompat.init(this);
        TextView tv = (TextView) findViewById(R.id.test_textView);
        RippleCompat.apply(tv, color[0]);

        Button btn = (Button) findViewById(R.id.test_button);
        RippleCompat.apply(btn, color[1]);

        EditText et = (EditText) findViewById(R.id.test_et);
        RippleCompat.apply(et, color[2]);

        final View iv = findViewById(R.id.test_heart);
        RippleConfig config = new RippleConfig();
        config.setRippleColor(color[3]);
        config.setIsFull(true);
        config.setBackgroundDrawable(getResources().getDrawable(R.drawable.lufi));
        config.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        RippleCompat.apply(iv, config);
    }
}
