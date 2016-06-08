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

import java.util.ArrayList;

public class OrgList extends OrgTreeNode {

    public static final int ENUMERATED = 0;
    public static final int UNENUMERATED = 1;

    protected ArrayList<OrgList> siblings = null;
    public final int listType;
    public final String marker;

    public OrgList (int indent, String rawMarker) {
        super(indent);
        this.marker = rawMarker.trim();
        switch (this.marker) {
            case "-":
            case "+":
            case "*":
                this.listType = OrgList.UNENUMERATED;
                break;
            default:
                this.listType = OrgList.ENUMERATED;
        }
    }

    public OrgList (TerminalNode indent, String rawMarker, String rawText) {
        super(indent);
        this.marker = rawMarker.trim();
        switch (this.marker) {
            case "-":
            case "+":
            case "*":
                this.listType = OrgList.UNENUMERATED;
                break;
            default:
                this.listType = OrgList.ENUMERATED;
        }
        this.text = new OrgText(rawText, this.indent + 2);
    }

    public void addSibling (OrgList sibling) {
        if (this.siblings == null) {
            this.siblings = new ArrayList<>();
            this.siblings.add(this);
            sibling.siblings = this.siblings;
        }
        this.siblings.add(sibling);
        sibling.setParent(this.parent);
    }

    public void addText (OrgText text) { this.text.add(text); }

    @Override
    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        this.doIndent(sb);
        sb.append(this.marker).append(" ").append(this.text.toOrgString());
        return sb.toString();
    }

    @Override
    public String toString () {
        return this.text.toString();
    }
}
