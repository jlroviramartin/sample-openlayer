package es.test.geo;

/**
 * {@link https://docs.microsoft.com/en-us/bingmaps/articles/bing-maps-tile-system}
 */
public class TileSystem {
	private static final double EarthRadius = 6378137;
	private static final double MaxLatitude = 85.05112878;
	private static final double MinLatitude = -MaxLatitude;
	private static final double MaxLongitude = 180;
	private static final double MinLongitude = -MaxLongitude;

	private final int size;

	public TileSystem() {
		this(256);
	}

	public TileSystem(int size) {
		this.size = size;
	}

	/**
	 * Clips a number to the specified minimum and maximum values.
	 * <p>
	 * 
	 * @param n
	 *            The number to clip.
	 * @param minValue
	 *            Minimum allowable value.
	 * @param maxValue
	 *            Maximum allowable value.
	 * @return The clipped value.
	 */
	private static double clip(double n, double minValue, double maxValue) {
		return Math.min(Math.max(n, minValue), maxValue);
	}

	/**
	 * Determines the map width and height (in pixels) at a specified level of
	 * detail.
	 * <p>
	 * 
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail).
	 * @return The map width and height in pixels.
	 */
	public int mapSize(int levelOfDetail) {
		return (int) size << levelOfDetail;
	}

	/**
	 * Gets the origin of the tile for the pixel.
	 * 
	 * @param pixelX
	 * @param pixelY
	 * @return
	 */
	public Pixel getOrigin(Pixel pixel) {
		int tileX = (pixel.x / size) * size;
		int tileY = (pixel.y / size) * size;
		return new Pixel(tileX, tileY);
	}

	/**
	 * Determines the ground resolution (in meters per pixel) at a specified
	 * latitude and level of detail.
	 * <p>
	 * 
	 * @param latitude
	 *            Latitude (in degrees) at which to measure the ground resolution.
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail).
	 * @return The ground resolution, in meters per pixel.
	 */
	public double groundResolution(double latitude, int levelOfDetail) {
		latitude = clip(latitude, MinLatitude, MaxLatitude);
		return Math.cos(Math.toRadians(latitude)) * 2 * Math.PI * EarthRadius / mapSize(levelOfDetail);
	}

	/**
	 * Determines the map scale at a specified latitude, level of detail, and screen
	 * resolution.
	 * <p>
	 * 
	 * @param latitude
	 *            Latitude (in degrees) at which to measure the map scale.
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail).
	 * @param screenDpi
	 *            Resolution of the screen, in dots per inch.
	 * @return The map scale, expressed as the denominator N of the ratio 1 : N.
	 */
	public double mapScale(double latitude, int levelOfDetail, int screenDpi) {
		return groundResolution(latitude, levelOfDetail) * screenDpi / 0.0254;
	}

	/**
	 * Converts a point from latitude/longitude WGS-84 coordinates (in degrees) into
	 * pixel XY coordinates at a specified level of detail.
	 * <p>
	 * 
	 * @param latitude
	 *            Latitude of the point, in degrees.
	 * @param longitude
	 *            Longitude of the point, in degrees.
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail).
	 * @param pixelX
	 *            Output parameter receiving the X coordinate in pixels.
	 * @param pixelY
	 *            Output parameter receiving the Y coordinate in pixels.
	 */
	public Pixel latLongToPixelXY(World world, int levelOfDetail) {
		double latitude = clip(world.latitude, MinLatitude, MaxLatitude);
		double longitude = clip(world.longitude, MinLongitude, MaxLongitude);

		double x = (longitude + 180) / 360;
		double sinLatitude = Math.sin(Math.toRadians(latitude));
		double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

		int mapSize = mapSize(levelOfDetail);
		int pixelX = (int) clip(x * mapSize + 0.5, 0, mapSize - 1);
		int pixelY = (int) clip(y * mapSize + 0.5, 0, mapSize - 1);
		return new Pixel(pixelX, pixelY);
	}

	/**
	 * Converts a pixel from pixel XY coordinates at a specified level of detail
	 * into latitude/longitude WGS-84 coordinates (in degrees).
	 * <p>
	 * 
	 * @param pixelX
	 *            X coordinate of the point, in pixels.
	 * @param pixelY
	 *            Y coordinates of the point, in pixels.
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail).
	 * @param latitude
	 *            Output parameter receiving the latitude in degrees.
	 * @param longitude
	 *            Output parameter receiving the longitude in degrees.
	 */
	public World pixelXYToLatLong(Pixel pixel, int levelOfDetail) {
		double mapSize = mapSize(levelOfDetail);
		double x = (clip(pixel.x, 0, mapSize - 1) / mapSize) - 0.5;
		double y = 0.5 - (clip(pixel.y, 0, mapSize - 1) / mapSize);

		double latitude = 90 - 2 * Math.toDegrees(Math.atan(Math.exp(-y * 2 * Math.PI)));
		double longitude = 360 * x;
		return new World(latitude, longitude);
	}

