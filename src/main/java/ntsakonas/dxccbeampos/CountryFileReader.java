package ntsakonas.dxccbeampos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ntsakonas.dxccbeampos.EntityInfo.EntityInfoFactory.from;

/**
 * Reads information from the countries file and extracts the DXCC entities' information.
 * <p>
 * The country file is downloaded from http://www.country-files.com/
 * The original file uses fixed column size and its format is described here: http://www.country-files.com/cty-dat-format/
 * Different logging programs use different variations and the easiest to use is the CSV version found here http://www.country-files.com/contest/aether/
 * <p>
 * The CSV version has the same filed order but it is not fixed width, just comma separated
 * The field order in the CSV file is as follows:
 * 1 	Primary DXCC Prefix
 * 2 	Country Name
 * 3    DXCC Entity number
 * 4    2-letter continent abbreviation
 * 5    CQ Zone
 * 6    ITU Zone
 * 7 	Latitude in degrees, + for North
 * 8    Longitude in degrees, + for West
 * 9 	Local time offset from GMT
 * 10   List of additional DXCC prefixes
 * <p>
 * NOTE:: the longitude is POSITIVE for WEST longitudes, which is the opposite of what is required for various calculations
 * so it is reverted when entities are created.
 */
public class CountryFileReader {

    private final static String COUNTRY_FILE = "countries.txt";

    private final static Predicate<String> validSecondaryPrefix = prefix -> !prefix.isEmpty() && !prefix.contains("[") && !prefix.contains("(") && !prefix.startsWith("=");

    public static Map<String, EntityInfo> loadDXCCEntities(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try (Stream<String> countryFile = br.lines()) {
            return countryFile
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.replace(";", ""))
                    .map(line -> line.split(","))
                    .flatMap(entityDetails -> Stream.concat(
                            // the main entity
                            Stream.of(from(entityDetails[0], entityDetails[1], entityDetails[6], entityDetails[7])),
                            // the additional entries
                            Stream.of(entityDetails[9].split(" "))
                                    .filter(validSecondaryPrefix::test)
                                    .map(prefix -> from(prefix, entityDetails[1], entityDetails[6], entityDetails[7]))
                                    .collect(Collectors.toList()).stream()
                    ))
                    .collect(Collectors.toMap(entity -> entity.prefix, entity -> entity, (entity1, entity2) -> entity1));
        } catch (Throwable e) {
            e.printStackTrace();
            return Collections.EMPTY_MAP;
        }
    }


    public static InputStream getCountryFile() {
        return (new CountryFileReader()).getClass().getClassLoader().getResourceAsStream(COUNTRY_FILE);
    }
}
