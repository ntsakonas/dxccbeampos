package ntsakonas.dxccbeampos;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BeamPositioning {

    public static Function<EntityInfo, Function<EntityInfo, Function<EntityInfo, BeamingInfo>>> beamingForPrefixes =
            myEntity -> dxEntity -> targetEntity -> {
                double bearingToTarget = DistanceCalculator.bearingTo(dxEntity.latitude, dxEntity.longitude, targetEntity.latitude, targetEntity.longitude);
                double distanceToTarget = DistanceCalculator.distanceFrom(dxEntity.latitude, dxEntity.longitude, targetEntity.latitude, targetEntity.longitude);
                double bearingToMyLocation = DistanceCalculator.bearingTo(dxEntity.latitude, dxEntity.longitude, myEntity.latitude, myEntity.longitude);
                double distanceToMyLocation = DistanceCalculator.distanceFrom(dxEntity.latitude, dxEntity.longitude, myEntity.latitude, myEntity.longitude);

                return new BeamingInfo(dxEntity.countryName, targetEntity.countryName, bearingToTarget, distanceToTarget, bearingToMyLocation, distanceToMyLocation);
            };

    public static BiFunction<Collection<EntityInfo>, String, Optional<EntityInfo>> entityForPrefix = (entitiesInfo, prefix) -> entitiesInfo.stream().filter(entityInfo -> entityInfo.prefix.equals(prefix)).findFirst();

}
