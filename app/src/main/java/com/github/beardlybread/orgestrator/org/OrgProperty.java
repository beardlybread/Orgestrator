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

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;

public class OrgProperty extends OrgNode {

    protected HashMap<String, String> properties = null;
    protected OrgDate lastRepeat = null;

    public OrgProperty (TerminalNode indent) {
        super(indent);
        this.properties = new HashMap<>();
    }

    public OrgProperty (OrgToDo parent) {
        super(parent.level + 1);
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
        this.doIndent(sb);
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

    @Override
    public String toString () {
        return this.toOrgString();
    }

}
