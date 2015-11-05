package com.desmond.rippledemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.desmond.ripple.RippleCompat;
import com.desmond.ripple.RippleCompatDrawable;
import com.desmond.ripple.RippleConfig;
import com.desmond.ripple.RippleUtil;
import com.desmond.rippledemo.R;

/**
 * Created by Jiayi Yao on 2015/11/3.
 */
public class PaletteFragment extends Fragment{
    RippleUtil.PaletteMode[] paletteModes = new RippleUtil.PaletteMode[]{
            RippleUtil.PaletteMode.VIBRANT,
            RippleUtil.PaletteMode.VIBRANT_LIGHT,
            RippleUtil.PaletteMode.VIBRANT_DARK,
            RippleUtil.PaletteMode.MUTED,
            RippleUtil.PaletteMode.MUTED_LIGHT,
            RippleUtil.PaletteMode.MUTED_DARK
    };
    ViewGroup mLayout;
    ImageView iv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = (ViewGroup) inflater.inflate(R.layout.fragment_scaletype, null);
        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Spinner spinner = (Spinner) mLayout.findViewById(R.id.spinner);
        iv = (ImageView) mLayout.findViewById(R.id.image_view);
        RippleConfig config = new RippleConfig();
        config.setIsEnablePalette(true);
        config.setIsFull(true);
        config.setIsSpin(true);
        config.setType(RippleCompatDrawable.Type.TRIANGLE);
        RippleCompat.apply(iv, config);

        String[] strings = getResources().getStringArray(R.array.palette_modes);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, strings);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RippleCompat.setPaletteMode(iv, paletteModes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RippleCompat.apply(spinner, config);
    }
}
