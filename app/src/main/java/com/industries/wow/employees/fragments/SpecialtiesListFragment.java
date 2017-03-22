package com.industries.wow.employees.fragments;


import com.industries.wow.employees.R;
import com.industries.wow.employees.model.Specialty;


public class SpecialtiesListFragment extends ListFragment<Specialty> implements TitledFragment{

    public static SpecialtiesListFragment newInstance(){
        return new SpecialtiesListFragment();
    }

    @Override
    public String getTitle() {
        return getString(R.string.specialty_title);
    }
}
