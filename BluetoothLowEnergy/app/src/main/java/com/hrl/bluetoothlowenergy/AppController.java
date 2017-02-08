package com.hrl.bluetoothlowenergy;

import android.app.Application;

/**
 * Created by sharukhhasan on 2/8/17.
 */
public class AppController extends Application {

    //public static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build());*/

        //if (LeakCanary.isInAnalyzerProcess(this)) {
        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.
        //return;
        //}
        //refWatcher = LeakCanary.install(this);
    }
}
