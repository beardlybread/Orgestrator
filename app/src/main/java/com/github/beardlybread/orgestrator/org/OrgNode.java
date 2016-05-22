package com.github.beardlybread.orgestrator.org;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.io.Writer;

public abstract class OrgNode {

    protected OrgNode parent = null;
    public final String type;
    public final int indent;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgNode () {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = 0;
    }

    public OrgNode (int indent) {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = indent;
    }

    public OrgNode (TerminalNode indent) {
        String type = this.getClass().getName();
        this.type = type.substring(type.lastIndexOf(".") + 1);
        this.indent = indent == null ? 0 : indent.getText().length();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public OrgNode getParent () { return this.parent; }

    public boolean isType (String... name) {
        for (String t: name) {
            if (this.type.equals(t))
                return true;
        }
        return false;
    }

    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        this.doIndent(sb);
        return sb.append(super.toString()).toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void setParent (OrgNode parent) { this.parent = parent; }

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////

    public void doIndent (StringBuilder sb) {
        for (int i = 0; i < this.indent; ++i)
            sb.append(" ");
    }

    public void write (Writer target) throws IOException {
        target.append(this.toOrgString());
        // target.append("type: " + this.type + "\n\n");
    }

}
