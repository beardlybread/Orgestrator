package com.github.beardlybread.orgestrator.org;

public class OrgList extends BaseTreeNode {

    public static final int ENUMERATED = 0;
    public static final int UNENUMERATED = 1;

    public final int indent;
    public final int listType;
    public final String marker;

    public OrgList(BaseTreeNode parent, String rawIndent, String rawMarker, String rawText) {
        super(parent);
        this.indent = rawIndent.length();
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
        this.text = new OrgText(this, rawText);
    }

}
