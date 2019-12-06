package com.example.connected.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;


public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    private int mVisibleCount;
    private boolean mInBackground;
    private FirebaseAuth mAuth;

    @Override public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override public void onActivityStarted(Activity activity) {
                mVisibleCount++;
                if (mInBackground && mVisibleCount > 0) {
                    mInBackground = false;
                }
            }

            @Override public void onActivityResumed(Activity activity) {
            }

            @Override public void onActivityPaused(Activity activity) {
            }

            @Override public void onActivityStopped(Activity activity) {
                mVisibleCount--;
                if (mVisibleCount == 0) {
                    if (activity.isFinishing()) {
                        Log.i(TAG, "App is finishing");
                    } else {
                        mInBackground = true;
                        Log.i(TAG, "App in background");
                    }
                }
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override public void onActivityDestroyed(Activity activity) {
//                mAuth = FirebaseAuth.getInstance();
//                mAuth.signOut();
            }
        });
    }

    public boolean isAppInBackground() {
        return mInBackground;
    }

    public boolean isAppVisible() {
        return mVisibleCount > 0;
    }

    public int getVisibleCount() {
        return mVisibleCount;
    }
}