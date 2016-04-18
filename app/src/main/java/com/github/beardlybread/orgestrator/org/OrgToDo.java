package com.github.beardlybread.orgestrator.org;

// TODO Add convenience methods for modifying/adding timestamps.

public class OrgToDo extends OrgHeading {

    public static final boolean TODO = true;
    public static final boolean DONE = false;

    protected boolean status = OrgToDo.TODO;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgToDo(String rawLevel, String rawText, String rawTodo) {
        super(rawLevel, rawText);
        this.status = rawTodo.trim().equalsIgnoreCase("DONE") ? OrgToDo.DONE : OrgToDo.TODO;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public boolean getStatus () { return this.status; }

    @Override
    public String toString () {
        String prefix = "*";
        for (int i = 1; i < this.level; i++) {
            prefix += "*";
        }
        String status = this.status ? "TODO" : "DONE";
        return prefix + " " + status + " " + this.text.toString() + "\n";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public boolean toggle () {
        // TODO adjust children
        this.status = !this.status;
        return this.status;
    }
}
