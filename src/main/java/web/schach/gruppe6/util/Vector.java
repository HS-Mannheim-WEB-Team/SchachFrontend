package web.schach.gruppe6.util;

import java.util.Objects;

/**
 * x: primary, vertical, '-'
 * y: secondary, horizontal, '|'
 * 0|0 is the lower left corner
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
public final class Vector {
	
	public static final Vector NULL_VECTOR = new Vector(0, 0);
	
	public final double x;
	public final double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(String schachPosition) {
		this(schachPosition.charAt(0) - 'a', schachPosition.charAt(1));
	}
	
	public Vector add(Vector pos) {
		return new Vector(x + pos.x, y + pos.y);
	}
	
	public Vector sub(Vector pos) {
		return new Vector(x - pos.x, y - pos.y);
	}
	
	public Vector negate() {
		return new Vector(-x, -y);
	}
	
	public Vector multiply(float multi) {
		return new Vector(x * multi, y * multi);
	}
	
	public boolean boundsCheck(Vector min, Vector max) {
		return min.x <= x && x < max.x && min.y <= y && y < max.y;
	}
	
	public Position toPosition() {
		return new Position((int) x, (int) y);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Vector))
			return false;
		Vector position = (Vector) o;
		return x == position.x && y == position.y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public String toString() {
		return x + "|" + y;
	}
}
