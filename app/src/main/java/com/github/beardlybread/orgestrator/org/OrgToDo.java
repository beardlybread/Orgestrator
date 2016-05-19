package com.github.beardlybread.orgestrator.org;

// TODO Add convenience methods for modifying/adding timestamps.

import com.github.beardlybread.orgestrator.util.Predicate;

public class OrgToDo extends OrgHeading {

    public static final boolean TODO = false;
    public static final boolean DONE = true;

    public static final Predicate<OrgNode> all = new Predicate<OrgNode>() {
        @Override
        public boolean call(OrgNode input) {
            return input.isType("OrgToDo");
        }
    };

    public static final Predicate<OrgNode> complete = new Predicate<OrgNode>() {
        @Override
        public boolean call(OrgNode input) {
            return input.isType("OrgToDo") && ((OrgToDo) input).getStatus();
        }
    };

    public static final Predicate<OrgNode> incomplete = new Predicate<OrgNode>() {
        @Override
        public boolean call(OrgNode input) {
            return input.isType("OrgToDo") && !((OrgToDo) input).getStatus();
        }
    };

    protected boolean status = OrgToDo.TODO;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgToDo (String rawLevel, String rawText, String rawTodo) {
        super(rawLevel, rawText);
        this.status = rawTodo.trim().equalsIgnoreCase("DONE") ? OrgToDo.DONE : OrgToDo.TODO;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public boolean getStatus () { return this.status; }

    @Override
    public String toOrgString () {
        String prefix = "*";
        for (int i = 1; i < this.level; i++) {
            prefix += "*";
        }
        String status = this.status ? "DONE" : "TODO";
        return prefix + " " + status + " " + this.text.toOrgString() + "\n";
    }

    @Override
    public String toString () {
        return this.text.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public boolean toggle () {
        // TODO Adjust children to reflect state change.
        this.status = !this.status;
        return this.status;
    }
}
