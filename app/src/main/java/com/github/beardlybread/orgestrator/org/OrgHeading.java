package com.github.beardlybread.orgestrator.org;

public class OrgHeading extends OrgTreeNode {

    public final int level;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgHeading (String rawLevel, String rawText) {
        super();
        this.level = rawLevel.trim().length();
        this.text = new OrgText(rawText.trim(), 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public String toOrgString () {
        String prefix = "*";
        for (int i = 1; i < this.level; i++) {
            prefix += "*";
        }
        return prefix + " " + this.text.toOrgString() + "\n";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void setText (String text) {
        this.text.clear();
        this.text.add(text);
    } 

}
