package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;

public abstract class BaseOrgNode implements OrgNode {

    protected OrgNode parent = null;
    public final String type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    BaseOrgNode () { this.type = this.getClass().getName(); }

    BaseOrgNode (OrgNode parent) {
        this();
        this.parent = parent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public String getType () { return this.type; }

    @Override
    public boolean isType (String name) {
        int i = this.type.lastIndexOf(".");
        return this.type.equalsIgnoreCase(name) || this.type.substring(i).equalsIgnoreCase(name);
    }

    @Override
    public String toString() { return this.toString(); }

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void write (Writer target) throws IOException {
        target.write(this.toString());
    }

}
