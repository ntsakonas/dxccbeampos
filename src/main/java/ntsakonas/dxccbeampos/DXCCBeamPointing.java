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
    public static  BiFunction<Optional<DXCCEntityInfo>, Optional<DXCCEntityInfo>, Optional<BeamingInfo>> beamingCalculationFromMyDXCC(DXCCEntityInfo myDXCCEntity){
        // set myDXCCEntity as the reference for all calculations
        return applyIfBothProvided(entitiesBeamingFrom(myDXCCEntity));
    }

    public static Function<String, Optional<BeamingInfo>> calculateBeamingInfo(
            BiFunction<Optional<DXCCEntityInfo>, Optional<DXCCEntityInfo>, Optional<BeamingInfo>> beamingCalculationFunction,
            Function<String, Optional<DXCCEntityInfo>> entityForPrefixFunc) {
        return inputLine -> {
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
    public static BiFunction<DXCCEntityInfo, DXCCEntityInfo, BeamingInfo> entitiesBeamingFrom(DXCCEntityInfo referenceEntity) {
        return (dxEntity, targetEntity) -> {
            double bearingToTarget = DistanceCalculator.bearingTo(dxEntity.latitude, dxEntity.longitude, targetEntity.latitude, targetEntity.longitude);
            double lpBearingToTarget = DistanceCalculator.longPathBearingFromShortPath(bearingToTarget);

            double distanceToTarget = DistanceCalculator.distanceFrom(dxEntity.latitude, dxEntity.longitude, targetEntity.latitude, targetEntity.longitude);
            double lpDistanceToTarget = DistanceCalculator.longPathDistanceFromShortPath(distanceToTarget);

            double bearingToMyLocation = DistanceCalculator.bearingTo(dxEntity.latitude, dxEntity.longitude, referenceEntity.latitude, referenceEntity.longitude);
            double lpBearingToMyLocation = DistanceCalculator.longPathBearingFromShortPath(bearingToMyLocation);

            double distanceToMyLocation = DistanceCalculator.distanceFrom(dxEntity.latitude, dxEntity.longitude, referenceEntity.latitude, referenceEntity.longitude);
            double lpDistanceToMyLocation = DistanceCalculator.longPathDistanceFromShortPath(distanceToMyLocation);

            return new BeamingInfo(dxEntity.countryName, targetEntity.countryName, bearingToTarget, distanceToTarget, bearingToMyLocation, distanceToMyLocation,
                    lpBearingToTarget, lpDistanceToTarget, lpBearingToMyLocation, lpDistanceToMyLocation);
        };
    }

    /*
        Returns the details of a DXCC entity based on the provided prefix
     */
    public static Function<String, Optional<DXCCEntityInfo>> entityForPrefixLookup(Map<String, DXCCEntityInfo> entitiesInfo) {
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
