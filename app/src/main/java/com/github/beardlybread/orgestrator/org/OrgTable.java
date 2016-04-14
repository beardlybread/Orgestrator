package com.github.beardlybread.orgestrator.org;

public class OrgTable extends BaseTreeNode {

    protected String[][] data = null;

    OrgTable () {}

    OrgTable (int nRows, int nCols) {
        this.data = new String[nRows][nCols];
    }
}
