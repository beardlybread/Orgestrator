package com.github.beardlybread.orgestrator.org;

import java.util.ArrayList;

public class OrgText extends BaseOrgNode {

    protected ArrayList<String> lines = null;
    private String cache = null;
    private boolean fresh = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgText (BaseTreeNode parent) {
        super(parent);
        this.lines = new ArrayList<>();
    }

    public OrgText (BaseTreeNode parent, String line) {
        this(parent);
        this.overwrite(line);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public int indexOf (String line) { return this.lines.indexOf(line); }

    @Override
    public String toString () {
        if (!fresh) {
            StringBuilder sb = new StringBuilder();
            for (String l: this.lines) {
                sb.append(l);
            }
            this.cache = sb.toString();
            this.fresh = true;
        }
        return this.cache;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void add (String line) { this.lines.add(line); }

    public void overwrite (String line) {
        this.lines.clear();
        this.lines.add(line);
        this.fresh = false;
    }

    public void overwrite (int index, String line) { this.lines.set(index, line); }

    public void overwrite (OrgText text) {
        this.lines = text.lines;
        this.fresh = false;
    }


}
