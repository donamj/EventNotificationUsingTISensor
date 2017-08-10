package thegirlsteam.whatshappening.sensor;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.onebeacon.api.Beacon;
import io.onebeacon.api.OneBeacon;
import io.onebeacon.api.ScanStrategy;
import thegirlsteam.whatshappening.R;
import thegirlsteam.whatshappening.tabs.TabbedActivity;
import thegirlsteam.whatshappening.adapters.BeaconListAdapter;

public class MainActivity extends AppCompatActivity implements ServiceConnection,updateListviewCallback {

    private ListView lst_beacons;
    private TextView noBeacons;
    private Context context=this;


    private BeaconListAdapter beaconListAdapter;
    private MonitorService mService = null;

    private List<Beacon> beacons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To check whether bluetooth is turned on
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (! mBtAdapter.isEnabled()) {
            alertDialogBuilder.setMessage("Application needs permission to turn on bluetooth.")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            mBtAdapter.enable();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });;

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }


        if (!bindService(new Intent(this, MonitorService.class), this, BIND_AUTO_CREATE)) {
            setTitle("Bind failed! Manifest?");
        }

        lst_beacons = (ListView)findViewById(R.id.lst_beacons_list);
        beaconListAdapter = new BeaconListAdapter(this,R.layout.list_item_layout,beacons);
        lst_beacons.setAdapter(beaconListAdapter);


        // To display information when there are no beacons around
        if(beacons.size()==0)
        {
            noBeacons = (TextView) findViewById(R.id.no_beacon);
            noBeacons.setText("No beacons detected!");
        }

        lst_beacons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Beacon selectedBeacon = beaconListAdapter.getItem(position);
                System.out.println(selectedBeacon);
                Intent intent = new Intent(MainActivity.this, TabbedActivity.class);

                //Add your data to bundle
                intent.putExtra("beaconUrl", selectedBeacon.getData());
                System.out.println("Beacon data is "+selectedBeacon.getData());


                startActivity(intent);

            }
        });


    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = ((MonitorService.LocalServiceBinder) iBinder).getService();
        mService.setMainActivity(this);
        setTitle("What's Happening");
        //getActionBar().setIcon(R.mipmap.ic_launcher);

        // make the service to stick around by actually starting it
        startService(new Intent(this, MonitorService.class));
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
        setTitle("Service disconnected");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Activity is visible, scan with most reliable results
        OneBeacon.setScanStrategy(ScanStrategy.LOW_LATENCY);
    }

    @Override
    protected void onPause() {
        // Activity is not in foreground, make a trade-off between battery usage and scan latency
        OneBeacon.setScanStrategy(ScanStrategy.BALANCED);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Activity is gone, set scan mode to use lowest possible power usage
        OneBeacon.setScanStrategy(ScanStrategy.LOW_POWER);
        if (null != mService) {
            // optionally stop the service if running in background is not desired
//            stopService(new Intent(this, MonitorService.class));
            unbindService(this);
            mService = null;
        }
        super.onDestroy();
        stopService(new Intent(this, MonitorService.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void addBeacon(Beacon beacon) {

        noBeacons = (TextView) findViewById(R.id.no_beacon);
        noBeacons.setText("");
        System.out.println("BEACON DATA : "+beacon.getData());
        //Filtering beacons
        if(beacon.getData().contains("http://")) {
            beacons.add(beacon);
            notifyii();
        }
        //System.out.println("**********Beacon ADDED********");
        lst_beacons.setAdapter(beaconListAdapter);
        //Sorted based on RSSI value
        Comparator<Beacon> comparator = new Comparator<Beacon>(){

            @Override
            public int compare(Beacon e1, Beacon e2) {
                return Double.compare(e1.getAverageRssi() , e2.getAverageRssi());
            }

        };
        Collections.sort(beacons,Collections.reverseOrder(comparator));


        beaconListAdapter.notifyDataSetChanged();
    }

    // Removing beacon
    @Override
    public void removeBeacon(Beacon beacon) {
        int index = beacons.indexOf(beacon);
        if(index > -1)
            beacons.remove(index);
        if(beacons.size()==0)
        {
            noBeacons = (TextView) findViewById(R.id.no_beacon);
            noBeacons.setText("No gifts around you!");
        }


        //Sorting the new list
        Comparator<Beacon> comparator = new Comparator<Beacon>(){

            @Override
            public int compare(Beacon e1, Beacon e2) {
                return Double.compare(e1.getAverageRssi() , e2.getAverageRssi());
            }

        };
        Collections.sort(beacons,Collections.reverseOrder(comparator));

        lst_beacons.setAdapter(beaconListAdapter);
        beaconListAdapter.notifyDataSetChanged();
        System.out.println("**********Beacon Removed********");


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void updateRange(Beacon beacon, int newRange) {

        //Removing the beacon when it moves away
        if ((newRange==0)||(newRange>5))
            removeBeacon(beacon);
        else if(!beacons.contains(beacon))
        {
            addBeacon(beacon);
        }


        Comparator<Beacon> comparator = new Comparator<Beacon>(){

            @Override
            public int compare(Beacon e1, Beacon e2) {
                return Double.compare(e1.getAverageRssi() , e2.getAverageRssi());
            }

        };
        Collections.sort(beacons,Collections.reverseOrder(comparator));
        beaconListAdapter.notifyDataSetChanged();

    }


    // Creating notification when beacons are detected
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notifyii() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RSSPullService");

        Intent newIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,newIntent, 0);
        Context context = getApplicationContext();

        Notification.Builder builder;
        builder = new Notification.Builder(context)
                .setContentTitle("Beacon Found NearBy")
                .setContentText("See WhatsHappening")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        System.out.println("beacon detected");

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);


    }
}
