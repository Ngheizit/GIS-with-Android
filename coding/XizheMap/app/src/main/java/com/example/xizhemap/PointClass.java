package com.example.xizhemap;

public class PointClass {

    public double Lon;
    public double Lat;
    public PointClass(double lon, double lat){
        this.Lon = lon;
        this.Lat = lat;
    }

    private static final double EARTH_RADIUS  = 6378.137;

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(PointClass pt1, PointClass pt2){

        double a = rad(pt1.Lat) - rad(pt2.Lat);
        double b = rad(pt1.Lon) - rad(pt2.Lon);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad(pt1.Lat)) * Math.cos(rad(pt2.Lat)) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

}
