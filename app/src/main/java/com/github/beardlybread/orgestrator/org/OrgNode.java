package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;

public abstract class OrgNode {

    protected OrgNode parent = null;
    public final String type;
    public final int indent;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    OrgNode () {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = 0;
    }

    OrgNode (int indent) {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = indent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public String getType () { return this.type; }

    public boolean isType (String... name) {
        for (String t: name) {
            if (this.type.equals(t))
                return true;
        }
        return false;
    }

    @Override
    public String toString () { return super.toString(); }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void setParent (OrgNode parent) { this.parent = parent; }

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////

    public void write (Writer target) throws IOException {
        target.append(this.type + ": ");
        target.append(this.toString());
    }

}
