package com.industries.wow.employees.fragments;

import com.industries.wow.employees.R;
import com.industries.wow.employees.model.Employee;

public class EmployeeListFragment extends ListFragment<Employee> implements TitledFragment{

    public static EmployeeListFragment newInstance(){
        return new EmployeeListFragment();
    }

    @Override
    public String getTitle() {
        return getString(R.string.employee_title);
    }
}
