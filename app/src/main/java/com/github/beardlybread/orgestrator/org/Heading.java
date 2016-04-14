package com.github.beardlybread.orgestrator.org;

public class Heading extends BaseTreeNode {

    protected int level;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public Heading(Heading parent, String rawLevel, String rawText) {
        super(parent);
        this.level = rawLevel.trim().length();
        this.text = new OrgText(this, rawText.trim());
        this.parent = parent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public int getLevel () { return this.level; }

    @Override
    public String toString () {
        String prefix = "*";
        for (int i = 1; i < this.level; i++) {
            prefix += "*";
        }
        return prefix + " " + this.text.toString() + "\n";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void setText (String text) {
        this.text.overwrite(text);
    } 

}
