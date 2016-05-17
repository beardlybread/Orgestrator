package com.github.beardlybread.orgestrator.org;

import java.util.ArrayList;

public class OrgText extends OrgNode {

    protected ArrayList<String> lines = null;
    private String cache = null;
    private String orgCache = null;
    private boolean fresh = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgText (String line, int indent) {
        super(indent);
        this.lines = new ArrayList<>();
        this.add(line);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public String toOrgString () {
        if (!this.fresh)
            this.refreshStrings();
        return this.orgCache;
    }

    @Override
    public String toString () {
        if (!this.fresh)
            this.refreshStrings();
        return this.cache;
    }

    private void refreshStrings () {
        StringBuilder rawString = new StringBuilder();
        StringBuilder orgString = new StringBuilder();
        rawString.append(this.lines.get(0).trim());
        orgString.append(this.lines.get(0));
        for (int l = 1; l < this.lines.size(); ++l) {
            rawString.append(" ").append(this.lines.get(l).trim());
            for (int i = 0; i < this.indent; ++i)
                orgString.append(" ");
            orgString.append(this.lines.get(l));
        }
        this.cache = rawString.toString();
        this.orgCache = orgString.toString();
        this.fresh = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void add (String line) {
        this.lines.add(line);
        this.fresh = false;
    }

    public void add (OrgText line) {
        this.lines.addAll(line.lines);
        this.fresh = false;
    }

    public void clear () {
        this.lines.clear();
        this.fresh = false;
    }

    public void overwrite (int index, String line) {
        this.lines.set(index, line);
        this.fresh = false;
    }

}
