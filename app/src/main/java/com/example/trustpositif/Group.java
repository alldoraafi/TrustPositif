package com.example.trustpositif;

/**
 * Created by Reza on 30/08/2017.
 */

import java.util.ArrayList;

public class Group {

    private String pertanyaan;
    private ArrayList<Child> Items;

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> Items) {
        this.Items = Items;
    }

}