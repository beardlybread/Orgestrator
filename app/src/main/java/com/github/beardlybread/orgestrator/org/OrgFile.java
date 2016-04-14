package com.github.beardlybread.orgestrator.org;

import java.util.ArrayList;

public class OrgFile {

    protected ArrayList<OrgNode> items = null;

    // Listener as input
    // on all exits, record last processed thing/indent level (stack) list and heading stack?
    // anything after headings > heading.level into heading on top
    // lists/tables after lists go into list if indent >= top
    // text gloms onto top text except for headings, ignoring indent
    // properties and timestamps attach to previous heading
}
