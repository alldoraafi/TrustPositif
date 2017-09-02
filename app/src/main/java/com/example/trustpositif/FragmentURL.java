package com.example.trustpositif;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class FragmentURL extends Fragment {
    private static String url;
    private View view;
    private ImageView mImageView;
    private EditText urlText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_url, container, false);

        //Ikon HTTP
        mImageView = (ImageView) view.findViewById(R.id.imageView7);
        mImageView.setImageResource(R.drawable.url);

        //TextView URL
        urlText = (EditText) view.findViewById(R.id.url);
        urlText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
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

    //Method Kirim Value URL
    public static String getURL() {
        if (!(url == null)) {
            return "URL yang dilaporkan : " + url;
        } else {
            return "";
        }
    }
}
