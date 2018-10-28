package web.schach.gruppe6.obj;

import javafx.scene.image.Image;

import static web.schach.gruppe6.obj.PlayerColor.*;

public enum FigureType {
	
	WHITE_PAWN('P', WHITE, "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_white.png"),
	WHITE_KING('*', WHITE, "web/schach/gruppe6/gui/iconsAndImages/iconset1/king_white.png"),
	WHITE_QUEEN('Q', WHITE, "web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_white.png"),
	WHITE_KNIGHT('K', WHITE, "web/schach/gruppe6/gui/iconsAndImages/iconset1/knight_white.png"),
	WHITE_ROOK('R', WHITE, "web/schach/gruppe6/gui/iconsAndImages/iconset1/rook_white.png"),
	WHITE_BISHOP('B', WHITE, "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_white.png"),
	
	BLACK_PAWN('P', BLACK, "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_black.png"),
	BLACK_KING('*', BLACK, "web/schach/gruppe6/gui/iconsAndImages/iconset1/king_black.png"),
	BLACK_QUEEN('Q', BLACK, "web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_black.png"),
	BLACK_KNIGHT('K', BLACK, "web/schach/gruppe6/gui/iconsAndImages/iconset1/knight_black.png"),
	BLACK_ROOK('R', BLACK, "web/schach/gruppe6/gui/iconsAndImages/iconset1/rook_black.png"),
	BLACK_BISHOP('B', BLACK, "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_black.png");
	
	public final char character;
	public final PlayerColor color;
	public final Image icon;
	
	FigureType(char character, PlayerColor color, String icon) {
		this.character = character;
		this.color = color;
		this.icon = new Image(icon);
	}
}
