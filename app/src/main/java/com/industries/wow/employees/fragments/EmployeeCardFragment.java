package com.industries.wow.employees.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.industries.wow.employees.R;
import com.industries.wow.employees.model.Employee;
import com.squareup.picasso.Picasso;

public class EmployeeCardFragment extends Fragment implements TitledFragment {

    private Employee employee;

    public static EmployeeCardFragment newInstance(@NonNull Employee employee) {
        Bundle args = new Bundle();
        args.putSerializable("employee", employee);
        EmployeeCardFragment fragment = new EmployeeCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employee = (Employee) getArguments().getSerializable("employee");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_employee_card, container, false);
        ImageView avatar = (ImageView) v.findViewById(R.id.avatar);
        TextView name = (TextView) v.findViewById(R.id.name_field);
        TextView dateOfBirth = (TextView) v.findViewById(R.id.date_of_birth_field);
        TextView specialty = (TextView) v.findViewById(R.id.specialty_field);

        if (employee != null) {
            if (employee.getAvatarUrl() != null && !TextUtils.isEmpty(employee.getAvatarUrl())) {
                if (getActivity() != null && isAdded()) {
                    Picasso.with(getActivity())
                            .load(employee.getAvatarUrl())
                            .centerCrop()
                            .resizeDimen(R.dimen.avatar_size, R.dimen.avatar_size)
                            .placeholder(R.drawable.avatar_placeholder)
                            .into(avatar);
                }
            }
            name.setText(employee.getName());
            if("-".equals(employee.getDateOfBirth())){
                dateOfBirth.setText(employee.getDateOfBirth());
            } else {
                dateOfBirth.setText(String.format(getString(R.string.date_template),
                        employee.getDateOfBirth(), employee.getAge()));
            }
            if(employee.getSpecialtyList() != null && employee.getSpecialtyList().size() > 0){
                specialty.setText(employee.getSpecialtyList().get(0).getName());
            }
        }
        return v;
    }

    @Override
    public String getTitle() {
        if (employee != null) {
            return employee.getName();
        } else {
            return null;
        }
    }
}
