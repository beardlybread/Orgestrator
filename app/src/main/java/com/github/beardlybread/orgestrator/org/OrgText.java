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

import java.util.ArrayList;

public class OrgText extends OrgNode {

    protected ArrayList<String> lines = null;
    private String cache = null;
    private String orgCache = null;
    private boolean fresh = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    public OrgText (String line, int indent) {
        super(indent);
        this.lines = new ArrayList<>();
        this.add(line);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public String toOrgString () {
        if (!this.fresh)
            this.refreshStrings();
        return this.orgCache;
    }

    @Override
    public String toString () {
        if (!this.fresh)
            this.refreshStrings();
        return this.cache;
    }

    private void refreshStrings () {
        StringBuilder rawString = new StringBuilder();
        StringBuilder orgString = new StringBuilder();
        rawString.append(this.lines.get(0).trim());
        orgString.append(this.lines.get(0));
        for (int l = 1; l < this.lines.size(); ++l) {
            rawString.append(" ").append(this.lines.get(l).trim());
            for (int i = 0; i < this.indent; ++i)
                orgString.append(" ");
            orgString.append(this.lines.get(l));
        }
        this.cache = rawString.toString();
        this.orgCache = orgString.toString();
        this.fresh = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public void add (String line) {
        this.lines.add(line);
        this.fresh = false;
    }

    public void add (OrgText line) {
        this.lines.addAll(line.lines);
        this.fresh = false;
    }

    public void clear () {
        this.lines.clear();
        this.fresh = false;
    }

    public void overwrite (int index, String line) {
        this.lines.set(index, line);
        this.fresh = false;
    }

}
