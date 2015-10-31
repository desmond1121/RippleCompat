package com.desmond.ripple;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private int[] color = new int[]{
            0x44ff0000,
            0x4400ff00,
            0x440000ff,
            0x4400ffff
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RippleCompat.init(this);

        TextView tv = (TextView) findViewById(R.id.test_textView);
        RippleCompat.apply(tv, color[0]);

        Button btn = (Button) findViewById(R.id.test_button);
        RippleCompat.apply(btn, color[1]);
        btn.setOnClickListener(this);

        EditText et = (EditText) findViewById(R.id.test_et);
        RippleCompat.apply(et, color[2]);

        RippleConfig config = new RippleConfig();
        config.setType(RippleCompatDrawable.Type.HEART);
        config.setRippleColor(color[3]);
        config.setIsFull(true);
        View iv = findViewById(R.id.test_heart);
        RippleCompat.apply(iv, config);

//        setContentView(R.layout.activity_main_1);
//        RippleCompat.init(this);
//        RippleConfig config = new RippleConfig();
//        config.setRippleColor(Color.BLUE & 0x7fffffff);
//        config.setType(RippleCompatDrawable.Type.TRIANGLE);
//        config.setIsSpin(true);
//        config.setIsFull(true);
//        final View v = findViewById(R.id.triangle_spin);
//        RippleCompat.apply(v, config, new RippleCompatDrawable.OnFinishListener() {
//            @Override
//            public void onFinish() {
//                Snackbar.make(v, "Ripple Finish!", Snackbar.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), "Pop!", Snackbar.LENGTH_SHORT).show();
    }
}
