package web.schach.gruppe6.obj;

import web.schach.gruppe6.util.Position;

import static web.schach.gruppe6.obj.ChessPositionNotation.fromChessNotation;
import static web.schach.gruppe6.obj.FigureType.*;

public enum Figures {
	
	WHITE_ROOK_1(WHITE_ROOK, fromChessNotation("a1")),
	WHITE_KNIGHT_1(WHITE_KNIGHT, fromChessNotation("b1")),
	WHITE_BISHOP_1(WHITE_BISHOP, fromChessNotation("c1")),
	WHITE_QUEEN_1(WHITE_QUEEN, fromChessNotation("d1")),
	WHITE_KING_1(WHITE_KING, fromChessNotation("e1")),
	WHITE_BISHOP_2(WHITE_BISHOP, fromChessNotation("f1")),
	WHITE_KNIGHT_2(WHITE_KNIGHT, fromChessNotation("g1")),
	WHITE_ROOK_2(WHITE_ROOK, fromChessNotation("h1")),
	WHITE_PAWN_1(WHITE_PAWN, fromChessNotation("a2")),
	WHITE_PAWN_2(WHITE_PAWN, fromChessNotation("b2")),
	WHITE_PAWN_3(WHITE_PAWN, fromChessNotation("c2")),
	WHITE_PAWN_4(WHITE_PAWN, fromChessNotation("d2")),
	WHITE_PAWN_5(WHITE_PAWN, fromChessNotation("e2")),
	WHITE_PAWN_6(WHITE_PAWN, fromChessNotation("f2")),
	WHITE_PAWN_7(WHITE_PAWN, fromChessNotation("g2")),
	WHITE_PAWN_8(WHITE_PAWN, fromChessNotation("h2")),
	
	BLACK_ROOK_1(BLACK_ROOK, fromChessNotation("a8")),
	BLACK_KNIGHT_1(BLACK_KNIGHT, fromChessNotation("b8")),
	BLACK_BISHOP_1(BLACK_BISHOP, fromChessNotation("c8")),
	BLACK_QUEEN_1(BLACK_QUEEN, fromChessNotation("d8")),
	BLACK_KING_1(BLACK_KING, fromChessNotation("e8")),
	BLACK_BISHOP_2(BLACK_BISHOP, fromChessNotation("f8")),
	BLACK_KNIGHT_2(BLACK_KNIGHT, fromChessNotation("g8")),
	BLACK_ROOK_2(BLACK_ROOK, fromChessNotation("h8")),
	BLACK_PAWN_1(BLACK_PAWN, fromChessNotation("a7")),
	BLACK_PAWN_2(BLACK_PAWN, fromChessNotation("b7")),
	BLACK_PAWN_3(BLACK_PAWN, fromChessNotation("c7")),
	BLACK_PAWN_4(BLACK_PAWN, fromChessNotation("d7")),
	BLACK_PAWN_5(BLACK_PAWN, fromChessNotation("e7")),
	BLACK_PAWN_6(BLACK_PAWN, fromChessNotation("f7")),
	BLACK_PAWN_7(BLACK_PAWN, fromChessNotation("g7")),
	BLACK_PAWN_8(BLACK_PAWN, fromChessNotation("h7"));
	
	public final FigureType type;
	public final Position positionInitial;
	public final Position positionBeaten;
	
	Figures(FigureType type, Position positionInitial) {
		this.type = type;
		this.positionInitial = positionInitial;
		this.positionBeaten = new Position(positionInitial.x, positionInitial.y <= 2 ? positionInitial.y : positionInitial.y - 6);
	}
	
	public String getIconPath() {
		return type.iconPath;
	}
}
