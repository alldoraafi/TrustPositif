package com.example.trustpositif;

/**
 * Created by Reza on 30/08/2017.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;

    public ExpandListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    //untuk mengambil nilai dari variabel jawaban pada kelas child pada posisi yang dipilih
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    //untuk mengambil id posisi dari jawaban yang dipilih
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //untuk memuat layout untuk menampilkan jawaban
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Child child = (Child) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.teksJawaban);

        //set teks jawaban pada textview yang ada pada halaman F.A.Q
        tv.setText(child.getJawaban().toString());

        return convertView;
    }

    // untuk mengambil nilai jumlah dari jawaban yang ada
    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    //untuk mengambil nilai dari variabel pertanyaan pada kelas group  pada posisi yang dipilih
    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    // untuk mengambil nilai jumlah dari pertanyaan yang ada
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    //untuk mengambil id posisi dari pertanyaan yang dipilih
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //memuat layout untuk menampilkan pertanyaan
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.group_name);
        tv.setText(group.getPertanyaan());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}