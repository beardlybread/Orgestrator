package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.org.antlr.OrgLexer;
import com.github.beardlybread.orgestrator.org.antlr.OrgParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Orgestrator {

    // These can be extended later if I want to add other targets to get the files.
    public static final int UNKNOWN_SOURCE = -1;
    public static final int RAW_RESOURCE = 0;
    public static final int DRIVE_RESOURCE = 1;
    public static final int OTHER_RESOURCE = 2;

    private ArrayList<OrgData> data = null;
    private Exception err = null;

    public Orgestrator () { this.data = new ArrayList<>(); }

    public boolean add (InputStream inStream, String path, int type) {
        try {
            this.data.add(new OrgData(inStream, path, type));
        } catch (IOException e) {
            this.err = e;
            return false;
        }
        return true;
    }

    public OrgData getData (int index) {
        return this.data.get(index);
    }

    public Exception clearError (){
        Exception e = this.err;
        this.err = null;
        return e;
    }

    public class OrgData {

        private String resourcePath = null;
        private int resourceType = Orgestrator.UNKNOWN_SOURCE;
        private OrgFile file = null;

        // TODO Some sort of path/id should be stored for the resource.
        public OrgData (InputStream inStream, String resourcePath, int resourceType)
                throws IOException {
            this.resourcePath = resourcePath;
            this.resourceType = resourceType;
            ParseTree tree;
            try (InputStreamReader r = new InputStreamReader(inStream)) {
                OrgLexer lexer = new OrgLexer(null);
                ANTLRInputStream ais = new ANTLRInputStream(r);
                lexer.setInputStream(ais);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                tree = (new OrgParser(tokens).file());
            }
            this.file = new OrgFile();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(file, tree);
        }

        public OrgFile getFile () { return this.file; }
        public String getResourcePath () { return this.resourcePath; }
        public int getResourceType () { return this.resourceType; }
    }

}
