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
        double bearingDifference = bearingToTargetPrefix -bearingToMyLocation;
        double absBearing = Math.abs(bearingDifference);

        if (absBearing < 180.0) {
            double directionOfTurn = Math.signum(bearingDifference) > 0 ?-1.0 : 1.0;
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


//        test_beamings();
//        BeamPositioning bp = new BeamPositioning("G", entitiesInfo);
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "ZS");
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "A5");
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "EI");
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "UR");

//        BeamPositioning bp = new BeamPositioning("UR", entitiesInfo);
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "ZS");
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "A5");
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "EI");
//        Optional<BeamPositioning.BeamingInfo> positioningForPrefix = bp.getPositioningForPrefix("SV", "UR");


}
