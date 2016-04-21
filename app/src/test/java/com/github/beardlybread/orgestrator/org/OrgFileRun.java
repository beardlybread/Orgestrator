package com.github.beardlybread.orgestrator.org;


import com.github.beardlybread.orgestrator.org.antlr.OrgLexer;
import com.github.beardlybread.orgestrator.org.antlr.OrgParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


import java.io.IOException;
import java.io.InputStreamReader;

public class OrgFileRun {

    public static void main (String[] args) throws IOException {
        try (InputStreamReader r = new InputStreamReader(System.in)) {
            OrgLexer lexer = new OrgLexer(null);
            ANTLRInputStream ais = new ANTLRInputStream(r);
            lexer.setInputStream(ais);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            OrgParser parser = new OrgParser(tokens);
            ParseTree tree = parser.file();
            OrgFile file = new OrgFile();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(file, tree);
            // something something
            // go crazy?
            // don't mind if I do
        }

    }
}
