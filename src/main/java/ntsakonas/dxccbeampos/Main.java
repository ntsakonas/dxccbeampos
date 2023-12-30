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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static ntsakonas.dxccbeampos.CountryFileReader.loadDXCCEntities;
import static ntsakonas.dxccbeampos.DXCCBeamPointing.*;

/*
    Main driver of the app.
    Make sure that the countries file is present and provide your own DXCC prefix that will be used as the center
    for all calculations
 */
public class Main {


    public static void main(String[] args) {

        System.out.println("""
        DXCC Beaming calculator v1.0 (Copyright (C) 2020, SV1DJG/2E0PZA)
        ----------------------------------------------------------------
        This is free software and comes with ABSOLUTELY NO WARRANTY.
        """);

        if (args.length != 1) {
            System.out.println("You need to provide your own DXCC prefix that will be used as the reference location.");
            return;
        }

        // load country file and extract DXCC entities info
        Map<String, EntityInfo> entitiesInfo = loadDXCCEntities(CountryFileReader.getCountryFile());

        if (entitiesInfo.isEmpty()) {
            System.out.println("hmmm...could not read the country file...exiting");
            return;
        }

        final Function<String, Optional<EntityInfo>> entityForPrefixLookupFunction = entityForPrefixLookup(entitiesInfo);

        // make sure that my own prefix is set correctly first.
        String myDXCCPrefix = args[0];
        EntityInfo myDXCCEntity = entityForPrefixLookupFunction.apply(myDXCCPrefix.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown prefix for my own DXCC country (could not find prefix %s", myDXCCPrefix)));

        // ready to start
        System.out.println(String.format("** My DXCC country is %s (%s) **", myDXCCEntity.prefix, myDXCCEntity.countryName));
        System.out.println("keep entering prefix pairs as follows: DX OTHER");
        System.out.println("(DX is the station of interest)");
        System.out.println("(press CTRL+C to exit)");
        System.out.println("READY!");

        // map the whole operation into a function of user input-> calculation
        Function<String, Optional<BeamingInfo>> inputToBeamingFunction = calculateBeamingInfo(beamingCalculationFromMyDXCC(myDXCCEntity), entityForPrefixLookupFunction);

        // read input and display results
        // very rough, keep reading from input until it is killed
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(input -> inputToBeamingFunction.apply(input).ifPresent(BeamingInfoPrinter::printBeamings));
    }

}
