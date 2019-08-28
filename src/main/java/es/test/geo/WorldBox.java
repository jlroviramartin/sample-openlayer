package es.test.geo;

import java.text.MessageFormat;

public class WorldBox {
    public WorldBox(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        assert minLatitude <= maxLatitude && minLongitude <= maxLongitude;
        if (!(minLatitude <= maxLatitude && minLongitude <= maxLongitude)) {
            System.out.println("Cagada");
        }

        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }

    public WorldBox(World min, World max) {
        this(min.latitude, max.latitude, min.longitude, max.longitude);
    }

    public final double minLatitude; // latitude (North/South)
    public final double maxLatitude; // latitude (North/South)
    public final double minLongitude; // longitude (East/West)
    public final double maxLongitude; // longitude (East/West)

    @Override
    public String toString() {
        return MessageFormat.format("Lat/Long: {0,number,#.##} ; {1,number,#.##} ; {2,number,#.##} ; {3,number,#.##}",
                minLatitude, maxLatitude, minLongitude, maxLongitude);
    }
}
