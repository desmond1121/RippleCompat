package com.desmond.ripple;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RippleCompat.init(this);

        TextView tv = (TextView) findViewById(R.id.test_textView);
        RippleCompat.apply(tv);

        Button btn = (Button) findViewById(R.id.test_button);
        RippleCompat.apply(btn);
        btn.setOnClickListener(this);

        EditText et = (EditText) findViewById(R.id.test_et);
        RippleCompat.apply(et);

        RippleConfig config = new RippleConfig();
        config.setType(RippleCompatDrawable.Type.HEART);
        View iv = findViewById(R.id.test_heart);
        RippleCompat.apply(iv, config);
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), "Pop!", Snackbar.LENGTH_SHORT).show();
    }
}
