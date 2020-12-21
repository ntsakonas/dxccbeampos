package ntsakonas.dxccbeampos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountryFileReader {
    private final static String COUNTRY_FILE = "countries.txt";

    public Collection<EntityInfo> loadPrefixes() {
        // https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
        // this does not work inside a jar
        // Path path = Paths.get(getClass().getClassLoader().getResource(COUNTRY_FILE).getPath());
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(COUNTRY_FILE)));
        try (Stream<String> countryFile = br.lines()) {
            return countryFile
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.replace(";", ""))
                    .flatMap(line -> extractDXCCEntities(line))
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (Throwable e) {
            e.printStackTrace();
            return Collections.EMPTY_SET;
        }
    }

    private Stream<EntityInfo> extractDXCCEntities(String line) {
        String[] entityDetails = line.split(",");
        EntityInfo entityInfo = new EntityInfo(entityDetails[0], entityDetails[1], toDouble(entityDetails[6]), -toDouble(entityDetails[7]));
        if (entityDetails.length == 10) {
            // entity as additional prefixes
            // keep it simple, and do not process special cases
            List<EntityInfo> additionalPrefixes = Stream.of(entityDetails[9].split(" "))
                    .filter(prefix -> !prefix.equals(entityInfo.prefix) && !prefix.isEmpty() && !prefix.contains("[") && !prefix.contains("(") && !prefix.startsWith("="))
                    .map(prefix -> entityInfo.withPrefix(prefix))
                    .collect(Collectors.toList());

            additionalPrefixes.add(entityInfo);
            return additionalPrefixes.stream();
        }
        return Stream.of(entityInfo);
    }

    private double toDouble(String numericalValue) {
        return Double.parseDouble(numericalValue);
    }
}
