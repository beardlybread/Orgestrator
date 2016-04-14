package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;

public abstract class BaseOrgNode implements OrgNode {

    protected OrgNode parent = null;
    public final String type;

    BaseOrgNode () {
        this.type = this.getClass().getName();
    }

    BaseOrgNode (OrgNode parent) {
        this();
        this.parent = parent;
    }

    @Override
    public String toString() { return this.toString(); }

    @Override
    public String getType () { return this.type; }

    @Override
    public void write (Writer target) throws IOException {
        target.write(this.toString());
    }

}
