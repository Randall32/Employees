package com.industries.wow.employees.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.industries.wow.employees.model.Response;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ApiFactory {

    private static final String LOG_TAG = ApiFactory.class.getSimpleName();

    private static final String BASE_URL = "http://65apps.com";

    private static final int TIMEOUT = 30;

    private static Observable<Response> observableRetrofit;
    private static BehaviorSubject<Response> observableResponse;
    private static Subscription subscription;

    private ApiFactory(){}

    public static void init(){
        Log.d(LOG_TAG, "init");

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Gson gson = new GsonBuilder().create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        observableRetrofit = apiService.data();
    }

    private static void resetObservable(){
        observableResponse = BehaviorSubject.create();

        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = observableRetrofit.subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                observableResponse.onError(e);
            }

            @Override
            public void onNext(Response response) {
                observableResponse.onNext(response);
            }
        });
    }

    public static Observable<Response> getResponseObservable(){
        if(observableResponse == null){
            resetObservable();
        }
        return observableResponse;
    }
}
