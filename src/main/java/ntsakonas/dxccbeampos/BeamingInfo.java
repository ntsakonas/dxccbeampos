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

public record BeamingInfo(
        String dxCountry,
        String targetCountry,
        double bearingToTargetPrefix,
        double distanceToTargetPrefix,
        double bearingToMyLocation,
        double distanceToMyLocation,
        double longPathBearingToTargetPrefix,
        double longPathDistanceToTargetPrefix,
        double longPathBearingToMyLocation,
        double longPathDistanceToMyLocation
) {

}

