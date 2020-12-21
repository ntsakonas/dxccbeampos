package ntsakonas.dxccbeampos;

import java.io.InputStream;
import java.util.Map;

import static ntsakonas.dxccbeampos.BeamPositioning.entityForPrefix;

/*
    Main driver of the app.
    Make sure that the countries file is present and provide your own DXCC prefix that will be used as the center
    for all calculations
 */
public class Main {

    private final static String COUNTRY_FILE = "countries.txt";

    public static void main(String[] args) {

        System.out.println("DXCC Beaming calculator v1.1 (2020), SV1DJG/2E0PZA");
        if (args.length != 1) {
            System.out.println("You need to provide your own DXCC prefix that will be used as your location.");
            return;
        }

        DXCCBeamPointing beamPointing = new DXCCBeamPointing();
        InputStream inputStream = beamPointing.getClass().getClassLoader().getResourceAsStream(COUNTRY_FILE);
        Map<String, EntityInfo> entitiesInfo = CountryFileReader.loadPrefixes(inputStream);

        if (entitiesInfo.isEmpty()) {
            System.out.println("hmmm...could not read the country files...exiting");
            return;
        }

        beamPointing.beamInfo(args[0], entityForPrefix.apply(entitiesInfo));
    }

}