	/**
	 * Converts pixel XY coordinates into tile XY coordinates of the tile containing
	 * the specified pixel.
	 * <p>
	 * 
	 * @param pixelX
	 *            Pixel X coordinate.
	 * @param pixelY
	 *            Pixel Y coordinate.
	 * @param tileX
	 *            Output parameter receiving the tile X coordinate.
	 * @param tileY
	 *            Output parameter receiving the tile Y coordinate.
	 */
	public Tile pixelXYToTileXY(Pixel pixel) {
		int tileX = pixel.x / size;
		int tileY = pixel.y / size;
		return new Tile(tileX, tileY);
	}

	/**
	 * Converts tile XY coordinates into pixel XY coordinates of the upper-left
	 * pixel of the specified tile.
	 * <p>
	 * 
	 * @param tileX
	 *            Tile X coordinate.
	 * @param tileY
	 *            Tile Y coordinate.
	 * @param pixelX
	 *            Output parameter receiving the pixel X coordinate.
	 * @param pixelY
	 *            Output parameter receiving the pixel Y coordinate.
	 */
	public Pixel tileXYToPixelXY(Tile tile) {
		int pixelX = tile.x * size;
		int pixelY = tile.y * size;
		return new Pixel(pixelX, pixelY);
	}

	/**
	 * Converts tile XY coordinates into a QuadKey at a specified level of detail.
	 * <p>
	 * 
	 * @param tileX
	 *            Tile X coordinate.
	 * @param tileY
	 *            Tile Y coordinate.
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail).
	 * @return A String containing the QuadKey.
	 */
	public String tileXYToQuadKey(Tile tile, int levelOfDetail) {
		StringBuilder quadKey = new StringBuilder();
		for (int i = levelOfDetail; i > 0; i--) {
			char digit = '0';
			int mask = 1 << (i - 1);
			if ((tile.x & mask) != 0) {
				digit++;
			}
			if ((tile.y & mask) != 0) {
				digit++;
				digit++;
			}
			quadKey.append(digit);
		}
		return quadKey.toString();
	}

	/**
	 * Converts a QuadKey into tile XY coordinates.
	 * <p>
	 * 
	 * @param quadKey
	 *            QuadKey of the tile.
	 * @param tileX
	 *            Output parameter receiving the tile X coordinate.
	 * @param tileY
	 *            Output parameter receiving the tile Y coordinate.
	 * @param levelOfDetail
	 *            Output parameter receiving the level of detail.
	 */
	public KeyTile quadKeyToTileXY(String quadKey) {
		int tileX = 0;
		int tileY = 0;
		int levelOfDetail = quadKey.length();
		for (int i = levelOfDetail; i > 0; i--) {
			int mask = 1 << (i - 1);
			switch (quadKey.charAt(levelOfDetail - i)) {
			case '0':
				break;

			case '1':
				tileX |= mask;
				break;

			case '2':
				tileY |= mask;
				break;

			case '3':
				tileX |= mask;
				tileY |= mask;
				break;

			default:
				throw new IllegalArgumentException("Invalid QuadKey digit sequence.");
			}
		}
		return new KeyTile(tileX, tileY, levelOfDetail);
	}

	public WorldBox getBox(Tile tile, int levelOfDetail) {
		Pixel minPixelXY = tileXYToPixelXY(tile);
		Pixel maxPixelXY = tileXYToPixelXY(new Tile(tile.x + 1, tile.y + 1));

		World minLatLon = pixelXYToLatLong(minPixelXY, levelOfDetail);
		World maxLatLon = pixelXYToLatLong(maxPixelXY, levelOfDetail);

		double lat1 = minLatLon.latitude;
		double lat2 = maxLatLon.latitude;
		double lon1 = minLatLon.longitude;
		double lon2 = maxLatLon.longitude;

		return new WorldBox(Math.min(lat1, lat2), Math.max(lat1, lat2), Math.min(lon1, lon2), Math.max(lon1, lon2));
	}
}
