package com.github.beardlybread.orgestrator.org;

import android.util.Log;

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

    private OrgEvent cachedEvent = null;
    protected boolean status = OrgToDo.TODO;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgToDo (String rawLevel, String rawText, String rawTodo) {
        super(rawLevel, rawText);
        this.status = (rawTodo.trim().equalsIgnoreCase("DONE"))
                ? OrgToDo.DONE : OrgToDo.TODO;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public boolean getStatus () { return this.status; }

    public OrgEvent getEvent () {
        if (this.cachedEvent == null) {
            for (OrgNode n: this.getChildren()) {
                if (n.isType("OrgEvent")) {
                    this.cachedEvent = (OrgEvent) n;
                    break;
                }
            }
            this.cachedEvent = (this.cachedEvent == null)
                    ? OrgEvent.NO_EVENT : this.cachedEvent;
        }
        return this.cachedEvent;
    }

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
        this.status = !this.status;
        if (this.getEvent() == OrgEvent.NO_EVENT) {
            this.cachedEvent = new OrgEvent(this);
            this.getChildren().add(0, this.cachedEvent);
            Log.d("OrgToDo", "Event added: " + this);
        }
        if (this.status) { // DONE
            this.cachedEvent.setClosed();
        } else {
            this.cachedEvent.undoClosed();
        }
        return this.status;
    }
}
