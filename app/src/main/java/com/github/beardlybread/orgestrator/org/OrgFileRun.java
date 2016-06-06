package com.github.beardlybread.orgestrator.org;


import com.github.beardlybread.orgestrator.org.antlr.OrgLexer;
import com.github.beardlybread.orgestrator.org.antlr.OrgParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class OrgFileRun {

    public static void main (String[] args) throws IOException {
        ParseTree tree = initialize(args.length < 1 ? System.in : new FileInputStream(args[0]));
        OrgFile file = new OrgFile();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(file, tree);
        OutputStream outStream = System.out;
        file.write(outStream);
        outStream.flush();
            // something something
            // go crazy?
            // don't mind if I do
    }

    public static ParseTree initialize (InputStream inStream) throws IOException {
        try (InputStreamReader r = new InputStreamReader(inStream)) {
            OrgLexer lexer = new OrgLexer(null);
            ANTLRInputStream ais = new ANTLRInputStream(r);
            lexer.setInputStream(ais);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            return (new OrgParser(tokens)).file();
        }
    }
}
