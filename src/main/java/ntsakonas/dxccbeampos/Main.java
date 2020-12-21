package ntsakonas.dxccbeampos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static ntsakonas.dxccbeampos.DXCCBeamPointing.calculateBeamingInfo;
import static ntsakonas.dxccbeampos.DXCCBeamPointing.entityForPrefixLookup;

/*
    Main driver of the app.
    Make sure that the countries file is present and provide your own DXCC prefix that will be used as the center
    for all calculations
 */
public class Main {


    public static void main(String[] args) {

        System.out.println("DXCC Beaming calculator v1.0 (2020), SV1DJG/2E0PZA");
        if (args.length != 1) {
            System.out.println("You need to provide your own DXCC prefix that will be used as the reference location.");
            return;
        }

        // load country file and extract DXCC entities info
        Map<String, EntityInfo> entitiesInfo = CountryFileReader.loadPrefixes(CountryFileReader.getCountryFile());

        if (entitiesInfo.isEmpty()) {
            System.out.println("hmmm...could not read the country file...exiting");
            return;
        }

        final Function<String, Optional<EntityInfo>> entityForPrefixLookupFunction = entityForPrefixLookup(entitiesInfo);

        // make sure that my own prefix is set correctly first.
        String myDXCCPrefix = args[0];
        EntityInfo myDXCCEntity = entityForPrefixLookupFunction.apply(myDXCCPrefix)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown prefix for my own DXCC country (could not find prefix %s", myDXCCPrefix)));

        // ready to start
        System.out.println(String.format("Your DXCC country is %s (%s)", myDXCCEntity.prefix, myDXCCEntity.countryName));
        System.out.println("(keep entering prefix pairs as follows: DX TARGET)");
        System.out.println("(press CTRL+C to exit)");
        System.out.println("READY!");

        // map the whole operation into a function of user input-> calculation
        Function<String, Optional<BeamingInfo>> inputToBeamingFunction = calculateBeamingInfo(myDXCCEntity, entityForPrefixLookupFunction);

        // read input and display results
        // very rough, keep reading from input until it is killed
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(input -> inputToBeamingFunction.apply(input).ifPresent(BeamPositioningPrinter::printBeamings));
    }

}
