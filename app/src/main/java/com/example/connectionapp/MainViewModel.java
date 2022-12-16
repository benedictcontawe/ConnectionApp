package com.example.connectionapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    MutableLiveData<Integer> progressData = new MutableLiveData<Integer>();

    //private UnicastSubject<String> subject = UnicastSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();
    //private Observable observable;
    private final PublishSubject<String> publishSubject = PublishSubject.create();

    public MainViewModel() {
        Log.d(TAG, "MainViewModel constructor");
        //https://medium.com/@nazarivanchuk/types-of-subjects-in-rxjava-96f3a0c068e4
        publishSubject.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        /*
        observable = Observable.empty();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
        disposable.add(
                subject.subscribe()
        );
        */
    }
    /*
    private <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    */

    public void addDisposable(Disposable disposable) {
        this.disposable.add(disposable);
    }

    public PublishSubject<String> observeData() {
        return publishSubject;
    }

    public void setData(String data) {
        this.publishSubject.onNext(data);
    }

    public LiveData<Integer> getProgressData() {
        return progressData;
    }

    public void setProgressData(Integer progressData) {
        this.progressData.setValue(progressData);
    }
    /*
    private Observable<String> getObservable() {
        return Observable.just("", data)
                .compose(applySchedulers())
                .subscribe(getObserver());
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Throwable {
                        return false;
                    }
                })
                .distinct()
                .throttleFirst(5000L, TimeUnit.MILLISECONDS)
                .subscribe(getObserver());
                .subscribe(subject);
    }
    */
    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable _disposable) {
                Log.d(TAG, "onSubscribe(" + _disposable + ")");
                disposable.add(_disposable);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext(" + s + ")");
            }

            @Override
            public void onError(@NonNull Throwable error) {
                Log.e(TAG, "onError(" + error + ")");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        }; //return subject;
    }

    public void onPause() {
        disposable.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "MainViewModel onCleared");
        //publishSubject.onComplete();
        disposable.dispose();
    }
}