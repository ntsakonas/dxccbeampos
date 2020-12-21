package ntsakonas.dxccbeampos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;

public class DXCCBeamPointing {

    private final static String COUNTRY_FILE = "countries.txt";

    public static void main(String[] args) {
        System.out.println("DXCC Beam pointing 1.1 (2020)");
        if (args.length != 1 && args.length != 3) {
            System.out.println("You need to provide your own DXCC prefix that will be used as your location.");
            return;
        }

        CountryFileReader cfReader = new CountryFileReader();
        Collection<EntityInfo> entitiesInfo = cfReader.loadPrefixes(cfReader.getClass().getClassLoader().getResourceAsStream(COUNTRY_FILE));
        if (entitiesInfo.isEmpty()) {
            System.out.println("hmmm...could not read the country files...");
        }


        // quick debugging
        if (args.length == 3) {
            BeamPositioning bp = new BeamPositioning(args[0].toUpperCase(), entitiesInfo);
            BeamPositioningPrinter bpPrinter = new BeamPositioningPrinter(bp);
            bpPrinter.showBeaming(args[1].toUpperCase() + " " + args[2].toUpperCase());
            return;
        }

        System.out.println("(...keep entering pairs: DX TARGET)");
        System.out.println("READY!");
        BeamPositioning bp = new BeamPositioning(args[0], entitiesInfo);
        BeamPositioningPrinter bpPrinter = new BeamPositioningPrinter(bp);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(s -> bpPrinter.showBeaming(s.toUpperCase()));
    }
}
