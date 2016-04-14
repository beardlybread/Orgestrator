package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public abstract class BaseTreeNode extends BaseOrgNode {

    protected ArrayList<OrgNode> children = null;
    protected BaseTreeNode parent = null;
    protected OrgText text = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public BaseTreeNode(BaseTreeNode parent) {
        super(parent);
        this.children = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////


    public OrgNode get (int index) throws IndexOutOfBoundsException {
        return this.children.get(index);
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
    }
    
    public void add (int index, OrgNode node) {
        this.children.add(index, node);
    }

    public boolean remove (OrgNode node) {
        return this.children.remove(node);
    } 

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void write (Writer target) throws IOException {
        target.write(this.toString());
        for (OrgNode n: this.children) {
            n.write(target);
        }
    }

}
