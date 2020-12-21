package ntsakonas.dxccbeampos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class BeamPositioning {
    public static class PositionInfo {
        public final String dxCountry;
        public final String targetCountry;
        public final double bearingToTargetPrefix;
        public final double distanceToTargetPrefix;
        public final double bearingToMyLocation;
        public final double distanceToMyLocation;

        public PositionInfo(String dxCountry, String targetCountry, double bearingToTargetPrefix, double distanceToTargetPrefix, double bearingToMyLocation, double distanceToMyLocation) {
            this.dxCountry = dxCountry;
            this.targetCountry = targetCountry;
            this.bearingToTargetPrefix = bearingToTargetPrefix;
            this.distanceToTargetPrefix = distanceToTargetPrefix;
            this.bearingToMyLocation = bearingToMyLocation;
            this.distanceToMyLocation = distanceToMyLocation;
        }
    }

    private final Collection<EntityInfo> entitiesInfo;
    private final double myLatitude;
    private final double myLongitude;


    public BeamPositioning(String myPrefix, Collection<EntityInfo> entitiesInfo) {
        this.entitiesInfo = entitiesInfo;
        Optional<EntityInfo> myEntityInfo = getForPrefix(myPrefix);
        if (!myEntityInfo.isPresent()) {
            throw new IllegalArgumentException("the prefix provided for my location does not match any dxcc entity");
        }
        this.myLatitude = myEntityInfo.get().latitude;
        this.myLongitude = myEntityInfo.get().longitude;
    }

    public Optional<PositionInfo> getPositioningForPrefix(String dxPrefix, String targetPrefix) {
        Optional<EntityInfo> dxPrefixInfo = getForPrefix(dxPrefix);
        if (!dxPrefixInfo.isPresent()) {
            System.out.println("sorry, could not find prefix " + dxPrefix);
            return Optional.empty();
        }

        Optional<EntityInfo> targetPrefixInfo = getForPrefix(targetPrefix);
        if (!targetPrefixInfo.isPresent()) {
            System.out.println("sorry, could not find prefix " + targetPrefix);
            return Optional.empty();
        }

        return targetPrefixInfo.map(entityInfo -> distanceAndBearing(dxPrefixInfo.get(), targetPrefixInfo.get()));
    }

    private PositionInfo distanceAndBearing(EntityInfo dxPrefixInfo, EntityInfo targetPrefixInfo) {
        double bearingToTarget = DistanceCalculator.bearingTo(dxPrefixInfo.latitude, dxPrefixInfo.longitude, targetPrefixInfo.latitude, targetPrefixInfo.longitude);
        double distanceToTarget = DistanceCalculator.distanceFrom(dxPrefixInfo.latitude, dxPrefixInfo.longitude, targetPrefixInfo.latitude, targetPrefixInfo.longitude);
        double bearingToMyLocation = DistanceCalculator.bearingTo(dxPrefixInfo.latitude, dxPrefixInfo.longitude, myLatitude, myLongitude);
        double distanceToMyLocation = DistanceCalculator.distanceFrom(dxPrefixInfo.latitude, dxPrefixInfo.longitude, myLatitude, myLongitude);

        return new PositionInfo(dxPrefixInfo.countryName, targetPrefixInfo.countryName, bearingToTarget, distanceToTarget, bearingToMyLocation, distanceToMyLocation);
    }

    private Optional<EntityInfo> getForPrefix(String prefix) {
        return entitiesInfo.stream().filter(entityInfo -> entityInfo.prefix.equals(prefix)).findFirst();
    }
}
