package com.github.beardlybread.orgestrator.org;

public class OrgHeading extends BaseTreeNode {

    protected int level = -1;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgHeading(String rawLevel, String rawText) {
        super();
        this.level = rawLevel.trim().length();
        this.text = new OrgText(rawText.trim(), 0);
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
        this.text.clear();
        this.text.add(text);
    } 

}
