package web.schach.gruppe6.obj;

public class ChessPositionNotation {
	
	public static char toChessNotationCharX(Position pos) {
		return (char) ('a' + pos.x);
	}
	
	public static char toChessNotationCharY(Position pos) {
		return (char) ('1' + pos.y);
	}
	
	public static String toChessNotation(Position pos) {
		return new String(new char[] {toChessNotationCharX(pos), toChessNotationCharY(pos)});
	}
	
	public static Position fromChessNotation(char x, char y) {
		if (!('a' <= x && x <= 'h' && '1' <= y && y <= '8'))
			throw new IllegalArgumentException(new String(new char[] {x, y}));
		return new Position(x - 'a', y - '1');
	}
	
	public static Position fromChessNotation(String str) {
		if (str.length() != 2)
			throw new IllegalArgumentException("String '" + str + "'.length() != 2");
		return fromChessNotation(str.charAt(0), str.charAt(1));
	}
}
