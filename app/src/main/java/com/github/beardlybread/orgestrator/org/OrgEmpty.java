package com.github.beardlybread.orgestrator.org;

public class OrgEmpty extends OrgNode {

    public OrgEmpty () { super(); }

    @Override
    public String toString () { return ""; }

    @Override
    public String toOrgString () { return "\n"; }

}

