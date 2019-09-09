package es.test.geo;

/**
 * EPSG:3857 (EPSG:900913)
 * <p>
 * WGS 84 / Pseudo-Mercator -- Spherical Mercator, Google Maps, OpenStreetMap, Bing, ArcGIS, ESRI
 * <p>
 * This is projected coordinate system used for rendering maps in Google Maps, OpenStreetMap, etc. For details see {@link https://www.maptiler.com/google-maps-coordinates-tile-bounds-projection/}.
 */
public class Pixel {
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public final int x;
    public final int y;

    public boolean epsilonEquals(Pixel other) {
        return Math.abs(x - other.x) <= 0.1 && Math.abs(y - other.y) <= 0.1;
    }
}
