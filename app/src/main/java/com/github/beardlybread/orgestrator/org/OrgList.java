package com.github.beardlybread.orgestrator.org;

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
        this.text = new OrgText(rawText, 0);
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

}
