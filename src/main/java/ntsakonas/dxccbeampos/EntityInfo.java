package ntsakonas.dxccbeampos;

class EntityInfo implements Comparable<EntityInfo> {

    public final String prefix;
    public final String countryName;
    public final double latitude;
    public final double longitude;

    private EntityInfo(String prefix, String countryName, double latitude, double longitude) {
        this.prefix = prefix;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public int compareTo(EntityInfo other) {
        return this.prefix.compareTo(other.prefix);
    }

    public static class EntityInfoFactory {

        //NOTE:: the longitude is POSITIVE for WEST longitudes, which is the opposite of what is required for various calculations
        // so it is reverted here

        public static EntityInfo from(String prefix, String countryName, String latitude, String longitude) {
            return new EntityInfo(cleanPrefix(prefix), countryName, toDouble(latitude), -toDouble(longitude));
        }

        private static double toDouble(String numericalValue) {
            return Double.parseDouble(numericalValue);
        }

        private static String cleanPrefix(String prefix) {
            // remove the '*' from the prefix if it exists - it is not part of the DXCC entity name
            return prefix.startsWith("*") ? prefix.substring(1) : prefix;
        }

    }
}
