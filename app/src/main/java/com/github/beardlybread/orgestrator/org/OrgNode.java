package com.github.beardlybread.orgestrator.org;

import java.io.IOException;
import java.io.Writer;

public interface OrgNode {

    String getType ();
    String toString();
    void write (Writer target) throws IOException;

}
