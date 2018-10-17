package web.schach.gruppe6.util;

import web.schach.gruppe6.obj.Position;

import static web.schach.gruppe6.obj.SchachPositionNotation.fromSchachPosition;

public class SchachUtils {
	
	public static final Position MIN = fromSchachPosition("a1");
	public static final Position MAX = fromSchachPosition("h8");
	public static final Position DELTA = MAX.sub(MIN);
}
