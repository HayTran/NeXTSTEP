package com.a20170208.tranvanhay.respberry3.UtilitiesClass;

import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Tran Van Hay on 3/27/2017.
 */

public class TimeAndDate {
    private static final String TAG = TimeAndDate.class.getSimpleName();
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    public static String currentTime = "";
    // Instance for a handler
    private Handler mHandler = new Handler();
    private Runnable fetchCurrentTime = new Runnable() {
        @Override
        public void run() {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss --- dd/MM/yyyy");
            format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
            mData.child("At Current").setValue(format.format(date));
            Log.d(TAG,"Send time and date to firebase");
            currentTime = format.format(date) + "";
            mHandler.postDelayed(fetchCurrentTime,1000);
        }
    };
    public void showCurrentTime(){
        mHandler.post(fetchCurrentTime);
    }
}
