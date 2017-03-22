package com.industries.wow.employees;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.industries.wow.employees.database.DatabaseHelper;
import com.industries.wow.employees.fragments.EmployeeCardFragment;
import com.industries.wow.employees.fragments.EmployeeListFragment;
import com.industries.wow.employees.fragments.SpecialtiesListFragment;
import com.industries.wow.employees.fragments.TitledFragment;
import com.industries.wow.employees.model.Employee;
import com.industries.wow.employees.model.Response;
import com.industries.wow.employees.model.Specialty;
import com.industries.wow.employees.network.ApiFactory;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DatabaseHelper dbHelper;

    private Subscription subscription;

    private Toolbar toolbar;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setSupportActionBar(toolbar);

        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        ApiFactory.init();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getCurrentFragment() != null
                                && getCurrentFragment() instanceof TitledFragment) {
                            setTitle(((TitledFragment) getCurrentFragment()).getTitle());
                        }
                        try{
                            if(getSupportActionBar() != null){
                                getSupportActionBar().setDisplayHomeAsUpEnabled(
                                        getSupportFragmentManager().getBackStackEntryCount() > 1
                                );
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });


        SpecialtiesListFragment fragment = SpecialtiesListFragment.newInstance();
        if(openFragment(fragment, true)){
            handleSpecialtyListItemClicks(fragment);
        }

        showLoadingIndicator(true);
        getEmployees();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
    }

    private void setTitle(String title) {
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    private boolean openFragment(Fragment fragment, boolean addToBackStack) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (addToBackStack) {
                ft.addToBackStack(null);
            }
            ft.replace(R.id.container, fragment).commitAllowingStateLoss();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Fragment getCurrentFragment() {
        try {
            return getSupportFragmentManager().findFragmentById(R.id.container);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    private void showLoadingIndicator(boolean isShowing) {
        if (loadingIndicator != null) {
            if (isShowing) {
                loadingIndicator.setVisibility(View.VISIBLE);
            } else {
                loadingIndicator.setVisibility(View.GONE);
            }
        }
    }

    private void handleSpecialtyListItemClicks(SpecialtiesListFragment fragment){
        fragment.getListItemClicksObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<Specialty, List<Employee>>() {
                    @Override
                    public List<Employee> call(Specialty specialty) {
                        if (dbHelper != null) {
                            return dbHelper.getEmployeesBySpecialty(specialty);
                        } else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Employee>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "Unsubscribe on fragment list item clicks");
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO: 22.03.2017 error handler
                    }

                    @Override
                    public void onNext(List<Employee> employees) {
                        if (employees != null) {
                            EmployeeListFragment fragment = EmployeeListFragment.newInstance();
                            fragment.setData(employees);
                            openFragment(fragment, true);
                            handleEmployeeListItemClicks(fragment);
                        }
                    }
                });
    }

    private void handleEmployeeListItemClicks(EmployeeListFragment fragment){
        fragment.getListItemClicksObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Employee>() {

                    // TODO: 22.03.2017 move to abstract subscriber

                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "Unsubscribe on fragment list item clicks");
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO: 22.03.2017 error handler
                    }

                    @Override
                    public void onNext(Employee employee) {
                        if(employee != null){
                            openFragment(EmployeeCardFragment.newInstance(employee), true);
                        }
                    }
                });
    }

    private void getEmployees() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = ApiFactory.getResponseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<Response, List<Specialty>>() {
                    @Override
                    public List<Specialty> call(Response response) {
                        List<Employee> employees = response.getData();
                        dbHelper.saveEmployees(employees);
                        return dbHelper.getSpecialties();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Specialty>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "onError: " + e.getMessage());
                        showLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(List<Specialty> specialties) {
                        Log.d(LOG_TAG, "onNext");
                        showLoadingIndicator(false);
                        if (getCurrentFragment() != null
                                && getCurrentFragment() instanceof SpecialtiesListFragment) {
                            ((SpecialtiesListFragment) getCurrentFragment()).setData(specialties);
                        }
                    }
                });
    }
}
