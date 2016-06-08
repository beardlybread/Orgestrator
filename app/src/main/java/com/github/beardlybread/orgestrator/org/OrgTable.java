/*
The MIT License (MIT)

Copyright (c) 2016 Bradley Steinbacher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < this.rows; r++) {
            this.doIndent(sb);
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

    @Override
    public String toString () {
        return this.toOrgString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void set (int row, int col, String value) throws IndexOutOfBoundsException {
        this.data[row][col] = value;
    }

}
