package com.github.beardlybread.orgestrator.tree;

import java.io.IOException;
import java.io.Writer;

public interface OrgNode {

    // Getters
    OrgNode get(int index) throws IndexOutOfBoundsException;
    int indexOf(OrgNode node);

    // Setters
    void add (OrgNode node);
    void add (int index, OrgNode node);
    boolean remove (OrgNode node);

    // Utility
    String toString ();
    void write (Writer target) throws IOException;

}
