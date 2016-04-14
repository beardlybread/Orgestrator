package com.github.beardlybread.orgestrator.org;

public class Unenumerated extends BaseTreeNode {

    public final int indent;
    protected final String marker;

    public Unenumerated(BaseTreeNode parent, String rawIndent, String rawMarker, String rawText) {
        super(parent);
        this.indent = rawIndent.length();
        this.marker = rawMarker.trim();
        this.text = new OrgText(this, rawText.trim());
    }

}
