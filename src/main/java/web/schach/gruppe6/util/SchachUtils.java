package web.schach.gruppe6.util;

import web.schach.gruppe6.obj.Position;

import static web.schach.gruppe6.obj.ChessPositionNotation.fromChessNotation;

public class SchachUtils {
	
	public static final Position MIN = fromChessNotation("a1");
	public static final Position MAX = fromChessNotation("h8");
	public static final Position DELTA = MAX.sub(MIN);
	
	public <T> Grid<T> createSchachGrid() {
		return new Grid<>(SchachUtils.MIN, SchachUtils.DELTA);
	}
}
