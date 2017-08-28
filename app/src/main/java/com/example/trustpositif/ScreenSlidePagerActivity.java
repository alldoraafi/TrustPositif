package com.example.trustpositif;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by AR-Laptop on 8/9/2017.
 */

public class ScreenSlidePagerActivity extends FragmentActivity {
    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private RadioGroup progressGroup;
    private Button nextButton;
    private ImageView trustImg;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem()==4){

                }else{
                    mPager.setCurrentItem(mPager.getCurrentItem()+1);
                }
            }
        });

        trustImg = (ImageView) findViewById(R.id.trustImage);
        trustImg.setImageResource(R.drawable.trust);
        mPager = (ViewPager) findViewById(R.id.viewPager1);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:((RadioButton)progressGroup.getChildAt(0)).setChecked(true);break;
                    case 1:((RadioButton)progressGroup.getChildAt(1)).setChecked(true);break;
                    case 2:((RadioButton)progressGroup.getChildAt(2)).setChecked(true);break;
                    case 3:((RadioButton)progressGroup.getChildAt(3)).setChecked(true);break;
                    case 4:((RadioButton)progressGroup.getChildAt(4)).setChecked(true);break;
                }
                if (position==4){
                    nextButton.setText("Submit");
                }else{nextButton.setText("Lanjutkan");}
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        progressGroup = (RadioGroup) findViewById(R.id.progressGroup);
        progressGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:mPager.setCurrentItem(0); break;
                    case R.id.radioButton2:mPager.setCurrentItem(1); break;
                    case R.id.radioButton3:mPager.setCurrentItem(2); break;
                    case R.id.radioButton4:mPager.setCurrentItem(3); break;
                    case R.id.radioButton5:mPager.setCurrentItem(4); break;
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(mPager.getCurrentItem()==0){
            super.onBackPressed();
        }else {
            mPager.setCurrentItem(mPager.getCurrentItem()-1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
        public ScreenSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }

        Fragment url = new FragmentURL();
        Fragment screenshot = new FragmentScreenshot();
        Fragment kategori = new FragmentKategori();
        Fragment keterangan = new FragmentKeterangan();
        Fragment submit = new FragmentSubmit();
        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0: return url;
                case 1: return screenshot;
                case 2: return kategori;
                case 3: return keterangan;
                case 4: return submit;
                default: return new FragmentURL();
            }
        }

        @Override
        public int getCount(){
            return NUM_PAGES;
        }
    }

    public interface getData{
        String getValue();
    }
}
