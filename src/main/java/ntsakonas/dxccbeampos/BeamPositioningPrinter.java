package ntsakonas.dxccbeampos;

/*
  Shows the results (minimal info for quick reading)
 */
public class BeamPositioningPrinter {

    public static void printCalculationFailure(){
        System.out.println("nah..that did not work.");
    }

    public static void printBeamings(BeamingInfo beamingInfo) {

        System.out.println(String.format("%s -> %s", beamingInfo.dxCountry, beamingInfo.targetCountry));
        System.out.println(String.format("beaming from DX to target vs my loc : Beaming diff %+.2f distance diff %+.2f",
                normalisedBearingDiff(beamingInfo.bearingToTargetPrefix, beamingInfo.bearingToMyLocation),
                beamingInfo.distanceToMyLocation - beamingInfo.distanceToTargetPrefix));
        System.out.println(String.format("DX -> my loc : Beaming %.2f distance %.2f", beamingInfo.bearingToMyLocation, beamingInfo.distanceToMyLocation));
        System.out.println(String.format("DX -> target : Beaming %.2f distance %.2f", beamingInfo.bearingToTargetPrefix, beamingInfo.distanceToTargetPrefix));
        System.out.println();
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

