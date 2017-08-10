package thegirlsteam.whatshappening.tabs;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import thegirlsteam.whatshappening.R;
import thegirlsteam.whatshappening.adapters.LectureListAdapter;
import thegirlsteam.whatshappening.utility.Utility;


/**
 * Created by Dona Maria on 11/26/2016.
 * Fragment that manages tab for lectures
 */

public class LectureTab extends Fragment{

    private ListView lectureListView;
    final static List<Lecture> lectures = new ArrayList<Lecture>();
    static LectureListAdapter lectureListAdapter;
    private String url ="https://mavspace.uta.edu/people/d/dx/dxj2757/output.json";
    private TextView noLecture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab_lectures, container, false);

        super.onCreate(savedInstanceState);
        //rootView.setContentView(R.layout.activity_tabbed);

        lectureListView = (ListView) rootView.findViewById(R.id.lecturelist);
        lectureListAdapter = new LectureListAdapter(getActivity().getApplicationContext(),R.layout.lecture_list_layout,lectures);
        lectureListView.setAdapter(lectureListAdapter);
        final String beaconUrl = TabbedActivity.getBeaconUrl();

        // Getting the lecture information from URL in the  form of JSON data and parsing it.
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject obj = null,sensorObj,sensorObj2 = null;
                        JSONArray data = null,sensorArr=null;
                        String sensorLat = null;
                        String sensorLon = null;

                        try {
                            obj = new JSONObject(response);
                            data = obj.getJSONArray("class");
                            sensorObj = obj.getJSONObject("sensor");
                            System.out.println("Beacon object : " + sensorObj);
                            sensorArr = sensorObj.getJSONArray(beaconUrl);
                            if(sensorArr!=null)
                                sensorObj2 = sensorArr.getJSONObject(0);

                            System.out.print(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < data.length(); i++) {

                            try {
                                JSONObject jsonObject = data.getJSONObject(i);
                                String classroom = jsonObject.getString("Classroom");
                                String course = jsonObject.getString("Course");
                                String professor = jsonObject.getString("Professor");
                                String timing = jsonObject.getString("Timing");
                                String startTime = jsonObject.getString("Start Time");
                                String section = jsonObject.getString("Section");
                                String lat = jsonObject.getString("Latitude");
                                String lon = jsonObject.getString("Longitude");
                                //String imageUrl = jsonObject.getString("Image");

                                Lecture temp = new Lecture();

                                // Calculating the  distance
                                if(sensorArr!=null && !(lat.equals("")))
                                {
                                    sensorLat = sensorObj2.getString("latitude");
                                    sensorLon = sensorObj2.getString("longitude");
                                    double dist = Utility.calculateDistance(Double.parseDouble(sensorLat),Double.parseDouble(lat),Double.parseDouble(sensorLon),Double.parseDouble(lon));
                                    double eventDistance = Math.round(dist* 100) / 100;
                                    temp.setDistance(eventDistance);
                                }
                                else
                                {
                                    temp.setDistance(0);
                                }

                                // Setting the time left for the lecture to start.
                                long timeToEvent = 0;
                                try {
                                    timeToEvent = Utility.timeToStart(startTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // To avoid display the lectures that are already done
                                if(timeToEvent > 30) {
                                    temp.setClassroom(classroom);
                                    temp.setCourse(course);
                                    temp.setProfessor(professor);
                                    temp.setSection(section);
                                    temp.setSensorLat(sensorLat);
                                    temp.setSensorLon(sensorLon);
                                    temp.setTimeToEvent(timeToEvent);
                                    temp.setStartTime(startTime);
                                    temp.setTiming(timing);
                                    temp.setLat(lat);
                                    temp.setLon(lon);
                                    //temp.setImageUrl(imageUrl);

                                    lectures.add(temp);
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        if(lectures.size()==0)
                        {
                            noLecture = (TextView) rootView.findViewById(R.id.no_lectures);
                            noLecture.setText("No lectures!");
                        }
                        else
                        {
                            noLecture = (TextView) rootView.findViewById(R.id.no_lectures);
                            noLecture.setText("");
                        }
                        lectureListAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);


        lectureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Lecture selectedLecture = lectureListAdapter.getItem(position);
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                //TextView textView = new TextView(getActivity());
                //textView.setText(selectedLecture.getCourse()+" - " +selectedLecture.getSection());
                alertDialog.setTitle(selectedLecture.getCourse().split("-")[0] +"- " +selectedLecture.getSection());
                //alertDialog.setCustomTitle(textView);
                String message =null;
                if(selectedLecture.getDistance()!=0)
                {
                    if(selectedLecture.getTimeToEvent() > 0)
                        message = "Lecture starts in "+selectedLecture.getTimeToEvent()+" minutes.\nDistance from you : "
                                + selectedLecture.getDistance() + " miles\n\n";
                    else
                        message = "Lecture started "+((-1)*selectedLecture.getTimeToEvent())+" minutes ago.\n" +
                                "Distance from you : " + selectedLecture.getDistance() + "\n\n";
                }
                else
                {
                    if(selectedLecture.getTimeToEvent() > 0)
                        message = "Lecture starts in "+selectedLecture.getTimeToEvent()+" minutes.\n";
                    else
                        message = "Lecture started "+((-1)*selectedLecture.getTimeToEvent())+" minutes ago.\n";
                    message += "Distance is information not available\n\n";
                }

                alertDialog.setMessage(message + selectedLecture.getCourse().split("-  ")[1] + "\n" +selectedLecture.getProfessor() + "\n" +selectedLecture.getClassroom() + "\n" + selectedLecture.getStartTime()+"\n");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                positiveButtonLL.gravity = Gravity.CENTER;
                positiveButton.setLayoutParams(positiveButtonLL);

            }
        });


        return rootView;
    }


    // SOrting the list

    public static void sortList(final int item, String order){

        Comparator<Lecture> comparator = new Comparator<Lecture>() {


                @Override

                public int compare(Lecture e1, Lecture e2) {
                    if (item == 1)
                        return Double.compare(e1.getDistance() , e2.getDistance());
                    else
                        return Double.compare(e1.getTimeToEvent() , e2.getTimeToEvent());
                }

            };
        if(order.equals("asc")) {
            Collections.sort(lectures, comparator);
        }
        else
        {
            Collections.sort(lectures, Collections.reverseOrder(comparator));
        }

        lectureListAdapter.notifyDataSetChanged();

    }
}
