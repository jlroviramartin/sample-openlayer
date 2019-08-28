package es.test.geo;

public class KeyTile extends Tile {
	public KeyTile(int x, int y, int z) {
		super(x, y);
		this.z = z;
	}

	public final int z;
}
