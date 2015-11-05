package com.desmond.rippledemo.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.desmond.ripple.RippleCompat;
import com.desmond.ripple.RippleCompatDrawable;
import com.desmond.ripple.RippleConfig;
import com.desmond.ripple.RippleUtil;
import com.desmond.rippledemo.R;

/**
 * Created by Jiayi Yao on 2015/11/4.
 */
public class WidgetTestFragment extends Fragment implements View.OnClickListener{
    private int[] color = new int[]{
            0x70ff0000,
            0x7000ff00,
            0x700000ff,
            0x70ff00ff
    };

    private ViewGroup mLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_widget, null);
        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tv = (TextView) mLayout.findViewById(R.id.test_textView);
        RippleCompat.apply(tv, color[0]);

        Button btn = (Button) mLayout.findViewById(R.id.test_button);
        RippleCompat.apply(btn, color[1]);
        btn.setOnClickListener(this);

        EditText et = (EditText) mLayout.findViewById(R.id.test_et);
        RippleCompat.apply(et, color[2]);

        final View iv = mLayout.findViewById(R.id.test_heart);
        RippleConfig config = new RippleConfig();
        config.setRippleColor(color[3]);
        config.setIsFull(true);
        config.setBackgroundDrawable(new ColorDrawable(RippleUtil.alphaColor(Color.GREEN, 128)));
        config.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        RippleCompat.apply(iv, config);
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Button Pressed!", Snackbar.LENGTH_SHORT).show();
    }
}
