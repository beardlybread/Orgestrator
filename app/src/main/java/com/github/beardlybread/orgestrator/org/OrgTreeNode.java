package com.github.beardlybread.orgestrator.org;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.io.Writer;
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
    public void write (Writer target) throws IOException {
        super.write(target);
        for (OrgNode n: this.children) {
            n.write(target);
        }
    }

}
