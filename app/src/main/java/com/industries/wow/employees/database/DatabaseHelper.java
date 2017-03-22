package com.industries.wow.employees.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.industries.wow.employees.model.Employee;
import com.industries.wow.employees.model.Specialty;
import com.industries.wow.employees.model.SpecialtyEmployee;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOG_TAG = "database";
    private static final String NAME = "database.db";

    private RuntimeExceptionDao<Employee, Integer> employeeDao;
    private RuntimeExceptionDao<Specialty, Integer> specialtyDao;
    private RuntimeExceptionDao<SpecialtyEmployee, Integer> linksDao;

    private static final int CURRENT_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, CURRENT_VERSION);

        employeeDao = getRuntimeExceptionDao(Employee.class);
        specialtyDao = getRuntimeExceptionDao(Specialty.class);
        linksDao = getRuntimeExceptionDao(SpecialtyEmployee.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.d(LOG_TAG, "onCreate");
        try {
            TableUtils.createTableIfNotExists(connectionSource, Employee.class);
            TableUtils.createTableIfNotExists(connectionSource, Specialty.class);
            TableUtils.createTableIfNotExists(connectionSource, SpecialtyEmployee.class);
        } catch (SQLException e) {
            Log.d(LOG_TAG, "Error on onCreate: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public void saveEmployees(List<Employee> employees){
        clearDB();
        for(Employee employee : employees){
            Log.d(LOG_TAG, "Create employee: " + employeeDao.create(employee));
            if(employee.getSpecialtyList() != null){
                for(Specialty specialty : employee.getSpecialtyList()){
                    Log.d(LOG_TAG, "Is created specialty: "
                            + specialtyDao.createOrUpdate(specialty).isCreated());
                    SpecialtyEmployee link = new SpecialtyEmployee(specialty, employee);
                    Log.d(LOG_TAG, "Link specialty and employee: " + linksDao.create(link));
                }
            }
        }
    }

    public List<Specialty> getSpecialties(){
        return specialtyDao.queryForAll();
    }

    public List<Employee> getEmployeesBySpecialty(@NonNull Specialty specialty){
        List<Employee> result;
        try {
            QueryBuilder<SpecialtyEmployee, Integer> queryBuilder = linksDao.queryBuilder();
            queryBuilder.selectColumns(Employee.ID_FIELD_NAME);
            queryBuilder.where().eq(Specialty.ID_FIELD_NAME, specialty.getId());
            result = employeeDao.queryBuilder().where()
                    .in(Employee.ID_FIELD_NAME, queryBuilder).query();
            for(Employee e : result){
                List<Specialty> specialtyList = new ArrayList<>();
                specialtyList.add(specialty);
                e.setSpecialtyList(specialtyList);
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage());
            result = new ArrayList<>();
        }
        return result;
    }

    private void clearDB(){
        List<Employee> employees = employeeDao.queryForAll();
        if(employees != null){
            employeeDao.delete(employees);
        }
        List<Specialty> specialties = specialtyDao.queryForAll();
        if(specialties != null){
            specialtyDao.delete(specialties);
        }
        List<SpecialtyEmployee> links = linksDao.queryForAll();
        if(links != null){
            linksDao.delete(links);
        }
    }
}
