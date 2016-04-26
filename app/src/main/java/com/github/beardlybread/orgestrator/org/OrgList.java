package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class OrgList extends OrgTreeNode {

    public static final int ENUMERATED = 0;
    public static final int UNENUMERATED = 1;

    protected ArrayList<OrgList> siblings = null;
    public final int listType;
    public final String marker;

    public OrgList (int indent, String rawMarker, String rawText) {
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
            sibling.siblings = this.siblings;
        }
        this.siblings.add(sibling);
        sibling.setParent(this.parent);
    }

    public void addText (OrgText text) { this.text.add(text); }

    @Override
    public String toString () {
        return this.marker + " " + this.text.toString();
    }

    @Override
    public void write (Writer target) throws IOException {
        super.write(target);
        if (this.siblings != null) {
            for (OrgList l : this.siblings) {
                this.writeSibling(l, target);
            }
        }
    }

    public void writeSibling (OrgList list, Writer target) throws IOException {
        target.append(list.toString());
        for (OrgNode n: list.children) {
            n.write(target);
        }
    }
}
