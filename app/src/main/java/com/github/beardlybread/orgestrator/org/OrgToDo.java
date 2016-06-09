/*
The MIT License (MIT)

Copyright (c) 2016 Bradley Steinbacher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.beardlybread.orgestrator.org;

import android.util.Log;

import com.github.beardlybread.orgestrator.util.Predicate;

import java.util.List;

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

    public OrgProperty getProperties () {
        List<OrgNode> props = this.getChildren("OrgProperty");
        if (props.size() > 0)
            return (OrgProperty) props.get(0);
        OrgProperty out = new OrgProperty(this);
        this.add(out);
        return out;
    }

    @Override
    public String toOrgString () {
        String prefix = "*";
        for (int i = 1; i < this.level; i++) {
            prefix += "*";
        }
        int eventStatus = this.getEvent().getStatus();
        String status = this.status && eventStatus == OrgEvent.CLOSED
                ? "DONE" : "TODO";
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
        }
        if (this.status) { // DONE
            if (this.cachedEvent.getCurrent() != null
                    && this.cachedEvent.getCurrent().getRepeatType() != OrgDate.REPEAT.NONE)
                this.getProperties().setLastRepeat(new OrgDate());
            this.cachedEvent.setClosed();
        } else {
            if (this.cachedEvent.getPrevious() != null
                    && this.cachedEvent.getPrevious().getRepeatType() != OrgDate.REPEAT.NONE)
                this.getProperties().setLastRepeat(this.cachedEvent.getPrevious());
            this.cachedEvent.undoClosed();
        }
        return this.status;
    }
}
