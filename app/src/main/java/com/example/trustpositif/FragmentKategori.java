package com.example.trustpositif;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by AR-Laptop on 8/8/2017.
 */

public class FragmentKategori extends Fragment {
    View view;
    private ImageView[] img;
    private int[] png, imgView;
    static int selectedKat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kategori, container, false);

        //Inisialisasi ImageView dan Ikon Kategori
        img = new ImageView[12];
        png = new int[]{R.drawable.kat, R.drawable.kat2, R.drawable.kat3, R.drawable.kat4, R.drawable.kat5, R.drawable.kat6,
                R.drawable.kat7, R.drawable.kat8, R.drawable.kat9, R.drawable.kat10, R.drawable.kat11, R.drawable.kat12};
        imgView = new int[]{R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6,
                R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12, R.id.imageView13};

        //Menetapkan tiap tombol Kategori
        for (int i = 0; i < 12; i++) {
            img[i] = (ImageView) view.findViewById(imgView[i]);
            img[i].setImageResource(png[i]);
            img[i].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            return true;
                        case MotionEvent.ACTION_UP:
                            for (ImageView i : img) {
                                i.setBackgroundColor(Color.rgb(255, 255, 255));
                                i.invalidate();
                            }
                            v.setBackgroundColor(Color.rgb(175, 175, 175));
                            v.invalidate();
                            selectedKat = v.getId();
                            return true;
                    }
                    return false;
                }
            });
        }
        return view;
    }

    //Mendapatkan value dari Kategori yang telah dipilih
    public static String getKategori() {
        switch (selectedKat) {
            case R.id.imageView:
                return "ADUAN - Pornografi";
            case R.id.imageView2:
                return "ADUAN - Perjudian";
            case R.id.imageView3:
                return "ADUAN - Radikalisme/Terorisme";
            case R.id.imageView4:
                return "ADUAN - Peninpuan Online";
            case R.id.imageView5:
                return "ADUAN - Investasi Ilegal";
            case R.id.imageView6:
                return "ADUAN - Obat-obatan dan Kosmetik Ilegal";
            case R.id.imageView8:
                return "ADUAN - Pelanggaran Hak Kekayaan Intelektual";
            case R.id.imageView9:
                return "ADUAN - Keamanan Internet";
            case R.id.imageView10:
                return "ADUAN - Kekerasan";
            case R.id.imageView11:
                return "ADUAN - Kekerasan/Pornografi Anak";
            case R.id.imageView12:
                return "ADUAN - SARA";
            case R.id.imageView13:
                return "ADUAN - Pencabulan";
            default:
                return "";
        }
    }
}