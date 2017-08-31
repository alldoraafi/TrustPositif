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

public class faqActivity extends Activity {
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
        ExpAdapter = new ExpandListAdapter(faqActivity.this, ExpListItems);
        ExpandList.setAdapter(ExpAdapter);
    }

    //set group item
    public ArrayList<Group> SetStandardGroups() {

        String group_names[] = {"gini boleh gak?", "gitu boleh gak?", "kalo ngono?"};

        String country_names[] = {"gak boleh", "eh boleh", "mantab abis"};

        int Images[] = {R.drawable.kat, R.drawable.kat2,
                R.drawable.kat3};

        ArrayList<Group> list = new ArrayList<Group>();

        ArrayList<Child> ch_list;

        int size = 1;
        int j = 0;

        for (String group_name : group_names) {
            Group gru = new Group();
            gru.setName(group_name);

            ch_list = new ArrayList<Child>();
            for (; j < size; j++) {
                Child ch = new Child();
                ch.setName(country_names[j]);
                //ch.setImage(Images[j]);
                ch_list.add(ch);
            }
            gru.setItems(ch_list);
            list.add(gru);

            size = size + 1;
        }

        return list;
    }

}
