package com.example.trustpositif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentURL extends Fragment {
    private static String url;
    private View view;
    private ImageView mImageView;
    private EditText urlText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_url, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView7);
        mImageView.setImageResource(R.drawable.url);
        urlText = (EditText) view.findViewById(R.id.url);

        urlText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                url = String.valueOf(urlText.getText());
            }
        });
        return view;
    }

    public static String getURL() {
        if (!(url == null)) {
            return "URL yang dilaporkan : " + url;
        } else {
            return "";
        }
    }
}
