package ntsakonas.dxccbeampos;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static ntsakonas.dxccbeampos.BeamPositioning.beamingForPrefixes;

public class DXCCBeamPointing {

    public static BiFunction<EntityInfo, Function<String, Optional<EntityInfo>>, Function<String, Optional<BeamingInfo>>> beamInfo =
            (myDXCCEntity, entityForPrefixFunc) -> inputLine -> {

                Function<EntityInfo, Function<EntityInfo, BeamingInfo>> beamCalcFunction = beamingForPrefixes.apply(myDXCCEntity);
                return Stream.of(inputLine)
                        .flatMap(input -> calculateBeamings(inputLine, entityForPrefixFunc, beamCalcFunction))
                        .findFirst()
                        .flatMap(beamingInfo -> beamingInfo);
            };

    // Given a pair of DXCC prefixes, calculate beaming/distance between the DX station and both the target and my location
    private static Stream<Optional<BeamingInfo>> calculateBeamings(String input,
                                                                   Function<String, Optional<EntityInfo>> entityForPrefix,
                                                                   Function<EntityInfo, Function<EntityInfo, BeamingInfo>> beaming) {
        return Stream.of(input.toUpperCase())
                .map(line -> line.split(" "))
                .filter(prefixes -> prefixes.length == 2)
                .map(prefixes -> calculatePositions(entityForPrefix.apply(prefixes[0]),
                        entityForPrefix.apply(prefixes[1]), beaming));
    }

    private static Optional<BeamingInfo> calculatePositions(Optional<EntityInfo> dxEntityInfo,
                                                            Optional<EntityInfo> targetEntityInfo,
                                                            Function<EntityInfo, Function<EntityInfo, BeamingInfo>> beaming) {
        return dxEntityInfo.flatMap(dxEntity -> targetEntityInfo.flatMap(targetEntity -> Optional.of(beaming.apply(dxEntity).apply(targetEntity))));
    }

}
