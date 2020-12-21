package ntsakonas.dxccbeampos;

public class BeamingInfo {

    public final String dxCountry;
    public final String targetCountry;
    public final double bearingToTargetPrefix;
    public final double distanceToTargetPrefix;
    public final double bearingToMyLocation;
    public final double distanceToMyLocation;

    public BeamingInfo(String dxCountry, String targetCountry, double bearingToTargetPrefix, double distanceToTargetPrefix, double bearingToMyLocation, double distanceToMyLocation) {
        this.dxCountry = dxCountry;
        this.targetCountry = targetCountry;
        this.bearingToTargetPrefix = bearingToTargetPrefix;
        this.distanceToTargetPrefix = distanceToTargetPrefix;
        this.bearingToMyLocation = bearingToMyLocation;
        this.distanceToMyLocation = distanceToMyLocation;
    }
}
