package web.schach.gruppe6.obj;

import static web.schach.gruppe6.obj.FigureType.*;
import static web.schach.gruppe6.obj.PlayerColor.*;
import static web.schach.gruppe6.obj.SchachPositionNotation.fromSchachPosition;

public enum Figures {
	
	WHITE_ROOK_1(ROOK, WHITE, fromSchachPosition("a1")),
	WHITE_KNIGHT_1(KNIGHT, WHITE, fromSchachPosition("b1")),
	WHITE_BISHOP_1(BISHOP, WHITE, fromSchachPosition("c1")),
	WHITE_QUEEN(QUEEN, WHITE, fromSchachPosition("d1")),
	WHITE_KING(KING, WHITE, fromSchachPosition("e1")),
	WHITE_BISHOP_2(BISHOP, WHITE, fromSchachPosition("f1")),
	WHITE_KNIGHT_2(KNIGHT, WHITE, fromSchachPosition("g1")),
	WHITE_ROOK_2(ROOK, WHITE, fromSchachPosition("h1")),
	WHITE_PAWN_1(PAWN, WHITE, fromSchachPosition("a2")),
	WHITE_PAWN_2(PAWN, WHITE, fromSchachPosition("b2")),
	WHITE_PAWN_3(PAWN, WHITE, fromSchachPosition("c2")),
	WHITE_PAWN_4(PAWN, WHITE, fromSchachPosition("d2")),
	WHITE_PAWN_5(PAWN, WHITE, fromSchachPosition("e2")),
	WHITE_PAWN_6(PAWN, WHITE, fromSchachPosition("f2")),
	WHITE_PAWN_7(PAWN, WHITE, fromSchachPosition("g2")),
	WHITE_PAWN_8(PAWN, WHITE, fromSchachPosition("h2")),
	
	BLACK_ROOK_1(ROOK, BLACK, fromSchachPosition("a8")),
	BLACK_KNIGHT_1(KNIGHT, BLACK, fromSchachPosition("b8")),
	BLACK_BISHOP_1(BISHOP, BLACK, fromSchachPosition("c8")),
	BLACK_QUEEN(QUEEN, BLACK, fromSchachPosition("d8")),
	BLACK_KING(KING, BLACK, fromSchachPosition("e8")),
	BLACK_BISHOP_2(BISHOP, BLACK, fromSchachPosition("f8")),
	BLACK_KNIGHT_2(KNIGHT, BLACK, fromSchachPosition("g8")),
	BLACK_ROOK_2(ROOK, BLACK, fromSchachPosition("h8")),
	BLACK_PAWN_1(PAWN, BLACK, fromSchachPosition("a7")),
	BLACK_PAWN_2(PAWN, BLACK, fromSchachPosition("b7")),
	BLACK_PAWN_3(PAWN, BLACK, fromSchachPosition("c7")),
	BLACK_PAWN_4(PAWN, BLACK, fromSchachPosition("d7")),
	BLACK_PAWN_5(PAWN, BLACK, fromSchachPosition("e7")),
	BLACK_PAWN_6(PAWN, BLACK, fromSchachPosition("f7")),
	BLACK_PAWN_7(PAWN, BLACK, fromSchachPosition("g7")),
	BLACK_PAWN_8(PAWN, BLACK, fromSchachPosition("h7"));
	
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
