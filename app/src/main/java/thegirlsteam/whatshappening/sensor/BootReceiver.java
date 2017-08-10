package thegirlsteam.whatshappening.sensor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Dona Maria on 12/2/2016.
 * To check for beacons in the background when the application is minimized.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops", "Ohhhh");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MonitorService.class);
            context.startService(serviceIntent);
        }
    }
}
