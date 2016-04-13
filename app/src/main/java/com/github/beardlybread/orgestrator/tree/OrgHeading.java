package com.github.beardlybread.orgestrator.tree;

public class OrgHeading extends BaseOrgNode {

    protected String text;
    public final int level;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgHeading () {
        super();
        level = -1;
    }

    public OrgHeading (String rawLevel, String rawText) {
        super();
        this.level = rawLevel.length() - 1;
        this.text = rawText.replaceAll("\\n", "");
    }

    public OrgHeading (OrgNode parent, String rawLevel, String rawText) {
        this(rawLevel, rawText);
        this.parent = parent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public String getText () {
        return this.text;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void setText (String text) {
        this.text = text;
    } 

    ////////////////////////////////////////////////////////////////////////////
    // Utility
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public String toString () {
        String prefix = "*";
        for (int i = 1; i < this.level; i++) {
            prefix += "*";
        }
        return prefix + " " + this.text + "\n";
    }
    
}
