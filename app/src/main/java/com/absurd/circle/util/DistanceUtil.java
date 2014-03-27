package com.absurd.circle.util;

/**
 * Created by absurd on 14-3-27.
 */
public class DistanceUtil {

    public static double caculate(double lat1, double long1, double lat2, double long2)
    {
        double R = 6371; // -- Kilometers   --- Miles = 3958.7558657440545 // Radius of Earth
        double dLat = ToRad((lat2 - lat1));
        double dLon = ToRad((long2 - long1));
        lat1 = ToRad(lat1);
        lat2 = ToRad(lat2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    public static double ToRad(double value)
    {
        return value * Math.PI / 180;
    }
}
