package com.industries.wow.employees.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.industries.wow.employees.model.Employee;
import com.industries.wow.employees.model.NamableObject;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter<T> extends BaseAdapter{

    private List<T> data = new ArrayList<>();

    public ListAdapter (List<T> data){
        this.data = data;
    }

    public void setData(@NonNull List<T> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            if(getItem(i) != null && getItem(i) instanceof Employee){
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(android.R.layout.simple_list_item_2, viewGroup, false);
            } else {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            }
        }
        TextView firstTextView = (TextView) view.findViewById(android.R.id.text1);
        if(getItem(i) != null){
            if(getItem(i) instanceof NamableObject){
                firstTextView.setText(((NamableObject)getItem(i)).getName());
            }
            if(getItem(i) instanceof Employee){
                TextView secondTextView = (TextView) view.findViewById(android.R.id.text2);
                secondTextView.setText(((Employee) getItem(i)).getAge());
            }
        }
        return view;
    }
}
