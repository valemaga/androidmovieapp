package com.android.movieapp;

import java.util.concurrent.Executor;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sIntance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    public AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }


}
