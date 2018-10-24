package web.schach.gruppe6.util;

import web.schach.gruppe6.obj.Position;

import java.util.Arrays;
import java.util.function.UnaryOperator;

import static web.schach.gruppe6.obj.Position.NULL_VECTOR;

public class Grid<T> {
	
	private final Position offset;
	private final Position size;
	private final T[] array;
	
	@SuppressWarnings("unchecked")
	public Grid(Position size) {
		this(NULL_VECTOR, size);
	}
	
	@SuppressWarnings("unchecked")
	public Grid(Position offset, Position size) {
		this(offset, size, (T[]) new Object[size.x * size.y]);
	}
	
	public Grid(Position offset, Position size, T[] array) {
		this.offset = offset;
		this.size = size;
		this.array = array;
	}
	
	//factory filler
	public static <T> Grid<T> createGridFiller(Position size, T filler) {
		return createGridFiller(NULL_VECTOR, size, filler);
	}
	
	public static <T> Grid<T> createGridFiller(Position offset, Position size, T filler) {
		Grid<T> grid = new Grid<>(offset, size);
		Arrays.fill(grid.array, filler);
		return grid;
	}
	
	//internal
	private int getIndex(Position position) throws ArrayIndexOutOfBoundsException {
		Position abs = position.sub(offset);
		if (!abs.boundsCheck(NULL_VECTOR, size))
			throw new ArrayIndexOutOfBoundsException(offset + " <= " + position + " < " + size.add(offset));
		return abs.y * size.x + abs.x;
	}
	
	//access
	public T get(Position position) throws ArrayIndexOutOfBoundsException {
		return array[getIndex(position)];
	}
	
	public void set(Position position, T obj) throws ArrayIndexOutOfBoundsException {
		array[getIndex(position)] = obj;
	}
	
	public void set(Position[] position, T obj) throws ArrayIndexOutOfBoundsException {
		int[] index = new int[position.length];
		for (int i = 0; i < position.length; i++)
			index[i] = getIndex(position[i]);
		for (int i : index)
			array[i] = obj;
	}
	
	public T replace(Position position, T obj) throws ArrayIndexOutOfBoundsException {
		int index = getIndex(position);
		T ret = array[index];
		array[index] = obj;
		return ret;
	}
	
	public T accumulate(Position position, UnaryOperator<T> function) {
		int index = getIndex(position);
		T ret = function.apply(array[index]);
		array[index] = ret;
		return ret;
	}
}
