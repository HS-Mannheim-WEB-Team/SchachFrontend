package web.schach.gruppe6.obj;

import static web.schach.gruppe6.obj.FigureType.*;
import static web.schach.gruppe6.obj.PlayerColor.*;

public enum Figures {
	
	WHITE_ROOK_1(ROOK, WHITE, SchachPositionNotation.fromSchachPosition("a1")),
	WHITE_KNIGHT_1(KNIGHT, WHITE, SchachPositionNotation.fromSchachPosition("b1")),
	WHITE_BISHOP_1(BISHOP, WHITE, SchachPositionNotation.fromSchachPosition("c1")),
	WHITE_QUEEN(QUEEN, WHITE, SchachPositionNotation.fromSchachPosition("d1")),
	WHITE_KING(KING, WHITE, SchachPositionNotation.fromSchachPosition("e1")),
	WHITE_BISHOP_2(BISHOP, WHITE, SchachPositionNotation.fromSchachPosition("f1")),
	WHITE_KNIGHT_2(KNIGHT, WHITE, SchachPositionNotation.fromSchachPosition("g1")),
	WHITE_ROOK_2(ROOK, WHITE, SchachPositionNotation.fromSchachPosition("h1")),
	WHITE_PAWN_1(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("a2")),
	WHITE_PAWN_2(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("b2")),
	WHITE_PAWN_3(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("c2")),
	WHITE_PAWN_4(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("d2")),
	WHITE_PAWN_5(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("e2")),
	WHITE_PAWN_6(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("f2")),
	WHITE_PAWN_7(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("g2")),
	WHITE_PAWN_8(PAWN, WHITE, SchachPositionNotation.fromSchachPosition("h2")),
	
	BLACK_ROOK_1(ROOK, BLACK, SchachPositionNotation.fromSchachPosition("a8")),
	BLACK_KNIGHT_1(KNIGHT, BLACK, SchachPositionNotation.fromSchachPosition("b8")),
	BLACK_BISHOP_1(BISHOP, BLACK, SchachPositionNotation.fromSchachPosition("c8")),
	BLACK_QUEEN(QUEEN, BLACK, SchachPositionNotation.fromSchachPosition("d8")),
	BLACK_KING(KING, BLACK, SchachPositionNotation.fromSchachPosition("e8")),
	BLACK_BISHOP_2(BISHOP, BLACK, SchachPositionNotation.fromSchachPosition("f8")),
	BLACK_KNIGHT_2(KNIGHT, BLACK, SchachPositionNotation.fromSchachPosition("g8")),
	BLACK_ROOK_2(ROOK, BLACK, SchachPositionNotation.fromSchachPosition("h8")),
	BLACK_PAWN_1(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("a7")),
	BLACK_PAWN_2(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("b7")),
	BLACK_PAWN_3(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("c7")),
	BLACK_PAWN_4(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("d7")),
	BLACK_PAWN_5(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("e7")),
	BLACK_PAWN_6(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("f7")),
	BLACK_PAWN_7(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("g7")),
	BLACK_PAWN_8(PAWN, BLACK, SchachPositionNotation.fromSchachPosition("h7"));
	
	public final FigureType type;
	public final PlayerColor color;
	public final Position initial;
	
	Figures(FigureType type, PlayerColor color, Position initial) {
		this.type = type;
		this.color = color;
		this.initial = initial;
	}
	
	public String getIconPath() {
		return color == WHITE ? type.iconPathWhite : type.iconPathBlack;
	}
}
