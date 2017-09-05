package com.example.trustpositif;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Reza on 30/08/2017.
 */

public class FaqActivity extends Activity {
    private ExpandListAdapter ExpAdapter;
    private ArrayList<Group> ExpListItems;
    private ExpandableListView ExpandList;
    ImageView trustImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_view);

        trustImg = (ImageView) findViewById(R.id.imageView16);
        trustImg.setImageResource(R.drawable.trust);

        ExpandList = (ExpandableListView) findViewById(R.id.exp_list);
        ExpListItems = SetStandardGroups();
        ExpAdapter = new ExpandListAdapter(FaqActivity.this, ExpListItems);
        ExpandList.setAdapter(ExpAdapter);
    }

    //set daftar item untuk F.A.Q
    public ArrayList<Group> SetStandardGroups() {
        //mengambil array pertanyaan dari resource string
        String pertanyaan[] = {getResources().getString(R.string.Faq1Q), getResources().getString(R.string.Faq2Q), getResources().getString(R.string.Faq3Q)};

        //mengambil array jawaban dari resource string
        String jawaban[] = {getResources().getString(R.string.Faq1A), getResources().getString(R.string.Faq2A), getResources().getString(R.string.Faq3A)};

        ArrayList<Group> list = new ArrayList<Group>();

        ArrayList<Child> ch_list;

        //variabel size untuk jumlah pertanyaan F.A.Q yang ada
        int size = 1;
        int j = 0;

        //perulangan untuk memasukkan nilai array pertanyaan dan jawaban ke kelas Group dan Child
        for (String group_name : pertanyaan) {
            Group gru = new Group();
            gru.setPertanyaan(group_name);

            ch_list = new ArrayList<Child>();
            for (; j < size; j++) {
                Child ch = new Child();
                ch.setJawaban(jawaban[j]);
                ch_list.add(ch);
            }
            gru.setItems(ch_list);
            list.add(gru);

            size = size + 1;
        }

        return list;
    }

}
