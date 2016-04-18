package com.github.beardlybread.orgestrator.org;

public class OrgList extends BaseTreeNode {

    public static final int ENUMERATED = 0;
    public static final int UNENUMERATED = 1;

    public final int listType;
    public final String marker;

    public OrgList(int indent, String rawMarker, String rawText) {
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

}
