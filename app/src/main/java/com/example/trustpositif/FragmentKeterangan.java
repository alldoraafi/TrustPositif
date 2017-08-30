package com.example.trustpositif;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by AR-Laptop on 8/8/2017.
 */

public class FragmentKeterangan extends Fragment {
    View view;
    static TextView keterangan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_keterangan,container, false);

        keterangan = (TextView) view.findViewById(R.id.textKeterangan);
        keterangan.clearFocus();

        return view;
    }

    public static String getKeterangan() {
        if (keterangan == null)
            return "";
        else
            return keterangan.getText().toString();
    }
}
