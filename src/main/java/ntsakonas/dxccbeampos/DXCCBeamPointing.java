package ntsakonas.dxccbeampos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static ntsakonas.dxccbeampos.BeamPositioning.beamingForPrefixes;
import static ntsakonas.dxccbeampos.BeamPositioningPrinter.printCalculationFailure;

public class DXCCBeamPointing {

    public final void beamInfo(String myDXCCPrefix, Function<String, Optional<EntityInfo>> entityForPrefix) {

        EntityInfo myEntityInfo = entityForPrefix.apply(myDXCCPrefix)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown prefix for my own DXCC country (could not find prefix %s", myDXCCPrefix)));

        Function<EntityInfo, Function<EntityInfo, BeamingInfo>> beamCalcFunction = beamingForPrefixes.apply(myEntityInfo);

        System.out.println(String.format("Your DXCC country is %s (%s)", myEntityInfo.prefix, myEntityInfo.countryName));
        System.out.println("(keep entering prefix pairs as follows: DX TARGET)");
        System.out.println("READY!");

        // read input and display results
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines()
                .flatMap(input -> calculateBeamings(input, entityForPrefix, beamCalcFunction))
                .peek(positionInfo -> {
                    if (!positionInfo.isPresent())
                        printCalculationFailure();
                })
                .forEach(input -> input.ifPresent(BeamPositioningPrinter::printBeamings));
    }

    // Given a pair of DXCC prefixes, calculate beaming/distance between the DX station and both the target and my location
    private Stream<Optional<BeamingInfo>> calculateBeamings(String input,
                                                            Function<String, Optional<EntityInfo>> entityForPrefix,
                                                            Function<EntityInfo, Function<EntityInfo, BeamingInfo>> beaming) {
        return Stream.of(input.toUpperCase())
                .map(line -> line.split(" "))
                .filter(prefixes -> prefixes.length == 2)
                .map(prefixes -> calculatePositions(entityForPrefix.apply(prefixes[0]),
                        entityForPrefix.apply(prefixes[1]), beaming));
    }

    private Optional<BeamingInfo> calculatePositions(Optional<EntityInfo> dxEntityInfo,
                                                     Optional<EntityInfo> targetEntityInfo,
                                                     Function<EntityInfo, Function<EntityInfo, BeamingInfo>> beaming) {
        return dxEntityInfo.flatMap(dxEntity -> targetEntityInfo.flatMap(targetEntity -> Optional.of(beaming.apply(dxEntity).apply(targetEntity))));
    }


}
