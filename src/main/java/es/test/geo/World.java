package es.test.geo;

import java.text.MessageFormat;

/**
 * Degrees. Geodetic coordinates
 * <p>
 * WGS84 (EPSG:4326)
 * <p>
 * Longitude and latitude coordinates used by GPS devices for defining position
 * on Earth using World Geodetic System defined in 1984 (WGS84).
 * <p>
 * WGS84 geodetic datum specify lon/lat (lambda/phi) coordinates on defined
 * ellipsoid shape with defined origin ([0,0] on a prime meridian).
 */
public class World {
	public World(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public final double latitude; // North/South
	public final double longitude; // East/West

    @Override
    public String toString() {
        return MessageFormat.format("Lat/Long: {0,number,#.##} ; {1,number,#.##}",
                latitude, longitude);
    }
}
