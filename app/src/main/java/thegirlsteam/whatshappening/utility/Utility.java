package thegirlsteam.whatshappening.utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.cos;

public class Utility {

    // To calcualte the distance
    public static double calculateDistance(double lat1, double lat2, double lon1,double lon2) {

        double p = 0.017453292519943295;
        double a = 0.5 - cos((lat2 - lat1) * p) / 2 + cos(lat1 * p) * cos(lat2 * p) * (1 - cos((lon2 - lon1) * p)) / 2;
        return 12742 * Math.asin(Math.sqrt(a));
    }


    // To find the time left for an event to begin
    public static long timeToStart(String eventTime) throws ParseException {
        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
        Calendar cal = Calendar.getInstance();
        System.out.println( formatDate.format(cal.getTime()) );
        Date timeNow = formatDate.parse(formatDate.format(cal.getTime()));
        Date timeEvent = formatDate.parse(eventTime);
        long difference = timeEvent.getTime() - timeNow.getTime();
        System.out.println(TimeUnit.MILLISECONDS.toMinutes(difference));
        return Long.parseLong(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(difference)));
    }
}
