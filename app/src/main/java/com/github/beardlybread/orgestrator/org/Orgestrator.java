package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.io.DriveApi;
import com.github.beardlybread.orgestrator.org.antlr.OrgLexer;
import com.github.beardlybread.orgestrator.org.antlr.OrgParser;
import com.github.beardlybread.orgestrator.util.Predicate;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Orgestrator {

    private Vector<OrgFile> data = null;
    private IOException err = null;

    private Orgestrator () { this.data = new Vector<>(); }
    private static final Orgestrator theInstance = new Orgestrator();
    public static Orgestrator getInstance () { return theInstance; }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public boolean isEmpty () { return this.data.isEmpty(); }

    public IOException getError () { return this.err; }
    public OrgFile getFile (int index) { return this.data.get(index); }

    public List<OrgNode> search (Predicate<OrgNode> predicate) {
        List<OrgNode> out = new ArrayList<>();
        for (OrgFile f: this.data) {
            out.addAll(f.search(predicate));
        }
        return out;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public boolean add (InputStream inStream, String path, int type) {
        try (InputStreamReader r = new InputStreamReader(inStream)) {
            ParseTree tree;
            OrgLexer lexer = new OrgLexer(null);
            ANTLRInputStream ais = new ANTLRInputStream(r);
            lexer.setInputStream(ais);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tree = (new OrgParser(tokens).file());
            OrgFile file = new OrgFile(path, type);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(file, tree);
            this.data.add(file);
        } catch (IOException e) {
            this.err = e;
            return false;
        }
        return true;
    }

    public void clear () { this.data.clear(); }

    public void clearError () { this.err = null; }

    public void downloadGoogleDrive (final String filePath) {
        final String id = filePath.split("\t")[1];
        DriveApi.getInstance()
                .new MakeRequest(new DriveApi.Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                makeRequest.getService().files().get(id)
                        .executeMediaAndDownloadTo(out);
                return out.toByteArray();
            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                add(new ByteArrayInputStream(output), filePath, OrgFile.DRIVE_RESOURCE);
            }

            @Override
            public void before(DriveApi.MakeRequest makeRequest) {}
            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {}
        }).execute();
    }
}
