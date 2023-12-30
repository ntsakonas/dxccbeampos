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

class DXCCEntityInfo implements Comparable<DXCCEntityInfo> {

    public final String prefix;
    public final String countryName;
    public final double latitude;
    public final double longitude;

    private DXCCEntityInfo(String prefix, String countryName, double latitude, double longitude) {
        this.prefix = prefix;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int compareTo(DXCCEntityInfo other) {
        return this.prefix.compareTo(other.prefix);
    }

    public static class EntityInfoFactory {

        //NOTE:: the longitude is POSITIVE for WEST longitudes, which is the opposite of what is required for various calculations
        // so it is reverted here

        public static DXCCEntityInfo from(String prefix, String countryName, String latitude, String longitude) {
            return new DXCCEntityInfo(cleanPrefix(prefix), countryName, toDouble(latitude), -toDouble(longitude));
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
