package com.example.connectionapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.UnicastSubject;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    MutableLiveData<String> data = new MutableLiveData<String>();
    MutableLiveData<Integer> progressData = new MutableLiveData<Integer>();
    //private UnicastSubject<String> subject = UnicastSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();
    //private Subscription subscription;
    //private Observable<String> locationObservable;

    public MainViewModel() {
        Log.d(TAG, "MainViewModel constructor");
    }

    public LiveData<String> getData() {
        //return LiveDataReactiveStreams.fromPublisher();
        return data;
    }

    public void setData(String data) {
        this.data.setValue(data);
        setObservable(data);
    }

    public LiveData<Integer> getProgressData() {
        return progressData;
    }

    public void setProgressData(Integer progressData) {
        this.progressData.setValue(progressData);
    }

    private void setObservable(String data) {
        Observable<String> observable = Observable.just("", data);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Throwable {
                        return false;
                    }
                })
              //.distinct()
              //.throttleFirst(5000L, TimeUnit.MILLISECONDS)
                .subscribe(getObserver()); //.subscribe(subject);
        //subject.subscribe(getObserver());
    }

    public Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe(" + d + ")");
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext(" + s + ")");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError(" + e + ")");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        }; //return subject;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear(); //disposable.dispose();
    }
}