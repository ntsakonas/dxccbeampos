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


public class DistanceCalculator {

    private static final double EARTH_RADIUS = 6371.0; // in km;


    public static class CoordinateSet {
        double _latitude;
        double _longitude;

        public CoordinateSet(double latitude, double longitude) {
            _latitude = latitude;
            _longitude = longitude;
        }

        public double getLatitude() {
            return _latitude;
        }

        public void setLatitude(double latitude) {
            _latitude = latitude;
        }

        public double getLongitude() {
            return _longitude;
        }

        public void setLongitude(double longitude) {
            _longitude = longitude;
        }

    }

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

	/*
		if (brng < 0)
			brng = 360.0 + brng;
	 */

        brng = (360.0 + brng) % 360.0;

        return brng;
    }


    //
    // calculates the coordinates af the mid-poind (the point that lies in half great-circle distance between two points)
    //
    public static CoordinateSet midpointOf(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {

        double lat1 = Math.toRadians(toLatitude);
        double lon1 = Math.toRadians(toLongitude);
        double lat2 = Math.toRadians(fromLatitude);
        double lon2 = Math.toRadians(fromLongitude);

        double dlon = (lon2 - lon1);

        double Bx = Math.cos(lat2) * Math.cos(dlon);
        double By = Math.cos(lat2) * Math.sin(dlon);
        double y = Math.sin(lat1) + Math.sin(lat2);
        double x = Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By);
        double midPointLat = Math.atan2(y, x);
        double midPointLon = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new CoordinateSet(Math.toDegrees(midPointLat), Math.toDegrees(midPointLon));

    }


    //
    // calculates the point that lies in a specific bearing and distance from a reference point (distance should be in km)
    //
    public static CoordinateSet targetAtBearingDistance(double fromLatitude, double fromLongitude, double bearing, double distance) {

        double brng = Math.toRadians(bearing % 360.0);
        double lat1 = Math.toRadians(fromLatitude);
        double lon1 = Math.toRadians(fromLongitude);

        double targetPointLat = Math.asin(Math.sin(lat1) * Math.cos(distance / EARTH_RADIUS) + Math.cos(lat1) * Math.sin(distance / EARTH_RADIUS) * Math.cos(brng));
        double y = Math.sin(brng) * Math.sin(distance / EARTH_RADIUS) * Math.cos(lat1);
        double x = Math.cos(distance / EARTH_RADIUS) - Math.sin(lat1) * Math.sin(targetPointLat);
        double targetPointLon = lon1 + Math.atan2(y, x);

        // normalize longitude into E/W (-180 ...180)
        targetPointLon = (targetPointLon + 3 * Math.PI) % (2 * Math.PI) - Math.PI;
        double targetPointLatInDegrees = Math.toDegrees(targetPointLat);
        double targetPointLonInDegrees = Math.toDegrees(targetPointLon);


        return new CoordinateSet(Math.toDegrees(targetPointLat), Math.toDegrees(targetPointLon));
    }


    //
    // this was implemented as a cross-reference (for verification of my algorithm vs Google's) . the result is the same
    //
    public static double GoogleDistanceFrom(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {

        double lat1 = Math.toRadians(toLatitude);
        double lon1 = Math.toRadians(toLongitude);
        double lat2 = Math.toRadians(fromLatitude);
        double lon2 = Math.toRadians(fromLongitude);

        double dlon = (lon2 - lon1);
        double dlat = (lat2 - lat1);

        double a = (Math.sin(dlat / 2)) * (Math.sin(dlat / 2))
                + (Math.cos(lat1) * Math.cos(lat2) * (Math.sin(dlon / 2))) * (Math.sin(dlon / 2));

        double c = 2 * Math.asin((Math.sqrt(a)));
        double km = EARTH_RADIUS * c;

        return km;//(km * 1000.0);
    }
}
