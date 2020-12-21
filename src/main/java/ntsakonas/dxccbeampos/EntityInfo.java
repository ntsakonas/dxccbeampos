package ntsakonas.dxccbeampos;

class EntityInfo implements Comparable<EntityInfo> {

    public final String prefix;
    public final String countryName;
    public final double latitude;
    public final double longitude;

    public EntityInfo(String prefix, String countryName, double latitude, double longitude) {
        this.prefix = prefix;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int compareTo(EntityInfo other) {
        return this.prefix.compareTo(other.prefix);
    }
}
