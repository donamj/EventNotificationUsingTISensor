package thegirlsteam.whatshappening.tabs;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import thegirlsteam.whatshappening.R;
import thegirlsteam.whatshappening.adapters.EventListAdapter;
import thegirlsteam.whatshappening.utility.Utility;


/**
 * Created by Dona Maria on 11/26/2016.
 * Manages the event tab
 */

public class EventsTab extends Fragment{


    private ListView eventListView;
    private TextView noEvents;
    static final List<Event> events = new ArrayList<>();
    static EventListAdapter eventAdapter = null;
    //URL to parse the information
    private String url ="???Path to json file with class schedule...output.json in my project";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.tab_events, container, false);

        super.onCreate(savedInstanceState);
        eventAdapter = new EventListAdapter(getActivity().getApplicationContext(),R.layout.event_list_layout,events);

        eventListView = (ListView) rootView.findViewById(R.id.eventlist);
        eventListView.setAdapter(eventAdapter);

        final String beaconUrl = TabbedActivity.getBeaconUrl();

        // To fetch the information from URL
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject obj,sensorObj,sensorObj2 = null;
                        JSONArray data = null,sensorArr = null;
                        String sensorLat = null;
                        String sensorLon = null;
                        try {
                            obj = new JSONObject(response);
                            data = obj.getJSONArray("events");
                            sensorObj = obj.getJSONObject("sensor");
                            System.out.println("Beacon object : " + sensorObj);
                            sensorArr = sensorObj.getJSONArray(beaconUrl);
                            if(sensorArr!=null)
                                sensorObj2 = sensorArr.getJSONObject(0);
                           // System.out.println("Beacon array : " + sensorArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < data.length(); i++) {

                            try {
                                // Getting information about an event
                                JSONObject jsonObject = data.getJSONObject(i);
                                String eventName = jsonObject.getString("Event");
                                String eventVenue = jsonObject.getString("Venue");
                                String eventTime = jsonObject.getString("Time");
                                String eventType = jsonObject.getString("Type");
                                String eventLat = jsonObject.getString("Latitude");
                                String eventLon = jsonObject.getString("Longitude");
                                String imageUrl = jsonObject.getString("Image");

                                Event temp = new Event();

                                // Case when the event venue information is not available
                                if(sensorArr!=null)
                                {
                                    sensorLat = sensorObj2.getString("latitude");
                                    sensorLon = sensorObj2.getString("longitude");
                                    double dist = Utility.calculateDistance(Double.parseDouble(sensorLat),Double.parseDouble(eventLat),Double.parseDouble(sensorLon),Double.parseDouble(eventLon));
                                    double eventDistance = Math.round(dist* 100) / 100;

                                    temp.setDistance(eventDistance);
                                }
                                else
                                {
                                    temp.setDistance(0);
                                }

                                // Calculating the time for event ot start
                                long timeToEvent = 0;
                                try {
                                    timeToEvent = Utility.timeToStart(eventTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                if(timeToEvent >30) {
                                    temp.setEventLat(eventLat);
                                    temp.setEventLon(eventLon);
                                    temp.setEventName(eventName);
                                    temp.setEventTime(eventTime);
                                    temp.setEventVenue(eventVenue);
                                    temp.setEventType(eventType);
                                    temp.setTimeToEvent(timeToEvent);
                                    temp.setImageUrl(imageUrl);

                                    events.add(temp);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }


                        if(events.size()==0)
                        {
                            noEvents = (TextView) rootView.findViewById(R.id.no_events);
                            noEvents.setText("No events!");
                        }
                        else
                        {
                            noEvents = (TextView) rootView.findViewById(R.id.no_events);
                            noEvents.setText("");
                        }
                        eventAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Event selectedEvent = eventAdapter.getItem(position);
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(selectedEvent.getEventName());
                String message =null;

                //Load image
/*                ImageView image = new ImageView(getActivity());
                URL urlImage = null;
                Bitmap bmp= null;

                try {
                    urlImage = new URL(selectedEvent.getImageUrl());
                    InputStream in = urlImage.openStream();
                    bmp =  BitmapFactory.decodeStream(in);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(bmp);
*/
                if(selectedEvent.getDistance()!=0)
                {
                    if(selectedEvent.getTimeToEvent() > 0)
                        message = "Event starts in "+selectedEvent.getTimeToEvent()+" minutes.\nDistance from you : " + selectedEvent.getDistance() + " miles\n\n";
                    else
                        message = "Event started "+((-1)*selectedEvent.getTimeToEvent())+" minutes ago.\nDistance from you : " + selectedEvent.getDistance() + " miles\n\n";
                }
                else
                {
                    if(selectedEvent.getTimeToEvent() > 0)
                        message = "Event starts in "+selectedEvent.getTimeToEvent()+" minutes.\n";
                    else
                        message = "Event started "+((-1)*selectedEvent.getTimeToEvent())+" minutes ago.\n";
                    message += "Distance information is not available\n\n";
                }
                alertDialog.setMessage(message + "Time : " +selectedEvent.getEventTime() + "\n" +"Venue : "+selectedEvent.getEventVenue() + "\n" );
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                //alertDialog.setView(image);
                alertDialog.show();

            }
        });

        setRetainInstance(true);




        return rootView;
    }

    public static void sortList(final int item, String order){

        Comparator<Event> comparator = new Comparator<Event>(){

            @Override
            public int compare(Event e1, Event e2) {
                if (item == 1)
                    return Double.compare(e1.getDistance() , e2.getDistance());
                else
                    return Double.compare(e1.getTimeToEvent() , e2.getTimeToEvent());
            }

        };

        if(order.equals("asc")) {
            Collections.sort(events, comparator);
        }
        else
        {
            Collections.sort(events, Collections.reverseOrder(comparator));
        }

        eventAdapter.notifyDataSetChanged();

    }






}
