package com.mycila;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class Matrix<T> {

    protected final T[][] matrix;
    protected final int rows;
    protected final int columns;

    @SuppressWarnings({"unchecked"})
    private Matrix(int rows, int columns) {
        matrix = (T[][]) new Object[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    public int rowCount() {
        return rows;
    }

    public int columnCount() {
        return columns;
    }

    public Matrix<T> set(int row, int col, T val) {
        matrix[row][col] = val;
        return this;
    }

    public T get(int row, int col) {
        if (matrix[row][col] == null) throw new IllegalArgumentException("(" + row + "," + col + ") not set");
        return matrix[row][col];
    }

    public T find(int row, int col) {
        return matrix[row][col];
    }

    public boolean isSet(int row, int col) {
        return matrix[row][col] != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < rowCount(); row++, sb.append("\n"))
            for (int col = 0; col < columnCount(); col++)
                sb.append(matrix[row][col]).append(" ");
        return sb.toString();
    }

    public static <T> Matrix<T> create(int rows, int columns) {
        return new Matrix<T>(rows, columns);
    }
}
