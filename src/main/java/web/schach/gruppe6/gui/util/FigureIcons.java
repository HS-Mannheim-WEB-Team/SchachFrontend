package web.schach.gruppe6.gui.util;

public enum FigureIcons {
    PAWN() {
        public String getPath(boolean isWhite) {
            if (isWhite)
                return "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_white.png";
            return "web/schach/gruppe6/gui/iconsAndImages/iconset1/pawn_black.png";
        }
    },

    KING() {
        public String getPath(boolean isWhite) {
            if (isWhite)
                return "web/schach/gruppe6/gui/iconsAndImages/iconset1/king_white.png";
            return "web/schach/gruppe6/gui/iconsAndImages/iconset1/king_black.png";
        }
    },

    QUEEN() {
        public String getPath(boolean isWhite) {
            if (isWhite)
                return "web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_white.png";
            return "web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_black.png";
        }
    },

    KNIGHT() {
        public String getPath(boolean isWhite) {
            if (isWhite)
                return "web/schach/gruppe6/gui/iconsAndImages/iconset1/knight_white.png";
            return "web/schach/gruppe6/gui/iconsAndImages/iconset1/knight_black.png";
        }
    },

    ROOK() {
        public String getPath(boolean isWhite) {
            if (isWhite)
                return "web/schach/gruppe6/gui/iconsAndImages/iconset1/rook_white.png";
            return "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_black.png";
        }
    },

    BISHOP() {
        public String getPath(boolean isWhite) {
            if (isWhite)
                return "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_white.png";
            return "web/schach/gruppe6/gui/iconsAndImages/iconset1/bishop_black.png";
        }
    };


    public abstract String getPath(boolean isWhite);

}
