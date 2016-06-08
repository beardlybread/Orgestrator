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

import java.io.IOException;
import java.io.OutputStream;

public abstract class OrgNode {

    protected OrgNode parent = null;
    public final int indent;

    private String cachedFullPath = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgNode () {
        this.indent = 0;
    }

    public OrgNode (int indent) {
        this.indent = indent;
    }

    public OrgNode (TerminalNode indent) {
        this.indent = indent == null ? 0 : indent.getText().length();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public String getFullPath () {
        if (this.cachedFullPath == null) {
            if (this.parent == null)
                return "";
            if (this.isType("OrgFile"))
                return ((OrgFile) this).getResourcePath();
            if (this.parent.isType("OrgFile"))
                return ((OrgFile) this.parent).getResourcePath();
            StringBuilder buf = new StringBuilder();
            OrgNode p = this.parent;
            while (!p.isType("OrgFile")) {
                while (!p.isType("OrgHeading", "OrgToDo", "OrgFile"))
                    p = p.parent;
                if (p.isType("OrgFile"))
                    continue;
                buf.insert(0, p.toString()).insert(0, "/");
                p = p.parent;
            }
            buf.insert(0, ((OrgFile) p).getResourcePath());
            this.cachedFullPath = buf.toString();
        }
        return this.cachedFullPath;
    }

    public OrgNode getParent () { return this.parent; }

    public boolean isType (String... name) {
        for (String t: name) {
            if (this.getClass().getSimpleName().equals(t))
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

    public void write (OutputStream target) throws IOException {
        target.write(this.toOrgString().getBytes());
    }

}
