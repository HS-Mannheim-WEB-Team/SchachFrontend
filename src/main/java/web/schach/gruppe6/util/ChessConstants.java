package web.schach.gruppe6.util;

import web.schach.gruppe6.obj.Position;

import static web.schach.gruppe6.obj.ChessPositionNotation.fromChessNotation;

public class ChessConstants {
	
	public static final Position MIN = fromChessNotation("a1");
	public static final Position MAX = fromChessNotation("h8").add(new Position(1, 1));
	public static final Position DELTA = MAX.sub(MIN);
	
	public static <T> Grid<T> createChessGrid() {
		return new Grid<>(ChessConstants.MIN, ChessConstants.DELTA);
	}
}
