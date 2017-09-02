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
import android.widget.TextView;

public class FragmentKeterangan extends Fragment {
    View view;
    private TextView keterangan;
    private static String textKeterangan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_keterangan, container, false);

        keterangan = (TextView) view.findViewById(R.id.textKeterangan);
        keterangan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
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
