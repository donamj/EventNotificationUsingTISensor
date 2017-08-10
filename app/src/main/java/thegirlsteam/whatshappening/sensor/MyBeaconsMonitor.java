package thegirlsteam.whatshappening.sensor;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import thegirlsteam.whatshappening.sensor.MonitorService;

import io.onebeacon.api.Beacon;
import io.onebeacon.api.BeaconsMonitor;
import io.onebeacon.api.Rangeable;

/** Example subclass for a BeaconsMonitor **/
class MyBeaconsMonitor extends BeaconsMonitor {
    MainActivity mainActivity;
    public MyBeaconsMonitor(Context context, MainActivity mainActivity) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onBeaconRemoved(Beacon beacon) {
        super.onBeaconRemoved(beacon);
    }


    @Override
    protected void onBeaconChangedRange(Rangeable rangeable) {
        super.onBeaconChangedRange(rangeable);
        log("something has been changed");
        log(String.format("Range changed to %s for %s", rangeable.getRange(), rangeable));
        mainActivity.updateRange((Beacon)rangeable,rangeable.getRange());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onBeaconAdded(Beacon beacon) {
        super.onBeaconAdded(beacon);
        log("Name: "+beacon.getName()+" pretty address: "+beacon.getPrettyAddress()+" address: "+beacon.getAddress()+" data "+beacon.getData());
        mainActivity.addBeacon(beacon);
    }

    // checkout the other available callbacks in the BeaconsManager base class

    private void log(String msg) {
        Log.d("MonitorService", msg);
    }
}