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
import java.util.ArrayList;
import java.util.List;

public abstract class OrgTreeNode extends OrgNode {

    protected ArrayList<OrgNode> children = null;
    protected OrgText text = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgTreeNode() {
        super();
        this.children = new ArrayList<>();
    }

    public OrgTreeNode(int indent) {
        super(indent);
        this.children = new ArrayList<>();
    }

    public OrgTreeNode(TerminalNode indent) {
        super(indent);
        this.children = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public OrgNode get (int index) throws IndexOutOfBoundsException {
        return this.children.get(index);
    }

    public List<OrgNode> getChildren () { return this.children; }

    public List<OrgNode> getChildren (String... types) {
        List<OrgNode> out = new ArrayList<>();
        for (OrgNode child: this.children) {
            for (String type: types) {
                if (child.isType(type) && out.indexOf(child) < 0)
                    out.add(child);
            }
        }
        return out;
    }

    public OrgText getText () { return this.text; }

    public int indexOf (OrgNode node) {
        return this.children.indexOf(node);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void add (OrgNode node) {
        this.children.add(node);
        node.setParent(this);
    }
    
    public void add (int index, OrgNode node) {
        this.children.add(index, node);
        node.setParent(this);
    }

    public boolean remove (OrgNode node) {
        return this.children.remove(node);
    } 

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void write (OutputStream target) throws IOException {
        super.write(target);
        for (OrgNode n: this.children) {
            n.write(target);
        }
    }

}
