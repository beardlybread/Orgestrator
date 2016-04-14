package com.github.beardlybread.orgestrator.org;

// TODO ToDo, Enumerated, Heading, Table, Unenumerated (and this)

public class ToDo extends Heading {

    public static final boolean TODO = true;
    public static final boolean DONE = false;

    protected boolean status = ToDo.TODO;

    public boolean toggle () {
        this.status = !this.status;
        return this.status;
    }
}
