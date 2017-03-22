package com.industries.wow.employees.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

@DatabaseTable(tableName = "specialty")
public class Specialty implements NamableObject {

    public static final String ID_FIELD_NAME = "specialty_id";

    @DatabaseField(id = true, columnName = ID_FIELD_NAME)
    @SerializedName("specialty_id")
    private int id;
    @DatabaseField
    @SerializedName("name")
    private String name;

    public Specialty() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return StringUtils.capitalize(name.toLowerCase());
    }
}
