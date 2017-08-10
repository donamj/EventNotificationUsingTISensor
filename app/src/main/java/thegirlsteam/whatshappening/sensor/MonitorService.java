package thegirlsteam.whatshappening.sensor;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import io.onebeacon.api.Beacon;
import io.onebeacon.api.BeaconsMonitor;
import thegirlsteam.whatshappening.R;

/**
 * Monitor Service
 */
public class MonitorService extends Service {

    MainActivity mainActivity;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    /**
     * Simple binder that returns the in-process service instance
     */
    class LocalServiceBinder extends Binder {
        MonitorService getService() {
            return MonitorService.this;
        }
    }

    private final LocalServiceBinder mBinder = new LocalServiceBinder();

    private boolean mServiceStarted = false;
    private BeaconsMonitor mBeaconsMonitor = null;

    @Override
    public IBinder onBind(Intent intent) {
        log("onBind");
        //notifyii();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("onStartCommand");

        if (!mServiceStarted) {
            if (null == mBeaconsMonitor) {
                // create and start a new beacons monitor, subclassing a few callbacks
                mBeaconsMonitor = new MyBeaconsMonitor(this, mainActivity);
            }
            mServiceStarted = true;
        }

        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;
    }

    @Override
    public void onDestroy() {
        log("onDestroy");
       Intent intent = new Intent("thegirlsteam.whatshappening.sensor");
        sendBroadcast(intent);
        // unregister from beacons API
        if (null != mBeaconsMonitor) {
            mBeaconsMonitor.close();
            mBeaconsMonitor = null;
        }

        mServiceStarted = false;

        super.onDestroy();
    }

    /**
     * Return all the known beacons, for example to be bound to an adapter for displaying them
     **/
    public Collection<Beacon> getBeacons() {
        return mBeaconsMonitor.getBeacons();
    }

    private void log(String msg) {
        Log.d("MonitorService", msg);
    }


        @Override
        public void onCreate() {
            super.onCreate();
        }

}