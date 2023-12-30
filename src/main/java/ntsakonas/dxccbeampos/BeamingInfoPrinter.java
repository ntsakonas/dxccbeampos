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

/*
  Shows the results (minimal info for quick reading)
 */
public class BeamingInfoPrinter {

    public static void printBeamings(BeamingInfo beamingInfo) {
        System.out.println(String.format("%s -> %s", beamingInfo.dxCountry(), beamingInfo.targetCountry()));
        System.out.println(String.format("via SP I am %+.2f degrees, %s",
                        normalisedBearingDiff(beamingInfo.bearingToTargetPrefix(), beamingInfo.bearingToMyLocation()),
                        humanReadableDistanceDiff(beamingInfo.distanceToMyLocation() - beamingInfo.distanceToTargetPrefix())
                )
        );
        System.out.println(String.format("via LP I am %+.2f degrees, %s",
                        normalisedBearingDiff(beamingInfo.longPathBearingToTargetPrefix(), beamingInfo.longPathBearingToMyLocation()),
                        humanReadableDistanceDiff(beamingInfo.longPathDistanceToMyLocation() - beamingInfo.longPathDistanceToTargetPrefix())
                )
        );

        System.out.println(
                String.format("""
                                SP:
                                %s --> %6.2f\u00B0/%8.2f Km --> %s
                                %s --> %6.2f\u00B0/%8.2f Km --> Me
                                LP:
                                %s --> %6.2f\u00B0/%8.2f Km --> %s
                                %s --> %6.2f\u00B0/%8.2f Km --> Me
                                """,
                        beamingInfo.dxCountry(), beamingInfo.bearingToTargetPrefix(), beamingInfo.distanceToTargetPrefix(),beamingInfo.targetCountry(),
                        beamingInfo.dxCountry(), beamingInfo.bearingToMyLocation(), beamingInfo.distanceToMyLocation(),
                        beamingInfo.dxCountry(), beamingInfo.longPathBearingToTargetPrefix(), beamingInfo.longPathDistanceToTargetPrefix(),beamingInfo.targetCountry(),
                        beamingInfo.dxCountry(), beamingInfo.longPathBearingToMyLocation(), beamingInfo.longPathDistanceToMyLocation()
                )
        );
    }

    private static String humanReadableDistanceDiff(double distanceDiff) {
        return "%.2f Km %s".formatted(Math.abs(distanceDiff), distanceDiff > 0 ? "farther" : "closer");
    }

    private static double normalisedBearingDiff(double bearingToTargetPrefix, double bearingToMyLocation) {
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
}

