package com.github.beardlybread.orgestrator.org;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;

public class OrgProperty extends OrgNode {

    protected HashMap<String, String> properties = null;
    protected OrgDate lastRepeat = null;

    public OrgProperty (TerminalNode indent) {
        super(indent);
        this.properties = new HashMap<>();
    }

    public void setLastRepeat (OrgDate d) {
        this.lastRepeat = d;
    }

    public void put (String key, String value) {
        this.properties.put(key, value);
    }
}
