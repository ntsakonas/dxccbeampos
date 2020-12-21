package ntsakonas.dxccbeampos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DXCCBeamPointing {

    private final static String COUNTRY_FILE = "countries.txt";

    public final void beamInfo(String myDXCCPrefix, BeamPositioningPrinter bpPrinter) throws Throwable {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(COUNTRY_FILE);
        Collection<EntityInfo> entitiesInfo = CountryFileReader.loadPrefixes(inputStream);

        if (entitiesInfo.isEmpty()) {
            System.out.println("hmmm...could not read the country files...");
            return;
        }

        EntityInfo myEntityInfo = BeamPositioning.entityForPrefix.apply(entitiesInfo, myDXCCPrefix).orElseThrow((Supplier<Throwable>) () -> new IllegalArgumentException("Unknown prefix for own DXCC country"));
        Function<EntityInfo, Function<EntityInfo, PositionInfo>> beaming = BeamPositioning.positionForPrefixes.apply(myEntityInfo);

        System.out.println("(...keep entering prefix pairs: DX TARGET)");
        System.out.println("READY!");

        // read input and display results
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(input -> showBeaming(input, entitiesInfo, beaming, bpPrinter));
    }

    private void showBeaming(String input, Collection<EntityInfo> entitiesInfo,
                             Function<EntityInfo, Function<EntityInfo, PositionInfo>> beaming,
                             BeamPositioningPrinter printer) {
        Stream.of(input.toUpperCase())
                .map(line -> line.split(" "))
                .filter(prefixes -> prefixes.length == 2)
                .map(prefixes -> calculatePositions(prefixes, entitiesInfo, beaming))
                .forEach(printer::printBeamings);
    }

    private Optional<PositionInfo> calculatePositions(String[] prefixes,
                                                      Collection<EntityInfo> entitiesInfo,
                                                      Function<EntityInfo, Function<EntityInfo, PositionInfo>> beaming) {
        String dx = prefixes[0];
        String target = prefixes[1];

        // read DX
        Optional<EntityInfo> dxEntityInfo = BeamPositioning.entityForPrefix.apply(entitiesInfo, dx);
        Optional<EntityInfo> targetEntityInfo = BeamPositioning.entityForPrefix.apply(entitiesInfo, target);

        return dxEntityInfo
                .flatMap(dxEntity ->
                        targetEntityInfo.flatMap(targetEntity -> Optional.of(beaming.apply(dxEntity).apply(targetEntity))
                        )
                );

    }


    public static void main(String[] args) throws Throwable {
        System.out.println("DXCC Beam pointing 1.1 (2020)");
        if (args.length != 1 && args.length != 3) {
            System.out.println("You need to provide your own DXCC prefix that will be used as your location.");
            return;
        }

        DXCCBeamPointing beamPointing = new DXCCBeamPointing();
        beamPointing.beamInfo(args[0], new BeamPositioningPrinter());
    }
}
