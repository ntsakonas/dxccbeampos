/*
    DXCC beaming position: a utility to quickly assess your positioning around the beaming of a DX station
     using their current QSOs with another station as an indicator.

    Copyright (C) 2020  Nick Tsakonas, SV1DJG/2E0PZA

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ntsakonas.dxccbeampos;

public class SomeTests {

    private static void test_beamings() {
        System.out.println(String.format("%+.2f should be +55.0", normalisedBearing_test(45.0, 100.0)));
        System.out.println(String.format("%+.2f should be -40.0", normalisedBearing_test(45.0, 5.0)));
        System.out.println(String.format("%+.2f should be -45.0", normalisedBearing_test(45.0, 0.0)));
        System.out.println(String.format("%+.2f should be -45.0", normalisedBearing_test(45.0, 360.0)));
        System.out.println(String.format("%+.2f should be -55.0", normalisedBearing_test(45.0, 350.0)));
        System.out.println(String.format("%+.2f should be 180.0", normalisedBearing_test(45.0, 225.0)));

        System.out.println(String.format("%+.2f should be +20.0", normalisedBearing_test(300.0, 320.0)));
        System.out.println(String.format("%+.2f should be -80.0", normalisedBearing_test(300.0, 220.0)));
        System.out.println(String.format("%+.2f should be +60.0", normalisedBearing_test(300.0, 0.0)));
        System.out.println(String.format("%+.2f should be +60.0", normalisedBearing_test(300.0, 360.0)));
        System.out.println(String.format("%+.2f should be +80.0", normalisedBearing_test(300.0, 20.0)));
        System.out.println(String.format("%+.2f should be +180.0", normalisedBearing_test(300.0, 120.0)));

        System.out.println(String.format("%+.2f should be -3.0", normalisedBearing_test(2.0, 359.0)));
        System.out.println(String.format("%+.2f should be +3.0", normalisedBearing_test(359.0, 2.0)));

    }

    private static double normalisedBearing_test(double bearingToTargetPrefix, double bearingToMyLocation) {
        double bearingDifference = bearingToTargetPrefix - bearingToMyLocation;
        double absBearing = Math.abs(bearingDifference);

        if (absBearing < 180.0) {
            double directionOfTurn = Math.signum(bearingDifference) > 0 ? -1.0 : 1.0;
            return directionOfTurn * absBearing;
        } else {
            // more than 180 degrees apart, go the other way around
            // extract the sign from the bearing diff to indicate
            double directionOfTurn = Math.signum(bearingDifference) > 0 ? 1.0 : -1.0;
            double diffToNewBearing = absBearing % 180.0;
            double shortestBearing = directionOfTurn * (180.0 - diffToNewBearing);
            return shortestBearing;
        }
    }

    public static void main(String[] args) {
        test_beamings();
    }
}
