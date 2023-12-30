//
// a set of useful coordinates calculations like distance between point, 
// bearing from point A to B, midpoint of A-B etc
//
// Copyright 2011, Nick Tsakonas
//
//   This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package ntsakonas.dxccbeampos;

/*
    Short path distance and bearing calculator
    (copied from my private library - to be replaced with a gradle dependency when the library is publicly available)
 */
public class DistanceCalculator {

    private static final double EARTH_RADIUS = 6371.0; // in km;
    // not part of my private library - added in this project
    //
    // arc length for 1 degree in kilometers, i.e. 60 Nautical Miles
    private static final double ONE_DEGREE_ARC_IN_KM = 111.2;


    //
    // calculates the distance in Km between 2 points.
    //
    public static double distanceFrom(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        double lat1 = Math.toRadians(fromLatitude);
        double lon1 = Math.toRadians(fromLongitude);
        double lat2 = Math.toRadians(toLatitude);
        double lon2 = Math.toRadians(toLongitude);


        double dlon = (lon2 - lon1);
        double dlat = (lat2 - lat1);

        double a = (Math.sin(dlat / 2)) * (Math.sin(dlat / 2))
                + (Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2));


        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double km = EARTH_RADIUS * c;

        return km;
    }

    // calculate the long path distance in Km between 2 points.
    public static double longPathDistanceFrom(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        return longPathDistanceFromShortPath(distanceFrom(fromLatitude, fromLongitude, toLatitude, toLongitude));
    }

    public static double longPathDistanceFromShortPath(double shortPathDistance) {
        return (ONE_DEGREE_ARC_IN_KM * 360.0) - shortPathDistance;
    }

    //
    // calculates the bearing (in degrees) of a distant point from a reference point 
    //
    public static double bearingTo(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {

        double lat1 = Math.toRadians(fromLatitude);
        double lon1 = Math.toRadians(fromLongitude);
        double lat2 = Math.toRadians(toLatitude);
        double lon2 = Math.toRadians(toLongitude);


        double dlon = (lon2 - lon1);
        double dlat = (lat2 - lat1);

        double y = Math.sin(dlon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(dlon);

        double brng = Math.toDegrees(Math.atan2(y, x));

        brng = (360.0 + brng) % 360.0;

        return brng;
    }

    public static double longPathBearingTo(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        return longPathBearingFromShortPath(bearingTo(fromLatitude, fromLongitude, toLatitude, toLongitude));
    }

    public static double longPathBearingFromShortPath(double shortPathBearing) {
        return (180.0 + shortPathBearing) % 360.0;
    }

}
