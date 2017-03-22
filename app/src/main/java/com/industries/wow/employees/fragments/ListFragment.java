package com.industries.wow.employees.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.industries.wow.employees.R;
import com.industries.wow.employees.adapters.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class ListFragment<T> extends Fragment {

    PublishSubject<T> listItemClicksObservable = PublishSubject.create();

    List<T> data = new ArrayList<>();

    ListAdapter<T> adapter;
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListAdapter<>(data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listItemClicksObservable.onCompleted();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(listView != null && adapter != null){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    listItemClicksObservable.onNext(adapter.getItem(i));
                }
            });
        }
    }

    public void setData(@NonNull List<T> data){
        this.data = data;
        if(adapter != null){
            adapter.setData(data);
        }
    }

    public Observable<T> getListItemClicksObservable(){
        return listItemClicksObservable;
    }
}
