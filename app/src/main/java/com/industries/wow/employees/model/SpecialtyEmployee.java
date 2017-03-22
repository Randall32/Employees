package com.industries.wow.employees.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "specialtyEmployee")
public class SpecialtyEmployee {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignColumnName = Specialty.ID_FIELD_NAME,
            foreignAutoRefresh = true, columnName = Specialty.ID_FIELD_NAME)
    private Specialty specialty;

    @DatabaseField(foreign = true, foreignColumnName = Employee.ID_FIELD_NAME,
            foreignAutoRefresh = true, columnName = Employee.ID_FIELD_NAME)
    private Employee employee;

    public SpecialtyEmployee(){}

    public SpecialtyEmployee(Specialty specialty, Employee employee){
        this.specialty = specialty;
        this.employee = employee;
    }
}
