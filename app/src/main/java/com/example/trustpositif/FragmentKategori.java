package com.example.trustpositif;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.R.attr.id;

/**
 * Created by AR-Laptop on 8/8/2017.
 */

public class FragmentKategori extends Fragment{
    View view;
    private ImageView[] img;
    private int[] png, imgView;
    static int selectedKat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_kategori,container, false);
        img = new ImageView[12];
        png = new int[]{R.drawable.kat, R.drawable.kat2, R.drawable.kat3, R.drawable.kat4, R.drawable.kat5, R.drawable.kat6,
                R.drawable.kat7, R.drawable.kat8, R.drawable.kat9, R.drawable.kat10, R.drawable.kat11, R.drawable.kat12};
        imgView = new int[]{R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6,
                R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12, R.id.imageView13};
        for (int i=0; i<12; i++){
            img[i] = (ImageView) view.findViewById(imgView[i]);
            img[i].setImageResource(png[i]);
            img[i].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    selectedKat = v.getId();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            for (ImageView i : img) {
                                i.setBackgroundColor(Color.rgb(255, 255, 255));
                                i.invalidate();
                            }
                            ImageView view = (ImageView) v;
                            view.setBackgroundColor(Color.rgb(175, 175, 175));
                            view.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            ImageView view = (ImageView) v;
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            break;
                        }
                    }
                    return false;
                }
            });
        }
        return view;
    }

    public static String getKategori() {
        switch (selectedKat){
            case R.id.imageView: return "Pornografi";
            case R.id.imageView2: return "Perjudian";
            case R.id.imageView3: return "Radikalisme/Terorisme";
            case R.id.imageView4: return "Peninpuan Online";
            case R.id.imageView5: return "Investasi Ilegal";
            case R.id.imageView6: return "Obat-obatan dan Kosmetik Ilegal";
            case R.id.imageView8: return "Pelanggaran Hak Kekayaan Intelektual";
            case R.id.imageView9: return "Keamanan Internet";
            case R.id.imageView10: return "Kekerasan";
            case R.id.imageView11: return "Kekerasan/Pornografi Anak";
            case R.id.imageView12: return "SARA";
            case R.id.imageView13: return "Pencabulan";
            default:return "";
        }
    }
//
//    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//            if(checkedId != -1){
//                kategori2.setOnCheckedChangeListener(null);
//                kategori3.setOnCheckedChangeListener(null);
//                kategori2.clearCheck();
//                kategori3.clearCheck();
//                kategori2.setOnCheckedChangeListener(listener2);
//                kategori3.setOnCheckedChangeListener(listener3);
//            }
//        }
//    };
//
//    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//            if(checkedId != -1){
//                kategori1.setOnCheckedChangeListener(null);
//                kategori3.setOnCheckedChangeListener(null);
//                kategori1.clearCheck();
//                kategori3.clearCheck();
//                kategori1.setOnCheckedChangeListener(listener1);
//                kategori3.setOnCheckedChangeListener(listener3);
//            }
//        }
//    };
//
//    private RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//            if(checkedId != -1){
//                kategori2.setOnCheckedChangeListener(null);
//                kategori1.setOnCheckedChangeListener(null);
//                kategori2.clearCheck();
//                kategori1.clearCheck();
//                kategori2.setOnCheckedChangeListener(listener2);
//                kategori1.setOnCheckedChangeListener(listener1);
//            }
//        }
//    };
}
