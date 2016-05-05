package com.github.beardlybread.orgestrator.org;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.regex.Pattern;


public class OrgTable extends OrgNode {

    public static final Pattern MATCH_SEPARATOR = Pattern.compile("(-+\\+)*-+");
    public final int rows;
    public final int cols;

    protected String[][] data = null;

    public OrgTable (int nRows, int nCols, int indent) {
        super(indent);
        this.rows = nRows;
        this.cols = nCols;
        this.data = new String[nRows][nCols];
    }

    public OrgTable (int nRows, int nCols, TerminalNode indent) {
        super(indent);
        this.rows = nRows;
        this.cols = nCols;
        this.data = new String[nRows][nCols];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public String get (int row, int col) throws IndexOutOfBoundsException {
        return this.data[row][col];
    }

    // TODO (feature) Justify columns.
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < this.rows; r++) {
            boolean isSeparator = MATCH_SEPARATOR.matcher(this.data[r][0]).matches();
            for (int c = 0; c < this.cols; c++) {
                if (isSeparator && c > 0)
                    break;
                sb.append("|");
                sb.append(this.data[r][c]);
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void set (int row, int col, String value) throws IndexOutOfBoundsException {
        this.data[row][col] = value;
    }

}
