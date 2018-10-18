package web.schach.gruppe6.obj;

import static web.schach.gruppe6.obj.FigureType.*;
import static web.schach.gruppe6.obj.PlayerColor.*;

public enum Figures {
	
	BLACK_KING(KING, BLACK),
	BLACK_QUEEN(QUEEN, BLACK),
	BLACK_ROOK_1(ROOK, BLACK),
	BLACK_ROOK_2(ROOK, BLACK),
	BLACK_BISHOP_1(BISHOP, BLACK),
	BLACK_BISHOP_2(BISHOP, BLACK),
	BLACK_KNIGHT_1(KNIGHT, BLACK),
	BLACK_KNIGHT_2(KNIGHT, BLACK),
	BLACK_PAWN_1(PAWN, BLACK),
	BLACK_PAWN_2(PAWN, BLACK),
	BLACK_PAWN_3(PAWN, BLACK),
	BLACK_PAWN_4(PAWN, BLACK),
	BLACK_PAWN_5(PAWN, BLACK),
	BLACK_PAWN_6(PAWN, BLACK),
	BLACK_PAWN_7(PAWN, BLACK),
	BLACK_PAWN_8(PAWN, BLACK),
	
	WHITE_KING(KING, WHITE),
	WHITE_QUEEN(QUEEN, WHITE),
	WHITE_ROOK_1(ROOK, WHITE),
	WHITE_ROOK_2(ROOK, WHITE),
	WHITE_BISHOP_1(BISHOP, WHITE),
	WHITE_BISHOP_2(BISHOP, WHITE),
	WHITE_KNIGHT_1(KNIGHT, WHITE),
	WHITE_KNIGHT_2(KNIGHT, WHITE),
	WHITE_PAWN_1(PAWN, WHITE),
	WHITE_PAWN_2(PAWN, WHITE),
	WHITE_PAWN_3(PAWN, WHITE),
	WHITE_PAWN_4(PAWN, WHITE),
	WHITE_PAWN_5(PAWN, WHITE),
	WHITE_PAWN_6(PAWN, WHITE),
	WHITE_PAWN_7(PAWN, WHITE),
	WHITE_PAWN_8(PAWN, WHITE);
	
	public final FigureType type;
	public final PlayerColor color;
	
	Figures(FigureType type, PlayerColor color) {
		this.type = type;
		this.color = color;
	}
	
	public String getIconPath() {
		return color == WHITE ? type.iconPathWhite : type.iconPathBlack;
	}
}
