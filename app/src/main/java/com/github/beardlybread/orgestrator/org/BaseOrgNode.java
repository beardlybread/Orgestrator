package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;

public abstract class BaseOrgNode implements OrgNode {

    protected BaseOrgNode parent = null;
    public final String type;
    public final int indent;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    BaseOrgNode () {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = 0;
    }

    BaseOrgNode (int indent) {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = indent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public String getType () { return this.type; }

    @Override
    public boolean isType (String name) {
        return this.type.equals(name);
    }

    @Override
    public String toString() { return this.toString(); }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void setParent (BaseOrgNode parent) { this.parent = parent; }

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void write (Writer target) throws IOException {
        target.write(this.toString());
    }

}
