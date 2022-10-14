package com.ttj.androidfirstintelliapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private List<Contact> contacts;
    private Context context;


    public MyListAdapter(Context context, List<Contact> contacts) {

        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.layout_contact,null);
        TextView txtName = view1.findViewById(R.id.txtName);
        TextView txtPhoneNum = view1.findViewById(R.id.txtPhoneNum);
        txtName.setText(contacts.get(i).getName());
        txtPhoneNum.setText(contacts.get(i).getPhoneNumber());
        return view1;
    }
}
