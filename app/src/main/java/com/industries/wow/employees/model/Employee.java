package com.industries.wow.employees.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.industries.wow.employees.utils.DateParser;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@DatabaseTable(tableName = "employees")
public class Employee implements NamableObject, Serializable{

    public static final String ID_FIELD_NAME = "employee_id";

    private static final String[] datePatterns = new String[]{"yyyy-MM-dd", "dd-MM-yyyy"};

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField
    @SerializedName("f_name")
    private String name;
    @DatabaseField
    @SerializedName("l_name")
    private String lastName;
    @DatabaseField
    @SerializedName("birthday")
    private String birthday;
    @DatabaseField
    @SerializedName("avatr_url")
    private String avatarUrl;
    @SerializedName("specialty")
    private List<Specialty> specialtyList;

    public Employee(){}

    public String getName() {
        return StringUtils.capitalize(name.toLowerCase()) + " "
                + StringUtils.capitalize(lastName.toLowerCase());
    }

    public String getDateOfBirth(){
        if(!TextUtils.isEmpty(birthday)){
            String date = DateParser.parseDate(birthday);
            if(!TextUtils.isEmpty(date)){
                return date;
            }  else {
                return "-";
            }
        } else {
            return "-";
        }
    }

    public String getAge(){
        String dateOfBirth = getDateOfBirth();
        if(!"-".equals(dateOfBirth)){
            try{
                Date date = sdf.parse(dateOfBirth);
                return DateParser.getAge(date);
            }catch (Exception e){
                return dateOfBirth;
            }
        } else {
            return dateOfBirth;
        }
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setSpecialtyList(List<Specialty> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public List<Specialty> getSpecialtyList() {
        return specialtyList;
    }
}
