package web.schach.gruppe6.obj;

import java.util.Objects;

/**
 * x: primary, vertical, '-'
 * y: secondary, horizontal, '|'
 * 0|0 is the lower left corner
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
public final class Position {
	
	public static final Position NULL_VECTOR = new Position(0, 0);
	
	public final int x;
	public final int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(String schachPosition) {
		this(schachPosition.charAt(0) - 'a', schachPosition.charAt(1));
	}
	
	public Position add(Position pos) {
		return new Position(x + pos.x, y + pos.y);
	}
	
	public Position sub(Position pos) {
		return new Position(x + pos.x, y + pos.y);
	}
	
	public Position negate() {
		return new Position(-x, -y);
	}
	
	public boolean boundsCheck(Position min, Position max) {
		return min.x <= x && x < max.x && min.y <= y && y < max.y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Position))
			return false;
		Position position = (Position) o;
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
