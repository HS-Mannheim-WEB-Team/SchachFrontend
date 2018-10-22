package web.schach.gruppe6.obj;

import static web.schach.gruppe6.obj.ChessPositionNotation.fromChessNotation;
import static web.schach.gruppe6.obj.FigureType.*;
import static web.schach.gruppe6.obj.PlayerColor.*;

public enum Figures {
	
	WHITE_ROOK_1(ROOK, WHITE, fromChessNotation("a1")),
	WHITE_KNIGHT_1(KNIGHT, WHITE, fromChessNotation("b1")),
	WHITE_BISHOP_1(BISHOP, WHITE, fromChessNotation("c1")),
	WHITE_QUEEN(QUEEN, WHITE, fromChessNotation("d1")),
	WHITE_KING(KING, WHITE, fromChessNotation("e1")),
	WHITE_BISHOP_2(BISHOP, WHITE, fromChessNotation("f1")),
	WHITE_KNIGHT_2(KNIGHT, WHITE, fromChessNotation("g1")),
	WHITE_ROOK_2(ROOK, WHITE, fromChessNotation("h1")),
	WHITE_PAWN_1(PAWN, WHITE, fromChessNotation("a2")),
	WHITE_PAWN_2(PAWN, WHITE, fromChessNotation("b2")),
	WHITE_PAWN_3(PAWN, WHITE, fromChessNotation("c2")),
	WHITE_PAWN_4(PAWN, WHITE, fromChessNotation("d2")),
	WHITE_PAWN_5(PAWN, WHITE, fromChessNotation("e2")),
	WHITE_PAWN_6(PAWN, WHITE, fromChessNotation("f2")),
	WHITE_PAWN_7(PAWN, WHITE, fromChessNotation("g2")),
	WHITE_PAWN_8(PAWN, WHITE, fromChessNotation("h2")),
	
	BLACK_ROOK_1(ROOK, BLACK, fromChessNotation("a8")),
	BLACK_KNIGHT_1(KNIGHT, BLACK, fromChessNotation("b8")),
	BLACK_BISHOP_1(BISHOP, BLACK, fromChessNotation("c8")),
	BLACK_QUEEN(QUEEN, BLACK, fromChessNotation("d8")),
	BLACK_KING(KING, BLACK, fromChessNotation("e8")),
	BLACK_BISHOP_2(BISHOP, BLACK, fromChessNotation("f8")),
	BLACK_KNIGHT_2(KNIGHT, BLACK, fromChessNotation("g8")),
	BLACK_ROOK_2(ROOK, BLACK, fromChessNotation("h8")),
	BLACK_PAWN_1(PAWN, BLACK, fromChessNotation("a7")),
	BLACK_PAWN_2(PAWN, BLACK, fromChessNotation("b7")),
	BLACK_PAWN_3(PAWN, BLACK, fromChessNotation("c7")),
	BLACK_PAWN_4(PAWN, BLACK, fromChessNotation("d7")),
	BLACK_PAWN_5(PAWN, BLACK, fromChessNotation("e7")),
	BLACK_PAWN_6(PAWN, BLACK, fromChessNotation("f7")),
	BLACK_PAWN_7(PAWN, BLACK, fromChessNotation("g7")),
	BLACK_PAWN_8(PAWN, BLACK, fromChessNotation("h7"));
	
	public final FigureType type;
	public final PlayerColor color;
	public final Position positionInitial;
	public final Position positionBeaten;
	
	Figures(FigureType type, PlayerColor color, Position positionInitial) {
		this.type = type;
		this.color = color;
		this.positionInitial = positionInitial;
		this.positionBeaten = new Position(positionInitial.x, positionInitial.y <= 2 ? positionInitial.y : positionInitial.y - 6);
	}
	
	public String getIconPath() {
		return color == WHITE ? type.iconPathWhite : type.iconPathBlack;
	}
}
