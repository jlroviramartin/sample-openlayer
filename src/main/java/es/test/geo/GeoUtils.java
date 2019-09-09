package es.test.geo;

public class GeoUtils {

    public static Pixel worldToTile(final World world, final int zoom) {
        int xtile = lon2tile(world.longitude, zoom);
        int ytile = lat2tile(world.latitude, zoom);
        return new Pixel(xtile, ytile);
    }

    public static World tile2World(final Pixel tile, final int zoom) {
        double longitude = tile2lon(tile.x, zoom);
        double latitude = tile2lat(tile.y, zoom);
        return new World(latitude, longitude);
    }

    public static WorldBox getBox(final Tile tile, final int zoom) {
        double lat1 = tile2lat(tile.y, zoom);
        double lat2 = tile2lat(tile.y + 1, zoom);
        double lon1 = tile2lon(tile.x, zoom);
        double lon2 = tile2lon(tile.x + 1, zoom);

        return new WorldBox(Math.min(lat1, lat2), Math.max(lat1, lat2), Math.min(lon1, lon2), Math.max(lon1, lon2));
    }

    public static boolean contains(WorldBox box, World pt) {
        double minLat = box.minLatitude;
        double maxLat = box.maxLatitude;
        double minLon = box.minLongitude;
        double maxLon = box.maxLongitude;

        return minLat <= pt.latitude && pt.latitude <= maxLat //
                && minLon <= pt.longitude && pt.longitude <= maxLon;
    }

    public static double tile2lat(int y, int zoom) {
        int pow2zoom = (1 << zoom);
        double n = Math.PI - (2.0 * Math.PI * y) / pow2zoom;
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    public static int lat2tile(double lat, int zoom) {
        int pow2zoom = (1 << zoom);
        double rad = Math.toRadians(lat);
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(rad) + 1 / Math.cos(rad)) / Math.PI) / 2 * pow2zoom);

        if (ytile < 0) {
            ytile = 0;
        } else if (ytile >= pow2zoom) {
            ytile = (pow2zoom - 1);
        }
        return ytile;
    }

    public static double tile2lon(int x, int zoom) {
        int pow2zoom = (1 << zoom);
        return (x / pow2zoom) * 360.0 - 180;
    }

    public static int lon2tile(double lon, int zoom) {
        int pow2zoom = (1 << zoom);
        int xtile = (int) Math.floor(((lon + 180) / 360.0) * pow2zoom);

        if (xtile < 0) {
            xtile = 0;
        } else if (xtile >= pow2zoom) {
            xtile = (pow2zoom - 1);
        }
        return xtile;
    }
}
