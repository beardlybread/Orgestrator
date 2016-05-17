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

    @Override
    public String toOrgString () {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.indent; ++i)
            sb.append(" ");
        String indent = sb.toString();
        sb.delete(0, sb.length());
        sb.append(indent).append(":PROPERTIES:\n");
        for (String k: this.properties.keySet())
            sb.append(indent).append(k).append(" ").append(this.properties.get(k)).append("\n");
        if (this.lastRepeat != null) {
            sb.append(indent).append(":LAST_REPEAT: [").append(this.lastRepeat.toOrgString());
            sb.append("]\n");
        }
        sb.append(indent).append(":END:\n");
        return sb.toString();
    }
}
