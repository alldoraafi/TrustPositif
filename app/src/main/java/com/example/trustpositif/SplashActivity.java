package com.example.trustpositif;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Reza on 08/08/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(SplashActivity.this, ScreenSlidePagerActivity.class);
        startActivity(i);
        finish();
    }
}
