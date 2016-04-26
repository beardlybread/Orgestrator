package com.github.beardlybread.orgestrator.org;

import java.util.ArrayList;

public class OrgText extends OrgNode {

    protected ArrayList<String> lines = null;
    private String cache = null;
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

    public int indexOf (String line) { return this.lines.indexOf(line); }

    @Override
    public String toString () {
        if (!fresh) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.lines.get(0));
            for (int l = 1; l < this.lines.size(); ++l) {
                for (int i = 0; i < this.indent; ++i)
                    sb.append(" ");
                sb.append(this.lines.get(l)).append("\n");
            }
            this.cache = sb.toString();
            this.fresh = true;
        }
        return this.cache;
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
