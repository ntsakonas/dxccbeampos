package ntsakonas.dxccbeampos;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static ntsakonas.dxccbeampos.DXCCBeamPointing.OptBiFunction.applyIfBothProvided;


/*
  Functions to calculate beaming between DXCC entities
 */
public class DXCCBeamPointing {

    /*
     Given a pair of DXCC prefixes, calculates beaming/distance between the DX station and both the target and
     the reference location.
     The input is provided as a string containing 2 DXCC entities , the DX entity first, followed by the target entity
     e.g. "VE DL"
   */
    public static Function<String, Optional<BeamingInfo>> calculateBeamingInfo(EntityInfo myDXCCEntity, Function<String, Optional<EntityInfo>> entityForPrefixFunc) {
        return inputLine -> {
            // set myDXCCEntity as the reference for all calculations
            BiFunction<Optional<EntityInfo>, Optional<EntityInfo>, Optional<BeamingInfo>> beamingCalculationFunction
                    = applyIfBothProvided(entitiesBeamingFrom(myDXCCEntity));

            return Stream.of(inputLine.toUpperCase())
                    .flatMap(input -> Stream.of(input)
                            .map(line -> line.split(" "))
                            .filter(prefixes -> prefixes.length == 2)
                            .map(prefixes -> beamingCalculationFunction.apply(entityForPrefixFunc.apply(prefixes[0]), entityForPrefixFunc.apply(prefixes[1]))))
                    .findFirst()
                    .flatMap(beamingInfo -> beamingInfo);
        };
    }

    /*
        Calculates the beaming/distance from the dx-> reference and dx->target
     */
    public static BiFunction<EntityInfo, EntityInfo, BeamingInfo> entitiesBeamingFrom(EntityInfo referenceEntity) {
        return (dxEntity, targetEntity) -> {
            double bearingToTarget = DistanceCalculator.bearingTo(dxEntity.latitude, dxEntity.longitude, targetEntity.latitude, targetEntity.longitude);
            double distanceToTarget = DistanceCalculator.distanceFrom(dxEntity.latitude, dxEntity.longitude, targetEntity.latitude, targetEntity.longitude);
            double bearingToMyLocation = DistanceCalculator.bearingTo(dxEntity.latitude, dxEntity.longitude, referenceEntity.latitude, referenceEntity.longitude);
            double distanceToMyLocation = DistanceCalculator.distanceFrom(dxEntity.latitude, dxEntity.longitude, referenceEntity.latitude, referenceEntity.longitude);

            return new BeamingInfo(dxEntity.countryName, targetEntity.countryName, bearingToTarget, distanceToTarget, bearingToMyLocation, distanceToMyLocation);
        };
    }

    /*
        Returns the details of a DXCC entity based on the provided prefix
     */
    public static Function<String, Optional<EntityInfo>> entityForPrefixLookup(Map<String, EntityInfo> entitiesInfo) {
        return prefix -> Optional.ofNullable(entitiesInfo.get(prefix));
    }

    /*
    A wrapper around a BiFunction, that is applied if both Optionals are present
     */
    public interface OptBiFunction<T, U, R> extends BiFunction<T, U, R> {

        static <T, U, R> BiFunction<Optional<T>, Optional<U>, Optional<R>> applyIfBothProvided(BiFunction<T, U, R> function) {
            Objects.requireNonNull(function);
            return (t, u) -> t.flatMap(_t -> u.flatMap(_u -> Optional.of(function.apply(_t, _u))));
        }
    }
}
