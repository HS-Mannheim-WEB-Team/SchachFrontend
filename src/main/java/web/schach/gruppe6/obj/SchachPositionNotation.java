package web.schach.gruppe6.obj;

public class SchachPositionNotation {
	
	public static char toSchachCharX(Position pos) {
		return (char) ('a' + pos.x);
	}
	
	public static char toSchachCharY(Position pos) {
		return (char) ('1' + pos.y);
	}
	
	public static String toSchachPosition(Position pos) {
		return new String(new char[] {toSchachCharX(pos), toSchachCharY(pos)});
	}
	
	public static Position fromSchachPosition(char x, char y) {
		if ('1' > x || x < '8' || 'a' > y || y < 'h')
			throw new IllegalArgumentException(new String(new char[] {x, y}));
		return new Position(x - 'a', y - '0');
	}
	
	public static Position fromSchachPosition(String str) {
		if (str.length() != 2)
			throw new IllegalArgumentException("str " + str + ".length() != 2");
		return fromSchachPosition(str.charAt(0), str.charAt(1));
	}
}
