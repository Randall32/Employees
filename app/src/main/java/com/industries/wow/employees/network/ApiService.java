package com.industries.wow.employees.network;

import com.industries.wow.employees.model.Response;

import retrofit2.http.GET;
import rx.Observable;

interface ApiService {

    @GET("/images/testTask.json")
    Observable<Response> data();
}
