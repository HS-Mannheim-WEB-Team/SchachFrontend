package web.schach.gruppe6.obj;

import web.schach.gruppe6.util.Grid;
import web.schach.gruppe6.util.SchachUtils;

public class Layout {
	
	public final Grid<Figure> grid = new Grid<>(SchachUtils.MIN, SchachUtils.DELTA);
}
