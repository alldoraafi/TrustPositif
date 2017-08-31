package com.example.trustpositif;

//import android.media.Image;
//import android.support.annotation.IdRes;
//import android.support.annotation.Nullable;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.ScaleAnimation;
//import android.view.animation.TranslateAnimation;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RadioGroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentKeterangan extends Fragment {
    View view;
    private TextView keterangan;
    private static String textKeterangan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_keterangan, container, false);

        keterangan = (TextView) view.findViewById(R.id.textKeterangan);
        keterangan.clearFocus();

        keterangan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textKeterangan = String.valueOf(keterangan.getText());
            }
        });

        return view;
    }

    public static String getKeterangan() {
        String returnKeterangan;
        if (textKeterangan == null) {
            returnKeterangan = "Keterangan : -";
            return returnKeterangan;
        } else {
            returnKeterangan = "Keterangan : \n" + textKeterangan;
            return returnKeterangan;
        }
    }
}
