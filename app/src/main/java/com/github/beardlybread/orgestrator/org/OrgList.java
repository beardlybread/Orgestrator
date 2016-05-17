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
        for (int i = 0; i < this.indent; ++i)
            sb.append(" ");
        sb.append(this.marker).append(" ").append(this.text.toOrgString());
        return sb.toString();
    }

}
