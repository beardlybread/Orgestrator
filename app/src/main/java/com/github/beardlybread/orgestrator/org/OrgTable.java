package com.github.beardlybread.orgestrator.org;

public class OrgTable extends BaseOrgNode {

    public final int rows;
    public final int cols;

    protected String[][] data = null;

    OrgTable (int nRows, int nCols, int indent) {
        super(indent);
        this.rows = nRows;
        this.cols = nCols;
        this.data = new String[nRows][nCols];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public String get(int row, int col) throws IndexOutOfBoundsException {
        return this.data[row][col];
    }

    // TODO (feature) Justify columns.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                sb.append(c == 0 ? "| " : " | ");
                sb.append(this.data[r][c]);
            }
            sb.append(" |\n");
        }
        return sb.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void set(int row, int col, String value) throws IndexOutOfBoundsException {
        this.data[row][col] = value;
    }

}
